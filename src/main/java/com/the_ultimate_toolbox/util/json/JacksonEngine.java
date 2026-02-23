package com.the_ultimate_toolbox.util.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
// import com.fasterxml.jackson.dataformat.json5.JSON5Factory; // Temporalmente comentado

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Motor de Jackson optimizado para máximo rendimiento.
 * Implementa Singleton thread-safe con configuración optimizada.
 *
 * Características de rendimiento:
 * - Reutilización de ObjectMapper (thread-safe)
 * - Cache de JsonFactory para diferentes formatos
 * - Configuración optimizada para minimizar overhead
 * - Gestión eficiente de memoria para archivos grandes
 *
 * @author The Ultimate Toolbox
 * @since 2.2.0
 */
public final class JacksonEngine {

    // Instancia Singleton thread-safe usando Bill Pugh pattern
    private static class SingletonHolder {
        private static final JacksonEngine INSTANCE = new JacksonEngine();
    }

    // ObjectMappers configurados y cacheados (thread-safe)
    private final ObjectMapper standardMapper;
    private final ObjectMapper json5Mapper;
    private final ObjectMapper jsoncMapper;

    // Cache para JsonFactories personalizadas
    private final ConcurrentHashMap<String, JsonFactory> factoryCache;

    // JsonNodeFactory optimizada para reutilización
    private final JsonNodeFactory nodeFactory;

    /**
     * Constructor privado para Singleton.
     * Configura los mappers con opciones optimizadas de rendimiento.
     */
    private JacksonEngine() {
        this.factoryCache = new ConcurrentHashMap<>();
        this.nodeFactory = JsonNodeFactory.withExactBigDecimals(false);

        // Configurar mapper estándar con optimizaciones de rendimiento
        this.standardMapper = createOptimizedMapper(new JsonFactory());

        // Configurar mapper para JSON5
        this.json5Mapper = createOptimizedMapper(createJSON5Factory());

        // Configurar mapper para JSONC (JSON con comentarios)
        this.jsoncMapper = createOptimizedMapper(createJSONCFactory());
    }

    /**
     * Obtiene la instancia singleton del motor Jackson.
     *
     * @return instancia única de JacksonEngine
     */
    public static JacksonEngine getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Crea un ObjectMapper optimizado para rendimiento.
     *
     * @param factory JsonFactory a utilizar
     * @return ObjectMapper configurado
     */
    private ObjectMapper createOptimizedMapper(JsonFactory factory) {
        ObjectMapper mapper = new ObjectMapper(factory);

        // Configuraciones de rendimiento
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        // Optimizaciones de rendimiento
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);

        // Usar factory de nodos optimizada
        mapper.setNodeFactory(nodeFactory);

        // Desactivar features innecesarias para mejor rendimiento
        mapper.disable(SerializationFeature.INDENT_OUTPUT); // Se puede activar cuando sea necesario
        mapper.disable(MapperFeature.USE_ANNOTATIONS); // Solo si no se usan anotaciones

        return mapper;
    }

    /**
     * Crea una JsonFactory para JSON5.
     * NOTA: Temporalmente usa la misma configuración que JSONC hasta resolver dependencia
     *
     * @return JsonFactory configurada para JSON5
     */
    private JsonFactory createJSON5Factory() {
        // Temporalmente usar configuración similar a JSONC
        return createJSONCFactory();
        /* Implementación completa cuando se resuelva la dependencia:
        return JSON5Factory.builder()
                .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
                .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
                .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
                .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
                .build();
        */
    }

    /**
     * Crea una JsonFactory para JSONC (JSON con comentarios).
     *
     * @return JsonFactory configurada para JSONC
     */
    private JsonFactory createJSONCFactory() {
        return JsonFactory.builder()
                .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
                .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
                .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
                .build();
    }

    /**
     * Obtiene el ObjectMapper para JSON estándar.
     *
     * @return ObjectMapper configurado para JSON estándar
     */
    public ObjectMapper getStandardMapper() {
        return standardMapper;
    }

    /**
     * Obtiene el ObjectMapper para JSON5.
     *
     * @return ObjectMapper configurado para JSON5
     */
    public ObjectMapper getJSON5Mapper() {
        return json5Mapper;
    }

    /**
     * Obtiene el ObjectMapper para JSONC.
     *
     * @return ObjectMapper configurado para JSONC
     */
    public ObjectMapper getJSONCMapper() {
        return jsoncMapper;
    }

    /**
     * Detecta automáticamente el formato JSON y retorna el mapper apropiado.
     *
     * @param content contenido JSON a analizar
     * @return ObjectMapper apropiado para el formato detectado
     */
    public ObjectMapper detectAndGetMapper(String content) {
        if (content == null || content.trim().isEmpty()) {
            return standardMapper;
        }

        String trimmed = content.trim();

        // Detectar comentarios (JSONC o JSON5)
        if (trimmed.contains("//") || trimmed.contains("/*")) {
            // Preferir JSON5 si hay características específicas de JSON5
            if (hasJSON5Features(trimmed)) {
                return json5Mapper;
            }
            return jsoncMapper;
        }

        // Detectar características de JSON5
        if (hasJSON5Features(trimmed)) {
            return json5Mapper;
        }

        // Por defecto usar mapper estándar
        return standardMapper;
    }

    /**
     * Verifica si el contenido tiene características específicas de JSON5.
     *
     * @param content contenido a verificar
     * @return true si detecta características de JSON5
     */
    private boolean hasJSON5Features(String content) {
        // Verificar trailing commas, unquoted keys, etc.
        return content.contains("Infinity") ||
               content.contains("NaN") ||
               content.contains("-Infinity") ||
               content.matches(".*,\\s*[\\]}].*"); // Trailing comma simple check
    }

    /**
     * Crea un JsonGenerator optimizado para escritura streaming.
     *
     * @param writer Writer de salida
     * @param pretty true para formato pretty-print
     * @return JsonGenerator configurado
     * @throws IOException si hay error al crear el generator
     */
    public JsonGenerator createGenerator(Writer writer, boolean pretty) throws IOException {
        JsonGenerator generator = standardMapper.getFactory().createGenerator(writer);
        if (pretty) {
            generator.setPrettyPrinter(standardMapper.getSerializationConfig().constructDefaultPrettyPrinter());
        }
        return generator;
    }

    /**
     * Crea un JsonParser optimizado para lectura streaming.
     *
     * @param reader Reader de entrada
     * @param format formato JSON (standard, json5, jsonc)
     * @return JsonParser configurado
     * @throws IOException si hay error al crear el parser
     */
    public JsonParser createParser(Reader reader, JSONFormat format) throws IOException {
        return switch (format) {
            case JSON5 -> json5Mapper.getFactory().createParser(reader);
            case JSONC -> jsoncMapper.getFactory().createParser(reader);
            default -> standardMapper.getFactory().createParser(reader);
        };
    }

    /**
     * Crea un JsonParser para archivos grandes con buffering optimizado.
     *
     * @param file archivo a parsear
     * @param format formato JSON
     * @param bufferSize tamaño del buffer en bytes (recomendado: 8192 para archivos grandes)
     * @return JsonParser configurado con buffering
     * @throws IOException si hay error al crear el parser
     */
    public JsonParser createBufferedParser(File file, JSONFormat format, int bufferSize)
            throws IOException {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8),
            bufferSize
        );
        return createParser(reader, format);
    }

    /**
     * Enum para tipos de formato JSON soportados.
     */
    public enum JSONFormat {
        STANDARD,
        JSON5,
        JSONC
    }

    /**
     * Limpia recursos cacheados si es necesario.
     * Normalmente no es necesario llamar este método.
     */
    public void clearCache() {
        factoryCache.clear();
    }
}