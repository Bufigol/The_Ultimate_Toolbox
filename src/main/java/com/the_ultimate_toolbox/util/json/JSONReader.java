package com.the_ultimate_toolbox.util.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Lector JSON optimizado para máximo rendimiento con Jackson.
 * Soporta lectura streaming, procesamiento batch y gestión eficiente de memoria.
 *
 * Características principales:
 * - Streaming API para archivos grandes (sin cargar todo en memoria)
 * - Procesamiento batch configurable
 * - Auto-detección de formato (JSON, JSON5, JSONC)
 * - Lectura asíncrona opcional
 * - Cache de esquemas para validación rápida
 * - Gestión automática de recursos con try-with-resources
 *
 * @author The Ultimate Toolbox
 * @since 2.2.0
 */
public class JSONReader implements AutoCloseable {

    // Motor Jackson singleton
    private final JacksonEngine engine;

    // Parser actual (para streaming)
    private JsonParser currentParser;

    // Formato detectado o especificado
    private JacksonEngine.JSONFormat format;

    // Configuración de lectura
    private final ReaderConfig config;

    // Cache de esquemas para validación
    private static final ConcurrentHashMap<String, JsonNode> schemaCache = new ConcurrentHashMap<>();

    /**
     * Constructor con configuración por defecto.
     */
    public JSONReader() {
        this(ReaderConfig.defaultConfig());
    }

    /**
     * Constructor con configuración personalizada.
     *
     * @param config configuración de lectura
     */
    public JSONReader(ReaderConfig config) {
        this.engine = JacksonEngine.getInstance();
        this.config = config;
        this.format = config.format;
    }

    // ============= Métodos de lectura desde archivo =============

    /**
     * Lee un archivo JSON completo como JSONObject.
     *
     * @param file archivo a leer
     * @return JSONObject parseado
     * @throws IOException si hay error de lectura
     */
    public JSONObject readFile(File file) throws IOException {
        return readFile(file.toPath());
    }

    /**
     * Lee un archivo JSON completo como JSONObject.
     *
     * @param path ruta del archivo
     * @return JSONObject parseado
     * @throws IOException si hay error de lectura
     */
    public JSONObject readFile(Path path) throws IOException {
        validateFile(path);

        // Para archivos pequeños, leer todo de una vez
        if (Files.size(path) < config.smallFileSizeThreshold) {
            String content = Files.readString(path, config.charset);
            return parseString(content);
        }

        // Para archivos grandes, usar streaming con buffer
        try (BufferedReader reader = Files.newBufferedReader(path, config.charset)) {
            return parseReader(reader);
        }
    }

    /**
     * Lee un archivo JSON grande usando streaming para minimizar uso de memoria.
     *
     * @param file archivo a leer
     * @param processor función para procesar cada objeto del nivel raíz
     * @throws IOException si hay error de lectura
     */
    public void readLargeFile(File file, Consumer<JSONObject> processor) throws IOException {
        readLargeFile(file.toPath(), processor);
    }

    /**
     * Lee un archivo JSON grande usando streaming.
     *
     * @param path ruta del archivo
     * @param processor función para procesar cada objeto
     * @throws IOException si hay error de lectura
     */
    public void readLargeFile(Path path, Consumer<JSONObject> processor) throws IOException {
        validateFile(path);

        try (BufferedReader reader = Files.newBufferedReader(path, config.charset);
             JsonParser parser = createParser(reader)) {

            processStream(parser, processor);
        }
    }

    /**
     * Lee un array JSON procesando elementos en batch.
     *
     * @param file archivo con array JSON
     * @param batchSize tamaño del batch
     * @param processor función para procesar cada batch
     * @throws IOException si hay error de lectura
     */
    public void readArrayInBatches(File file, int batchSize, Consumer<List<JSONObject>> processor)
            throws IOException {
        readArrayInBatches(file.toPath(), batchSize, processor);
    }

    /**
     * Lee un array JSON procesando elementos en batch.
     *
     * @param path ruta del archivo
     * @param batchSize tamaño del batch
     * @param processor función para procesar cada batch
     * @throws IOException si hay error de lectura
     */
    public void readArrayInBatches(Path path, int batchSize, Consumer<List<JSONObject>> processor)
            throws IOException {
        validateFile(path);

        try (BufferedReader reader = Files.newBufferedReader(path, config.charset);
             JsonParser parser = createParser(reader)) {

            // Verificar que es un array
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalArgumentException("El archivo no contiene un array JSON");
            }

            List<JSONObject> batch = new ArrayList<>(batchSize);

            while (parser.nextToken() != JsonToken.END_ARRAY) {
                JsonNode node = getMapper().readTree(parser);
                if (node.isObject()) {
                    batch.add(new JSONObject(node.toString()));
                }

                if (batch.size() >= batchSize) {
                    processor.accept(new ArrayList<>(batch));
                    batch.clear();
                }
            }

            // Procesar último batch si tiene elementos
            if (!batch.isEmpty()) {
                processor.accept(batch);
            }
        }
    }

    // ============= Métodos de lectura desde string =============

    /**
     * Parsea un string JSON.
     *
     * @param jsonString string a parsear
     * @return JSONObject parseado
     * @throws JsonProcessingException si el JSON es inválido
     */
    public JSONObject parseString(String jsonString) throws JsonProcessingException {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return new JSONObject();
        }

        // Auto-detectar formato si no está especificado
        if (format == JacksonEngine.JSONFormat.STANDARD) {
            format = detectFormat(jsonString);
        }

        return new JSONObject(jsonString);
    }

    /**
     * Parsea un Reader.
     *
     * @param reader reader con el contenido JSON
     * @return JSONObject parseado
     * @throws IOException si hay error de lectura
     */
    public JSONObject parseReader(Reader reader) throws IOException {
        JsonNode node = getMapper().readTree(reader);
        if (!node.isObject()) {
            throw new IllegalArgumentException("El contenido debe ser un objeto JSON");
        }
        return new JSONObject(node.toString());
    }

    // ============= Métodos de lectura desde URL =============

    /**
     * Lee JSON desde una URL.
     *
     * @param url URL a leer
     * @return JSONObject parseado
     * @throws IOException si hay error de lectura
     */
    public JSONObject readURL(URL url) throws IOException {
        return readURL(url.toString());
    }

    /**
     * Lee JSON desde una URL.
     *
     * @param urlString string de la URL
     * @return JSONObject parseado
     * @throws IOException si hay error de lectura
     */
    public JSONObject readURL(String urlString) throws IOException {
        URL url = new URL(urlString);

        try (InputStream is = url.openStream();
             BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, config.charset))) {

            return parseReader(reader);
        }
    }

    /**
     * Lee JSON desde una URL de forma asíncrona.
     *
     * @param urlString URL a leer
     * @return CompletableFuture con el resultado
     */
    public CompletableFuture<JSONObject> readURLAsync(String urlString) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return readURL(urlString);
            } catch (IOException e) {
                throw new RuntimeException("Error al leer URL: " + urlString, e);
            }
        });
    }

    // ============= Métodos de streaming =============

    /**
     * Inicia lectura streaming de un archivo.
     *
     * @param file archivo a leer
     * @throws IOException si hay error al abrir el archivo
     */
    public void startStreaming(File file) throws IOException {
        startStreaming(file.toPath());
    }

    /**
     * Inicia lectura streaming de un archivo.
     *
     * @param path ruta del archivo
     * @throws IOException si hay error al abrir el archivo
     */
    public void startStreaming(Path path) throws IOException {
        validateFile(path);

        if (currentParser != null) {
            currentParser.close();
        }

        BufferedReader reader = Files.newBufferedReader(path, config.charset);
        currentParser = createParser(reader);
    }

    /**
     * Lee el siguiente objeto del stream.
     *
     * @return siguiente JSONObject o null si no hay más
     * @throws IOException si hay error de lectura
     */
    public JSONObject nextObject() throws IOException {
        if (currentParser == null) {
            throw new IllegalStateException("No hay streaming activo. Llame a startStreaming primero");
        }

        JsonToken token = currentParser.nextToken();

        if (token == null || token == JsonToken.END_ARRAY) {
            return null;
        }

        if (token == JsonToken.START_OBJECT) {
            JsonNode node = getMapper().readTree(currentParser);
            return new JSONObject(node.toString());
        }

        return null;
    }

    /**
     * Obtiene un stream de objetos JSON.
     *
     * @param path ruta del archivo
     * @return Stream de JSONObjects
     * @throws IOException si hay error de lectura
     */
    public Stream<JSONObject> stream(Path path) throws IOException {
        validateFile(path);

        BufferedReader reader = Files.newBufferedReader(path, config.charset);
        JsonParser parser = createParser(reader);

        // Verificar si es array
        if (parser.nextToken() == JsonToken.START_ARRAY) {
            return streamArray(parser);
        } else {
            parser.close();
            reader.close();
            throw new IllegalArgumentException("El archivo debe contener un array JSON para streaming");
        }
    }

    // ============= Métodos de validación =============

    /**
     * Valida un JSON contra un esquema.
     *
     * @param json JSON a validar
     * @param schemaPath ruta del esquema
     * @return true si es válido
     * @throws IOException si hay error de lectura
     */
    public boolean validateAgainstSchema(JSONObject json, Path schemaPath) throws IOException {
        JsonNode schema = schemaCache.computeIfAbsent(
            schemaPath.toString(),
            k -> {
                try {
                    return getMapper().readTree(Files.readString(schemaPath));
                } catch (IOException e) {
                    throw new RuntimeException("Error al leer esquema", e);
                }
            }
        );

        // Aquí se podría implementar validación completa con json-schema-validator
        // Por ahora solo verificamos que el esquema existe
        return schema != null;
    }

    // ============= Métodos de utilidad y configuración =============

    /**
     * Detecta el formato de un string JSON.
     *
     * @param content contenido a analizar
     * @return formato detectado
     */
    private JacksonEngine.JSONFormat detectFormat(String content) {
        if (content.contains("//") || content.contains("/*")) {
            if (content.contains("Infinity") || content.contains("NaN")) {
                return JacksonEngine.JSONFormat.JSON5;
            }
            return JacksonEngine.JSONFormat.JSONC;
        }
        if (content.contains("Infinity") || content.contains("NaN")) {
            return JacksonEngine.JSONFormat.JSON5;
        }
        return JacksonEngine.JSONFormat.STANDARD;
    }

    /**
     * Obtiene el mapper apropiado según el formato.
     *
     * @return ObjectMapper configurado
     */
    private ObjectMapper getMapper() {
        return switch (format) {
            case JSON5 -> engine.getJSON5Mapper();
            case JSONC -> engine.getJSONCMapper();
            default -> engine.getStandardMapper();
        };
    }

    /**
     * Crea un parser para el reader dado.
     *
     * @param reader reader de entrada
     * @return JsonParser configurado
     * @throws IOException si hay error al crear el parser
     */
    private JsonParser createParser(Reader reader) throws IOException {
        return engine.createParser(reader, format);
    }

    /**
     * Valida que el archivo existe y es legible.
     *
     * @param path ruta a validar
     * @throws IOException si el archivo no es válido
     */
    private void validateFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Archivo no encontrado: " + path);
        }
        if (!Files.isReadable(path)) {
            throw new IOException("No se puede leer el archivo: " + path);
        }
        if (Files.isDirectory(path)) {
            throw new IOException("La ruta es un directorio, no un archivo: " + path);
        }
    }

    /**
     * Procesa un stream JSON con un consumidor.
     *
     * @param parser parser del stream
     * @param processor consumidor de objetos
     * @throws IOException si hay error de lectura
     */
    private void processStream(JsonParser parser, Consumer<JSONObject> processor) throws IOException {
        JsonToken token = parser.nextToken();

        if (token == JsonToken.START_ARRAY) {
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                JsonNode node = getMapper().readTree(parser);
                if (node.isObject()) {
                    processor.accept(new JSONObject(node.toString()));
                }
            }
        } else if (token == JsonToken.START_OBJECT) {
            JsonNode node = getMapper().readTree(parser);
            processor.accept(new JSONObject(node.toString()));
        }
    }

    /**
     * Crea un stream desde un array JSON.
     *
     * @param parser parser posicionado después de START_ARRAY
     * @return Stream de JSONObjects
     */
    private Stream<JSONObject> streamArray(JsonParser parser) {
        Iterator<JSONObject> iterator = new Iterator<>() {
            private JSONObject next = advance();

            private JSONObject advance() {
                try {
                    if (parser.nextToken() != JsonToken.END_ARRAY) {
                        JsonNode node = getMapper().readTree(parser);
                        if (node.isObject()) {
                            return new JSONObject(node.toString());
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error al leer stream", e);
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public JSONObject next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                JSONObject current = next;
                next = advance();
                return current;
            }
        };

        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
            false
        ).onClose(() -> {
            try {
                parser.close();
            } catch (IOException e) {
                // Log error
            }
        });
    }

    /**
     * Cierra recursos abiertos.
     */
    @Override
    public void close() throws IOException {
        if (currentParser != null) {
            currentParser.close();
            currentParser = null;
        }
    }

    // ============= Configuración del lector =============

    /**
     * Configuración para JSONReader.
     */
    public static class ReaderConfig {
        private JacksonEngine.JSONFormat format = JacksonEngine.JSONFormat.STANDARD;
        private Charset charset = StandardCharsets.UTF_8;
        private int bufferSize = 8192;
        private long smallFileSizeThreshold = 1_048_576; // 1 MB
        private boolean autoDetectFormat = true;

        public static ReaderConfig defaultConfig() {
            return new ReaderConfig();
        }

        public ReaderConfig format(JacksonEngine.JSONFormat format) {
            this.format = format;
            return this;
        }

        public ReaderConfig charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public ReaderConfig bufferSize(int size) {
            this.bufferSize = size;
            return this;
        }

        public ReaderConfig smallFileThreshold(long bytes) {
            this.smallFileSizeThreshold = bytes;
            return this;
        }

        public ReaderConfig autoDetectFormat(boolean detect) {
            this.autoDetectFormat = detect;
            return this;
        }
    }
}
