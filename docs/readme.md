## Contribuir

¡Contribuciones son bienvenidas! Si deseas contribuir a The_Ultimate_Toolbox, por favor:

1. Haz un fork del repositorio
2. Crea una rama para tu característica (`git checkout -b feature/amazing-feature`)
3. Realiza tus cambios y haz commit (`git commit -m 'Add some amazing feature'`)
4. Sube los cambios a tu fork (`git push origin feature/amazing-feature`)
5. Abre un Pull Request

## Versionado

Usamos [SemVer](http://semver.org/) para el versionado. Para las versiones disponibles, consulta los [tags en este repositorio](https://github.com/yourusername/ultimate-toolbox/tags).

## Licencia

Este proyecto está licenciado bajo la Licencia Apache 2.0 - ver el archivo [LICENSE](LICENSE) para más detalles.

## Agradecimientos

- A todos los colaboradores que participan en este proyecto
- A la comunidad Java por sus invaluables aportes
- A todas las bibliotecas de código abierto que hacen posible este proyecto

## Contacto

- Sitio web: [https://ultimatetoolbox.com](https://ultimatetoolbox.com)
- Repositorio: [https://github.com/yourusername/ultimate-toolbox](https://github.com/yourusername/ultimate-toolbox)
- Issues: [https://github.com/yourusername/ultimate-toolbox/issues](https://github.com/yourusername/ultimate-toolbox/issues)

## Roadmap

El desarrollo futuro se centrará en:

1. Expandir la colección de modelos predefinidos
2. Añadir más anotaciones de validación
3. Mejorar el rendimiento de las operaciones de base de datos
4. Añadir soporte para más formatos de archivo
5. Integrar con más servicios externos y APIs
6. Desarrollar herramientas de análisis y visualización de datos
7. Ampliar la documentación y ejemplos# The_Ultimate_Toolbox

Una biblioteca Java completa que proporciona herramientas esenciales para el desarrollo rápido y eficiente de aplicaciones Java.

## Descripción

The_Ultimate_Toolbox es una dependencia Java diseñada para simplificar el desarrollo de aplicaciones Java proporcionando un conjunto integral de utilidades, modelos predefinidos, validaciones, gestión de errores y mucho más. La biblioteca está diseñada para ser altamente extensible, permitiendo un crecimiento orgánico basado en las necesidades de la comunidad.

## Características Principales

- **Modelos Predefinidos:** Estructuras de datos comunes como JSON, email, usuario, etc.
- **Anotaciones de Validación:** Validación de email, documentos de identidad, contraseñas, monedas, etc.
- **Conexión a Bases de Datos:** Solución agnóstica para cualquier gestor SQL con enfoque híbrido ORM/JDBC.
- **Gestión de Errores:** Sistema de excepciones personalizable con códigos de error e internacionalización.
- **Sistema de Logging:** Framework robusto con almacenamiento en archivos Markdown.
- **Utilidades de Archivos:** Operaciones simplificadas de lectura/escritura para diversos formatos.
- **Utilidades Matemáticas:** Conversiones de unidades, monedas y funciones estadísticas.
- **Entrada por Teclado:** Herramientas para lectura, validación y formateo de datos de consola.
- **APIs Públicas:** Cliente REST genérico con soporte para autenticación, caché y manejo de errores.

## Instalación

### Maven

```xml
<dependency>
    <groupId>com.ultimatetoolbox</groupId>
    <artifactId>ultimate-toolbox-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.ultimatetoolbox:ultimate-toolbox-core:1.0.0'
```

## Uso Básico

### Modelos Predefinidos

```java
// Crear un objeto JSON
JsonObject user = new JsonObject();
user.addProperty("name", "Juan");
user.addProperty("age", 30);
user.addProperty("active", true);

JsonArray hobbies = new JsonArray();
hobbies.add("programming");
hobbies.add("reading");
user.add("hobbies", hobbies);

String json = user.toString(); // Convierte a String JSON
```

### Validación con Anotaciones

```java
public class Usuario {
    @ValidEmail(message = "El correo electrónico no es válido")
    private String email;
    
    @StrongPassword(minLength = 8, requireUppercase = true)
    private String password;
    
    @ValidId(country = "ES", message = "El DNI español no es válido")
    private String documentoId;
    
    // Getters y setters
}

// Validar un objeto
ValidationResult result = Validator.validate(usuario);
if (!result.isValid()) {
    List<ValidationError> errors = result.getErrors();
    // Manejar errores
}
```

### Conexión a Base de Datos

```java
// Configuración
DatabaseConfig config = new DatabaseConfig.Builder()
    .url("jdbc:mysql://localhost:3306/mydb")
    .username("user")
    .password("pass")
    .build();

Database db = DatabaseFactory.createDatabase(config);

// Consulta
List<Usuario> usuarios = db.find(Usuario.class)
    .where("activo = ?", true)
    .orderBy("nombre")
    .list();
```

### Gestión de Errores

```java
try {
    // Código que puede lanzar excepción
} catch (ValidationException e) {
    // Error de validación específico
    System.err.println("Código: " + e.getErrorCode().getCode());
    System.err.println("Mensaje: " + e.getMessage());
} catch (ToolboxException e) {
    // Cualquier error de la biblioteca
    ErrorHandler.handle(e); // Registra y exporta el error
}
```

### Logging

```java
Logger logger = LoggerFactory.getLogger("MiClase");

logger.info("Iniciando proceso");
try {
    // Operación importante
    logger.debug("Detalles de la operación: {}", detalles);
    
    if (condiciónProblemática) {
        logger.warn("Se detectó una condición problemática");
    }
    
    // Completado exitoso
    logger.info("Proceso completado");
} catch (Exception e) {
    logger.error("Error en el proceso", e);
}
```

### Utilidades de Archivos

```java
// Lectura de archivo
String content = FileHandler.read("config.txt");

// Escritura de archivo
FileHandler.write("output.txt", "Contenido del archivo");

// Lectura de CSV a objetos
List<Usuario> usuarios = CsvReader.read("usuarios.csv", Usuario.class);

// Escritura de objetos a CSV
CsvWriter.write("usuarios.csv", usuarios);
```

### Utilidades Matemáticas

```java
// Conversión de unidades
double meters = LengthConverter.convert(5, LengthUnit.FEET, LengthUnit.METERS);

// Conversión de monedas
double euros = CurrencyConverter.convert(100, "USD", "EUR", 0.85);

// Estadísticas
double[] data = {1.0, 2.0, 5.0, 7.0, 9.0};
double mean = StatUtils.mean(data);
double stdDev = StatUtils.standardDeviation(data);
```

### Entrada por Teclado

```java
// Lectura simple
String name = ConsoleInput.readString("Introduce tu nombre: ");

// Con validación
String email = ConsoleInput.readString("Introduce tu email: ", 
    input -> input.contains("@"), 
    "Email no válido. Debe contener @");

// Menú de opciones
String[] options = {"Ver datos", "Añadir registro", "Eliminar registro", "Salir"};
int option = ConsoleInput.readMenu("Seleccione una opción:", options);
```

### Cliente API REST

```java
// Configuración
ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com/v1")
    .authenticator(new BearerAuthenticator("my-token"))
    .build();

// GET con parámetros
ApiResponse<User> response = client.get("/users/{id}", 
    Map.of("id", "123"),
    User.class);
User user = response.getBody();

// POST con cuerpo
User newUser = new User("john", "john@example.com");
ApiResponse<User> response = client.post("/users", newUser, User.class);
```

## Documentación

La documentación completa está disponible en los siguientes enlaces:

- [Documentación de Modelos](docs/models.md)
- [Documentación de Anotaciones](docs/annotations.md)
- [Documentación de Base de Datos](docs/database.md)
- [Documentación de Errores](docs/error-handling.md)
- [Documentación de Logging](docs/logging.md)
- [Documentación de Archivos](docs/file-utils.md)
- [Documentación de Matemáticas](docs/math-utils.md)
- [Documentación de Entrada por Teclado](docs/input-utils.md)
- [Documentación de APIs](docs/api-utils.md)
- [Bibliotecas de Terceros Recomendadas](docs/third-party-libraries.md)

## Estructura del Proyecto

```
com.ultimatetoolbox/
├── models/              # Modelos predefinidos
├── validation/          # Anotaciones de validación y validadores
├── database/            # Herramientas de conexión a bases de datos
├── error/               # Sistema de gestión de errores
├── logging/             # Sistema de logging
├── files/               # Utilidades de archivos
├── math/                # Utilidades matemáticas
├── input/               # Utilidades de entrada por teclado
├── api/                 # Cliente para APIs públicas
└── util/                # Utilidades generales