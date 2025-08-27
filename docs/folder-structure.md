# Estructura de Carpetas de The_Ultimate_Toolbox

Este documento detalla la estructura de carpetas completa del proyecto The_Ultimate_Toolbox, proporcionando una visión general de la organización del código y la documentación.

## Estructura Principal

```
the-ultimate-toolbox/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── ultimatetoolbox/
│   │   │           ├── models/
│   │   │           ├── validation/
│   │   │           ├── database/
│   │   │           ├── error/
│   │   │           ├── logging/
│   │   │           ├── files/
│   │   │           ├── math/
│   │   │           ├── input/
│   │   │           ├── api/
│   │   │           └── util/
│   │   └── resources/
│   │       ├── i18n/
│   │       │   ├── errors/
│   │       │   ├── messages/
│   │       │   └── validation/
│   │       └── config/
│   │           ├── default-logging.properties
│   │           └── database-templates.xml
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── ultimatetoolbox/
│       │           ├── models/
│       │           ├── validation/
│       │           ├── database/
│       │           ├── error/
│       │           ├── logging/
│       │           ├── files/
│       │           ├── math/
│       │           ├── input/
│       │           ├── api/
│       │           └── util/
│       └── resources/
│           ├── test-data/
│           └── config/
│               └── test-config.properties
├── docs/
│   ├── prd.md
│   ├── models.md
│   ├── annotations.md
│   ├── database.md
│   ├── error-handling.md
│   ├── logging.md
│   ├── file-utils.md
│   ├── math-utils.md
│   ├── input-utils.md
│   ├── api-utils.md
│   ├── third-party-libraries.md
│   └── examples/
│       ├── simple-crud/
│       ├── validation-demo/
│       ├── logging-example/
│       └── api-client-demo/
├── scripts/
│   ├── build/
│   ├── ci/
│   └── release/
├── .github/
│   ├── workflows/
│   │   ├── build.yml
│   │   ├── test.yml
│   │   └── release.yml
│   └── ISSUE_TEMPLATE/
│       ├── bug_report.md
│       ├── feature_request.md
│       └── question.md
├── gradle/
├── .gitignore
├── build.gradle
├── settings.gradle
├── LICENSE
└── README.md
```

## Descripción de Carpetas Principales

### 1. `src/main/java/com/ultimatetoolbox/`

#### `models/`
Contiene todas las clases de modelos predefinidos organizadas por tipo:
```
models/
├── core/
│   ├── Model.java
│   └── ModelFactory.java
├── common/
│   ├── json/
│   ├── user/
│   ├── email/
│   └── http/
└── util/
    ├── ModelConverter.java
    └── ModelValidator.java
```

#### `validation/`
Contiene anotaciones de validación, validadores y utilidades relacionadas:
```
validation/
├── annotations/
│   ├── ValidEmail.java
│   ├── StrongPassword.java
│   ├── ValidId.java
│   └── CurrencyAmount.java
├── validators/
│   ├── EmailValidator.java
│   ├── PasswordValidator.java
│   ├── IdValidator.java
│   └── CurrencyValidator.java
├── constraints/
└── messages/
    ├── ValidationMessages.java
    └── resources/
```

#### `database/`
Contiene clases para la conexión y operación con bases de datos:
```
database/
├── core/
│   ├── Database.java
│   ├── DatabaseFactory.java
│   ├── DatabaseConfig.java
│   └── Transaction.java
├── orm/
│   ├── mapping/
│   ├── query/
│   └── criteria/
├── jdbc/
│   ├── template/
│   └── executor/
├── dialect/
│   ├── mysql/
│   ├── postgresql/
│   ├── sqlite/
│   └── h2/
└── util/
```

#### `error/`
Sistema de gestión de errores:
```
error/
├── core/
│   ├── ToolboxException.java
│   ├── ErrorCode.java
│   ├── ErrorCategory.java
│   └── ErrorSeverity.java
├── types/
│   ├── ValidationException.java
│   ├── DatabaseException.java
│   └── ...
├── handler/
├── i18n/
│   ├── ErrorMessages.java
│   └── resources/
└── util/
    ├── ErrorExporter.java
    └── ErrorContext.java
```

#### `logging/`
Sistema de logging:
```
logging/
├── core/
│   ├── Logger.java
│   ├── LogLevel.java
│   ├── LogEntry.java
│   └── LoggerFactory.java
├── appenders/
│   ├── ConsoleAppender.java
│   ├── FileAppender.java
│   └── MarkdownFileAppender.java
├── formatters/
├── adapters/
├── config/
└── util/
```

#### `files/`
Utilidades para manipulación de archivos:
```
files/
├── core/
│   ├── FileHandler.java
│   └── FileOperation.java
├── readers/
│   ├── TextFileReader.java
│   ├── BinaryFileReader.java
│   ├── CsvReader.java
│   └── JsonFileReader.java
├── writers/
│   ├── TextFileWriter.java
│   ├── BinaryFileWriter.java
│   ├── CsvWriter.java
│   └── JsonFileWriter.java
├── watchers/
├── batch/
└── util/
```

#### `math/`
Utilidades matemáticas:
```
math/
├── core/
│   ├── MathUtils.java
│   └── NumberUtils.java
├── statistics/
│   ├── StatUtils.java
│   └── Descriptive.java
├── conversion/
│   ├── UnitConverter.java
│   ├── LengthConverter.java
│   └── ...
├── currency/
│   ├── CurrencyConverter.java
│   └── ExchangeRate.java
├── geometry/
└── finance/
```

#### `input/`
Utilidades para entrada por teclado:
```
input/
├── core/
│   ├── InputHandler.java
│   └── ConsoleInput.java
├── readers/
│   ├── StringReader.java
│   ├── NumberReader.java
│   └── DateReader.java
├── validators/
├── formatters/
├── prompts/
│   ├── TextPrompt.java
│   └── MenuPrompt.java
└── util/
```

#### `api/`
Cliente para APIs públicas:
```
api/
├── core/
│   ├── ApiClient.java
│   ├── ApiRequest.java
│   └── ApiResponse.java
├── http/
│   ├── HttpMethod.java
│   └── HttpClient.java
├── auth/
│   ├── Authenticator.java
│   ├── BasicAuthenticator.java
│   └── BearerAuthenticator.java
├── serialization/
├── cache/
├── error/
├── interceptor/
└── util/
```

#### `util/`
Utilidades generales:
```
util/
├── string/
│   ├── StringUtils.java
│   └── StringFormatter.java
├── collection/
│   ├── CollectionUtils.java
│   └── MapUtils.java
├── reflection/
│   └── ReflectionUtils.java
├── concurrent/
│   └── ConcurrentUtils.java
└── io/
    └── IOUtils.java
```

### 2. `src/main/resources/`

Contiene recursos como archivos de propiedades, configuraciones y archivos de internacionalización.

### 3. `src/test/`

Tests unitarios y de integración organizados en la misma estructura que el código principal.

### 4. `docs/`

Documentación completa del proyecto, incluyendo guías de usuario, documentación de API y ejemplos.

### 5. `scripts/`

Scripts para construcción, integración continua y lanzamiento de versiones.

### 6. `.github/`

Configuraciones para GitHub, incluyendo plantillas para issues y flujos de trabajo para GitHub Actions.

## Estructura de Paquetes y Convenios de Nombrado

### Convenios de Nombrado

- **Clases:** Usan PascalCase (ej: `JsonObject`, `EmailValidator`)
- **Interfaces:** Usan PascalCase (ej: `ApiClient`, `ErrorHandler`)
- **Métodos:** Usan camelCase (ej: `readString()`, `convertToJson()`)
- **Constantes:** Usan UPPER_SNAKE_CASE (ej: `MAX_SIZE`, `DEFAULT_TIMEOUT`)
- **Paquetes:** Usan minúsculas (ej: `com.ultimatetoolbox.models`)

### Estándares de Codificación

- Se utilizan las convenciones estándar de Java para la documentación (Javadoc)
- Cada clase incluye una descripción detallada de su propósito y comportamiento
- Cada método público incluye documentación completa con parámetros, retorno y excepciones
- Se utilizan pruebas unitarias para todas las clases públicas

## Estructura de Módulos

El proyecto está diseñado con un enfoque modular que permitirá en el futuro separarlo en múltiples artefactos si es necesario:

```
ultimate-toolbox-core/
ultimate-toolbox-models/
ultimate-toolbox-validation/
ultimate-toolbox-database/
ultimate-toolbox-logging/
ultimate-toolbox-files/
ultimate-toolbox-math/
ultimate-toolbox-input/
ultimate-toolbox-api/
```

Esta estructura facilitará a los usuarios incluir sólo los módulos que necesitan, reduciendo las dependencias y el tamaño final de sus aplicaciones.
