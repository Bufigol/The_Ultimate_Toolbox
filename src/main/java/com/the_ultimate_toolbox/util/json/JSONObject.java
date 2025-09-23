package com.the_ultimate_toolbox.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Representación optimizada de un objeto JSON con soporte para manipulación fluida.
 * Thread-safe y optimizada para rendimiento con Jackson.
 *
 * Características principales:
 * - API fluida para construcción y manipulación
 * - Thread-safe mediante copy-on-write para operaciones de escritura
 * - Caché de rutas para acceso rápido a elementos anidados
 * - Soporte para JSON, JSON5 y JSONC
 * - Gestión eficiente de memoria
 *
 * @author The Ultimate Toolbox
 * @since 2.2.0
 */
public class JSONObject implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    // Nodo raíz de Jackson
    private volatile ObjectNode rootNode;

    // Motor Jackson singleton
    private final JacksonEngine engine;

    // Formato JSON utilizado
    private JacksonEngine.JSONFormat format;

    // Cache de rutas para acceso rápido (thread-safe)
    private final ConcurrentHashMap<String, JsonNode> pathCache;

    // Flag para indicar si el cache necesita invalidación
    private volatile boolean cacheValid = false;

    /**
     * Constructor por defecto. Crea un objeto JSON vacío.
     */
    public JSONObject() {
        this.engine = JacksonEngine.getInstance();
        this.rootNode = engine.getStandardMapper().createObjectNode();
        this.format = JacksonEngine.JSONFormat.STANDARD;
        this.pathCache = new ConcurrentHashMap<>();
    }

    /**
     * Constructor desde string JSON.
     *
     * @param jsonString string JSON a parsear
     * @throws JsonProcessingException si el JSON es inválido
     */
    public JSONObject(String jsonString) throws JsonProcessingException {
        this.engine = JacksonEngine.getInstance();
        this.pathCache = new ConcurrentHashMap<>();

        // Detectar formato automáticamente
        ObjectMapper mapper = engine.detectAndGetMapper(jsonString);
        this.format = detectFormat(jsonString);

        JsonNode node = mapper.readTree(jsonString);
        if (!node.isObject()) {
            throw new IllegalArgumentException("El JSON debe ser un objeto, no un array o valor primitivo");
        }
        this.rootNode = (ObjectNode) node;
    }

    /**
     * Constructor desde JsonNode de Jackson.
     *
     * @param node nodo a envolver
     */
    protected JSONObject(ObjectNode node) {
        this.engine = JacksonEngine.getInstance();
        this.rootNode = node;
        this.format = JacksonEngine.JSONFormat.STANDARD;
        this.pathCache = new ConcurrentHashMap<>();
    }

    /**
     * Constructor desde Map.
     *
     * @param map mapa con los valores iniciales
     */
    public JSONObject(Map<String, Object> map) {
        this();
        if (map != null) {
            map.forEach(this::put);
        }
    }

    /**
     * Crea un builder para construcción fluida.
     *
     * @return nuevo builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Detecta el formato JSON del string.
     *
     * @param jsonString string a analizar
     * @return formato detectado
     */
    private JacksonEngine.JSONFormat detectFormat(String jsonString) {
        if (jsonString.contains("//") || jsonString.contains("/*")) {
            if (jsonString.contains("Infinity") || jsonString.contains("NaN")) {
                return JacksonEngine.JSONFormat.JSON5;
            }
            return JacksonEngine.JSONFormat.JSONC;
        }
        if (jsonString.contains("Infinity") || jsonString.contains("NaN")) {
            return JacksonEngine.JSONFormat.JSON5;
        }
        return JacksonEngine.JSONFormat.STANDARD;
    }

    // ============= Métodos de escritura (Fluent API) =============

    /**
     * Agrega o actualiza un valor. Thread-safe mediante copy-on-write.
     *
     * @param key clave
     * @param value valor
     * @return este objeto para encadenamiento
     */
    public JSONObject put(String key, Object value) {
        Objects.requireNonNull(key, "La clave no puede ser null");

        // Copy-on-write para thread-safety
        synchronized (this) {
            invalidateCache();
            if (value == null) {
                rootNode.putNull(key);
            } else if (value instanceof String) {
                rootNode.put(key, (String) value);
            } else if (value instanceof Integer) {
                rootNode.put(key, (Integer) value);
            } else if (value instanceof Long) {
                rootNode.put(key, (Long) value);
            } else if (value instanceof Double) {
                rootNode.put(key, (Double) value);
            } else if (value instanceof Float) {
                rootNode.put(key, (Float) value);
            } else if (value instanceof Boolean) {
                rootNode.put(key, (Boolean) value);
            } else if (value instanceof BigInteger) {
                rootNode.put(key, (BigInteger) value);
            } else if (value instanceof BigDecimal) {
                rootNode.put(key, (BigDecimal) value);
            } else if (value instanceof JSONObject) {
                rootNode.set(key, ((JSONObject) value).rootNode);
            } else if (value instanceof JsonNode) {
                rootNode.set(key, (JsonNode) value);
            } else if (value instanceof Map) {
                rootNode.set(key, engine.getStandardMapper().valueToTree(value));
            } else if (value instanceof Collection) {
                rootNode.set(key, engine.getStandardMapper().valueToTree(value));
            } else {
                // Intentar convertir objetos complejos
                rootNode.set(key, engine.getStandardMapper().valueToTree(value));
            }
        }
        return this;
    }

    /**
     * Agrega múltiples pares clave-valor.
     *
     * @param entries mapa con las entradas
     * @return este objeto para encadenamiento
     */
    public JSONObject putAll(Map<String, Object> entries) {
        if (entries != null) {
            entries.forEach(this::put);
        }
        return this;
    }

    /**
     * Elimina una clave.
     *
     * @param key clave a eliminar
     * @return este objeto para encadenamiento
     */
    public JSONObject remove(String key) {
        synchronized (this) {
            invalidateCache();
            rootNode.remove(key);
        }
        return this;
    }

    // ============= Métodos de lectura =============

    /**
     * Obtiene un valor por clave.
     *
     * @param key clave
     * @return valor o null si no existe
     */
    public Object get(String key) {
        JsonNode node = rootNode.get(key);
        return nodeToObject(node);
    }

    /**
     * Obtiene un valor con tipo específico.
     *
     * @param key clave
     * @param type clase del tipo esperado
     * @param <T> tipo genérico
     * @return valor convertido o null
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        JsonNode node = rootNode.get(key);
        if (node == null || node.isNull()) {
            return null;
        }
        try {
            return engine.getStandardMapper().treeToValue(node, type);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("No se puede convertir el valor a " + type.getName(), e);
        }
    }

    /**
     * Obtiene un valor usando path notation (ej: "user.address.street").
     *
     * @param path ruta con notación de puntos
     * @return valor o null si no existe
     */
    public Object getByPath(String path) {
        // Usar caché si está válido
        if (cacheValid && pathCache.containsKey(path)) {
            return nodeToObject(pathCache.get(path));
        }

        JsonNode node = rootNode.at("/" + path.replace(".", "/"));
        if (!node.isMissingNode() && cacheValid) {
            pathCache.put(path, node);
        }
        return nodeToObject(node);
    }

    /**
     * Obtiene un string.
     *
     * @param key clave
     * @return valor string o null
     */
    public String getString(String key) {
        JsonNode node = rootNode.get(key);
        return (node != null && !node.isNull()) ? node.asText() : null;
    }

    /**
     * Obtiene un entero.
     *
     * @param key clave
     * @param defaultValue valor por defecto
     * @return valor entero o el valor por defecto
     */
    public int getInt(String key, int defaultValue) {
        JsonNode node = rootNode.get(key);
        return (node != null && node.isNumber()) ? node.asInt() : defaultValue;
    }

    /**
     * Obtiene un long.
     *
     * @param key clave
     * @param defaultValue valor por defecto
     * @return valor long o el valor por defecto
     */
    public long getLong(String key, long defaultValue) {
        JsonNode node = rootNode.get(key);
        return (node != null && node.isNumber()) ? node.asLong() : defaultValue;
    }

    /**
     * Obtiene un double.
     *
     * @param key clave
     * @param defaultValue valor por defecto
     * @return valor double o el valor por defecto
     */
    public double getDouble(String key, double defaultValue) {
        JsonNode node = rootNode.get(key);
        return (node != null && node.isNumber()) ? node.asDouble() : defaultValue;
    }

    /**
     * Obtiene un boolean.
     *
     * @param key clave
     * @param defaultValue valor por defecto
     * @return valor boolean o el valor por defecto
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        JsonNode node = rootNode.get(key);
        return (node != null && node.isBoolean()) ? node.asBoolean() : defaultValue;
    }

    /**
     * Obtiene un JSONObject anidado.
     *
     * @param key clave
     * @return JSONObject o null
     */
    public JSONObject getJSONObject(String key) {
        JsonNode node = rootNode.get(key);
        if (node != null && node.isObject()) {
            return new JSONObject((ObjectNode) node);
        }
        return null;
    }

    // ============= Métodos de validación =============

    /**
     * Verifica si contiene una clave.
     *
     * @param key clave a verificar
     * @return true si existe la clave
     */
    public boolean has(String key) {
        return rootNode.has(key);
    }

    /**
     * Verifica si el valor de una clave es null.
     *
     * @param key clave a verificar
     * @return true si el valor es null o no existe
     */
    public boolean isNull(String key) {
        JsonNode node = rootNode.get(key);
        return node == null || node.isNull();
    }

    /**
     * Verifica si está vacío.
     *
     * @return true si no tiene elementos
     */
    public boolean isEmpty() {
        return rootNode.isEmpty();
    }

    /**
     * Obtiene el número de elementos.
     *
     * @return tamaño del objeto
     */
    public int size() {
        return rootNode.size();
    }

    // ============= Métodos de serialización =============

    /**
     * Convierte a string JSON.
     *
     * @return representación JSON
     */
    @Override
    public String toString() {
        try {
            return getMapper().writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar JSON", e);
        }
    }

    /**
     * Convierte a string JSON con formato pretty.
     *
     * @return JSON formateado
     */
    public String toPrettyString() {
        try {
            return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar JSON", e);
        }
    }

    /**
     * Escribe a un archivo.
     *
     * @param file archivo destino
     * @param pretty true para formato pretty
     * @throws IOException si hay error de E/O
     */
    public void writeToFile(File file, boolean pretty) throws IOException {
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            if (pretty) {
                getMapper().writerWithDefaultPrettyPrinter().writeValue(writer, rootNode);
            } else {
                getMapper().writeValue(writer, rootNode);
            }
        }
    }

    // ============= Métodos de utilidad =============

    /**
     * Obtiene todas las claves.
     *
     * @return set de claves
     */
    public Set<String> keySet() {
        Set<String> keys = new LinkedHashSet<>();
        rootNode.fieldNames().forEachRemaining(keys::add);
        return keys;
    }

    /**
     * Obtiene un stream de las entradas.
     *
     * @return stream de pares clave-valor
     */
    public Stream<Map.Entry<String, Object>> stream() {
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(rootNode.fields(), Spliterator.ORDERED),
            false
        ).map(entry -> new AbstractMap.SimpleEntry<>(
            entry.getKey(),
            nodeToObject(entry.getValue())
        ));
    }

    /**
     * Convierte a Map.
     *
     * @return mapa con los valores
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        rootNode.fields().forEachRemaining(entry ->
            map.put(entry.getKey(), nodeToObject(entry.getValue()))
        );
        return map;
    }

    /**
     * Clona el objeto.
     *
     * @return copia profunda
     */
    @Override
    public JSONObject clone() {
        try {
            JSONObject cloned = (JSONObject) super.clone();
            cloned.rootNode = rootNode.deepCopy();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar JSONObject", e);
        }
    }

    /**
     * Mezcla otro JSONObject en este.
     *
     * @param other objeto a mezclar
     * @param deep true para mezcla profunda
     * @return este objeto
     */
    public JSONObject merge(JSONObject other, boolean deep) {
        if (other == null) return this;

        synchronized (this) {
            invalidateCache();
            if (deep) {
                deepMerge(this.rootNode, other.rootNode);
            } else {
                other.rootNode.fields().forEachRemaining(entry ->
                    rootNode.set(entry.getKey(), entry.getValue())
                );
            }
        }
        return this;
    }

    // ============= Métodos privados =============

    private ObjectMapper getMapper() {
        return switch (format) {
            case JSON5 -> engine.getJSON5Mapper();
            case JSONC -> engine.getJSONCMapper();
            default -> engine.getStandardMapper();
        };
    }

    private void invalidateCache() {
        cacheValid = false;
        pathCache.clear();
    }

    private Object nodeToObject(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isTextual()) {
            return node.asText();
        }
        if (node.isInt()) {
            return node.asInt();
        }
        if (node.isLong()) {
            return node.asLong();
        }
        if (node.isDouble()) {
            return node.asDouble();
        }
        if (node.isBoolean()) {
            return node.asBoolean();
        }
        if (node.isObject()) {
            return new JSONObject((ObjectNode) node);
        }
        if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            node.forEach(item -> list.add(nodeToObject(item)));
            return list;
        }
        return node.toString();
    }

    private void deepMerge(ObjectNode target, ObjectNode source) {
        source.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode sourceValue = entry.getValue();

            if (target.has(key)) {
                JsonNode targetValue = target.get(key);
                if (targetValue.isObject() && sourceValue.isObject()) {
                    deepMerge((ObjectNode) targetValue, (ObjectNode) sourceValue);
                } else {
                    target.set(key, sourceValue);
                }
            } else {
                target.set(key, sourceValue);
            }
        });
    }

    // ============= Builder Pattern =============

    /**
     * Builder para construcción fluida de JSONObject.
     */
    public static class Builder {
        private final JSONObject json;

        public Builder() {
            this.json = new JSONObject();
        }

        public Builder put(String key, Object value) {
            json.put(key, value);
            return this;
        }

        public Builder putAll(Map<String, Object> map) {
            json.putAll(map);
            return this;
        }

        public Builder format(JacksonEngine.JSONFormat format) {
            json.format = format;
            return this;
        }

        public JSONObject build() {
            return json;
        }
    }
}
