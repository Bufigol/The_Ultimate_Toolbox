# Índice de Documentación - The_Ultimate_Toolbox

Este documento proporciona un índice completo de toda la documentación disponible para The_Ultimate_Toolbox, facilitando la navegación entre los diferentes componentes y módulos del proyecto.

## Documentación General

| Documento | Descripción |
|-----------|-------------|
| [README.md](README.md) | Introducción general al proyecto, características principales e instrucciones básicas de instalación y uso |
| [Documento de Requisitos del Producto (PRD)](docs/prd.md) | Definición completa del alcance, características y requisitos del proyecto |
| [Estructura de Carpetas](docs/folder-structure.md) | Descripción detallada de la estructura de carpetas y organización del código |
| [Bibliotecas de Terceros Recomendadas](docs/third-party-libraries.md) | Listado completo de bibliotecas de terceros recomendadas para usar con The_Ultimate_Toolbox |

## Documentación por Módulo

### 1. Módulo de Modelos

El módulo de modelos proporciona estructuras de datos predefinidas para objetos comúnmente utilizados en desarrollo Java.

| Documento | Descripción |
|-----------|-------------|
| [Documentación de Modelos](docs/models.md) | Guía completa de las clases de modelos disponibles, su uso y personalización |

**Clases Principales:**
- `JsonObject` - Representación de objetos JSON
- `User` - Modelo de usuario base
- `EmailMessage` - Modelo para mensajes de correo
- `HttpRequest`/`HttpResponse` - Modelos para comunicación HTTP

### 2. Módulo de Validación

El módulo de validación proporciona anotaciones y validadores para verificar datos de entrada.

| Documento | Descripción |
|-----------|-------------|
| [Documentación de Anotaciones](docs/annotations.md) | Referencia completa de las anotaciones de validación disponibles y su configuración |

**Anotaciones Principales:**
- `@ValidEmail` - Validación de direcciones de correo electrónico
- `@StrongPassword` - Validación de contraseñas seguras
- `@ValidId` - Validación de documentos de identidad por país
- `@CurrencyAmount` - Validación y conversión de cantidades monetarias

### 3. Módulo de Base de Datos

El módulo de base de datos proporciona herramientas para la conexión y operación con bases de datos SQL.

| Documento | Descripción |
|-----------|-------------|
| [Documentación de Base de Datos](docs/database.md) | Guía completa para configurar y utilizar la conexión a bases de datos |

**Características Principales:**
- Pool de conexiones configurable
- Operaciones CRUD simplificadas
- Mapeo objeto-relacional ligero
- Soporte para múltiples gestores de bases de datos
- Transacciones automáticas y manuales

### 4. Módulo de Gestión de Errores

El módulo de gestión de errores proporciona un sistema robusto para manejar, documentar y registrar errores.

| Documento | Descripción |
|-----------|-------------|
| [Documentación de Gestión de Errores](docs/error-handling.md) | Guía para implementar y configurar el sistema de gestión de errores |

**Características Principales:**
- Sistema de excepciones jerárquico
- Códigos de error estandarizados
- Internacionalización de mensajes de error
- Exportación de errores a archivos Markdown
- Contextualización de errores para depuración

### 5. Módulo de Logging

El módulo de logging proporciona un sistema para registrar eventos y actividades en aplicaciones.

| Documento | Descripción |
|-----------|-------------|
| [Documentación de Sistema de Logging](docs/logging.md) | Guía para configurar y utilizar el sistema de logging |

**Características Principales:**
- Múltiples niveles de logging (TRACE, DEBUG, INFO, WARN, ERROR, FATAL)
- Appenders configurables (consola, archivo, Markdown)
- Formateo de mensajes personalizable
- Integración con frameworks de logging existentes
- Contextualización de logs

### 6. Módulo de Archivos

El módulo de archivos proporciona utilidades para operaciones comunes con archivos.

| Documento | Descripción |
|-----------|-------------|
| [Documentación de Utilidades de Archivos](docs/file-utils.md) | Guía completa para operaciones de lectura, escritura y procesamiento de archivos |

**Características Principales:**
- Operaciones de lectura/escritura simplificadas
- Soporte para diferentes formatos (texto, binario, CSV, JSON, XML)
- Observación de cambios en archivos
- Operaciones por lotes
- Utilidades para rutas y directorios

### 7. Módulo de Matemáticas

El módulo de matemáticas proporciona utilidades para cálculos y conversiones.

| Documento | Descripción |
|-----------|-------------|
| [Documentación de Utilidades Matemáticas](docs/math-utils.md) | Referencia completa de las funciones matemáticas y conversiones disponibles |

**Características Principales:**
- Operaciones matemáticas seguras
- Funciones estadísticas
- Conversión de unidades (longitud, peso, volumen, temperatura, etc.)
- Conversión de monedas
- Cálculos geométricos y financieros

### 8. Módulo de Entrada por Teclado

El módulo de entrada por teclado proporciona herramientas para gestionar la entrada de datos por consola.

| Documento | Descripción |
|-----------|-------------|
| [Documentación de Utilidades de Entrada por Teclado](docs/input-utils.md) | Guía para implementar interfaces de consola interactivas |

**Características Principales:**
- Lectura de diferentes tipos de datos con validación
- Menús interactivos
- Formateo y colorización
- Entrada de datos complejos
- Entrada multilinea y especial

### 9. Módulo de APIs Públicas

El módulo de APIs proporciona herramientas para consumir servicios web REST.

| Documento | Descripción |
|-----------|-------------|
| [Documentación de Uso de APIs Públicas](docs/api-utils.md) | Guía completa para configurar y utilizar el cliente de API |

**Características Principales:**
- Cliente API genérico
- Construcción fluida de peticiones
- Autenticación flexible (Basic, Bearer, API Key, OAuth)
- Caché configurable
- Manejo robusto de errores
- Interceptores personalizables

## Ejemplos y Guías Prácticas

| Documento | Descripción |
|-----------|-------------|
| [Ejemplo CRUD Básico](docs/examples/simple-crud/README.md) | Aplicación CRUD completa usando varios módulos de The_Ultimate_Toolbox |
| [Demo de Validación](docs/examples/validation-demo/README.md) | Ejemplos prácticos de uso de anotaciones de validación |
| [Ejemplo de Logging](docs/examples/logging-example/README.md) | Configuración y uso avanzado del sistema de logging |
| [Demo de Cliente API](docs/examples/api-client-demo/README.md) | Ejemplos de integración con APIs públicas populares |

## Guías de Contribución

| Documento | Descripción |
|-----------|-------------|
| [Guía de Contribución](CONTRIBUTING.md) | Información sobre cómo contribuir al proyecto |
| [Código de Conducta](CODE_OF_CONDUCT.md) | Código de conducta para colaboradores |
| [Estándares de Codificación](docs/CODING_STANDARDS.md) | Guía de estilo y convenciones de código |
| [Proceso de Revisión](docs/REVIEW_PROCESS.md) | Información sobre el proceso de revisión de código |

## Referencia de la API

La documentación completa de la API Java está disponible en formato Javadoc en:

- [Javadoc Online](https://ultimatetoolbox.com/javadoc/)
- En el código fuente con comentarios exhaustivos

## Planificación y Roadmap

| Documento | Descripción |
|-----------|-------------|
| [Roadmap](docs/ROADMAP.md) | Planificación de futuras versiones y características |
| [Registro de Cambios](CHANGELOG.md) | Historial completo de cambios por versión |

## Soporte y Comunidad

| Recurso | Descripción |
|---------|-------------|
| [Preguntas Frecuentes](docs/FAQ.md) | Respuestas a preguntas frecuentes sobre el proyecto |
| [Solución de Problemas](docs/TROUBLESHOOTING.md) | Guía para diagnosticar y resolver problemas comunes |
| [Comunidad](https://github.com/yourusername/ultimate-toolbox/discussions) | Foro de discusión para la comunidad |
| [Canal de Slack](https://ultimatetoolbox.slack.com) | Canal de comunicación en tiempo real |

## Licencia y Aviso Legal

| Documento | Descripción |
|-----------|-------------|
| [Licencia](LICENSE) | Texto completo de la licencia Apache 2.0 |
| [Aviso de Terceros](NOTICE) | Avisos de atribución para componentes de terceros |

---

Este índice se actualizará regularmente a medida que se añadan nuevas características y documentación. Si no encuentras lo que buscas, no dudes en [abrir un issue](https://github.com/yourusername/ultimate-toolbox/issues) solicitando más información.
