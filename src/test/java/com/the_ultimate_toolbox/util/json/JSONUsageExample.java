package com.the_ultimate_toolbox.util.json;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Ejemplos de uso de las clases JSONObject y JSONReader optimizadas con Jackson.
 *
 * @author The Ultimate Toolbox
 * @since 2.2.0
 */
public class JSONUsageExample {

    public static void main(String[] args) {
        try {
            // ======= EJEMPLO 1: Crear y manipular JSONObject con Fluent API =======
            System.out.println("=== EJEMPLO 1: Creación con Fluent API ===");

            JSONObject json = JSONObject.builder()
                .put("nombre", "The Ultimate Toolbox")
                .put("version", "2.2.0")
                .put("activo", true)
                .put("descargas", 15000)
                .put("rating", 4.8)
                .build();

            System.out.println("JSON creado: " + json.toPrettyString());

            // ======= EJEMPLO 2: Crear desde Map =======
            System.out.println("\n=== EJEMPLO 2: Creación desde Map ===");

            Map<String, Object> datos = new HashMap<>();
            datos.put("usuario", "developer");
            datos.put("experiencia", 5);
            datos.put("skills", new String[]{"Java", "Spring", "Jackson"});

            JSONObject jsonFromMap = new JSONObject(datos);
            System.out.println("JSON desde Map: " + jsonFromMap.toPrettyString());

            // ======= EJEMPLO 3: Acceso a valores con tipo seguro =======
            System.out.println("\n=== EJEMPLO 3: Acceso a valores ===");

            String nombre = json.getString("nombre");
            int descargas = json.getInt("descargas", 0);
            double rating = json.getDouble("rating", 0.0);

            System.out.println("Nombre: " + nombre);
            System.out.println("Descargas: " + descargas);
            System.out.println("Rating: " + rating);

            // ======= EJEMPLO 4: JSONObject anidado =======
            System.out.println("\n=== EJEMPLO 4: Objetos anidados ===");

            JSONObject config = new JSONObject()
                .put("servidor", new JSONObject()
                    .put("host", "localhost")
                    .put("puerto", 8080)
                    .put("ssl", false))
                .put("database", new JSONObject()
                    .put("url", "jdbc:mysql://localhost:3306/db")
                    .put("usuario", "admin")
                    .put("pool_size", 10));

            System.out.println("Configuración: " + config.toPrettyString());

            // Acceso con path notation
            Object puerto = config.getByPath("servidor.puerto");
            System.out.println("Puerto del servidor: " + puerto);

            // ======= EJEMPLO 5: Merge de JSONObjects =======
            System.out.println("\n=== EJEMPLO 5: Merge de objetos ===");

            JSONObject defaults = new JSONObject()
                .put("timeout", 5000)
                .put("retries", 3)
                .put("debug", false);

            JSONObject userConfig = new JSONObject()
                .put("timeout", 10000)
                .put("debug", true)
                .put("custom", "value");

            // Merge con prioridad a userConfig
            defaults.merge(userConfig, false);
            System.out.println("Config merged: " + defaults.toPrettyString());

            // ======= EJEMPLO 6: Lectura de JSON desde string =======
            System.out.println("\n=== EJEMPLO 6: Parseo desde String ===");

            String jsonString = """
                {
                    "producto": "Laptop",
                    "precio": 1299.99,
                    "stock": 25,
                    "specs": {
                        "cpu": "Intel i7",
                        "ram": "16GB",
                        "ssd": "512GB"
                    }
                }
                """;

            JSONObject producto = new JSONObject(jsonString);
            System.out.println("Producto parseado: " + producto.getString("producto"));
            System.out.println("Precio: $" + producto.getDouble("precio", 0));

            JSONObject specs = producto.getJSONObject("specs");
            System.out.println("CPU: " + specs.getString("cpu"));

            // ======= EJEMPLO 7: JSONReader para archivos =======
            System.out.println("\n=== EJEMPLO 7: Uso de JSONReader ===");

            // Configurar reader
            JSONReader.ReaderConfig config2 = JSONReader.ReaderConfig.defaultConfig()
                .bufferSize(16384)
                .smallFileThreshold(2_097_152); // 2MB

            JSONReader reader = new JSONReader(config2);

            // Ejemplo de lectura de string
            JSONObject parsed = reader.parseString(jsonString);
            System.out.println("Leído con JSONReader: " + parsed.getString("producto"));

            // ======= EJEMPLO 8: Stream de objetos JSON =======
            System.out.println("\n=== EJEMPLO 8: Procesamiento con Stream API ===");

            // Usar Stream API del JSONObject
            json.stream()
                .filter(entry -> entry.getValue() instanceof Number)
                .forEach(entry ->
                    System.out.println("Campo numérico: " + entry.getKey() + " = " + entry.getValue())
                );

            // ======= EJEMPLO 9: Validación y comprobaciones =======
            System.out.println("\n=== EJEMPLO 9: Validaciones ===");

            System.out.println("¿Tiene 'version'?: " + json.has("version"));
            System.out.println("¿Tiene 'noexiste'?: " + json.has("noexiste"));
            System.out.println("¿'noexiste' es null?: " + json.isNull("noexiste"));
            System.out.println("Número de campos: " + json.size());
            System.out.println("Claves: " + json.keySet());

            // ======= EJEMPLO 10: Clonación y modificación =======
            System.out.println("\n=== EJEMPLO 10: Clonación ===");

            JSONObject original = new JSONObject()
                .put("valor", 100)
                .put("texto", "original");

            JSONObject clon = original.clone();
            clon.put("texto", "modificado")
                .put("nuevo", true);

            System.out.println("Original: " + original.toString());
            System.out.println("Clon modificado: " + clon.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ejemplo de procesamiento de archivo JSON grande con streaming.
     */
    public static void procesarArchivoGrande(Path archivo) {
        try (JSONReader reader = new JSONReader()) {

            // Procesar archivo grande línea por línea sin cargar todo en memoria
            reader.readLargeFile(archivo.toFile(), jsonObject -> {
                // Procesar cada objeto
                System.out.println("Procesando: " + jsonObject.getString("id"));
                // Hacer algo con el objeto...
            });

            // O procesar en batches
            reader.readArrayInBatches(archivo, 100, batch -> {
                System.out.println("Procesando batch de " + batch.size() + " elementos");
                // Procesar el batch...
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}