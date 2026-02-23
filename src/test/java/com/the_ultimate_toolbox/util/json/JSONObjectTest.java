package com.the_ultimate_toolbox.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para JSONObject.
 *
 * @author The Ultimate Toolbox Team
 * @since 2.2.0
 */
@DisplayName("JSONObject Tests")
public class JSONObjectTest {

    private JSONObject jsonObject;

    @BeforeEach
    void setUp() {
        jsonObject = new JSONObject();
    }

    @Test
    @DisplayName("Debe crear un JSONObject vacío")
    void testCreateEmptyObject() {
        assertTrue(jsonObject.isEmpty());
        assertEquals(0, jsonObject.size());
    }

    @Test
    @DisplayName("Debe agregar y recuperar valores básicos")
    void testPutAndGetBasicValues() {
        jsonObject.put("string", "Hello World");
        jsonObject.put("integer", 42);
        jsonObject.put("double", 3.14159);
        jsonObject.put("boolean", true);
        jsonObject.put("null", null);

        assertEquals("Hello World", jsonObject.getString("string"));
        assertEquals(42, jsonObject.getInt("integer", 0));
        assertEquals(3.14159, jsonObject.getDouble("double", 0.0), 0.00001);
        assertTrue(jsonObject.getBoolean("boolean", false));
        assertTrue(jsonObject.isNull("null"));
    }

    @Test
    @DisplayName("Debe manejar API fluida correctamente")
    void testFluentAPI() {
        JSONObject result = jsonObject
            .put("name", "John")
            .put("age", 30)
            .put("city", "New York");

        assertSame(jsonObject, result);
        assertEquals("John", jsonObject.getString("name"));
        assertEquals(30, jsonObject.getInt("age", 0));
        assertEquals("New York", jsonObject.getString("city"));
    }

    @Test
    @DisplayName("Debe construir con Builder pattern")
    void testBuilderPattern() {
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
    @DisplayName("Debe parsear JSON string válido")
    void testParseValidJSON() throws JsonProcessingException {
        String json = "{\"name\":\"Alice\",\"age\":25,\"active\":true}";
        JSONObject parsed = new JSONObject(json);

        assertEquals("Alice", parsed.getString("name"));
        assertEquals(25, parsed.getInt("age", 0));
        assertTrue(parsed.getBoolean("active", false));
    }

    @Test
    @DisplayName("Debe manejar objetos anidados")
    void testNestedObjects() {
        JSONObject address = new JSONObject()
            .put("street", "123 Main St")
            .put("city", "Boston")
            .put("zip", "02101");

        jsonObject.put("name", "John")
                  .put("address", address);

        JSONObject retrievedAddress = jsonObject.getJSONObject("address");
        assertNotNull(retrievedAddress);
        assertEquals("Boston", retrievedAddress.getString("city"));
    }

    @Test
    @DisplayName("Debe convertir a Map correctamente")
    void testToMap() {
        jsonObject.put("key1", "value1")
                  .put("key2", 42)
                  .put("key3", true);

        Map<String, Object> map = jsonObject.toMap();

        assertEquals(3, map.size());
        assertEquals("value1", map.get("key1"));
        assertEquals(42, map.get("key2"));
        assertEquals(true, map.get("key3"));
    }

    @Test
    @DisplayName("Debe serializar a JSON string")
    void testToString() {
        jsonObject.put("name", "Test")
                  .put("value", 123);

        String json = jsonObject.toString();

        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"Test\""));
        assertTrue(json.contains("\"value\":123"));
    }

    @Test
    @DisplayName("Debe manejar pretty print")
    void testPrettyPrint() {
        jsonObject.put("name", "Test")
                  .put("value", 123);

        String pretty = jsonObject.toPrettyString();

        assertNotNull(pretty);
        assertTrue(pretty.contains("\n"));
        assertTrue(pretty.contains("  "));
    }

    @Test
    @DisplayName("Debe verificar existencia de claves")
    void testHasKey() {
        jsonObject.put("existing", "value");

        assertTrue(jsonObject.has("existing"));
        assertFalse(jsonObject.has("nonexistent"));
    }

    @Test
    @DisplayName("Debe eliminar claves correctamente")
    void testRemoveKey() {
        jsonObject.put("toRemove", "value")
                  .put("toKeep", "keepThis");

        jsonObject.remove("toRemove");

        assertFalse(jsonObject.has("toRemove"));
        assertTrue(jsonObject.has("toKeep"));
    }

    @Test
    @DisplayName("Debe obtener todas las claves")
    void testKeySet() {
        jsonObject.put("key1", "value1")
                  .put("key2", "value2")
                  .put("key3", "value3");

        Set<String> keys = jsonObject.keySet();

        assertEquals(3, keys.size());
        assertTrue(keys.contains("key1"));
        assertTrue(keys.contains("key2"));
        assertTrue(keys.contains("key3"));
    }

    @Test
    @DisplayName("Debe clonar correctamente")
    void testClone() {
        jsonObject.put("original", "value");

        JSONObject cloned = jsonObject.clone();

        assertEquals(jsonObject.toString(), cloned.toString());

        // Modificar el clon no debe afectar el original
        cloned.put("new", "clonedValue");
        assertFalse(jsonObject.has("new"));
    }

    @Test
    @DisplayName("Debe mezclar objetos correctamente")
    void testMerge() {
        jsonObject.put("key1", "value1")
                  .put("key2", "value2");

        JSONObject toMerge = new JSONObject()
            .put("key2", "newValue2")  // Sobrescribir
            .put("key3", "value3");     // Nuevo

        jsonObject.merge(toMerge, false);

        assertEquals("value1", jsonObject.getString("key1"));
        assertEquals("newValue2", jsonObject.getString("key2"));
        assertEquals("value3", jsonObject.getString("key3"));
    }

    @Test
    @DisplayName("Debe manejar arrays correctamente")
    void testArrayHandling() {
        List<String> list = Arrays.asList("item1", "item2", "item3");
        jsonObject.put("array", list);

        Object retrieved = jsonObject.get("array");
        assertNotNull(retrieved);
        assertTrue(retrieved instanceof List);

        @SuppressWarnings("unchecked")
        List<String> retrievedList = (List<String>) retrieved;
        assertEquals(3, retrievedList.size());
        assertEquals("item1", retrievedList.get(0));
    }

    @Test
    @DisplayName("Debe manejar valores con path notation")
    void testGetByPath() {
        JSONObject nested = new JSONObject()
            .put("inner", new JSONObject()
                .put("value", "deep"));

        jsonObject.put("outer", nested);

        Object value = jsonObject.getByPath("outer.inner.value");
        assertEquals("deep", value);
    }

    @Test
    @DisplayName("Debe validar JSON malformado")
    void testInvalidJSON() {
        assertThrows(JsonProcessingException.class, () -> {
            new JSONObject("{invalid json}");
        });
    }

    @Test
    @DisplayName("Debe manejar Map en constructor")
    void testMapConstructor() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", 42);

        JSONObject fromMap = new JSONObject(map);

        assertEquals("value1", fromMap.getString("key1"));
        assertEquals(42, fromMap.getInt("key2", 0));
    }

    @Test
    @DisplayName("Debe manejar valores por defecto")
    void testDefaultValues() {
        assertEquals(100, jsonObject.getInt("missing", 100));
        assertEquals(3.14, jsonObject.getDouble("missing", 3.14), 0.01);
        assertFalse(jsonObject.getBoolean("missing", false));
        assertEquals(999L, jsonObject.getLong("missing", 999L));
    }
}