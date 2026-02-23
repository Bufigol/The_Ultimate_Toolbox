package com.the_ultimate_toolbox.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite completa de tests para {@link JSONReader}.
 * Cubre lectura desde archivos, strings, readers, streaming,
 * procesamiento batch, deteccion de formato, validacion de archivos,
 * configuracion con ReaderConfig y gestion de recursos.
 *
 * @author The Ultimate Toolbox Team
 * @since 2.2.0
 */
@DisplayName("JSONReader Tests")
class JSONReaderTest {

    @TempDir
    Path tempDir;

    private JSONReader reader;

    @BeforeEach
    void setUp() {
        reader = new JSONReader();
    }

    @AfterEach
    void tearDown() throws IOException {
        reader.close();
    }

    // ================================================================
    // Constructores y ReaderConfig
    // ================================================================
    @Nested
    @DisplayName("Constructores y ReaderConfig")
    class ConstructorAndConfigTests {

        @Test
        @DisplayName("Constructor por defecto debe crear reader funcional")
        void shouldCreateFunctionalReader_WithDefaultConstructor() {
            JSONReader defaultReader = new JSONReader();
            assertNotNull(defaultReader);
            assertDoesNotThrow(defaultReader::close);
        }

        @Test
        @DisplayName("Constructor con configuracion personalizada")
        void shouldCreateReader_WithCustomConfig() {
            JSONReader.ReaderConfig config = JSONReader.ReaderConfig.defaultConfig()
                    .format(JacksonEngine.JSONFormat.JSONC)
                    .charset(StandardCharsets.UTF_8)
                    .bufferSize(16384)
                    .smallFileThreshold(2_097_152L)
                    .autoDetectFormat(true);

            JSONReader customReader = new JSONReader(config);
            assertNotNull(customReader);
            assertDoesNotThrow(customReader::close);
        }

        @Test
        @DisplayName("ReaderConfig.defaultConfig() debe retornar configuracion valida")
        void shouldReturnValidDefaultConfig() {
            JSONReader.ReaderConfig config = JSONReader.ReaderConfig.defaultConfig();
            assertNotNull(config);
        }

        @Test
        @DisplayName("ReaderConfig debe soportar encadenamiento fluido")
        void shouldSupportFluentChaining() {
            JSONReader.ReaderConfig config = JSONReader.ReaderConfig.defaultConfig()
                    .format(JacksonEngine.JSONFormat.STANDARD)
                    .charset(StandardCharsets.US_ASCII)
                    .bufferSize(4096)
                    .smallFileThreshold(512000L)
                    .autoDetectFormat(false);

            assertNotNull(config);
        }

        @Test
        @DisplayName("ReaderConfig con formato JSON5")
        void shouldCreateConfig_WithJSON5Format() {
            JSONReader.ReaderConfig config = JSONReader.ReaderConfig.defaultConfig()
                    .format(JacksonEngine.JSONFormat.JSON5);

            JSONReader json5Reader = new JSONReader(config);
            assertNotNull(json5Reader);
            assertDoesNotThrow(json5Reader::close);
        }
    }

    // ================================================================
    // readFile(File) y readFile(Path)
    // ================================================================
    @Nested
    @DisplayName("Lectura desde archivo: readFile()")
    class ReadFileTests {

        @Test
        @DisplayName("Debe leer archivo JSON pequeno correctamente con Path")
        void shouldReadSmallFile_WithPath() throws IOException {
            Path jsonFile = tempDir.resolve("small.json");
            Files.writeString(jsonFile, "{\"name\":\"test\",\"value\":42}");

            JSONObject result = reader.readFile(jsonFile);

            assertNotNull(result);
            assertEquals("test", result.getString("name"));
            assertEquals(42, result.getInt("value", 0));
        }

        @Test
        @DisplayName("Debe leer archivo JSON pequeno correctamente con File")
        void shouldReadSmallFile_WithFile() throws IOException {
            Path jsonFile = tempDir.resolve("small_file.json");
            Files.writeString(jsonFile, "{\"key\":\"value\"}");

            JSONObject result = reader.readFile(jsonFile.toFile());

            assertNotNull(result);
            assertEquals("value", result.getString("key"));
        }

        @Test
        @DisplayName("Debe leer archivo con multiples campos")
        void shouldReadFile_WithMultipleFields() throws IOException {
            String json = """
                    {
                        "string": "hello",
                        "integer": 100,
                        "decimal": 1.5,
                        "boolean": true,
                        "null_val": null
                    }
                    """;
            Path jsonFile = tempDir.resolve("multi.json");
            Files.writeString(jsonFile, json);

            JSONObject result = reader.readFile(jsonFile);

            assertEquals("hello", result.getString("string"));
            assertEquals(100, result.getInt("integer", 0));
            assertEquals(1.5, result.getDouble("decimal", 0.0), 0.01);
            assertTrue(result.getBoolean("boolean", false));
            assertTrue(result.isNull("null_val"));
        }

        @Test
        @DisplayName("Debe leer archivo con objetos anidados")
        void shouldReadFile_WithNestedObjects() throws IOException {
            String json = """
                    {
                        "user": {
                            "name": "Alice",
                            "address": {
                                "city": "Boston"
                            }
                        }
                    }
                    """;
            Path jsonFile = tempDir.resolve("nested.json");
            Files.writeString(jsonFile, json);

            JSONObject result = reader.readFile(jsonFile);

            JSONObject user = result.getJSONObject("user");
            assertNotNull(user);
            assertEquals("Alice", user.getString("name"));
        }

        @Test
        @DisplayName("Debe lanzar FileNotFoundException para archivo inexistente")
        void shouldThrowFileNotFoundException_WhenFileDoesNotExist() {
            Path nonExistent = tempDir.resolve("nonexistent.json");
            assertThrows(FileNotFoundException.class, () -> reader.readFile(nonExistent));
        }

        @Test
        @DisplayName("Debe lanzar IOException para directorio")
        void shouldThrowIOException_WhenPathIsDirectory() {
            assertThrows(IOException.class, () -> reader.readFile(tempDir));
        }

        @Test
        @DisplayName("Debe leer archivo grande usando streaming (superando threshold)")
        void shouldReadLargeFile_UsingStreaming() throws IOException {
            // Crear un reader con threshold muy bajo para forzar streaming
            JSONReader.ReaderConfig config = JSONReader.ReaderConfig.defaultConfig()
                    .smallFileThreshold(10L); // 10 bytes => cualquier cosa se lee con streaming

            JSONReader smallThresholdReader = new JSONReader(config);

            String json = "{\"large\": true, \"data\": \"content with some text to exceed threshold\"}";
            Path jsonFile = tempDir.resolve("large_threshold.json");
            Files.writeString(jsonFile, json);

            JSONObject result = smallThresholdReader.readFile(jsonFile);

            assertNotNull(result);
            assertTrue(result.getBoolean("large", false));
            smallThresholdReader.close();
        }

        @Test
        @DisplayName("Debe leer archivo con encoding UTF-8")
        void shouldReadFile_WithUTF8Encoding() throws IOException {
            String json = "{\"texto\":\"cafe con acento: cafe\",\"emoji\":\"test\"}";
            Path jsonFile = tempDir.resolve("utf8.json");
            Files.writeString(jsonFile, json, StandardCharsets.UTF_8);

            JSONObject result = reader.readFile(jsonFile);
            assertNotNull(result.getString("texto"));
        }
    }

    // ================================================================
    // readLargeFile() con Consumer
    // ================================================================
    @Nested
    @DisplayName("Lectura streaming: readLargeFile()")
    class ReadLargeFileTests {

        @Test
        @DisplayName("Debe procesar array JSON con consumer")
        void shouldProcessArrayJSON_WithConsumer() throws IOException {
            String json = """
                    [
                        {"id": 1, "name": "Alice"},
                        {"id": 2, "name": "Bob"},
                        {"id": 3, "name": "Charlie"}
                    ]
                    """;
            Path jsonFile = tempDir.resolve("array.json");
            Files.writeString(jsonFile, json);

            List<JSONObject> results = new ArrayList<>();
            reader.readLargeFile(jsonFile, results::add);

            assertEquals(3, results.size());
            assertEquals(1, results.get(0).getInt("id", 0));
            assertEquals("Bob", results.get(1).getString("name"));
        }

        @Test
        @DisplayName("Debe procesar objeto JSON unico con consumer")
        void shouldProcessSingleObject_WithConsumer() throws IOException {
            String json = "{\"single\": true, \"value\": 42}";
            Path jsonFile = tempDir.resolve("single.json");
            Files.writeString(jsonFile, json);

            List<JSONObject> results = new ArrayList<>();
            reader.readLargeFile(jsonFile, results::add);

            assertEquals(1, results.size());
            assertTrue(results.get(0).getBoolean("single", false));
        }

        @Test
        @DisplayName("Debe procesar array JSON con File")
        void shouldProcessArrayJSON_WithFile() throws IOException {
            String json = "[{\"a\":1},{\"a\":2}]";
            Path jsonFile = tempDir.resolve("array_file.json");
            Files.writeString(jsonFile, json);

            List<JSONObject> results = new ArrayList<>();
            reader.readLargeFile(jsonFile.toFile(), results::add);

            assertEquals(2, results.size());
        }

        @Test
        @DisplayName("Debe lanzar excepcion para archivo inexistente")
        void shouldThrowException_WhenFileDoesNotExist() {
            Path nonExistent = tempDir.resolve("nope.json");
            assertThrows(FileNotFoundException.class,
                    () -> reader.readLargeFile(nonExistent, obj -> {}));
        }
    }

    // ================================================================
    // readArrayInBatches()
    // ================================================================
    @Nested
    @DisplayName("Procesamiento batch: readArrayInBatches()")
    class ReadArrayInBatchesTests {

        @Test
        @DisplayName("Debe procesar array en batches del tamano especificado")
        void shouldProcessArray_InSpecifiedBatchSize() throws IOException {
            // Array de 5 elementos con batch de 2 => 3 batches (2+2+1)
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < 5; i++) {
                if (i > 0) sb.append(",");
                sb.append("{\"id\":").append(i).append("}");
            }
            sb.append("]");

            Path jsonFile = tempDir.resolve("batch.json");
            Files.writeString(jsonFile, sb.toString());

            List<List<JSONObject>> batches = new ArrayList<>();
            reader.readArrayInBatches(jsonFile, 2, batches::add);

            assertEquals(3, batches.size());
            assertEquals(2, batches.get(0).size());
            assertEquals(2, batches.get(1).size());
            assertEquals(1, batches.get(2).size());
        }

        @Test
        @DisplayName("Debe procesar array con batch mayor que el total de elementos")
        void shouldProcessAllInOneBatch_WhenBatchSizeExceedsTotal() throws IOException {
            String json = "[{\"a\":1},{\"a\":2},{\"a\":3}]";
            Path jsonFile = tempDir.resolve("small_batch.json");
            Files.writeString(jsonFile, json);

            List<List<JSONObject>> batches = new ArrayList<>();
            reader.readArrayInBatches(jsonFile, 100, batches::add);

            assertEquals(1, batches.size());
            assertEquals(3, batches.get(0).size());
        }

        @Test
        @DisplayName("Debe procesar array con batch de 1")
        void shouldProcessOneByOne_WhenBatchSizeIsOne() throws IOException {
            String json = "[{\"x\":1},{\"x\":2},{\"x\":3}]";
            Path jsonFile = tempDir.resolve("batch_one.json");
            Files.writeString(jsonFile, json);

            List<List<JSONObject>> batches = new ArrayList<>();
            reader.readArrayInBatches(jsonFile, 1, batches::add);

            assertEquals(3, batches.size());
            for (List<JSONObject> batch : batches) {
                assertEquals(1, batch.size());
            }
        }

        @Test
        @DisplayName("Debe funcionar con File en lugar de Path")
        void shouldWork_WithFileParameter() throws IOException {
            String json = "[{\"v\":1},{\"v\":2}]";
            Path jsonFile = tempDir.resolve("batch_file.json");
            Files.writeString(jsonFile, json);

            List<List<JSONObject>> batches = new ArrayList<>();
            reader.readArrayInBatches(jsonFile.toFile(), 5, batches::add);

            assertEquals(1, batches.size());
            assertEquals(2, batches.get(0).size());
        }

        @Test
        @DisplayName("Debe lanzar excepcion para archivo que no contiene array")
        void shouldThrowException_WhenFileIsNotArray() throws IOException {
            String json = "{\"not\":\"array\"}";
            Path jsonFile = tempDir.resolve("not_array.json");
            Files.writeString(jsonFile, json);

            assertThrows(IllegalArgumentException.class,
                    () -> reader.readArrayInBatches(jsonFile, 10, batch -> {}));
        }

        @Test
        @DisplayName("Debe lanzar excepcion para archivo inexistente")
        void shouldThrowException_WhenFileDoesNotExist() {
            Path nonExistent = tempDir.resolve("missing_batch.json");
            assertThrows(FileNotFoundException.class,
                    () -> reader.readArrayInBatches(nonExistent, 10, batch -> {}));
        }

        @Test
        @DisplayName("Debe procesar array vacio sin invocar el consumer")
        void shouldNotInvokeConsumer_WhenArrayIsEmpty() throws IOException {
            String json = "[]";
            Path jsonFile = tempDir.resolve("empty_array.json");
            Files.writeString(jsonFile, json);

            List<List<JSONObject>> batches = new ArrayList<>();
            reader.readArrayInBatches(jsonFile, 10, batches::add);

            assertTrue(batches.isEmpty(), "No debe haber batches para array vacio");
        }
    }

    // ================================================================
    // parseString()
    // ================================================================
    @Nested
    @DisplayName("Parsing desde string: parseString()")
    class ParseStringTests {

        @Test
        @DisplayName("Debe parsear string JSON valido")
        void shouldParseValidJSONString() throws JsonProcessingException {
            JSONObject result = reader.parseString("{\"key\":\"value\"}");
            assertEquals("value", result.getString("key"));
        }

        @Test
        @DisplayName("Debe retornar objeto vacio para null")
        void shouldReturnEmptyObject_ForNull() throws JsonProcessingException {
            JSONObject result = reader.parseString(null);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Debe retornar objeto vacio para string vacio")
        void shouldReturnEmptyObject_ForEmptyString() throws JsonProcessingException {
            JSONObject result = reader.parseString("");
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Debe retornar objeto vacio para string con solo espacios")
        void shouldReturnEmptyObject_ForBlankString() throws JsonProcessingException {
            JSONObject result = reader.parseString("   ");
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Debe parsear JSON complejo")
        void shouldParseComplexJSON() throws JsonProcessingException {
            String json = """
                    {
                        "users": [
                            {"name": "Alice", "age": 30},
                            {"name": "Bob", "age": 25}
                        ],
                        "count": 2
                    }
                    """;
            JSONObject result = reader.parseString(json);

            assertEquals(2, result.getInt("count", 0));
        }

        @Test
        @DisplayName("Debe detectar automaticamente formato JSONC")
        void shouldAutoDetectJSONCFormat() throws JsonProcessingException {
            String json = """
                    {
                        // comentario
                        "key": "value"
                    }
                    """;
            JSONObject result = reader.parseString(json);
            assertEquals("value", result.getString("key"));
        }

        @Test
        @DisplayName("Debe lanzar excepcion para JSON invalido")
        void shouldThrowException_ForInvalidJSON() {
            assertThrows(JsonProcessingException.class,
                    () -> reader.parseString("{broken json}"));
        }
    }

    // ================================================================
    // parseReader()
    // ================================================================
    @Nested
    @DisplayName("Parsing desde Reader: parseReader()")
    class ParseReaderTests {

        @Test
        @DisplayName("Debe parsear desde StringReader")
        void shouldParseFromStringReader() throws IOException {
            Reader stringReader = new StringReader("{\"from\":\"reader\"}");
            JSONObject result = reader.parseReader(stringReader);

            assertEquals("reader", result.getString("from"));
        }

        @Test
        @DisplayName("Debe parsear desde BufferedReader")
        void shouldParseFromBufferedReader() throws IOException {
            Reader buffered = new BufferedReader(new StringReader("{\"buffered\":true}"));
            JSONObject result = reader.parseReader(buffered);

            assertTrue(result.getBoolean("buffered", false));
        }

        @Test
        @DisplayName("Debe lanzar excepcion para contenido que no es objeto")
        void shouldThrowException_WhenContentIsNotObject() {
            Reader arrayReader = new StringReader("[1, 2, 3]");
            assertThrows(IllegalArgumentException.class,
                    () -> reader.parseReader(arrayReader));
        }

        @Test
        @DisplayName("Debe parsear desde FileReader")
        void shouldParseFromFileReader() throws IOException {
            Path jsonFile = tempDir.resolve("file_reader.json");
            Files.writeString(jsonFile, "{\"source\":\"file\"}");

            try (Reader fileReader = Files.newBufferedReader(jsonFile, StandardCharsets.UTF_8)) {
                JSONObject result = reader.parseReader(fileReader);
                assertEquals("file", result.getString("source"));
            }
        }
    }

    // ================================================================
    // Streaming: startStreaming(), nextObject()
    // ================================================================
    @Nested
    @DisplayName("Streaming manual: startStreaming() / nextObject()")
    class ManualStreamingTests {

        @Test
        @DisplayName("Debe lanzar excepcion si nextObject() se llama sin streaming activo")
        void shouldThrowException_WhenNoStreamingActive() {
            assertThrows(IllegalStateException.class, () -> reader.nextObject());
        }

        @Test
        @DisplayName("Debe hacer streaming de un solo objeto")
        void shouldStreamSingleObject() throws IOException {
            Path jsonFile = tempDir.resolve("stream_single.json");
            Files.writeString(jsonFile, "{\"streamed\":true}");

            reader.startStreaming(jsonFile);
            JSONObject first = reader.nextObject();

            assertNotNull(first);
            assertTrue(first.getBoolean("streamed", false));
        }

        @Test
        @DisplayName("nextObject con array retorna null en primer token START_ARRAY")
        void shouldReturnNull_WhenFirstTokenIsStartArray() throws IOException {
            // nextObject() solo procesa START_OBJECT como token valido.
            // Para arrays, la API correcta es readLargeFile() o stream().
            // Al encontrar START_ARRAY, nextObject retorna null porque no es START_OBJECT.
            String json = "[{\"id\":1},{\"id\":2}]";
            Path jsonFile = tempDir.resolve("stream_array.json");
            Files.writeString(jsonFile, json);

            reader.startStreaming(jsonFile);

            // El primer token es START_ARRAY, que nextObject no maneja => retorna null
            JSONObject first = reader.nextObject();
            assertNull(first,
                    "nextObject debe retornar null para START_ARRAY; usar stream() o readLargeFile() para arrays");
        }

        @Test
        @DisplayName("Debe poder iniciar nuevo streaming despues de cerrar anterior")
        void shouldRestartStreaming_AfterClosure() throws IOException {
            Path file1 = tempDir.resolve("stream1.json");
            Files.writeString(file1, "{\"file\":1}");

            Path file2 = tempDir.resolve("stream2.json");
            Files.writeString(file2, "{\"file\":2}");

            reader.startStreaming(file1);
            // Iniciar otro streaming cierra el anterior
            reader.startStreaming(file2);

            JSONObject result = reader.nextObject();
            assertNotNull(result);
            assertEquals(2, result.getInt("file", 0));
        }

        @Test
        @DisplayName("startStreaming con File debe funcionar igual que con Path")
        void shouldWork_WithFileParameter() throws IOException {
            Path jsonFile = tempDir.resolve("stream_file.json");
            Files.writeString(jsonFile, "{\"via\":\"file\"}");

            reader.startStreaming(jsonFile.toFile());
            JSONObject result = reader.nextObject();

            assertNotNull(result);
            assertEquals("file", result.getString("via"));
        }

        @Test
        @DisplayName("startStreaming debe lanzar excepcion para archivo inexistente")
        void shouldThrowException_WhenFileDoesNotExist() {
            Path nonExistent = tempDir.resolve("no_stream.json");
            assertThrows(FileNotFoundException.class,
                    () -> reader.startStreaming(nonExistent));
        }

        @Test
        @DisplayName("startStreaming debe lanzar excepcion para directorio")
        void shouldThrowException_WhenPathIsDirectory() {
            assertThrows(IOException.class, () -> reader.startStreaming(tempDir));
        }
    }

    // ================================================================
    // stream(Path) - Stream API
    // ================================================================
    @Nested
    @DisplayName("Stream API: stream(Path)")
    class StreamAPITests {

        @Test
        @DisplayName("Debe crear stream desde array JSON")
        void shouldCreateStream_FromJSONArray() throws IOException {
            String json = "[{\"id\":1},{\"id\":2},{\"id\":3}]";
            Path jsonFile = tempDir.resolve("stream_api.json");
            Files.writeString(jsonFile, json);

            try (Stream<JSONObject> stream = reader.stream(jsonFile)) {
                List<JSONObject> results = stream.collect(Collectors.toList());
                assertEquals(3, results.size());
                assertEquals(1, results.get(0).getInt("id", 0));
                assertEquals(3, results.get(2).getInt("id", 0));
            }
        }

        @Test
        @DisplayName("Debe soportar operaciones de filter y map en el stream")
        void shouldSupportFilterAndMap_InStream() throws IOException {
            String json = """
                    [
                        {"name": "Alice", "age": 30},
                        {"name": "Bob", "age": 20},
                        {"name": "Charlie", "age": 35}
                    ]
                    """;
            Path jsonFile = tempDir.resolve("stream_filter.json");
            Files.writeString(jsonFile, json);

            try (Stream<JSONObject> stream = reader.stream(jsonFile)) {
                List<String> names = stream
                        .filter(obj -> obj.getInt("age", 0) >= 25)
                        .map(obj -> obj.getString("name"))
                        .collect(Collectors.toList());

                assertEquals(2, names.size());
                assertTrue(names.contains("Alice"));
                assertTrue(names.contains("Charlie"));
            }
        }

        @Test
        @DisplayName("Debe crear stream vacio desde array JSON vacio")
        void shouldCreateEmptyStream_FromEmptyArray() throws IOException {
            Path jsonFile = tempDir.resolve("empty_stream.json");
            Files.writeString(jsonFile, "[]");

            try (Stream<JSONObject> stream = reader.stream(jsonFile)) {
                assertEquals(0, stream.count());
            }
        }

        @Test
        @DisplayName("Debe lanzar excepcion si el archivo no es array")
        void shouldThrowException_WhenFileIsNotArray() throws IOException {
            Path jsonFile = tempDir.resolve("not_array_stream.json");
            Files.writeString(jsonFile, "{\"not\":\"array\"}");

            assertThrows(IllegalArgumentException.class, () -> reader.stream(jsonFile));
        }

        @Test
        @DisplayName("Debe lanzar excepcion para archivo inexistente")
        void shouldThrowException_WhenFileDoesNotExist() {
            Path nonExistent = tempDir.resolve("no_stream_api.json");
            assertThrows(FileNotFoundException.class, () -> reader.stream(nonExistent));
        }
    }

    // ================================================================
    // validateAgainstSchema()
    // ================================================================
    @Nested
    @DisplayName("Validacion contra esquema: validateAgainstSchema()")
    class ValidateSchemaTests {

        @Test
        @DisplayName("Debe retornar true cuando el esquema existe (validacion basica)")
        void shouldReturnTrue_WhenSchemaExists() throws IOException {
            // La implementacion actual solo verifica que el esquema existe
            Path schemaFile = tempDir.resolve("schema.json");
            Files.writeString(schemaFile, """
                    {
                        "type": "object",
                        "properties": {
                            "name": {"type": "string"}
                        }
                    }
                    """);

            JSONObject json = new JSONObject().put("name", "test");
            boolean isValid = reader.validateAgainstSchema(json, schemaFile);

            assertTrue(isValid);
        }

        @Test
        @DisplayName("Debe lanzar excepcion para esquema inexistente")
        void shouldThrowException_WhenSchemaDoesNotExist() {
            Path nonExistent = tempDir.resolve("no_schema.json");
            JSONObject json = new JSONObject().put("key", "value");

            assertThrows(RuntimeException.class,
                    () -> reader.validateAgainstSchema(json, nonExistent));
        }
    }

    // ================================================================
    // close() y gestion de recursos
    // ================================================================
    @Nested
    @DisplayName("Gestion de recursos: close()")
    class ResourceManagementTests {

        @Test
        @DisplayName("close() sin streaming activo no debe lanzar excepcion")
        void shouldNotThrow_WhenClosingWithNoStreaming() {
            assertDoesNotThrow(() -> reader.close());
        }

        @Test
        @DisplayName("close() multiples veces no debe lanzar excepcion")
        void shouldNotThrow_WhenClosingMultipleTimes() {
            assertDoesNotThrow(() -> {
                reader.close();
                reader.close();
                reader.close();
            });
        }

        @Test
        @DisplayName("close() debe cerrar el streaming activo")
        void shouldCloseActiveStreaming() throws IOException {
            Path jsonFile = tempDir.resolve("close_test.json");
            Files.writeString(jsonFile, "{\"key\":\"value\"}");

            reader.startStreaming(jsonFile);
            assertDoesNotThrow(() -> reader.close());

            // Despues de cerrar, nextObject debe lanzar excepcion
            assertThrows(IllegalStateException.class, () -> reader.nextObject());
        }

        @Test
        @DisplayName("Debe funcionar con try-with-resources")
        void shouldWorkWithTryWithResources() throws IOException {
            Path jsonFile = tempDir.resolve("twr.json");
            Files.writeString(jsonFile, "{\"auto\":\"close\"}");

            JSONObject result;
            try (JSONReader autoReader = new JSONReader()) {
                result = autoReader.readFile(jsonFile);
            }

            assertNotNull(result);
            assertEquals("close", result.getString("auto"));
        }
    }

    // ================================================================
    // Validacion de archivos
    // ================================================================
    @Nested
    @DisplayName("Validacion de archivos")
    class FileValidationTests {

        @Test
        @DisplayName("Debe lanzar FileNotFoundException para archivo que no existe")
        void shouldThrowFileNotFoundException_ForMissingFile() {
            Path missing = tempDir.resolve("absolutely_missing.json");
            assertThrows(FileNotFoundException.class, () -> reader.readFile(missing));
        }

        @Test
        @DisplayName("Debe lanzar IOException para directorio")
        void shouldThrowIOException_ForDirectory() {
            assertThrows(IOException.class, () -> reader.readFile(tempDir));
        }

        @Test
        @DisplayName("readLargeFile debe validar archivo")
        void shouldValidateFile_InReadLargeFile() {
            Path missing = tempDir.resolve("missing_large.json");
            assertThrows(FileNotFoundException.class,
                    () -> reader.readLargeFile(missing, obj -> {}));
        }

        @Test
        @DisplayName("readArrayInBatches debe validar archivo")
        void shouldValidateFile_InReadArrayInBatches() {
            Path missing = tempDir.resolve("missing_batch.json");
            assertThrows(FileNotFoundException.class,
                    () -> reader.readArrayInBatches(missing, 10, batch -> {}));
        }

        @Test
        @DisplayName("stream debe validar archivo")
        void shouldValidateFile_InStream() {
            Path missing = tempDir.resolve("missing_stream.json");
            assertThrows(FileNotFoundException.class,
                    () -> reader.stream(missing));
        }
    }

    // ================================================================
    // readURLAsync (solo test de estructura, sin red real)
    // ================================================================
    @Nested
    @DisplayName("Lectura asincrona: readURLAsync()")
    class AsyncURLTests {

        @Test
        @DisplayName("readURLAsync debe retornar CompletableFuture no nulo")
        void shouldReturnNonNullFuture() {
            // Usamos una URL que probablemente fallara, pero verificamos que retorna el future
            CompletableFuture<JSONObject> future = reader.readURLAsync("http://localhost:1/not-a-real-endpoint");
            assertNotNull(future);
            // No esperamos el resultado porque la URL no es real
        }

        @Test
        @DisplayName("readURLAsync debe completar con error para URL invalida")
        void shouldCompleteWithError_ForInvalidURL() {
            CompletableFuture<JSONObject> future = reader.readURLAsync("http://localhost:1/fake");

            assertThrows(Exception.class, () -> {
                try {
                    future.get(5, TimeUnit.SECONDS);
                } catch (java.util.concurrent.ExecutionException e) {
                    throw e.getCause();
                }
            });
        }
    }

    // ================================================================
    // Deteccion de formato en contexto de lectura
    // ================================================================
    @Nested
    @DisplayName("Deteccion de formato")
    class FormatDetectionTests {

        @Test
        @DisplayName("Debe leer archivo JSONC con comentarios correctamente")
        void shouldReadJSONCFile_WithComments() throws IOException {
            String jsonc = """
                    {
                        // Configuracion del servidor
                        "host": "localhost",
                        /* Puerto por defecto */
                        "port": 8080
                    }
                    """;
            Path jsonFile = tempDir.resolve("config.jsonc");
            Files.writeString(jsonFile, jsonc);

            JSONObject result = reader.parseString(jsonc);
            assertEquals("localhost", result.getString("host"));
        }

        @Test
        @DisplayName("Debe leer JSON estandar sin problemas")
        void shouldReadStandardJSON() throws IOException {
            Path jsonFile = tempDir.resolve("standard.json");
            Files.writeString(jsonFile, "{\"standard\":true}");

            JSONObject result = reader.readFile(jsonFile);
            assertTrue(result.getBoolean("standard", false));
        }

        @Test
        @DisplayName("Debe auto-detectar y parsear JSONC con reader configurado como STANDARD")
        void shouldAutoDetectJSONC_WithStandardConfig() throws JsonProcessingException {
            String jsonc = """
                    {
                        // comentario
                        "detected": true
                    }
                    """;
            // El reader con config standard debe auto-detectar el formato
            JSONObject result = reader.parseString(jsonc);
            assertTrue(result.getBoolean("detected", false));
        }
    }

    // ================================================================
    // Edge cases y robustez
    // ================================================================
    @Nested
    @DisplayName("Edge Cases y Robustez")
    class EdgeCaseTests {

        @Test
        @DisplayName("Debe manejar archivo con objeto JSON vacio")
        void shouldHandleEmptyJSONObject() throws IOException {
            Path jsonFile = tempDir.resolve("empty_obj.json");
            Files.writeString(jsonFile, "{}");

            JSONObject result = reader.readFile(jsonFile);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Debe manejar archivo con JSON minificado")
        void shouldHandleMinifiedJSON() throws IOException {
            String json = "{\"a\":1,\"b\":\"text\",\"c\":true,\"d\":{\"e\":2}}";
            Path jsonFile = tempDir.resolve("minified.json");
            Files.writeString(jsonFile, json);

            JSONObject result = reader.readFile(jsonFile);
            assertEquals(4, result.size());
        }

        @Test
        @DisplayName("Debe manejar archivo con JSON con muchos espacios y newlines")
        void shouldHandleFormattedJSON_WithExtraWhitespace() throws IOException {
            String json = """


                    {

                        "key"   :    "value"

                    }

                    """;
            Path jsonFile = tempDir.resolve("whitespace.json");
            Files.writeString(jsonFile, json);

            JSONObject result = reader.readFile(jsonFile);
            assertEquals("value", result.getString("key"));
        }

        @Test
        @DisplayName("Debe manejar batch con array de un solo elemento")
        void shouldHandleBatch_WithSingleElement() throws IOException {
            String json = "[{\"solo\":true}]";
            Path jsonFile = tempDir.resolve("single_element.json");
            Files.writeString(jsonFile, json);

            List<List<JSONObject>> batches = new ArrayList<>();
            reader.readArrayInBatches(jsonFile, 10, batches::add);

            assertEquals(1, batches.size());
            assertEquals(1, batches.get(0).size());
        }

        @Test
        @DisplayName("Debe manejar lectura de archivo con caracteres unicode")
        void shouldHandleUnicodeContent() throws IOException {
            String json = "{\"nombre\":\"Jose\",\"ciudad\":\"Madrid\"}";
            Path jsonFile = tempDir.resolve("unicode.json");
            Files.writeString(jsonFile, json, StandardCharsets.UTF_8);

            JSONObject result = reader.readFile(jsonFile);
            assertNotNull(result.getString("nombre"));
            assertNotNull(result.getString("ciudad"));
        }

        @Test
        @DisplayName("Debe manejar array grande en batches correctamente")
        void shouldHandleLargeArray_InBatches() throws IOException {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < 100; i++) {
                if (i > 0) sb.append(",");
                sb.append("{\"index\":").append(i).append("}");
            }
            sb.append("]");

            Path jsonFile = tempDir.resolve("large_array.json");
            Files.writeString(jsonFile, sb.toString());

            List<Integer> batchSizes = new ArrayList<>();
            reader.readArrayInBatches(jsonFile, 30, batch -> batchSizes.add(batch.size()));

            // 100 elementos / batch 30 = 4 batches (30+30+30+10)
            assertEquals(4, batchSizes.size());
            assertEquals(30, batchSizes.get(0));
            assertEquals(30, batchSizes.get(1));
            assertEquals(30, batchSizes.get(2));
            assertEquals(10, batchSizes.get(3));
        }
    }
}
