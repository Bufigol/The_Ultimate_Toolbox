# Documentación de Gestión de Errores

## Sistema de Gestión de Errores en The_Ultimate_Toolbox

### Introducción

El módulo de gestión de errores de The_Ultimate_Toolbox proporciona un sistema robusto y flexible para manejar, documentar y registrar errores en aplicaciones Java. El sistema está diseñado para facilitar la identificación, seguimiento y resolución de problemas, mejorando la experiencia tanto para desarrolladores como para usuarios finales.

### Arquitectura

La arquitectura del sistema de gestión de errores está estructurada de la siguiente manera:

```
com.ultimatetoolbox.error/
├── core/                # Núcleo del sistema de errores
│   ├── ToolboxException.java        # Excepción base
│   ├── ErrorCode.java               # Interface para códigos de error
│   ├── ErrorCategory.java           # Categorías de errores
│   ├── ErrorSeverity.java           # Niveles de severidad
├── types/               # Tipos específicos de excepciones
│   ├── ValidationException.java     # Errores de validación
│   ├── DatabaseException.java       # Errores de base de datos
│   ├── ConfigurationException.java  # Errores de configuración
│   ├── IOException.java             # Errores de entrada/salida
│   ├── NetworkException.java        # Errores de red/comunicación
│   ├── SecurityException.java       # Errores de seguridad
├── handler/             # Manejadores de excepciones
│   ├── ErrorHandler.java            # Interface para manejadores
│   ├── LoggingErrorHandler.java     # Manejador con logging
│   ├── FileErrorHandler.java        # Manejador con exportación a archivo
├── i18n/                # Internacionalización de mensajes
│   ├── ErrorMessages.java           # Gestor de mensajes
│   ├── resources/                   # Archivos de recursos
│       ├── errors_es.properties     # Mensajes en español
│       ├── errors_en.properties     # Mensajes en inglés
└── util/                # Utilidades
    ├── ErrorExporter.java           # Exportador de errores
    ├── ErrorContext.java            # Contexto detallado de errores
    ├── StackTraceFilter.java        # Filtro de trazas de pila
```

### Características Principales (MVP)

#### 1. Sistema de Excepciones Jerárquico

El sistema utiliza una jerarquía de excepciones que extiende de `ToolboxException`, la clase base para todas las excepciones de la biblioteca.

**Ejemplo de jerarquía:**
```
ToolboxException
├── ValidationException
│   ├── EmailValidationException
│   ├── PasswordValidationException
├── DatabaseException
│   ├── ConnectionException
│   ├── QueryException
├── ConfigurationException
└── ...
```

**Uso básico:**
```java
try {
    // Código que puede lanzar excepción
} catch (ValidationException e) {
    // Manejar error de validación
} catch (DatabaseException e) {
    // Manejar error de base de datos
} catch (ToolboxException e) {
    // Manejar cualquier otro error de la biblioteca
}
```

#### 2. Códigos de Error Estandarizados

Cada excepción incluye un código de error que facilita su identificación y documentación.

**Definición de códigos de error:**
```java
public enum DatabaseErrorCode implements ErrorCode {
    CONNECTION_FAILED("DB-001"),
    QUERY_SYNTAX_ERROR("DB-002"),
    CONSTRAINT_VIOLATION("DB-003"),
    TRANSACTION_FAILED("DB-004");
    
    private final String code;
    
    DatabaseErrorCode(String code) {
        this.code = code;
    }
    
    @Override
    public String getCode() {
        return code;
    }
}
```

**Uso en excepciones:**
```java
throw new ConnectionException(
    DatabaseErrorCode.CONNECTION_FAILED,
    "No se pudo conectar a la base de datos"
);
```

#### 3. Internacionalización de Mensajes de Error

Los mensajes de error se pueden internacionalizar fácilmente para soportar múltiples idiomas.

**Archivos de recursos:**
- `errors_es.properties`:
  ```
  DB-001=No se pudo conectar a la base de datos: {0}
  DB-002=Error de sintaxis en la consulta SQL: {0}
  ```
- `errors_en.properties`:
  ```
  DB-001=Could not connect to database: {0}
  DB-002=Syntax error in SQL query: {0}
  ```

**Uso:**
```java
// El mensaje se resolverá según el Locale
throw new ConnectionException(
    DatabaseErrorCode.CONNECTION_FAILED,
    ErrorMessages.getMessage(DatabaseErrorCode.CONNECTION_FAILED, Locale.getDefault(), "localhost:3306")
);
```

#### 4. Exportación de Errores a Archivos Markdown

El sistema puede exportar información detallada de errores a archivos Markdown para facilitar su documentación y análisis.

**Configuración básica:**
```java
ErrorHandler handler = new FileErrorHandler.Builder()
    .directory("./logs/errors")
    .format(ErrorExportFormat.MARKDOWN)
    .includeStackTrace(true)
    .includeSystemInfo(true)
    .build();

ErrorManager.setDefaultHandler(handler);
```

**Contenido típico de un archivo de error:**
```markdown
# Error Report: DB-001

## Basic Information

- **Error Code:** DB-001
- **Category:** DATABASE
- **Severity:** HIGH
- **Timestamp:** 2023-04-15 14:32:45
- **Thread:** main

## Message

No se pudo conectar a la base de datos: localhost:3306

## Context

- **Database URL:** jdbc:mysql://localhost:3306/mydb
- **User:** appuser
- **Application Module:** UserService

## Stack Trace

```
com.example.app.service.UserServiceImpl.findUser(UserServiceImpl.java:45)
com.example.app.controller.UserController.getUser(UserController.java:23)
...
```

## System Information

- **Java Version:** 11.0.12
- **OS:** Linux 5.10.0
- **Available Memory:** 2.4 GB / 8.0 GB
```

#### 5. Contexto de Error Enriquecido

Se puede añadir contexto adicional a las excepciones para facilitar la depuración y el análisis.

**Añadir contexto:**
```java
try {
    // Operación de base de datos
} catch (SQLException e) {
    throw new QueryException(DatabaseErrorCode.QUERY_SYNTAX_ERROR, e)
        .addContext("query", "SELECT * FROM users WHERE id = ?")
        .addContext("parameters", Arrays.toString(new Object[] { userId }))
        .addContext("module", "UserService");
}
```

**Recuperar contexto:**
```java
try {
    // Código que puede lanzar excepción
} catch (ToolboxException e) {
    Map<String, Object> context = e.getContext();
    System.out.println("Error en módulo: " + context.get("module"));
}
```

### Integración con el Sistema de Logging

El sistema de errores se integra perfectamente con el módulo de logging de The_Ultimate_Toolbox.

**Configuración automática:**
```java
// El ErrorHandler usará el Logger configurado automáticamente
ErrorManager.createDefaultHandler();
```

**Configuración manual:**
```java
Logger logger = LoggerFactory.getLogger("error-logger");
ErrorHandler handler = new LoggingErrorHandler(logger);
ErrorManager.setDefaultHandler(handler);
```

### Gestión Global de Excepciones

El sistema proporciona un manejador global de excepciones que puede integrarse con frameworks web como Spring o Jakarta EE.

**Ejemplo para Spring:**
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ToolboxException.class)
    public ResponseEntity<ErrorResponse> handleToolboxException(ToolboxException ex) {
        ErrorCode code = ex.getErrorCode();
        ErrorResponse response = new ErrorResponse(code.getCode(), ex.getMessage());
        
        // El error ya se registra automáticamente
        
        return ResponseEntity
            .status(determineHttpStatus(ex))
            .body(response);
    }
    
    private HttpStatus determineHttpStatus(ToolboxException ex) {
        if (ex instanceof ValidationException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof SecurityException) {
            return HttpStatus.FORBIDDEN;
        }
        // Otros mapeos...
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
```

### Inyección de Dependencias

El sistema es compatible con frameworks de inyección de dependencias como Spring.

**Ejemplo en Spring:**
```java
@Configuration
public class ErrorConfig {
    
    @Bean
    public ErrorHandler errorHandler() {
        return new FileErrorHandler.Builder()
            .directory("./logs/errors")
            .format(ErrorExportFormat.MARKDOWN)
            .build();
    }
    
    @Bean
    public ErrorManager errorManager(ErrorHandler errorHandler) {
        ErrorManager manager = new ErrorManager();
        manager.setDefaultHandler(errorHandler);
        return manager;
    }
}
```

### Personalización

El sistema permite una amplia personalización para adaptarse a las necesidades específicas de cada aplicación.

**Creación de excepciones personalizadas:**
```java
public class PaymentProcessingException extends ToolboxException {
    
    public static enum PaymentErrorCode implements ErrorCode {
        INSUFFICIENT_FUNDS("PAY-001"),
        CARD_EXPIRED("PAY-002"),
        GATEWAY_ERROR("PAY-003");
        
        private final String code;
        
        PaymentErrorCode(String code) {
            this.code = code;
        }
        
        @Override
        public String getCode() {
            return code;
        }
    }
    
    public PaymentProcessingException(PaymentErrorCode code, String message) {
        super(code, message);
    }
    
    // Otros constructores y métodos
}
```

**Personalización del formato de exportación:**
```java
public class CustomMarkdownExporter implements ErrorExporter {
    
    @Override
    public String export(ToolboxException exception) {
        StringBuilder sb = new StringBuilder();
        // Implementación personalizada...
        return sb.toString();
    }
}

// Uso
FileErrorHandler handler = new FileErrorHandler.Builder()
    .directory("./logs/errors")
    .exporter(new CustomMarkdownExporter())
    .build();
```

### Roadmap de Desarrollo Futuro

1. **MVP (3 meses):**
   - Implementación del sistema de excepciones jerárquico
   - Códigos de error estandarizados
   - Internacionalización básica
   - Exportación a archivos Markdown
   - Integración con el sistema de logging

2. **Versiones Futuras:**
   - Interfaz web para visualizar y analizar errores
   - Agrupación automática de errores similares
   - Sugerencias automáticas de soluciones
   - Alertas y notificaciones configurables
   - Mejor integración con frameworks populares
   - Soporte para métricas y análisis estadístico de errores

### Consideraciones Técnicas

- **Compatibilidad:** Java 8+
- **Rendimiento:** Optimizado para minimizar el impacto en tiempo de ejecución
- **Thread-safety:** Seguro para uso en entornos multihilo
- **Serialización:** Todas las excepciones son serializables
- **Tamaño:** Minimizar el tamaño de los archivos de error generados
- **Seguridad:** Evitar exponer información sensible en los registros de error
