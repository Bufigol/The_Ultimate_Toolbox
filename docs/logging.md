# Documentación del Sistema de Logging

## Sistema de Logging en The_Ultimate_Toolbox

### Introducción

El módulo de logging de The_Ultimate_Toolbox proporciona un sistema robusto, flexible y fácil de usar para el registro de eventos y actividades en aplicaciones Java. Este sistema está diseñado para integrar múltiples frameworks de logging existentes bajo una interfaz unificada, permitiendo una configuración sencilla y una salida en formato Markdown que facilita la lectura y el análisis.

### Arquitectura

La arquitectura del sistema de logging está estructurada de la siguiente manera:

```
com.ultimatetoolbox.logging/
├── core/                # Núcleo del sistema de logging
│   ├── Logger.java                 # Interfaz principal
│   ├── LogLevel.java               # Niveles de log
│   ├── LogEntry.java               # Entrada de log individual
│   ├── LoggerFactory.java          # Fábrica de loggers
├── appenders/           # Destinos de salida para logs
│   ├── Appender.java              # Interfaz para appenders
│   ├── FileAppender.java          # Appender para archivos
│   ├── ConsoleAppender.java       # Appender para consola
│   ├── MarkdownFileAppender.java  # Appender para archivos Markdown
│   ├── MultiAppender.java         # Appender múltiple
├── formatters/          # Formateadores de mensajes
│   ├── LogFormatter.java          # Interfaz para formateadores
│   ├── DefaultFormatter.java      # Formateador predeterminado
│   ├── MarkdownFormatter.java     # Formateador para Markdown
│   ├── JsonFormatter.java         # Formateador para JSON
├── adapters/            # Adaptadores para sistemas existentes
│   ├── Slf4jAdapter.java          # Adaptador para SLF4J
│   ├── Log4jAdapter.java          # Adaptador para Log4j
│   ├── JulAdapter.java            # Adaptador para java.util.logging
├── config/              # Configuración del sistema
│   ├── LogConfig.java             # Configuración del logger
│   ├── AppenderConfig.java        # Configuración de appenders
│   ├── LoggingProperties.java     # Propiedades de configuración
└── util/                # Utilidades
    ├── LogExporter.java           # Exportador de logs
    ├── LogRotation.java           # Rotación de archivos de log
    ├── LogContext.java            # Contexto de logging
```

### Características Principales (MVP)

#### 1. Niveles de Logging Completos

El sistema proporciona los siguientes niveles de logging:

- **TRACE:** Información muy detallada, útil para depuración profunda.
- **DEBUG:** Información útil para depuración.
- **INFO:** Información general sobre el progreso de la aplicación.
- **WARN:** Advertencias sobre situaciones potencialmente problemáticas.
- **ERROR:** Errores que permiten a la aplicación continuar.
- **FATAL:** Errores graves que pueden provocar la terminación de la aplicación.

**Ejemplo de uso:**
```java
Logger logger = LoggerFactory.getLogger("MiClase");

logger.trace("Detalle muy específico: valor={}", variable);
logger.debug("Información de depuración");
logger.info("Iniciando proceso de sincronización");
logger.warn("La operación está tardando más de lo esperado");
logger.error("No se pudo completar la operación", excepcion);
logger.fatal("Error crítico que requiere atención inmediata");
```

#### 2. Appenders Flexibles

Los appenders determinan dónde se envían los mensajes de log.

**Configuración básica:**
```java
LogConfig config = new LogConfig.Builder()
    .appender(new ConsoleAppender())
    .appender(new MarkdownFileAppender("./logs/app.md"))
    .build();

LoggerFactory.initialize(config);
```

**Configuración avanzada:**
```java
Appender fileAppender = new MarkdownFileAppender.Builder()
    .path("./logs/app.md")
    .rotationSize(10 * 1024 * 1024) // 10 MB
    .maxFiles(5)
    .append(true)
    .build();

Appender errorAppender = new MarkdownFileAppender.Builder()
    .path("./logs/errors.md")
    .threshold(LogLevel.ERROR)
    .build();

LogConfig config = new LogConfig.Builder()
    .appender(new ConsoleAppender())
    .appender(fileAppender)
    .appender(errorAppender)
    .build();

LoggerFactory.initialize(config);
```

#### 3. Formateo de Mensajes en Markdown

El sistema puede generar logs en formato Markdown para una mejor legibilidad y estructura.

**Ejemplo de archivo de log en Markdown:**
```markdown
# Log: 2023-04-15

## INFO [14:32:45] [main] com.example.app.UserService
Iniciando sesión de usuario: juan123

## WARN [14:32:46] [main] com.example.app.UserService
Múltiples intentos de inicio de sesión detectados
- Usuario: juan123
- Intentos: 3
- Última IP: 192.168.1.5

## ERROR [14:32:47] [main] com.example.app.UserService
No se pudo completar el inicio de sesión

### Excepción
```
java.sql.SQLException: Error de conexión a la base de datos
    at com.example.app.db.ConnectionManager.getConnection(ConnectionManager.java:45)
    at com.example.app.service.UserServiceImpl.login(UserServiceImpl.java:32)
    ...
```

### Contexto
- User: juan123
- IP: 192.168.1.5
- Timestamp: 2023-04-15 14:32:47
```

#### 4. Contextualización de Logs

El sistema permite añadir contexto a los logs para enriquecer la información disponible.

**Contexto global:**
```java
LogContext.put("appVersion", "1.2.3");
LogContext.put("environment", "production");

// Los logs incluirán esta información automáticamente
logger.info("Operación completada");
```

**Contexto por hilo (Thread):**
```java
try (LogContext.ThreadScope scope = LogContext.startThreadScope()) {
    scope.put("userId", user.getId());
    scope.put("sessionId", session.getId());
    
    // Operaciones con logging...
    logger.info("Usuario ha iniciado sesión");
    userService.updateLastLogin();
    // ...
}
// Al salir del bloque, el contexto se limpia automáticamente
```

**Contexto para un solo log:**
```java
logger.info("Procesando pago", Map.of(
    "orderId", order.getId(),
    "amount", order.getTotal(),
    "currency", order.getCurrency()
));
```

#### 5. Integración con Frameworks Existentes

El sistema se integra con los frameworks de logging más populares, permitiendo usar The_Ultimate_Toolbox con código existente.

**Integración con SLF4J:**
```java
// Configuración en aplicación que ya usa SLF4J
LogConfig config = new LogConfig.Builder()
    .useSlf4jBridge(true)
    .build();

LoggerFactory.initialize(config);

// Ahora el código existente que usa SLF4J enviará logs al sistema de The_Ultimate_Toolbox
org.slf4j.Logger slf4jLogger = org.slf4j.LoggerFactory.getLogger("ExistingClass");
slf4jLogger.info("Este mensaje irá al sistema de The_Ultimate_Toolbox");
```

**Uso como implementación de SLF4J:**
```java
// Configuración como implementación de SLF4J
LogConfig config = new LogConfig.Builder()
    .registerAsSlf4jImplementation(true)
    .appender(new MarkdownFileAppender("./logs/app.md"))
    .build();

LoggerFactory.initialize(config);
```

#### 6. Configuración Flexible

El sistema puede configurarse a través de código, archivo de propiedades o variables de entorno.

**Configuración por código:**
```java
LogConfig config = new LogConfig.Builder()
    .rootLevel(LogLevel.INFO)
    .logger("com.example.app.service", LogLevel.DEBUG)
    .logger("com.example.app.repository", LogLevel.WARN)
    .appender(new ConsoleAppender())
    .appender(new MarkdownFileAppender("./logs/app.md"))
    .build();

LoggerFactory.initialize(config);
```

**Configuración por propiedades:**
```properties
# logging.properties
logging.root.level=INFO
logging.logger.com.example.app.service=DEBUG
logging.logger.com.example.app.repository=WARN

logging.appender.console=true
logging.appender.file.path=./logs/app.md
logging.appender.file.rotation=10MB
logging.appender.file.max-files=5
```

```java
LogConfig config = LoggingProperties.fromFile("logging.properties");
LoggerFactory.initialize(config);
```

### Personalización Avanzada

#### 1. Appenders Personalizados

Se pueden crear appenders personalizados para enviar logs a destinos específicos.

**Ejemplo de appender personalizado:**
```java
public class DatabaseAppender implements Appender {
    private final DataSource dataSource;
    
    public DatabaseAppender(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public void append(LogEntry entry) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO app_logs (timestamp, level, logger, message, thread, exception) VALUES (?, ?, ?, ?, ?, ?)")) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(entry.getTimestamp()));
            stmt.setString(2, entry.getLevel().name());
            stmt.setString(3, entry.getLogger());
            stmt.setString(4, entry.getMessage());
            stmt.setString(5, entry.getThread());
            stmt.setString(6, entry.getException() != null ? 
                ExceptionUtils.getStackTrace(entry.getException()) : null);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Fallback al sistema de consola
            System.err.println("Error al guardar log en base de datos: " + e.getMessage());
        }
    }
}
```

**Uso:**
```java
DataSource dataSource = // obtener datasource
Appender dbAppender = new DatabaseAppender(dataSource);

LogConfig config = new LogConfig.Builder()
    .appender(dbAppender)
    .build();

LoggerFactory.initialize(config);
```

#### 2. Filtros de Log

Los filtros permiten un control más granular sobre qué mensajes se registran.

**Ejemplo de filtro:**
```java
public class SensitiveDataFilter implements LogFilter {
    private final Pattern creditCardPattern = Pattern.compile("\\d{4}-\\d{4}-\\d{4}-\\d{4}");
    private final Pattern ssnPattern = Pattern.compile("\\d{3}-\\d{2}-\\d{4}");
    
    @Override
    public LogEntry filter(LogEntry entry) {
        if (entry == null) return null;
        
        String message = entry.getMessage();
        if (message != null) {
            message = creditCardPattern.matcher(message).replaceAll("XXXX-XXXX-XXXX-XXXX");
            message = ssnPattern.matcher(message).replaceAll("XXX-XX-XXXX");
            
            return new LogEntry.Builder(entry)
                .message(message)
                .build();
        }
        
        return entry;
    }
}
```

**Uso:**
```java
LogConfig config = new LogConfig.Builder()
    .filter(new SensitiveDataFilter())
    .appender(new ConsoleAppender())
    .build();

LoggerFactory.initialize(config);
```

### Integración con el Sistema de Gestión de Errores

El sistema de logging se integra perfectamente con el módulo de gestión de errores de The_Ultimate_Toolbox.

```java
try {
    // Operación que puede fallar
} catch (Exception e) {
    logger.error("Error en operación", e);
    
    // El sistema de errores puede usar el mismo archivo de log
    ErrorManager.handle(e);
}
```

### Múltiples Archivos de Log

El sistema permite configurar múltiples archivos de log para diferentes propósitos.

```java
Appender appLogs = new MarkdownFileAppender.Builder()
    .path("./logs/app.md")
    .build();

Appender accessLogs = new MarkdownFileAppender.Builder()
    .path("./logs/access.md")
    .build();

Appender errorLogs = new MarkdownFileAppender.Builder()
    .path("./logs/errors.md")
    .threshold(LogLevel.ERROR)
    .build();

// Logger específico para accesos
Logger accessLogger = LoggerFactory.getLogger("access");
LoggerFactory.configure(accessLogger, new LogConfig.Builder()
    .rootLevel(LogLevel.INFO)
    .appender(accessLogs)
    .build());

// Configuración global
LogConfig config = new LogConfig.Builder()
    .rootLevel(LogLevel.INFO)
    .appender(appLogs)
    .appender(errorLogs)
    .build();

LoggerFactory.initialize(config);
```

**Uso:**
```java
// Logger general (va a app.md y errors.md para errores)
Logger logger = LoggerFactory.getLogger("MiClase");
logger.info("Información general");
logger.error("Este mensaje va a app.md y errors.md");

// Logger de acceso (va solo a access.md)
Logger accessLogger = LoggerFactory.getLogger("access");
accessLogger.info("Usuario accedió a /api/resource");
```

### Roadmap de Desarrollo Futuro

1. **MVP (3 meses):**
   - Implementación de niveles de logging completos
   - Appenders para consola y archivos Markdown
   - Formateo básico de mensajes
   - Contextualización de logs
   - Integración básica con frameworks existentes

2. **Versiones Futuras:**
   - Interfaz web para visualizar y analizar logs
   - Más appenders (base de datos, sistemas cloud, etc.)
   - Mejoras en rendimiento para entornos de alta carga
   - Compresión automática de archivos de log antiguos
   - Búsqueda y análisis avanzado en archivos de log
   - Alertas basadas en patrones de log

### Consideraciones Técnicas

- **Compatibilidad:** Java 8+
- **Rendimiento:** Optimizado para minimizar el impacto en tiempo de ejecución
- **Thread-safety:** Seguro para uso en entornos multihilo
- **Tamaño:** Control sobre el tamaño de los archivos generados
- **Seguridad:** Filtrado de información sensible
- **Impacto en memoria:** Bajo consumo de memoria incluso con gran cantidad de logs
