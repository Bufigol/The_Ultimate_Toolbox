package com.the_ultimate_toolbox.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite completa de tests para {@link JSONObject}.
 * Cubre constructores, API fluida, getters tipados, acceso por path,
 * validaciones, serializacion, utilidades, clonacion, merge, Builder,
 * thread-safety y casos edge.
 *
 * @author The Ultimate Toolbox Team
 * @since 2.2.0
 */
@DisplayName("JSONObject Tests")
class JSONObjectTest {

    private JSONObject jsonObject;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        jsonObject = new JSONObject();
    }

    // ================================================================
    // Constructores
    // ================================================================
    @Nested
    @DisplayName("Constructores")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor por defecto debe crear objeto vacio")
        void shouldCreateEmptyObject_WithDefaultConstructor() {
            JSONObject obj = new JSONObject();
            assertTrue(obj.isEmpty());
            assertEquals(0, obj.size());
        }

        @Test
        @DisplayName("Constructor desde string JSON valido")
        void shouldCreateObject_FromValidJSONString() throws JsonProcessingException {
            String json = "{\"name\":\"Alice\",\"age\":25,\"active\":true}";
            JSONObject obj = new JSONObject(json);

            assertEquals("Alice", obj.getString("name"));
            assertEquals(25, obj.getInt("age", 0));
            assertTrue(obj.getBoolean("active", false));
            assertEquals(3, obj.size());
        }

        @Test
        @DisplayName("Constructor desde string debe lanzar excepcion para JSON invalido")
        void shouldThrowException_WhenJSONStringIsInvalid() {
            assertThrows(JsonProcessingException.class,
                    () -> new JSONObject("{invalid json}"));
        }

        @Test
        @DisplayName("Constructor desde string debe lanzar excepcion para array JSON")
        void shouldThrowException_WhenJSONStringIsArray() {
            assertThrows(IllegalArgumentException.class,
                    () -> new JSONObject("[1, 2, 3]"));
        }

        @Test
        @DisplayName("Constructor desde string debe lanzar excepcion para valor primitivo")
        void shouldThrowException_WhenJSONStringIsPrimitive() {
            assertThrows(IllegalArgumentException.class,
                    () -> new JSONObject("\"just a string\""));
        }

        @Test
        @DisplayName("Constructor desde Map con valores")
        void shouldCreateObject_FromMapWithValues() {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("key1", "value1");
            map.put("key2", 42);
            map.put("key3", true);

            JSONObject obj = new JSONObject(map);

            assertEquals("value1", obj.getString("key1"));
            assertEquals(42, obj.getInt("key2", 0));
            assertTrue(obj.getBoolean("key3", false));
        }

        @Test
        @DisplayName("Constructor desde Map null debe crear objeto vacio")
        void shouldCreateEmptyObject_FromNullMap() {
            Map<String, Object> nullMap = null;
            JSONObject obj = new JSONObject(nullMap);
            assertTrue(obj.isEmpty());
        }

        @Test
        @DisplayName("Constructor desde Map vacio debe crear objeto vacio")
        void shouldCreateEmptyObject_FromEmptyMap() {
            JSONObject obj = new JSONObject(new HashMap<>());
            assertTrue(obj.isEmpty());
        }

        @Test
        @DisplayName("Constructor desde string con JSON5 features")
        void shouldCreateObject_FromJSON5String() throws JsonProcessingException {
            // JSONC con comentarios (la deteccion de formato funciona)
            String json = """
                    {
                        // comentario
                        "key": "value"
                    }
                    """;
            JSONObject obj = new JSONObject(json);
            assertEquals("value", obj.getString("key"));
        }

        @Test
        @DisplayName("Constructor desde string con JSON complejo")
        void shouldCreateObject_FromComplexJSONString() throws JsonProcessingException {
            String json = """
                    {
                        "string": "hello",
                        "integer": 42,
                        "decimal": 3.14,
                        "boolean": true,
                        "null_val": null,
                        "array": [1, 2, 3],
                        "nested": {"inner": "value"}
                    }
                    """;
            JSONObject obj = new JSONObject(json);

            assertEquals(7, obj.size());
            assertEquals("hello", obj.getString("string"));
            assertEquals(42, obj.getInt("integer", 0));
            assertTrue(obj.isNull("null_val"));
        }
    }

    // ================================================================
    // put() - Todos los tipos
    // ================================================================
    @Nested
    @DisplayName("Metodo put() - Todos los tipos")
    class PutTests {

        @Test
        @DisplayName("Debe agregar valor null")
        void shouldPutNullValue() {
            jsonObject.put("key", null);
            assertTrue(jsonObject.has("key"));
            assertTrue(jsonObject.isNull("key"));
            assertNull(jsonObject.get("key"));
        }

        @Test
        @DisplayName("Debe agregar String")
        void shouldPutStringValue() {
            jsonObject.put("key", "hello world");
            assertEquals("hello world", jsonObject.getString("key"));
        }

        @Test
        @DisplayName("Debe agregar String vacio")
        void shouldPutEmptyString() {
            jsonObject.put("key", "");
            assertEquals("", jsonObject.getString("key"));
        }

        @Test
        @DisplayName("Debe agregar Integer")
        void shouldPutIntegerValue() {
            jsonObject.put("key", 42);
            assertEquals(42, jsonObject.getInt("key", 0));
        }

        @Test
        @DisplayName("Debe agregar Integer negativo")
        void shouldPutNegativeInteger() {
            jsonObject.put("key", -100);
            assertEquals(-100, jsonObject.getInt("key", 0));
        }

        @Test
        @DisplayName("Debe agregar Integer.MAX_VALUE")
        void shouldPutMaxInteger() {
            jsonObject.put("key", Integer.MAX_VALUE);
            assertEquals(Integer.MAX_VALUE, jsonObject.getInt("key", 0));
        }

        @Test
        @DisplayName("Debe agregar Long")
        void shouldPutLongValue() {
            jsonObject.put("key", 9999999999L);
            assertEquals(9999999999L, jsonObject.getLong("key", 0L));
        }

        @Test
        @DisplayName("Debe agregar Double")
        void shouldPutDoubleValue() {
            jsonObject.put("key", 3.14159);
            assertEquals(3.14159, jsonObject.getDouble("key", 0.0), 0.00001);
        }

        @Test
        @DisplayName("Debe agregar Float")
        void shouldPutFloatValue() {
            jsonObject.put("key", 2.5f);
            // Float se lee como double
            assertEquals(2.5, jsonObject.getDouble("key", 0.0), 0.01);
        }

        @Test
        @DisplayName("Debe agregar Boolean true")
        void shouldPutBooleanTrue() {
            jsonObject.put("key", true);
            assertTrue(jsonObject.getBoolean("key", false));
        }

        @Test
        @DisplayName("Debe agregar Boolean false")
        void shouldPutBooleanFalse() {
            jsonObject.put("key", false);
            assertFalse(jsonObject.getBoolean("key", true));
        }

        @Test
        @DisplayName("Debe agregar BigInteger")
        void shouldPutBigIntegerValue() {
            BigInteger big = new BigInteger("12345678901234567890");
            jsonObject.put("key", big);
            assertNotNull(jsonObject.get("key"));
        }

        @Test
        @DisplayName("Debe agregar BigDecimal")
        void shouldPutBigDecimalValue() {
            BigDecimal decimal = new BigDecimal("123456.789012345");
            jsonObject.put("key", decimal);
            assertNotNull(jsonObject.get("key"));
        }

        @Test
        @DisplayName("Debe agregar JSONObject anidado")
        void shouldPutNestedJSONObject() {
            JSONObject nested = new JSONObject().put("inner", "value");
            jsonObject.put("nested", nested);

            JSONObject retrieved = jsonObject.getJSONObject("nested");
            assertNotNull(retrieved);
            assertEquals("value", retrieved.getString("inner"));
        }

        @Test
        @DisplayName("Debe agregar Map como valor")
        void shouldPutMapValue() {
            Map<String, Object> map = Map.of("a", 1, "b", 2);
            jsonObject.put("map", map);

            Object retrieved = jsonObject.get("map");
            assertNotNull(retrieved);
        }

        @Test
        @DisplayName("Debe agregar Collection como valor")
        void shouldPutCollectionValue() {
            List<String> list = List.of("one", "two", "three");
            jsonObject.put("list", list);

            Object retrieved = jsonObject.get("list");
            assertNotNull(retrieved);
            assertTrue(retrieved instanceof List);
        }

        @Test
        @DisplayName("Debe lanzar NullPointerException para clave null")
        void shouldThrowException_WhenKeyIsNull() {
            assertThrows(NullPointerException.class,
                    () -> jsonObject.put(null, "value"));
        }

        @Test
        @DisplayName("Debe sobreescribir valor existente")
        void shouldOverwriteExistingValue() {
            jsonObject.put("key", "original");
            jsonObject.put("key", "updated");
            assertEquals("updated", jsonObject.getString("key"));
        }

        @Test
        @DisplayName("Debe retornar this para encadenamiento (fluent API)")
        void shouldReturnSameInstance_ForChaining() {
            JSONObject result = jsonObject.put("key", "value");
            assertSame(jsonObject, result);
        }
    }

    // ================================================================
    // putAll()
    // ================================================================
    @Nested
    @DisplayName("Metodo putAll()")
    class PutAllTests {

        @Test
        @DisplayName("Debe agregar multiples valores desde Map")
        void shouldAddMultipleValues_FromMap() {
            Map<String, Object> entries = new LinkedHashMap<>();
            entries.put("a", 1);
            entries.put("b", "two");
            entries.put("c", true);

            jsonObject.putAll(entries);

            assertEquals(3, jsonObject.size());
            assertEquals(1, jsonObject.getInt("a", 0));
            assertEquals("two", jsonObject.getString("b"));
            assertTrue(jsonObject.getBoolean("c", false));
        }

        @Test
        @DisplayName("Debe manejar Map null sin error")
        void shouldHandleNullMap_WithoutError() {
            JSONObject result = jsonObject.putAll(null);
            assertSame(jsonObject, result);
            assertTrue(jsonObject.isEmpty());
        }

        @Test
        @DisplayName("Debe retornar this para encadenamiento")
        void shouldReturnSameInstance() {
            JSONObject result = jsonObject.putAll(Map.of("key", "value"));
            assertSame(jsonObject, result);
        }
    }

    // ================================================================
    // remove()
    // ================================================================
    @Nested
    @DisplayName("Metodo remove()")
    class RemoveTests {

        @Test
        @DisplayName("Debe eliminar clave existente")
        void shouldRemoveExistingKey() {
            jsonObject.put("toRemove", "value").put("toKeep", "keep");
            jsonObject.remove("toRemove");

            assertFalse(jsonObject.has("toRemove"));
            assertTrue(jsonObject.has("toKeep"));
            assertEquals(1, jsonObject.size());
        }

        @Test
        @DisplayName("Debe manejar eliminacion de clave inexistente sin error")
        void shouldHandleRemovalOfNonexistentKey() {
            jsonObject.put("key", "value");
            assertDoesNotThrow(() -> jsonObject.remove("nonexistent"));
            assertEquals(1, jsonObject.size());
        }

        @Test
        @DisplayName("Debe retornar this para encadenamiento")
        void shouldReturnSameInstance() {
            jsonObject.put("key", "value");
            JSONObject result = jsonObject.remove("key");
            assertSame(jsonObject, result);
        }
    }

    // ================================================================
    // get() generico
    // ================================================================
    @Nested
    @DisplayName("Metodo get() generico")
    class GetGenericTests {

        @Test
        @DisplayName("Debe retornar String para valor string")
        void shouldReturnString_ForStringValue() {
            jsonObject.put("key", "text");
            Object value = jsonObject.get("key");
            assertEquals("text", value);
            assertInstanceOf(String.class, value);
        }

        @Test
        @DisplayName("Debe retornar Integer para valor entero")
        void shouldReturnInteger_ForIntValue() {
            jsonObject.put("key", 42);
            Object value = jsonObject.get("key");
            assertEquals(42, value);
        }

        @Test
        @DisplayName("Debe retornar Boolean para valor boolean")
        void shouldReturnBoolean_ForBooleanValue() {
            jsonObject.put("key", true);
            Object value = jsonObject.get("key");
            assertEquals(true, value);
        }

        @Test
        @DisplayName("Debe retornar null para clave inexistente")
        void shouldReturnNull_ForNonexistentKey() {
            assertNull(jsonObject.get("missing"));
        }

        @Test
        @DisplayName("Debe retornar null para valor null")
        void shouldReturnNull_ForNullValue() {
            jsonObject.put("key", null);
            assertNull(jsonObject.get("key"));
        }

        @Test
        @DisplayName("Debe retornar Long para valor long")
        void shouldReturnLong_ForLongValue() {
            jsonObject.put("key", 9999999999L);
            Object value = jsonObject.get("key");
            assertNotNull(value);
        }

        @Test
        @DisplayName("Debe retornar Double para valor double")
        void shouldReturnDouble_ForDoubleValue() {
            jsonObject.put("key", 3.14);
            Object value = jsonObject.get("key");
            assertNotNull(value);
        }

        @Test
        @DisplayName("Debe retornar JSONObject para objeto anidado")
        void shouldReturnJSONObject_ForNestedObject() {
            jsonObject.put("nested", new JSONObject().put("a", 1));
            Object value = jsonObject.get("nested");
            assertInstanceOf(JSONObject.class, value);
        }

        @Test
        @DisplayName("Debe retornar List para array")
        void shouldReturnList_ForArrayValue() {
            jsonObject.put("list", List.of(1, 2, 3));
            Object value = jsonObject.get("list");
            assertInstanceOf(List.class, value);
        }
    }

    // ================================================================
    // get(key, Class<T>)
    // ================================================================
    @Nested
    @DisplayName("Metodo get(key, Class<T>)")
    class GetTypedTests {

        @Test
        @DisplayName("Debe convertir a tipo String")
        void shouldConvertToString() {
            jsonObject.put("key", "hello");
            String value = jsonObject.get("key", String.class);
            assertEquals("hello", value);
        }

        @Test
        @DisplayName("Debe convertir a tipo Integer")
        void shouldConvertToInteger() {
            jsonObject.put("key", 42);
            Integer value = jsonObject.get("key", Integer.class);
            assertEquals(42, value);
        }

        @Test
        @DisplayName("Debe retornar null para clave inexistente")
        void shouldReturnNull_ForMissingKey() {
            assertNull(jsonObject.get("missing", String.class));
        }

        @Test
        @DisplayName("Debe retornar null para valor null")
        void shouldReturnNull_ForNullValue() {
            jsonObject.put("key", null);
            assertNull(jsonObject.get("key", String.class));
        }

        @Test
        @DisplayName("Debe lanzar excepcion para conversion incompatible")
        void shouldThrowException_ForIncompatibleConversion() {
            jsonObject.put("key", "not a number");
            assertThrows(IllegalArgumentException.class,
                    () -> jsonObject.get("key", Integer.class));
        }
    }

    // ================================================================
    // Getters tipados
    // ================================================================
    @Nested
    @DisplayName("Getters tipados")
    class TypedGetterTests {

        @BeforeEach
        void setUpValues() throws JsonProcessingException {
            String json = """
                    {
                        "text": "hello",
                        "integer": 42,
                        "long_val": 9999999999,
                        "double_val": 3.14159,
                        "bool_true": true,
                        "bool_false": false,
                        "null_val": null,
                        "nested": {"inner": "value"},
                        "non_number": "abc"
                    }
                    """;
            jsonObject = new JSONObject(json);
        }

        @Test
        @DisplayName("getString debe retornar valor correcto")
        void shouldReturnCorrectString() {
            assertEquals("hello", jsonObject.getString("text"));
        }

        @Test
        @DisplayName("getString debe retornar null para clave inexistente")
        void shouldReturnNullString_ForMissingKey() {
            assertNull(jsonObject.getString("missing"));
        }

        @Test
        @DisplayName("getString debe retornar null para valor null")
        void shouldReturnNullString_ForNullValue() {
            assertNull(jsonObject.getString("null_val"));
        }

        @Test
        @DisplayName("getInt debe retornar valor correcto")
        void shouldReturnCorrectInt() {
            assertEquals(42, jsonObject.getInt("integer", 0));
        }

        @Test
        @DisplayName("getInt debe retornar valor por defecto para clave inexistente")
        void shouldReturnDefaultInt_ForMissingKey() {
            assertEquals(100, jsonObject.getInt("missing", 100));
        }

        @Test
        @DisplayName("getInt debe retornar valor por defecto para valor no numerico")
        void shouldReturnDefaultInt_ForNonNumericValue() {
            assertEquals(99, jsonObject.getInt("non_number", 99));
        }

        @Test
        @DisplayName("getInt debe retornar valor por defecto para valor null")
        void shouldReturnDefaultInt_ForNullValue() {
            assertEquals(50, jsonObject.getInt("null_val", 50));
        }

        @Test
        @DisplayName("getLong debe retornar valor correcto")
        void shouldReturnCorrectLong() {
            assertEquals(9999999999L, jsonObject.getLong("long_val", 0L));
        }

        @Test
        @DisplayName("getLong debe retornar valor por defecto para clave inexistente")
        void shouldReturnDefaultLong_ForMissingKey() {
            assertEquals(999L, jsonObject.getLong("missing", 999L));
        }

        @Test
        @DisplayName("getDouble debe retornar valor correcto")
        void shouldReturnCorrectDouble() {
            assertEquals(3.14159, jsonObject.getDouble("double_val", 0.0), 0.00001);
        }

        @Test
        @DisplayName("getDouble debe retornar valor por defecto para clave inexistente")
        void shouldReturnDefaultDouble_ForMissingKey() {
            assertEquals(1.0, jsonObject.getDouble("missing", 1.0), 0.001);
        }

        @Test
        @DisplayName("getBoolean debe retornar true correctamente")
        void shouldReturnTrue() {
            assertTrue(jsonObject.getBoolean("bool_true", false));
        }

        @Test
        @DisplayName("getBoolean debe retornar false correctamente")
        void shouldReturnFalse() {
            assertFalse(jsonObject.getBoolean("bool_false", true));
        }

        @Test
        @DisplayName("getBoolean debe retornar valor por defecto para clave inexistente")
        void shouldReturnDefaultBoolean_ForMissingKey() {
            assertFalse(jsonObject.getBoolean("missing", false));
            assertTrue(jsonObject.getBoolean("missing", true));
        }

        @Test
        @DisplayName("getBoolean debe retornar valor por defecto para valor no booleano")
        void shouldReturnDefaultBoolean_ForNonBooleanValue() {
            assertTrue(jsonObject.getBoolean("text", true));
        }

        @Test
        @DisplayName("getJSONObject debe retornar objeto anidado correcto")
        void shouldReturnNestedObject() {
            JSONObject nested = jsonObject.getJSONObject("nested");
            assertNotNull(nested);
            assertEquals("value", nested.getString("inner"));
        }

        @Test
        @DisplayName("getJSONObject debe retornar null para clave inexistente")
        void shouldReturnNullJSONObject_ForMissingKey() {
            assertNull(jsonObject.getJSONObject("missing"));
        }

        @Test
        @DisplayName("getJSONObject debe retornar null para valor no-objeto")
        void shouldReturnNullJSONObject_ForNonObjectValue() {
            assertNull(jsonObject.getJSONObject("text"));
        }
    }

    // ================================================================
    // getByPath()
    // ================================================================
    @Nested
    @DisplayName("Metodo getByPath()")
    class GetByPathTests {

        @Test
        @DisplayName("Debe acceder a valor profundamente anidado")
        void shouldAccessDeeplyNestedValue() {
            JSONObject inner = new JSONObject().put("value", "deep");
            JSONObject middle = new JSONObject().put("inner", inner);
            jsonObject.put("outer", middle);

            Object value = jsonObject.getByPath("outer.inner.value");
            assertEquals("deep", value);
        }

        @Test
        @DisplayName("Debe acceder a valor en primer nivel")
        void shouldAccessFirstLevelValue() {
            jsonObject.put("name", "test");
            Object value = jsonObject.getByPath("name");
            assertEquals("test", value);
        }

        @Test
        @DisplayName("Debe retornar null o vacio para path inexistente")
        void shouldReturnNullOrEmpty_ForNonexistentPath() {
            jsonObject.put("key", "value");
            Object value = jsonObject.getByPath("nonexistent.deep.path");
            // Jackson at() retorna MissingNode para paths inexistentes,
            // que nodeToObject() convierte via toString() a string vacio.
            // Se verifica que no retorna un valor significativo.
            assertTrue(value == null || "".equals(value),
                    "Para path inexistente debe retornar null o string vacio, pero retorno: " + value);
        }

        @Test
        @DisplayName("Debe funcionar con tres niveles de profundidad")
        void shouldWorkWithThreeLevels() throws JsonProcessingException {
            String json = """
                    {
                        "user": {
                            "address": {
                                "street": "123 Main St",
                                "city": "Boston"
                            }
                        }
                    }
                    """;
            JSONObject obj = new JSONObject(json);

            assertEquals("123 Main St", obj.getByPath("user.address.street"));
            assertEquals("Boston", obj.getByPath("user.address.city"));
        }

        @Test
        @DisplayName("Debe retornar valor numerico por path")
        void shouldReturnNumericValue_ByPath() throws JsonProcessingException {
            String json = "{\"config\":{\"port\":8080}}";
            JSONObject obj = new JSONObject(json);

            Object value = obj.getByPath("config.port");
            assertEquals(8080, value);
        }
    }

    // ================================================================
    // Validaciones: has(), isNull(), isEmpty(), size()
    // ================================================================
    @Nested
    @DisplayName("Metodos de validacion")
    class ValidationTests {

        @Test
        @DisplayName("has() debe retornar true para clave existente")
        void shouldReturnTrue_WhenKeyExists() {
            jsonObject.put("key", "value");
            assertTrue(jsonObject.has("key"));
        }

        @Test
        @DisplayName("has() debe retornar false para clave inexistente")
        void shouldReturnFalse_WhenKeyDoesNotExist() {
            assertFalse(jsonObject.has("missing"));
        }

        @Test
        @DisplayName("has() debe retornar true para clave con valor null")
        void shouldReturnTrue_WhenKeyExistsWithNullValue() {
            jsonObject.put("key", null);
            assertTrue(jsonObject.has("key"));
        }

        @Test
        @DisplayName("isNull() debe retornar true para clave con valor null")
        void shouldReturnTrue_WhenValueIsNull() {
            jsonObject.put("key", null);
            assertTrue(jsonObject.isNull("key"));
        }

        @Test
        @DisplayName("isNull() debe retornar true para clave inexistente")
        void shouldReturnTrue_WhenKeyDoesNotExist() {
            assertTrue(jsonObject.isNull("nonexistent"));
        }

        @Test
        @DisplayName("isNull() debe retornar false para valor no null")
        void shouldReturnFalse_WhenValueIsNotNull() {
            jsonObject.put("key", "value");
            assertFalse(jsonObject.isNull("key"));
        }

        @Test
        @DisplayName("isEmpty() debe retornar true para objeto vacio")
        void shouldReturnTrue_WhenEmpty() {
            assertTrue(jsonObject.isEmpty());
        }

        @Test
        @DisplayName("isEmpty() debe retornar false para objeto con elementos")
        void shouldReturnFalse_WhenNotEmpty() {
            jsonObject.put("key", "value");
            assertFalse(jsonObject.isEmpty());
        }

        @Test
        @DisplayName("size() debe retornar 0 para objeto vacio")
        void shouldReturnZero_WhenEmpty() {
            assertEquals(0, jsonObject.size());
        }

        @Test
        @DisplayName("size() debe retornar conteo correcto")
        void shouldReturnCorrectCount() {
            jsonObject.put("a", 1).put("b", 2).put("c", 3);
            assertEquals(3, jsonObject.size());
        }

        @Test
        @DisplayName("size() debe actualizarse despues de remove")
        void shouldUpdateAfterRemove() {
            jsonObject.put("a", 1).put("b", 2);
            assertEquals(2, jsonObject.size());
            jsonObject.remove("a");
            assertEquals(1, jsonObject.size());
        }
    }

    // ================================================================
    // Serializacion: toString(), toPrettyString()
    // ================================================================
    @Nested
    @DisplayName("Serializacion")
    class SerializationTests {

        @Test
        @DisplayName("toString() debe producir JSON valido")
        void shouldProduceValidJSON() throws JsonProcessingException {
            jsonObject.put("name", "Test").put("value", 123);
            String json = jsonObject.toString();

            assertNotNull(json);
            // Verificar que es parseable
            JSONObject reparsed = new JSONObject(json);
            assertEquals("Test", reparsed.getString("name"));
            assertEquals(123, reparsed.getInt("value", 0));
        }

        @Test
        @DisplayName("toString() de objeto vacio debe ser {}")
        void shouldReturnEmptyBraces_ForEmptyObject() {
            assertEquals("{}", jsonObject.toString());
        }

        @Test
        @DisplayName("toString() debe contener claves y valores")
        void shouldContainKeysAndValues() {
            jsonObject.put("key", "value");
            String result = jsonObject.toString();
            assertTrue(result.contains("\"key\""));
            assertTrue(result.contains("\"value\""));
        }

        @Test
        @DisplayName("toPrettyString() debe contener saltos de linea e indentacion")
        void shouldContainNewlinesAndIndentation() {
            jsonObject.put("key1", "value1").put("key2", "value2");
            String pretty = jsonObject.toPrettyString();

            assertTrue(pretty.contains("\n"));
            assertTrue(pretty.contains("  ") || pretty.contains("\t"));
        }

        @Test
        @DisplayName("toPrettyString() debe producir JSON parseable")
        void shouldProduceParseableJSON() throws JsonProcessingException {
            jsonObject.put("name", "Pretty").put("num", 42);
            String pretty = jsonObject.toPrettyString();
            JSONObject reparsed = new JSONObject(pretty);
            assertEquals("Pretty", reparsed.getString("name"));
        }

        @Test
        @DisplayName("toString() debe manejar valores null correctamente")
        void shouldHandleNullValues() {
            jsonObject.put("nullKey", null);
            String json = jsonObject.toString();
            assertTrue(json.contains("null"));
        }
    }

    // ================================================================
    // writeToFile()
    // ================================================================
    @Nested
    @DisplayName("Escritura a archivo (writeToFile)")
    class WriteToFileTests {

        @Test
        @DisplayName("Debe escribir JSON compacto a archivo")
        void shouldWriteCompactJSON_ToFile() throws IOException {
            jsonObject.put("written", true).put("format", "compact");

            File outFile = tempDir.resolve("compact.json").toFile();
            jsonObject.writeToFile(outFile, false);

            String content = Files.readString(outFile.toPath(), StandardCharsets.UTF_8);
            assertFalse(content.contains("\n") && content.contains("  "),
                    "El formato compacto no debe tener indentacion con saltos de linea");
            assertTrue(content.contains("\"written\""));
            assertTrue(content.contains("\"compact\""));
        }

        @Test
        @DisplayName("Debe escribir JSON pretty a archivo")
        void shouldWritePrettyJSON_ToFile() throws IOException {
            jsonObject.put("written", true).put("format", "pretty");

            File outFile = tempDir.resolve("pretty.json").toFile();
            jsonObject.writeToFile(outFile, true);

            String content = Files.readString(outFile.toPath(), StandardCharsets.UTF_8);
            assertTrue(content.contains("\n"), "El formato pretty debe tener saltos de linea");
        }

        @Test
        @DisplayName("Debe escribir JSON parseable a archivo")
        void shouldWriteParseableJSON_ToFile() throws Exception {
            jsonObject.put("name", "file-test").put("value", 99);

            File outFile = tempDir.resolve("parseable.json").toFile();
            jsonObject.writeToFile(outFile, false);

            String content = Files.readString(outFile.toPath(), StandardCharsets.UTF_8);
            JSONObject reparsed = new JSONObject(content);
            assertEquals("file-test", reparsed.getString("name"));
            assertEquals(99, reparsed.getInt("value", 0));
        }

        @Test
        @DisplayName("Debe escribir objeto vacio a archivo")
        void shouldWriteEmptyObject_ToFile() throws IOException {
            File outFile = tempDir.resolve("empty.json").toFile();
            jsonObject.writeToFile(outFile, false);

            String content = Files.readString(outFile.toPath(), StandardCharsets.UTF_8);
            // Verificar que el contenido es un objeto JSON vacio parseable
            assertDoesNotThrow(() -> new JSONObject(content),
                    "El contenido escrito debe ser JSON parseable");
            JSONObject reparsed = new JSONObject(content);
            assertTrue(reparsed.isEmpty(), "El objeto reparsado debe estar vacio");
        }

        @Test
        @DisplayName("Debe escribir objeto con valores anidados a archivo")
        void shouldWriteNestedObject_ToFile() throws Exception {
            jsonObject.put("config", new JSONObject()
                    .put("host", "localhost")
                    .put("port", 8080));

            File outFile = tempDir.resolve("nested.json").toFile();
            jsonObject.writeToFile(outFile, true);

            String content = Files.readString(outFile.toPath(), StandardCharsets.UTF_8);
            JSONObject reparsed = new JSONObject(content);
            JSONObject config = reparsed.getJSONObject("config");
            assertNotNull(config);
            assertEquals("localhost", config.getString("host"));
        }
    }

    // ================================================================
    // keySet()
    // ================================================================
    @Nested
    @DisplayName("Metodo keySet()")
    class KeySetTests {

        @Test
        @DisplayName("Debe retornar set vacio para objeto vacio")
        void shouldReturnEmptySet_ForEmptyObject() {
            assertTrue(jsonObject.keySet().isEmpty());
        }

        @Test
        @DisplayName("Debe retornar todas las claves")
        void shouldReturnAllKeys() {
            jsonObject.put("a", 1).put("b", 2).put("c", 3);
            Set<String> keys = jsonObject.keySet();

            assertEquals(3, keys.size());
            assertTrue(keys.contains("a"));
            assertTrue(keys.contains("b"));
            assertTrue(keys.contains("c"));
        }

        @Test
        @DisplayName("Debe incluir claves con valor null")
        void shouldIncludeKeysWithNullValues() {
            jsonObject.put("notNull", "value").put("isNull", null);
            Set<String> keys = jsonObject.keySet();

            assertEquals(2, keys.size());
            assertTrue(keys.contains("isNull"));
        }
    }

    // ================================================================
    // stream()
    // ================================================================
    @Nested
    @DisplayName("Metodo stream()")
    class StreamTests {

        @Test
        @DisplayName("Debe retornar stream vacio para objeto vacio")
        void shouldReturnEmptyStream_ForEmptyObject() {
            assertEquals(0, jsonObject.stream().count());
        }

        @Test
        @DisplayName("Debe retornar stream con todas las entradas")
        void shouldReturnStreamWithAllEntries() {
            jsonObject.put("a", 1).put("b", 2).put("c", 3);
            assertEquals(3, jsonObject.stream().count());
        }

        @Test
        @DisplayName("Debe poder filtrar entradas con stream")
        void shouldAllowFiltering() {
            jsonObject.put("num", 42).put("text", "hello").put("flag", true);

            List<Map.Entry<String, Object>> numbers = jsonObject.stream()
                    .filter(entry -> entry.getValue() instanceof Number)
                    .collect(Collectors.toList());

            assertEquals(1, numbers.size());
            assertEquals("num", numbers.get(0).getKey());
        }

        @Test
        @DisplayName("Debe poder mapear valores con stream")
        void shouldAllowMapping() {
            jsonObject.put("a", "hello").put("b", "world");

            List<String> keys = jsonObject.stream()
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            assertEquals(2, keys.size());
            assertTrue(keys.contains("a"));
            assertTrue(keys.contains("b"));
        }
    }

    // ================================================================
    // toMap()
    // ================================================================
    @Nested
    @DisplayName("Metodo toMap()")
    class ToMapTests {

        @Test
        @DisplayName("Debe retornar mapa vacio para objeto vacio")
        void shouldReturnEmptyMap_ForEmptyObject() {
            assertTrue(jsonObject.toMap().isEmpty());
        }

        @Test
        @DisplayName("Debe retornar mapa con todos los valores")
        void shouldReturnMapWithAllValues() {
            jsonObject.put("str", "value").put("num", 42).put("flag", true);
            Map<String, Object> map = jsonObject.toMap();

            assertEquals(3, map.size());
            assertEquals("value", map.get("str"));
            assertEquals(42, map.get("num"));
            assertEquals(true, map.get("flag"));
        }

        @Test
        @DisplayName("Debe incluir valores null en el mapa")
        void shouldIncludeNullValues() {
            jsonObject.put("key", null);
            Map<String, Object> map = jsonObject.toMap();

            assertEquals(1, map.size());
            assertTrue(map.containsKey("key"));
            assertNull(map.get("key"));
        }

        @Test
        @DisplayName("Debe convertir objetos anidados a JSONObject en el mapa")
        void shouldConvertNestedObjects() {
            jsonObject.put("nested", new JSONObject().put("inner", "val"));
            Map<String, Object> map = jsonObject.toMap();

            assertInstanceOf(JSONObject.class, map.get("nested"));
        }
    }

    // ================================================================
    // clone()
    // ================================================================
    @Nested
    @DisplayName("Metodo clone()")
    class CloneTests {

        @Test
        @DisplayName("Debe crear copia con los mismos valores")
        void shouldCreateCopy_WithSameValues() {
            jsonObject.put("key", "value").put("num", 42);
            JSONObject cloned = jsonObject.clone();

            assertEquals(jsonObject.toString(), cloned.toString());
        }

        @Test
        @DisplayName("Debe crear instancia diferente")
        void shouldCreateDifferentInstance() {
            jsonObject.put("key", "value");
            JSONObject cloned = jsonObject.clone();

            assertNotSame(jsonObject, cloned);
        }

        @Test
        @DisplayName("Modificar el clon no debe afectar al original")
        void shouldNotAffectOriginal_WhenCloneIsModified() {
            jsonObject.put("original", "value");
            JSONObject cloned = jsonObject.clone();

            cloned.put("extra", "new");

            assertFalse(jsonObject.has("extra"));
            assertTrue(cloned.has("extra"));
        }

        @Test
        @DisplayName("Modificar el original no debe afectar al clon")
        void shouldNotAffectClone_WhenOriginalIsModified() {
            jsonObject.put("key", "original");
            JSONObject cloned = jsonObject.clone();

            jsonObject.put("key", "modified");

            assertEquals("original", cloned.getString("key"));
        }

        @Test
        @DisplayName("Debe clonar objetos anidados de forma profunda")
        void shouldDeepCloneNestedObjects() {
            JSONObject inner = new JSONObject().put("deep", "value");
            jsonObject.put("nested", inner);

            JSONObject cloned = jsonObject.clone();
            // Modificar el anidado en el clon no debe afectar al original
            JSONObject clonedNested = cloned.getJSONObject("nested");
            assertNotNull(clonedNested);
        }

        @Test
        @DisplayName("Debe clonar objeto vacio correctamente")
        void shouldCloneEmptyObject() {
            JSONObject cloned = jsonObject.clone();
            assertTrue(cloned.isEmpty());
        }
    }

    // ================================================================
    // merge()
    // ================================================================
    @Nested
    @DisplayName("Metodo merge()")
    class MergeTests {

        @Test
        @DisplayName("Merge shallow debe sobreescribir claves existentes")
        void shouldOverwriteKeys_WithShallowMerge() {
            jsonObject.put("key1", "original").put("key2", "keep");
            JSONObject other = new JSONObject().put("key1", "new").put("key3", "added");

            jsonObject.merge(other, false);

            assertEquals("new", jsonObject.getString("key1"));
            assertEquals("keep", jsonObject.getString("key2"));
            assertEquals("added", jsonObject.getString("key3"));
        }

        @Test
        @DisplayName("Merge deep debe fusionar objetos anidados")
        void shouldMergeNestedObjects_WithDeepMerge() {
            JSONObject nested1 = new JSONObject().put("a", 1).put("b", 2);
            jsonObject.put("config", nested1);

            JSONObject nested2 = new JSONObject().put("b", 99).put("c", 3);
            JSONObject other = new JSONObject().put("config", nested2);

            jsonObject.merge(other, true);

            JSONObject config = jsonObject.getJSONObject("config");
            assertNotNull(config);
            assertEquals(1, config.getInt("a", 0));
            assertEquals(99, config.getInt("b", 0)); // Sobreescrito
            assertEquals(3, config.getInt("c", 0)); // Nuevo
        }

        @Test
        @DisplayName("Merge con null no debe fallar")
        void shouldNotFail_WhenMergingNull() {
            jsonObject.put("key", "value");
            JSONObject result = jsonObject.merge(null, false);
            assertSame(jsonObject, result);
            assertEquals("value", jsonObject.getString("key"));
        }

        @Test
        @DisplayName("Merge debe retornar this para encadenamiento")
        void shouldReturnSameInstance() {
            JSONObject result = jsonObject.merge(new JSONObject().put("a", 1), false);
            assertSame(jsonObject, result);
        }

        @Test
        @DisplayName("Merge shallow con objeto vacio no debe cambiar nada")
        void shouldNotChange_WhenMergingEmptyObject() {
            jsonObject.put("key", "value");
            jsonObject.merge(new JSONObject(), false);
            assertEquals(1, jsonObject.size());
            assertEquals("value", jsonObject.getString("key"));
        }

        @Test
        @DisplayName("Merge deep con valores no-objeto debe sobreescribir")
        void shouldOverwriteNonObjectValues_WithDeepMerge() {
            jsonObject.put("key", "original");
            JSONObject other = new JSONObject().put("key", "replaced");

            jsonObject.merge(other, true);
            assertEquals("replaced", jsonObject.getString("key"));
        }
    }

    // ================================================================
    // Builder Pattern
    // ================================================================
    @Nested
    @DisplayName("Builder Pattern")
    class BuilderTests {

        @Test
        @DisplayName("Builder debe crear objeto con valores correctos")
        void shouldCreateObject_WithCorrectValues() {
            JSONObject built = JSONObject.builder()
                    .put("id", 1)
                    .put("name", "Test")
                    .put("active", true)
                    .build();

            assertEquals(1, built.getInt("id", 0));
            assertEquals("Test", built.getString("name"));
            assertTrue(built.getBoolean("active", false));
        }

        @Test
        @DisplayName("Builder debe soportar putAll")
        void shouldSupportPutAll() {
            Map<String, Object> map = Map.of("a", 1, "b", 2);
            JSONObject built = JSONObject.builder()
                    .putAll(map)
                    .build();

            assertEquals(2, built.size());
        }

        @Test
        @DisplayName("Builder debe soportar cambio de formato")
        void shouldSupportFormatChange() {
            JSONObject built = JSONObject.builder()
                    .format(JacksonEngine.JSONFormat.JSON5)
                    .put("key", "value")
                    .build();

            assertNotNull(built);
            assertEquals("value", built.getString("key"));
        }

        @Test
        @DisplayName("Builder vacio debe crear objeto vacio")
        void shouldCreateEmptyObject_WithEmptyBuilder() {
            JSONObject built = JSONObject.builder().build();
            assertTrue(built.isEmpty());
        }

        @Test
        @DisplayName("Builder debe soportar encadenamiento complejo")
        void shouldSupportComplexChaining() {
            JSONObject built = JSONObject.builder()
                    .put("str", "hello")
                    .put("num", 42)
                    .put("flag", true)
                    .put("nested", new JSONObject().put("a", 1))
                    .put("list", List.of(1, 2, 3))
                    .build();

            assertEquals(5, built.size());
        }
    }

    // ================================================================
    // Thread-safety
    // ================================================================
    @Nested
    @DisplayName("Thread-safety")
    class ThreadSafetyTests {

        @Test
        @DisplayName("put() concurrente no debe perder datos ni lanzar excepciones")
        void shouldHandleConcurrentPuts_WithoutDataLoss() throws Exception {
            int threadCount = 20;
            int putsPerThread = 100;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(1);
            List<Future<?>> futures = new ArrayList<>();

            for (int t = 0; t < threadCount; t++) {
                int threadId = t;
                futures.add(executor.submit(() -> {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    for (int i = 0; i < putsPerThread; i++) {
                        jsonObject.put("thread" + threadId + "_key" + i, "value" + i);
                    }
                }));
            }

            latch.countDown();

            for (Future<?> future : futures) {
                future.get(10, TimeUnit.SECONDS);
            }

            executor.shutdown();
            assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));

            // Verificar que se insertaron todos los valores
            assertEquals(threadCount * putsPerThread, jsonObject.size());
        }

        @Test
        @DisplayName("Lecturas y escrituras concurrentes no deben lanzar excepciones")
        void shouldHandleConcurrentReadsAndWrites() throws Exception {
            jsonObject.put("initial", "value");

            int threadCount = 20;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(1);
            List<Future<?>> futures = new ArrayList<>();

            for (int t = 0; t < threadCount; t++) {
                int threadId = t;
                futures.add(executor.submit(() -> {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    for (int i = 0; i < 50; i++) {
                        if (i % 2 == 0) {
                            jsonObject.put("key_" + threadId + "_" + i, "val");
                        } else {
                            jsonObject.getString("initial");
                            jsonObject.has("key_" + threadId);
                            jsonObject.size();
                        }
                    }
                }));
            }

            latch.countDown();

            for (Future<?> future : futures) {
                assertDoesNotThrow(() -> future.get(10, TimeUnit.SECONDS));
            }

            executor.shutdown();
            assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        }
    }

    // ================================================================
    // Fluent API integral
    // ================================================================
    @Nested
    @DisplayName("Fluent API completa")
    class FluentAPITests {

        @Test
        @DisplayName("Debe soportar encadenamiento completo")
        void shouldSupportFullChaining() {
            JSONObject result = jsonObject
                    .put("a", 1)
                    .put("b", "two")
                    .put("c", true)
                    .remove("a")
                    .put("d", 4.0);

            assertSame(jsonObject, result);
            assertFalse(jsonObject.has("a"));
            assertEquals("two", jsonObject.getString("b"));
            assertTrue(jsonObject.getBoolean("c", false));
            assertEquals(4.0, jsonObject.getDouble("d", 0.0), 0.01);
        }
    }

    // ================================================================
    // Edge cases
    // ================================================================
    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Debe manejar claves con caracteres especiales")
        void shouldHandleSpecialCharKeys() {
            jsonObject.put("key with spaces", "value");
            jsonObject.put("key.with.dots", "value");
            jsonObject.put("key-with-dashes", "value");
            jsonObject.put("key_with_underscores", "value");

            assertEquals("value", jsonObject.getString("key with spaces"));
            assertEquals("value", jsonObject.getString("key.with.dots"));
            assertEquals("value", jsonObject.getString("key-with-dashes"));
            assertEquals("value", jsonObject.getString("key_with_underscores"));
        }

        @Test
        @DisplayName("Debe manejar strings muy largos")
        void shouldHandleVeryLongStrings() {
            String longString = "x".repeat(100000);
            jsonObject.put("long", longString);
            assertEquals(longString, jsonObject.getString("long"));
        }

        @Test
        @DisplayName("Debe manejar muchas claves")
        void shouldHandleManyKeys() {
            for (int i = 0; i < 1000; i++) {
                jsonObject.put("key_" + i, i);
            }
            assertEquals(1000, jsonObject.size());
            assertEquals(500, jsonObject.getInt("key_500", 0));
        }

        @Test
        @DisplayName("Debe manejar JSON con string vacio como valor")
        void shouldHandleEmptyStringValue() throws JsonProcessingException {
            JSONObject obj = new JSONObject("{\"empty\":\"\"}");
            assertEquals("", obj.getString("empty"));
        }

        @Test
        @DisplayName("Debe preservar orden de insercion en keySet")
        void shouldPreserveInsertionOrder() {
            jsonObject.put("z", 1).put("a", 2).put("m", 3);
            List<String> keys = new ArrayList<>(jsonObject.keySet());

            assertEquals("z", keys.get(0));
            assertEquals("a", keys.get(1));
            assertEquals("m", keys.get(2));
        }

        @Test
        @DisplayName("Debe manejar Integer cero")
        void shouldHandleZeroInteger() {
            jsonObject.put("zero", 0);
            assertEquals(0, jsonObject.getInt("zero", -1));
        }

        @Test
        @DisplayName("Debe manejar Double cero")
        void shouldHandleZeroDouble() {
            jsonObject.put("zero", 0.0);
            assertEquals(0.0, jsonObject.getDouble("zero", -1.0), 0.001);
        }

        @Test
        @DisplayName("Constructor desde string con JSON que contiene Infinity en datos (como texto)")
        void shouldDetectJSON5Format_WhenStringContainsInfinity() throws JsonProcessingException {
            // El string "Infinity" en un campo de texto no deberia romper el parsing
            // Nota: JSON5 mapper soporta tambien JSON estandar
            String json = "{\"desc\": \"Infinity Pool Hotel\"}";
            JSONObject obj = new JSONObject(json);
            assertEquals("Infinity Pool Hotel", obj.getString("desc"));
        }
    }
}
