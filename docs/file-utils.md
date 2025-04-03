# Documentación de Utilidades de Archivos

## Utilidades para Lectura/Escritura de Archivos en The_Ultimate_Toolbox

### Introducción

El módulo de utilidades de archivos de The_Ultimate_Toolbox proporciona un conjunto de herramientas simplificadas y potentes para realizar operaciones comunes de lectura y escritura de archivos en aplicaciones Java. Este módulo está diseñado para eliminar el código repetitivo y proporcionar una API intuitiva que cubra la mayoría de los escenarios de uso relacionados con la manipulación de archivos.

### Arquitectura

La arquitectura del módulo de utilidades de archivos está estructurada de la siguiente manera:

```
com.ultimatetoolbox.files/
├── core/                # Núcleo del sistema de archivos
│   ├── FileHandler.java          # Interfaz principal
│   ├── DefaultFileHandler.java   # Implementación por defecto
│   ├── FileOperation.java        # Operaciones base
│   ├── FileResult.java           # Resultados de operaciones
├── readers/             # Lectores de archivos
│   ├── TextFileReader.java       # Lector de archivos de texto
│   ├── BinaryFileReader.java     # Lector de archivos binarios
│   ├── PropertiesReader.java     # Lector de propiedades
│   ├── CsvReader.java            # Lector de archivos CSV
│   ├── JsonFileReader.java       # Lector de archivos JSON
│   ├── XmlFileReader.java        # Lector de archivos XML
├── writers/             # Escritores de archivos
│   ├── TextFileWriter.java       # Escritor de archivos de texto
│   ├── BinaryFileWriter.java     # Escritor de archivos binarios
│   ├── PropertiesWriter.java     # Escritor de propiedades
│   ├── CsvWriter.java            # Escritor de archivos CSV
│   ├── JsonFileWriter.java       # Escritor de archivos JSON
│   ├── XmlFileWriter.java        # Escritor de archivos XML
├── watchers/            # Observadores de archivos
│   ├── FileWatcher.java          # Observador de cambios
│   ├── DirectoryWatcher.java     # Observador de directorios
├── batch/               # Operaciones por lotes
│   ├── BatchOperation.java       # Interface para operaciones
│   ├── BatchProcessor.java       # Procesador de lotes
└── util/                # Utilidades
    ├── PathUtils.java            # Utilidades de rutas
    ├── FileTypeDetector.java     # Detector de tipos de archivo
    ├── FileComparator.java       # Comparador de archivos
    ├── FileSizeFormatter.java    # Formateador de tamaños
```

### Características Principales (MVP)

#### 1. Operaciones de Lectura de Archivos

El sistema proporciona una API simple y unificada para leer diferentes tipos de archivos.

**Lectura de archivos de texto:**
```java
// Lectura simple
String content = FileHandler.read("config.txt");

// Con opciones
String content = FileHandler.read("data.txt", TextOptions.builder()
    .charset(StandardCharsets.UTF_8)
    .trimLines(true)
    .skipEmptyLines(true)
    .build());

// Lectura línea por línea
FileHandler.readLines("logs.txt", line -> {
    // Procesar cada línea
    System.out.println(line);
});

// Lectura con Stream API
try (Stream<String> lines = FileHandler.lines("data.txt")) {
    lines.filter(line -> line.contains("ERROR"))
         .map(String::trim)
         .forEach(System.out::println);
}
```

**Lectura de archivos binarios:**
```java
// Lectura completa a bytes
byte[] data = FileHandler.readBytes("image.jpg");

// Lectura a InputStream
try (InputStream is = FileHandler.readStream("document.pdf")) {
    // Usar el stream
}
```

**Lectura de propiedades:**
```java
// Cargar todas las propiedades
Properties props = FileHandler.readProperties("config.properties");

// Cargar propiedades con prefijo
Map<String, String> dbProps = FileHandler.readProperties("config.properties", "db.");
String dbUrl = dbProps.get("url");
```

#### 2. Operaciones de Escritura de Archivos

El sistema proporciona métodos simplificados para escribir diferentes tipos de archivos.

**Escritura de archivos de texto:**
```java
// Escritura simple
FileHandler.write("output.txt", "Contenido del archivo");

// Con opciones
FileHandler.write("output.txt", content, TextOptions.builder()
    .charset(StandardCharsets.UTF_8)
    .append(true)
    .build());

// Escritura línea por línea
List<String> lines = Arrays.asList("Línea 1", "Línea 2", "Línea 3");
FileHandler.writeLines("output.txt", lines);
```

**Escritura de archivos binarios:**
```java
// Escritura de bytes
FileHandler.writeBytes("output.bin", byteData);

// Escritura desde InputStream
try (InputStream is = obtenerDatos()) {
    FileHandler.writeFromStream("output.dat", is);
}
```

**Escritura de propiedades:**
```java
// Guardar propiedades
Properties props = new Properties();
props.setProperty("db.url", "jdbc:mysql://localhost:3306/mydb");
props.setProperty("db.user", "admin");
FileHandler.writeProperties("config.properties", props, "Configuración de Base de Datos");
```

#### 3. Manejo de Rutas y Directorios

El sistema simplifica el trabajo con rutas y directorios.

**Operaciones con directorios:**
```java
// Crear directorio
FileHandler.createDirectory("data/backups");

// Listar archivos
List<File> files = FileHandler.listFiles("logs", "*.log");

// Buscar archivos recursivamente
List<File> results = FileHandler.findFiles("src", file -> 
    file.getName().endsWith(".java") && file.length() > 1024);

// Verificar existencia
boolean exists = FileHandler.exists("config.txt");
boolean isDir = FileHandler.isDirectory("data");
```

**Manipulación de rutas:**
```java
// Construir rutas independientes del sistema operativo
String path = PathUtils.combine("data", "users", "profiles");

// Obtener componentes de ruta
String fileName = PathUtils.getFileName("/path/to/file.txt"); // "file.txt"
String extension = PathUtils.getExtension("/path/to/file.txt"); // "txt"
String dir = PathUtils.getDirectory("/path/to/file.txt"); // "/path/to"
```

#### 4. Operaciones Avanzadas

El sistema proporciona operaciones más avanzadas para escenarios complejos.

**Copia y movimiento:**
```java
// Copiar archivos
FileHandler.copy("source.txt", "destination.txt");
FileHandler.copy("source.txt", "destination.txt", CopyOptions.builder()
    .overwrite(true)
    .preserveAttributes(true)
    .build());

// Mover archivos
FileHandler.move("old/location.txt", "new/location.txt");
```

**Operaciones atómicas:**
```java
// Reemplazar archivo de forma atómica
FileHandler.atomicReplace("config.txt", "nuevo contenido");

// Actualizar parte del contenido de forma atómica
FileHandler.atomicUpdate("config.txt", content -> 
    content.replace("oldValue=123", "oldValue=456"));
```

**Bloqueo de archivos:**
```java
try (FileLock lock = FileHandler.lock("data.txt")) {
    // Operaciones con el archivo bloqueado
    FileHandler.write("data.txt", "Contenido actualizado");
}
```

#### 5. Procesamiento de Archivos Específicos

El sistema incluye utilidades para formatos de archivo comunes.

**Archivos CSV:**
```java
// Leer CSV a lista de mapas
List<Map<String, String>> data = CsvReader.read("data.csv");

// Leer CSV a objetos
List<Usuario> usuarios = CsvReader.read("usuarios.csv", Usuario.class);

// Escribir objetos a CSV
List<Usuario> usuarios = obtenerUsuarios();
CsvWriter.write("usuarios.csv", usuarios);
```

**Archivos JSON:**
```java
// Leer JSON a objeto
Configuracion config = JsonFileReader.read("config.json", Configuracion.class);

// Leer JSON a estructura genérica
Map<String, Object> data = JsonFileReader.readMap("data.json");

// Escribir objeto a JSON
JsonFileWriter.write("config.json", config, true); // true = pretty print
```

### Observación de Cambios en Archivos

El sistema permite monitorear cambios en archivos y directorios.

```java
// Observar un archivo
FileWatcher watcher = FileHandler.watchFile("config.properties", event -> {
    System.out.println("El archivo ha cambiado: " + event.getType());
    
    // Recargar configuración
    Properties newProps = FileHandler.readProperties("config.properties");
    actualizarConfiguracion(newProps);
});

// Iniciar la observación
watcher.start();

// Detener cuando ya no sea necesario
watcher.stop();

// Observar un directorio
DirectoryWatcher dirWatcher = FileHandler.watchDirectory("logs", event -> {
    if (event.getType() == FileEventType.CREATED && event.getPath().endsWith(".log")) {
        System.out.println("Nuevo archivo de log: " + event.getPath());
        procesarNuevoLog(event.getPath());
    }
});

dirWatcher.start();
```

### Operaciones por Lotes

El sistema soporta operaciones por lotes para procesamiento eficiente.

```java
// Procesamiento por lotes
BatchProcessor.process("data/*.csv", file -> {
    List<Registro> registros = CsvReader.read(file, Registro.class);
    procesarRegistros(registros);
});

// Operaciones complejas por lotes
BatchProcessor.builder()
    .source("documents/*.txt")
    .filter(file -> file.length() > 0)
    .transform(file -> {
        String content = FileHandler.read(file);
        return content.toUpperCase();
    })
    .consume((file, transformedContent) -> {
        String outputPath = "processed/" + file.getName();
        FileHandler.write(outputPath, transformedContent);
    })
    .parallel(true)
    .batchSize(10)
    .execute();
```

### Utilidades Adicionales

El sistema proporciona utilidades adicionales para escenarios comunes.

**Compresión/Descompresión:**
```java
// Comprimir archivos
FileHandler.compress("data.txt", "data.zip");
FileHandler.compressDirectory("logs", "logs.zip");

// Descomprimir archivos
FileHandler.decompress("data.zip", "extracted");
```

**Cálculo de hashes:**
```java
// Calcular hash de archivo
String md5 = FileHandler.calculateHash("document.pdf", HashAlgorithm.MD5);
String sha256 = FileHandler.calculateHash("document.pdf", HashAlgorithm.SHA_256);
```

**Serialización/Deserialización:**
```java
// Serializar objeto
Usuario usuario = new Usuario("juan", "juan@example.com");
FileHandler.serialize("usuario.ser", usuario);

// Deserializar objeto
Usuario cargado = FileHandler.deserialize("usuario.ser", Usuario.class);
```

### Roadmap de Desarrollo Futuro

1. **MVP (3 meses):**
   - Implementación de operaciones básicas de lectura/escritura
   - Soporte para diferentes formatos de archivo (texto, binario, propiedades)
   - Manejo de rutas y directorios
   - Utilidades para formatos comunes (CSV, JSON)

2. **Versiones Futuras:**
   - Soporte para más formatos de archivo (XML, YAML, etc.)
   - Mejoras en rendimiento para archivos grandes
   - Operaciones asíncronas y paralelas
   - Integración con servicios de almacenamiento en la nube
   - Cifrado/descifrado de archivos
   - Más operaciones por lotes y procesamiento en streaming

### Consideraciones Técnicas

- **Compatibilidad:** Java 8+
- **Rendimiento:** Optimizado para diferentes tamaños de archivo
- **Thread-safety:** Seguro para uso en entornos multihilo
- **Recursos:** Manejo adecuado de recursos (cierre de streams, etc.)
- **Seguridad:** Prevención de ataques como path traversal
- **Extensibilidad:** Facilidad para añadir soporte para nuevos formatos