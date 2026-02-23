package com.the_ultimate_toolbox.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de tests para {@link JacksonEngine}.
 * Verifica el patron Singleton, la configuracion de ObjectMappers,
 * la auto-deteccion de formatos JSON/JSON5/JSONC, la creacion de
 * generators y parsers, y la seguridad en contextos multihilo.
 *
 * @author The Ultimate Toolbox Team
 * @since 2.2.0
 */
@DisplayName("JacksonEngine Tests")
class JacksonEngineTest {

    private JacksonEngine engine;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        engine = JacksonEngine.getInstance();
    }

    // ================================================================
    // Singleton
    // ================================================================
    @Nested
    @DisplayName("Singleton Pattern")
    class SingletonTests {

        @Test
        @DisplayName("Debe retornar siempre la misma instancia con getInstance()")
        void shouldReturnSameInstance_WhenCalledMultipleTimes() {
            JacksonEngine instance1 = JacksonEngine.getInstance();
            JacksonEngine instance2 = JacksonEngine.getInstance();

            assertSame(instance1, instance2,
                    "getInstance() debe retornar la misma referencia de objeto");
        }

        @Test
        @DisplayName("Debe ser thread-safe retornando la misma instancia desde multiples hilos")
        void shouldReturnSameInstance_WhenCalledFromMultipleThreads() throws Exception {
            int threadCount = 50;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(1);
            List<Future<JacksonEngine>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    latch.await(); // Esperar a que todos los hilos esten listos
                    return JacksonEngine.getInstance();
                }));
            }

            latch.countDown(); // Liberar todos los hilos a la vez

            JacksonEngine expected = JacksonEngine.getInstance();
            for (Future<JacksonEngine> future : futures) {
                assertSame(expected, future.get(5, TimeUnit.SECONDS),
                        "Todas las instancias deben ser la misma referencia");
            }

            executor.shutdown();
            assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        }
    }

    // ================================================================
    // ObjectMappers
    // ================================================================
    @Nested
    @DisplayName("ObjectMapper Configuration")
    class MapperTests {

        @Test
        @DisplayName("Debe retornar un ObjectMapper standard no nulo")
        void shouldReturnNonNullStandardMapper() {
            ObjectMapper mapper = engine.getStandardMapper();
            assertNotNull(mapper, "El mapper standard no debe ser null");
        }

        @Test
        @DisplayName("Debe retornar un ObjectMapper JSON5 no nulo")
        void shouldReturnNonNullJSON5Mapper() {
            ObjectMapper mapper = engine.getJSON5Mapper();
            assertNotNull(mapper, "El mapper JSON5 no debe ser null");
        }

        @Test
        @DisplayName("Debe retornar un ObjectMapper JSONC no nulo")
        void shouldReturnNonNullJSONCMapper() {
            ObjectMapper mapper = engine.getJSONCMapper();
            assertNotNull(mapper, "El mapper JSONC no debe ser null");
        }

        @Test
        @DisplayName("Los tres mappers deben ser instancias diferentes")
        void shouldReturnDifferentMapperInstances() {
            ObjectMapper standard = engine.getStandardMapper();
            ObjectMapper json5 = engine.getJSON5Mapper();
            ObjectMapper jsonc = engine.getJSONCMapper();

            assertNotSame(standard, json5, "Standard y JSON5 deben ser instancias diferentes");
            assertNotSame(standard, jsonc, "Standard y JSONC deben ser instancias diferentes");
            // JSON5 y JSONC actualmente comparten factory, pero deben ser mappers diferentes
            assertNotSame(json5, jsonc, "JSON5 y JSONC deben ser instancias diferentes");
        }

        @Test
        @DisplayName("El mapper standard debe retornar siempre la misma instancia")
        void shouldReturnSameStandardMapperInstance_WhenCalledMultipleTimes() {
            ObjectMapper first = engine.getStandardMapper();
            ObjectMapper second = engine.getStandardMapper();
            assertSame(first, second, "Debe retornar la misma instancia del mapper");
        }

        @Test
        @DisplayName("El mapper standard debe estar configurado para ignorar propiedades desconocidas")
        void shouldIgnoreUnknownProperties_WithStandardMapper() throws Exception {
            ObjectMapper mapper = engine.getStandardMapper();
            // JSON con propiedad extra que no existe en un Map simple
            String json = "{\"known\":\"value\",\"extra\":123}";
            JsonNode node = mapper.readTree(json);

            assertNotNull(node);
            assertEquals("value", node.get("known").asText());
            assertEquals(123, node.get("extra").asInt());
        }

        @Test
        @DisplayName("El mapper standard debe permitir campos sin comillas")
        void shouldAllowUnquotedFieldNames_WithStandardMapper() throws Exception {
            ObjectMapper mapper = engine.getStandardMapper();
            String json = "{unquoted: \"value\"}";
            JsonNode node = mapper.readTree(json);

            assertNotNull(node);
            assertEquals("value", node.get("unquoted").asText());
        }

        @Test
        @DisplayName("El mapper standard debe permitir comillas simples")
        void shouldAllowSingleQuotes_WithStandardMapper() throws Exception {
            ObjectMapper mapper = engine.getStandardMapper();
            String json = "{'key': 'value'}";
            JsonNode node = mapper.readTree(json);

            assertNotNull(node);
            assertEquals("value", node.get("key").asText());
        }

        @Test
        @DisplayName("El mapper JSONC debe soportar comentarios de linea")
        void shouldSupportLineComments_WithJSONCMapper() throws Exception {
            ObjectMapper mapper = engine.getJSONCMapper();
            String json = """
                    {
                        // Este es un comentario
                        "key": "value"
                    }
                    """;
            JsonNode node = mapper.readTree(json);

            assertNotNull(node);
            assertEquals("value", node.get("key").asText());
        }

        @Test
        @DisplayName("El mapper JSONC debe soportar comentarios de bloque")
        void shouldSupportBlockComments_WithJSONCMapper() throws Exception {
            ObjectMapper mapper = engine.getJSONCMapper();
            String json = """
                    {
                        /* Este es un comentario
                           de bloque */
                        "key": "value"
                    }
                    """;
            JsonNode node = mapper.readTree(json);

            assertNotNull(node);
            assertEquals("value", node.get("key").asText());
        }

        @Test
        @DisplayName("El mapper JSONC debe soportar trailing commas")
        void shouldSupportTrailingCommas_WithJSONCMapper() throws Exception {
            ObjectMapper mapper = engine.getJSONCMapper();
            String json = """
                    {
                        "key1": "value1",
                        "key2": "value2",
                    }
                    """;
            JsonNode node = mapper.readTree(json);

            assertNotNull(node);
            assertEquals("value1", node.get("key1").asText());
            assertEquals("value2", node.get("key2").asText());
        }

        @Test
        @DisplayName("El mapper standard debe parsear JSON valido correctamente")
        void shouldParseValidJSON_WithStandardMapper() throws Exception {
            ObjectMapper mapper = engine.getStandardMapper();
            String json = """
                    {
                        "string": "hello",
                        "number": 42,
                        "decimal": 3.14,
                        "boolean": true,
                        "null_val": null,
                        "array": [1, 2, 3],
                        "nested": {"a": "b"}
                    }
                    """;
            JsonNode node = mapper.readTree(json);

            assertEquals("hello", node.get("string").asText());
            assertEquals(42, node.get("number").asInt());
            assertEquals(3.14, node.get("decimal").asDouble(), 0.001);
            assertTrue(node.get("boolean").asBoolean());
            assertTrue(node.get("null_val").isNull());
            assertTrue(node.get("array").isArray());
            assertEquals(3, node.get("array").size());
            assertTrue(node.get("nested").isObject());
        }
    }

    // ================================================================
    // Auto-deteccion de formato
    // ================================================================
    @Nested
    @DisplayName("Deteccion automatica de formato")
    class FormatDetectionTests {

        @Test
        @DisplayName("Debe retornar standard mapper para null")
        void shouldReturnStandardMapper_WhenContentIsNull() {
            ObjectMapper mapper = engine.detectAndGetMapper(null);
            assertSame(engine.getStandardMapper(), mapper);
        }

        @Test
        @DisplayName("Debe retornar standard mapper para string vacio")
        void shouldReturnStandardMapper_WhenContentIsEmpty() {
            ObjectMapper mapper = engine.detectAndGetMapper("");
            assertSame(engine.getStandardMapper(), mapper);
        }

        @Test
        @DisplayName("Debe retornar standard mapper para string con solo espacios")
        void shouldReturnStandardMapper_WhenContentIsBlank() {
            ObjectMapper mapper = engine.detectAndGetMapper("   ");
            assertSame(engine.getStandardMapper(), mapper);
        }

        @Test
        @DisplayName("Debe retornar standard mapper para JSON estandar")
        void shouldReturnStandardMapper_WhenContentIsStandardJSON() {
            String json = "{\"key\": \"value\", \"num\": 42}";
            ObjectMapper mapper = engine.detectAndGetMapper(json);
            assertSame(engine.getStandardMapper(), mapper);
        }

        @Test
        @DisplayName("Debe retornar JSONC mapper para contenido con comentarios de linea")
        void shouldReturnJSONCMapper_WhenContentHasLineComments() {
            String json = """
                    {
                        // comentario
                        "key": "value"
                    }
                    """;
            ObjectMapper mapper = engine.detectAndGetMapper(json);
            assertSame(engine.getJSONCMapper(), mapper);
        }

        @Test
        @DisplayName("Debe retornar JSONC mapper para contenido con comentarios de bloque")
        void shouldReturnJSONCMapper_WhenContentHasBlockComments() {
            String json = """
                    {
                        /* comentario */
                        "key": "value"
                    }
                    """;
            ObjectMapper mapper = engine.detectAndGetMapper(json);
            assertSame(engine.getJSONCMapper(), mapper);
        }

        @Test
        @DisplayName("Debe retornar JSON5 mapper para contenido con Infinity")
        void shouldReturnJSON5Mapper_WhenContentHasInfinity() {
            String json = "{\"value\": Infinity}";
            ObjectMapper mapper = engine.detectAndGetMapper(json);
            assertSame(engine.getJSON5Mapper(), mapper);
        }

        @Test
        @DisplayName("Debe retornar JSON5 mapper para contenido con NaN")
        void shouldReturnJSON5Mapper_WhenContentHasNaN() {
            String json = "{\"value\": NaN}";
            ObjectMapper mapper = engine.detectAndGetMapper(json);
            assertSame(engine.getJSON5Mapper(), mapper);
        }

        @Test
        @DisplayName("Debe retornar JSON5 mapper para contenido con -Infinity")
        void shouldReturnJSON5Mapper_WhenContentHasNegativeInfinity() {
            String json = "{\"value\": -Infinity}";
            ObjectMapper mapper = engine.detectAndGetMapper(json);
            assertSame(engine.getJSON5Mapper(), mapper);
        }

        @Test
        @DisplayName("Debe retornar JSON5 mapper para contenido con trailing comma")
        void shouldReturnJSON5Mapper_WhenContentHasTrailingComma() {
            String json = "{\"key\": \"value\",}";
            ObjectMapper mapper = engine.detectAndGetMapper(json);
            assertSame(engine.getJSON5Mapper(), mapper);
        }

        @Test
        @DisplayName("Debe retornar JSON5 mapper para contenido con comentarios e Infinity")
        void shouldReturnJSON5Mapper_WhenContentHasCommentsAndInfinity() {
            String json = """
                    {
                        // comentario
                        "value": Infinity
                    }
                    """;
            ObjectMapper mapper = engine.detectAndGetMapper(json);
            assertSame(engine.getJSON5Mapper(), mapper);
        }

        @Test
        @DisplayName("Debe retornar JSON5 mapper para contenido con comentarios y NaN")
        void shouldReturnJSON5Mapper_WhenContentHasCommentsAndNaN() {
            String json = """
                    {
                        /* bloque */
                        "value": NaN
                    }
                    """;
            ObjectMapper mapper = engine.detectAndGetMapper(json);
            assertSame(engine.getJSON5Mapper(), mapper);
        }
    }

    // ================================================================
    // Generators
    // ================================================================
    @Nested
    @DisplayName("JsonGenerator Creation")
    class GeneratorTests {

        @Test
        @DisplayName("Debe crear un generator sin pretty-print")
        void shouldCreateGenerator_WithoutPrettyPrint() throws Exception {
            StringWriter writer = new StringWriter();
            try (JsonGenerator generator = engine.createGenerator(writer, false)) {
                assertNotNull(generator, "El generator no debe ser null");

                generator.writeStartObject();
                generator.writeStringField("key", "value");
                generator.writeEndObject();
                generator.flush();
            }

            String result = writer.toString();
            assertFalse(result.contains("\n"),
                    "El output sin pretty-print no debe contener saltos de linea");
            assertTrue(result.contains("\"key\":\"value\""));
        }

        @Test
        @DisplayName("Debe crear un generator con pretty-print")
        void shouldCreateGenerator_WithPrettyPrint() throws Exception {
            StringWriter writer = new StringWriter();
            try (JsonGenerator generator = engine.createGenerator(writer, true)) {
                assertNotNull(generator, "El generator no debe ser null");

                generator.writeStartObject();
                generator.writeStringField("key", "value");
                generator.writeEndObject();
                generator.flush();
            }

            String result = writer.toString();
            assertTrue(result.contains("\n"),
                    "El output con pretty-print debe contener saltos de linea");
        }

        @Test
        @DisplayName("Debe manejar escritura de tipos variados en el generator")
        void shouldWriteVariousTypes_WithGenerator() throws Exception {
            StringWriter writer = new StringWriter();
            try (JsonGenerator generator = engine.createGenerator(writer, false)) {
                generator.writeStartObject();
                generator.writeStringField("text", "hello");
                generator.writeNumberField("integer", 42);
                generator.writeNumberField("decimal", 3.14);
                generator.writeBooleanField("flag", true);
                generator.writeNullField("nothing");
                generator.writeEndObject();
                generator.flush();
            }

            String result = writer.toString();
            assertTrue(result.contains("\"text\":\"hello\""));
            assertTrue(result.contains("\"integer\":42"));
            assertTrue(result.contains("\"flag\":true"));
            assertTrue(result.contains("\"nothing\":null"));
        }
    }

    // ================================================================
    // Parsers
    // ================================================================
    @Nested
    @DisplayName("JsonParser Creation")
    class ParserTests {

        @Test
        @DisplayName("Debe crear un parser STANDARD para JSON valido")
        void shouldCreateStandardParser_ForValidJSON() throws Exception {
            String json = "{\"key\": \"value\"}";
            Reader reader = new StringReader(json);

            try (JsonParser parser = engine.createParser(reader, JacksonEngine.JSONFormat.STANDARD)) {
                assertNotNull(parser, "El parser no debe ser null");

                JsonNode node = engine.getStandardMapper().readTree(parser);
                assertEquals("value", node.get("key").asText());
            }
        }

        @Test
        @DisplayName("Debe crear un parser JSONC para JSON con comentarios")
        void shouldCreateJSONCParser_ForJSONWithComments() throws Exception {
            String json = """
                    {
                        // comentario
                        "key": "value"
                    }
                    """;
            Reader reader = new StringReader(json);

            try (JsonParser parser = engine.createParser(reader, JacksonEngine.JSONFormat.JSONC)) {
                assertNotNull(parser);

                JsonNode node = engine.getJSONCMapper().readTree(parser);
                assertEquals("value", node.get("key").asText());
            }
        }

        @Test
        @DisplayName("Debe crear un parser JSON5")
        void shouldCreateJSON5Parser() throws Exception {
            String json = "{\"key\": \"value\"}";
            Reader reader = new StringReader(json);

            try (JsonParser parser = engine.createParser(reader, JacksonEngine.JSONFormat.JSON5)) {
                assertNotNull(parser);

                JsonNode node = engine.getJSON5Mapper().readTree(parser);
                assertEquals("value", node.get("key").asText());
            }
        }

        @Test
        @DisplayName("Debe crear un buffered parser para archivo")
        void shouldCreateBufferedParser_ForFile() throws Exception {
            Path jsonFile = tempDir.resolve("test.json");
            Files.writeString(jsonFile, "{\"buffered\": true}");

            try (JsonParser parser = engine.createBufferedParser(
                    jsonFile.toFile(), JacksonEngine.JSONFormat.STANDARD, 8192)) {
                assertNotNull(parser);

                JsonNode node = engine.getStandardMapper().readTree(parser);
                assertTrue(node.get("buffered").asBoolean());
            }
        }

        @Test
        @DisplayName("Debe crear un buffered parser con buffer grande")
        void shouldCreateBufferedParser_WithLargeBuffer() throws Exception {
            Path jsonFile = tempDir.resolve("large_buffer.json");
            Files.writeString(jsonFile, "{\"size\": \"large\"}");

            try (JsonParser parser = engine.createBufferedParser(
                    jsonFile.toFile(), JacksonEngine.JSONFormat.STANDARD, 32768)) {
                assertNotNull(parser);

                JsonNode node = engine.getStandardMapper().readTree(parser);
                assertEquals("large", node.get("size").asText());
            }
        }

        @Test
        @DisplayName("Debe lanzar excepcion al crear buffered parser con archivo inexistente")
        void shouldThrowException_WhenFileDoesNotExist() {
            File noFile = new File(tempDir.toFile(), "nonexistent.json");
            assertThrows(FileNotFoundException.class,
                    () -> engine.createBufferedParser(noFile, JacksonEngine.JSONFormat.STANDARD, 8192));
        }

        @Test
        @DisplayName("Debe crear un buffered parser JSONC para archivo con comentarios")
        void shouldCreateBufferedJSONCParser_ForFileWithComments() throws Exception {
            Path jsonFile = tempDir.resolve("commented.json");
            Files.writeString(jsonFile, """
                    {
                        // comentario
                        "key": "value"
                    }
                    """);

            try (JsonParser parser = engine.createBufferedParser(
                    jsonFile.toFile(), JacksonEngine.JSONFormat.JSONC, 8192)) {
                JsonNode node = engine.getJSONCMapper().readTree(parser);
                assertEquals("value", node.get("key").asText());
            }
        }
    }

    // ================================================================
    // Cache
    // ================================================================
    @Nested
    @DisplayName("Cache Management")
    class CacheTests {

        @Test
        @DisplayName("clearCache() no debe lanzar excepcion")
        void shouldNotThrow_WhenClearingCache() {
            assertDoesNotThrow(() -> engine.clearCache());
        }

        @Test
        @DisplayName("clearCache() debe poder llamarse multiples veces sin problemas")
        void shouldClearCacheMultipleTimes_WithoutIssues() {
            assertDoesNotThrow(() -> {
                engine.clearCache();
                engine.clearCache();
                engine.clearCache();
            });
        }

        @Test
        @DisplayName("Los mappers deben funcionar correctamente despues de limpiar cache")
        void shouldMappersWorkCorrectly_AfterClearingCache() throws Exception {
            engine.clearCache();

            ObjectMapper mapper = engine.getStandardMapper();
            String json = "{\"after\": \"clear\"}";
            JsonNode node = mapper.readTree(json);

            assertEquals("clear", node.get("after").asText());
        }
    }

    // ================================================================
    // JSONFormat enum
    // ================================================================
    @Nested
    @DisplayName("JSONFormat Enum")
    class JSONFormatTests {

        @Test
        @DisplayName("El enum JSONFormat debe tener tres valores")
        void shouldHaveThreeValues() {
            JacksonEngine.JSONFormat[] values = JacksonEngine.JSONFormat.values();
            assertEquals(3, values.length);
        }

        @Test
        @DisplayName("El enum JSONFormat debe contener STANDARD, JSON5 y JSONC")
        void shouldContainExpectedValues() {
            assertNotNull(JacksonEngine.JSONFormat.STANDARD);
            assertNotNull(JacksonEngine.JSONFormat.JSON5);
            assertNotNull(JacksonEngine.JSONFormat.JSONC);
        }

        @Test
        @DisplayName("valueOf debe funcionar para cada formato")
        void shouldResolveByName() {
            assertEquals(JacksonEngine.JSONFormat.STANDARD,
                    JacksonEngine.JSONFormat.valueOf("STANDARD"));
            assertEquals(JacksonEngine.JSONFormat.JSON5,
                    JacksonEngine.JSONFormat.valueOf("JSON5"));
            assertEquals(JacksonEngine.JSONFormat.JSONC,
                    JacksonEngine.JSONFormat.valueOf("JSONC"));
        }
    }

    // ================================================================
    // Edge cases y robustez
    // ================================================================
    @Nested
    @DisplayName("Edge Cases y Robustez")
    class EdgeCaseTests {

        @Test
        @DisplayName("Debe parsear JSON con objetos profundamente anidados")
        void shouldParseDeeplyNestedJSON() throws Exception {
            String json = "{\"l1\":{\"l2\":{\"l3\":{\"l4\":{\"value\":\"deep\"}}}}}";
            ObjectMapper mapper = engine.getStandardMapper();
            JsonNode node = mapper.readTree(json);

            assertEquals("deep", node.at("/l1/l2/l3/l4/value").asText());
        }

        @Test
        @DisplayName("Debe parsear JSON con array vacio")
        void shouldParseJSONWithEmptyArray() throws Exception {
            String json = "{\"items\":[]}";
            ObjectMapper mapper = engine.getStandardMapper();
            JsonNode node = mapper.readTree(json);

            assertTrue(node.get("items").isArray());
            assertEquals(0, node.get("items").size());
        }

        @Test
        @DisplayName("Debe parsear JSON con objeto vacio")
        void shouldParseJSONWithEmptyObject() throws Exception {
            String json = "{}";
            ObjectMapper mapper = engine.getStandardMapper();
            JsonNode node = mapper.readTree(json);

            assertTrue(node.isObject());
            assertEquals(0, node.size());
        }

        @Test
        @DisplayName("Debe parsear JSON con caracteres unicode")
        void shouldParseJSONWithUnicode() throws Exception {
            String json = "{\"emoji\":\"\\u2764\",\"text\":\"cafe\\u0301\"}";
            ObjectMapper mapper = engine.getStandardMapper();
            JsonNode node = mapper.readTree(json);

            assertNotNull(node.get("emoji"));
            assertNotNull(node.get("text"));
        }

        @Test
        @DisplayName("Debe parsear JSON con numeros grandes")
        void shouldParseJSONWithLargeNumbers() throws Exception {
            String json = "{\"big\":999999999999999999,\"decimal\":1.23456789012345}";
            ObjectMapper mapper = engine.getStandardMapper();
            JsonNode node = mapper.readTree(json);

            assertNotNull(node.get("big"));
            assertNotNull(node.get("decimal"));
        }

        @Test
        @DisplayName("Debe parsear JSON con strings que contienen caracteres especiales")
        void shouldParseJSONWithSpecialCharStrings() throws Exception {
            String json = "{\"escaped\":\"line1\\nline2\\ttab\\\\\"}";
            ObjectMapper mapper = engine.getStandardMapper();
            JsonNode node = mapper.readTree(json);

            String value = node.get("escaped").asText();
            assertTrue(value.contains("\n"));
            assertTrue(value.contains("\t"));
            assertTrue(value.contains("\\"));
        }
    }
}
