# Documentación de Uso de APIs Públicas

## Herramientas para APIs Públicas en The_Ultimate_Toolbox

### Introducción

El módulo de APIs públicas de The_Ultimate_Toolbox proporciona un conjunto de herramientas para simplificar la integración y consumo de APIs REST en aplicaciones Java. Este módulo está diseñado para abstraer la complejidad de las comunicaciones HTTP, manejo de autenticación, serialización/deserialización, y tratamiento de errores al consumir servicios web.

### Arquitectura

La arquitectura del módulo de APIs públicas está estructurada de la siguiente manera:

```
com.ultimatetoolbox.api/
├── core/                # Núcleo del cliente de API
│   ├── ApiClient.java            # Cliente genérico de API
│   ├── ApiRequest.java           # Representación de solicitud
│   ├── ApiResponse.java          # Representación de respuesta
│   ├── ResponseHandler.java      # Manejador de respuestas
├── http/                # Implementaciones HTTP
│   ├── HttpMethod.java           # Enumeración de métodos HTTP
│   ├── HttpClient.java           # Cliente HTTP abstracto
│   ├── JavaHttpClient.java       # Implementación con HttpClient de Java
│   ├── ApacheHttpClient.java     # Implementación con Apache HttpClient
│   ├── OkHttpClient.java         # Implementación con OkHttp
├── auth/                # Autenticación
│   ├── Authenticator.java        # Interfaz para autenticación
│   ├── BasicAuthenticator.java   # Autenticación básica
│   ├── BearerAuthenticator.java  # Autenticación con token Bearer
│   ├── ApiKeyAuthenticator.java  # Autenticación con API Key
│   ├── OAuth2Authenticator.java  # Autenticación OAuth 2.0
├── serialization/       # Serialización/Deserialización
│   ├── ContentHandler.java       # Manejador de contenido
│   ├── JsonHandler.java          # Manejador para JSON
│   ├── XmlHandler.java           # Manejador para XML
│   ├── FormHandler.java          # Manejador para formularios
├── cache/               # Caché de respuestas
│   ├── ApiCache.java             # Interfaz para caché
│   ├── MemoryCache.java          # Implementación en memoria
│   ├── FileCache.java            # Implementación en archivos
│   ├── CachePolicy.java          # Políticas de caché
├── error/               # Manejo de errores
│   ├── ApiException.java         # Excepción base
│   ├── HttpStatusException.java  # Excepción por código HTTP
│   ├── NetworkException.java     # Excepción de red
│   ├── SerializationException.java # Excepción de serialización
│   ├── AuthenticationException.java # Excepción de autenticación
├── interceptor/         # Interceptores de peticiones/respuestas
│   ├── RequestInterceptor.java   # Interceptor de solicitudes
│   ├── ResponseInterceptor.java  # Interceptor de respuestas
│   ├── LoggingInterceptor.java   # Interceptor para registro
└── util/                # Utilidades
    ├── UrlBuilder.java           # Constructor de URLs
    ├── QueryStringBuilder.java   # Constructor de cadenas de consulta
    ├── HeaderUtils.java          # Utilidades para cabeceras
    ├── RetryUtils.java           # Utilidades para reintentos
```

### Características Principales (MVP)

#### 1. Cliente API Genérico

El sistema proporciona un cliente API genérico que puede ser utilizado para consumir cualquier API REST.

**Configuración básica:**
```java
// Cliente simple
ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com/v1")
    .build();

// Cliente con opciones
ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com/v1")
    .authenticator(new BearerAuthenticator("my-token"))
    .contentHandler(new JsonHandler())
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .build();
```

**Realizar peticiones:**
```java
// GET simple
ApiResponse<String> response = client.get("/users/123");
String userData = response.getBody();

// GET con parámetros y tipo específico
ApiResponse<User> response = client.get("/users/{id}", 
    Map.of("id", "123"),
    User.class);
User user = response.getBody();

// POST con cuerpo
User newUser = new User("john", "john@example.com");
ApiResponse<User> response = client.post("/users", newUser, User.class);
User createdUser = response.getBody();

// PUT con cuerpo
user.setEmail("newemail@example.com");
ApiResponse<User> response = client.put("/users/{id}", 
    Map.of("id", user.getId()), 
    user, 
    User.class);

// DELETE
ApiResponse<Void> response = client.delete("/users/{id}", 
    Map.of("id", "123"));
```

#### 2. Construcción Fluida de Peticiones

El sistema permite construir peticiones de forma fluida para mayor flexibilidad.

```java
// Petición GET con parámetros de consulta
ApiResponse<List<User>> response = client.request()
    .method(HttpMethod.GET)
    .path("/users")
    .queryParam("active", true)
    .queryParam("role", "admin")
    .queryParam("limit", 10)
    .execute(new TypeReference<List<User>>() {});

// Petición POST con cabeceras personalizadas
ApiResponse<Order> response = client.request()
    .method(HttpMethod.POST)
    .path("/orders")
    .header("X-Custom-Header", "value")
    .body(order)
    .execute(Order.class);

// Petición con varias opciones
ApiResponse<byte[]> response = client.request()
    .method(HttpMethod.GET)
    .path("/files/{fileId}")
    .pathParam("fileId", "abc123")
    .header("Accept", "application/octet-stream")
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(120, TimeUnit.SECONDS)
    .execute(byte[].class);
```

#### 3. Autenticación Flexible

El sistema soporta múltiples métodos de autenticación comunes.

**Autenticación básica:**
```java
Authenticator auth = new BasicAuthenticator("username", "password");
ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com")
    .authenticator(auth)
    .build();
```

**Autenticación con token Bearer:**
```java
Authenticator auth = new BearerAuthenticator("eyJhbGciOiJIUzI1NiIsInR5cCI6...");
ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com")
    .authenticator(auth)
    .build();
```

**Autenticación con API Key:**
```java
// En cabecera
Authenticator auth = new ApiKeyAuthenticator("X-API-Key", "abcd1234", ApiKeyLocation.HEADER);

// En parámetro de consulta
Authenticator auth = new ApiKeyAuthenticator("api_key", "abcd1234", ApiKeyLocation.QUERY);

ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com")
    .authenticator(auth)
    .build();
```

**OAuth 2.0:**
```java
OAuth2Config config = new OAuth2Config.Builder()
    .tokenUrl("https://auth.example.com/oauth/token")
    .clientId("client-id")
    .clientSecret("client-secret")
    .build();

Authenticator auth = new OAuth2Authenticator(config);

ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com")
    .authenticator(auth)
    .build();
```

#### 4. Manejo de Respuestas

El sistema facilita el acceso a los datos de respuesta y metadatos.

```java
// Acceso a datos de respuesta
ApiResponse<User> response = client.get("/users/123", User.class);

// Cuerpo de respuesta
User user = response.getBody();

// Código de estado
int statusCode = response.getStatusCode();

// Cabeceras de respuesta
Map<String, List<String>> headers = response.getHeaders();
String contentType = response.getHeader("Content-Type");

// Verificar resultado exitoso
if (response.isSuccessful()) {
    // Operación exitosa
} else {
    // Manejar error
}
```

**Conversión de tipos automática:**
```java
// Lista de objetos
ApiResponse<List<User>> response = client.get("/users", 
    new TypeReference<List<User>>() {});
List<User> users = response.getBody();

// Mapas anidados
ApiResponse<Map<String, Object>> response = client.get("/data",
    new TypeReference<Map<String, Object>>() {});
Map<String, Object> data = response.getBody();
```

#### 5. Manejo de Errores

El sistema proporciona un manejo de errores robusto y personalizable.

**Manejo de excepciones:**
```java
try {
    ApiResponse<User> response = client.get("/users/123", User.class);
    User user = response.getBody();
} catch (HttpStatusException e) {
    // Error con código HTTP específico
    int statusCode = e.getStatusCode();
    String errorBody = e.getErrorBody();
    System.err.println("Error HTTP " + statusCode + ": " + errorBody);
} catch (NetworkException e) {
    // Error de red (sin conexión, timeout, etc.)
    System.err.println("Error de red: " + e.getMessage());
} catch (ApiException e) {
    // Cualquier otro error de API
    System.err.println("Error de API: " + e.getMessage());
}
```

**Manejo simplificado:**
```java
ApiResponse<User> response = client.request()
    .method(HttpMethod.GET)
    .path("/users/{id}")
    .pathParam("id", "123")
    .onStatus(404, r -> new UserNotFoundException("Usuario no encontrado: 123"))
    .onStatus(403, r -> new AccessDeniedException("Acceso denegado"))
    .execute(User.class);
```

**Analizador de errores personalizado:**
```java
ErrorResponseHandler errorHandler = response -> {
    if (response.getStatusCode() == 400) {
        // Parsear respuesta de error JSON
        ErrorResponse error = response.parseError(ErrorResponse.class);
        return new ValidationException(error.getMessage(), error.getErrors());
    }
    // Comportamiento por defecto para otros errores
    return null;
};

ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com")
    .errorResponseHandler(errorHandler)
    .build();
```

#### 6. Caché de Respuestas

El sistema proporciona caché configurable para optimizar las solicitudes.

**Configuración de caché:**
```java
// Caché en memoria con expiración
ApiCache cache = new MemoryCache.Builder()
    .maxSize(100)
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build();

// Caché en archivos
ApiCache cache = new FileCache.Builder()
    .directory("./cache")
    .maxSize(50 * 1024 * 1024) // 50 MB
    .build();

// Configurar cliente con caché
ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com")
    .cache(cache)
    .build();
```

**Políticas de caché:**
```java
// Usar caché solo para GETs
ApiResponse<List<Product>> response = client.request()
    .method(HttpMethod.GET)
    .path("/products")
    .cachePolicy(CachePolicy.ENABLED_FOR_READ)
    .execute(new TypeReference<List<Product>>() {});

// Forzar refresco ignorando caché
ApiResponse<User> response = client.request()
    .method(HttpMethod.GET)
    .path("/users/me")
    .cachePolicy(CachePolicy.FORCE_NETWORK)
    .execute(User.class);

// Usar caché incluso sin conexión
ApiResponse<Settings> response = client.request()
    .method(HttpMethod.GET)
    .path("/settings")
    .cachePolicy(CachePolicy.CACHE_FIRST)
    .execute(Settings.class);
```

#### 7. Interceptores

El sistema permite insertar interceptores para modificar las peticiones o respuestas.

**Interceptor de logging:**
```java
RequestInterceptor logInterceptor = request -> {
    System.out.println("Ejecutando " + request.getMethod() + " " + request.getUrl());
    return request;
};

ResponseInterceptor logResponseInterceptor = (request, response) -> {
    System.out.println("Recibido " + response.getStatusCode() + " de " + request.getUrl());
    return response;
};

ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com")
    .addRequestInterceptor(logInterceptor)
    .addResponseInterceptor(logResponseInterceptor)
    .build();
```

**Interceptor para añadir cabeceras:**
```java
RequestInterceptor headerInterceptor = request -> {
    return request.newBuilder()
        .header("X-App-Version", "1.0.0")
        .header("X-Device-ID", getDeviceId())
        .build();
};

ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com")
    .addRequestInterceptor(headerInterceptor)
    .build();
```

**Interceptor para métricas:**
```java
ResponseInterceptor metricsInterceptor = (request, response) -> {
    long startTime = (long) request.getTag("startTime");
    long endTime = System.currentTimeMillis();
    MetricsCollector.recordApiCall(
        request.getUrl(),
        response.getStatusCode(),
        endTime - startTime
    );
    return response;
};

RequestInterceptor timeStartInterceptor = request -> {
    return request.newBuilder()
        .tag("startTime", System.currentTimeMillis())
        .build();
};

ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com")
    .addRequestInterceptor(timeStartInterceptor)
    .addResponseInterceptor(metricsInterceptor)
    .build();
```

### Serialización/Deserialización Flexible

El sistema permite trabajar con diferentes formatos de datos.

**Configuración de manejadores de contenido:**
```java
// JSON por defecto
ContentHandler jsonHandler = new JsonHandler();

// XML como alternativa
ContentHandler xmlHandler = new XmlHandler();

// Form URL encoded
ContentHandler formHandler = new FormHandler();

ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com")
    .contentHandler(jsonHandler)
    .alternativeContentHandler("application/xml", xmlHandler)
    .alternativeContentHandler("application/x-www-form-urlencoded", formHandler)
    .build();
```

**Uso de diferentes formatos:**
```java
// JSON por defecto
ApiResponse<User> response = client.get("/users/123", User.class);

// Solicitar XML específicamente
ApiResponse<User> response = client.request()
    .method(HttpMethod.GET)
    .path("/users/123")
    .accept("application/xml")
    .execute(User.class);

// Enviar formulario
Map<String, String> formData = new HashMap<>();
formData.put("username", "john");
formData.put("password", "secret");

ApiResponse<AuthResponse> response = client.request()
    .method(HttpMethod.POST)
    .path("/login")
    .contentType("application/x-www-form-urlencoded")
    .body(formData)
    .execute(AuthResponse.class);
```

### Descarga de Archivos

El sistema facilita la descarga de archivos.

```java
// Descargar a array de bytes
ApiResponse<byte[]> response = client.request()
    .method(HttpMethod.GET)
    .path("/files/{id}")
    .pathParam("id", "123")
    .execute(byte[].class);

byte[] fileData = response.getBody();

// Descargar a archivo
File destination = new File("downloaded-file.pdf");
client.request()
    .method(HttpMethod.GET)
    .path("/files/{id}")
    .pathParam("id", "123")
    .downloadTo(destination);

// Descargar con progreso
client.request()
    .method(HttpMethod.GET)
    .path("/files/{id}")
    .pathParam("id", "123")
    .downloadTo(destination, (bytesRead, contentLength, done) -> {
        int progress = (int) (bytesRead * 100 / contentLength);
        System.out.println("Progreso: " + progress + "%");
    });
```

### Carga de Archivos

El sistema simplifica la carga de archivos.

```java
// Cargar un archivo
File file = new File("document.pdf");
ApiResponse<FileUploadResponse> response = client.request()
    .method(HttpMethod.POST)
    .path("/upload")
    .file("file", file, "application/pdf")
    .execute(FileUploadResponse.class);

// Cargar múltiples archivos
ApiResponse<FileUploadResponse> response = client.request()
    .method(HttpMethod.POST)
    .path("/upload")
    .file("file1", new File("document1.pdf"), "application/pdf")
    .file("file2", new File("document2.pdf"), "application/pdf")
    .field("description", "Documentos importantes")
    .execute(FileUploadResponse.class);

// Cargar con progreso
ApiResponse<FileUploadResponse> response = client.request()
    .method(HttpMethod.POST)
    .path("/upload")
    .file("file", file, "application/pdf")
    .uploadProgressListener((bytesWritten, contentLength, done) -> {
        int progress = (int) (bytesWritten * 100 / contentLength);
        System.out.println("Progreso de carga: " + progress + "%");
    })
    .execute(FileUploadResponse.class);
```

### Ejemplos de Uso Completos

**Ejemplo 1: Cliente para una API de clima**
```java
public class WeatherService {
    private final ApiClient client;
    
    public WeatherService(String apiKey) {
        this.client = new ApiClient.Builder()
            .baseUrl("https://api.weather.example.com/v1")
            .authenticator(new ApiKeyAuthenticator("api_key", apiKey, ApiKeyLocation.QUERY))
            .connectTimeout(10, TimeUnit.SECONDS)
            .cache(new MemoryCache.Builder()
                .maxSize(100)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build())
            .build();
    }
    
    public WeatherData getCurrentWeather(String city) throws ApiException {
        ApiResponse<WeatherData> response = client.request()
            .method(HttpMethod.GET)
            .path("/current")
            .queryParam("city", city)
            .execute(WeatherData.class);
            
        return response.getBody();
    }
    
    public List<ForecastData> getForecast(String city, int days) throws ApiException {
        ApiResponse<ForecastResponse> response = client.request()
            .method(HttpMethod.GET)
            .path("/forecast")
            .queryParam("city", city)
            .queryParam("days", days)
            .execute(ForecastResponse.class);
            
        return response.getBody().getForecast();
    }
    
    public WeatherMap getWeatherMap(double lat, double lon, int zoom) throws ApiException {
        File mapFile = new File("weather_map.png");
        
        client.request()
            .method(HttpMethod.GET)
            .path("/map")
            .queryParam("lat", lat)
            .queryParam("lon", lon)
            .queryParam("zoom", zoom)
            .queryParam("type", "temperature")
            .downloadTo(mapFile);
            
        return new WeatherMap(mapFile);
    }
}
```

**Ejemplo 2: Cliente para una API de gestión de usuarios**
```java
public class UserApiClient {
    private final ApiClient client;
    
    public UserApiClient(String baseUrl, String token) {
        this.client = new ApiClient.Builder()
            .baseUrl(baseUrl)
            .authenticator(new BearerAuthenticator(token))
            .addRequestInterceptor(request -> {
                return request.newBuilder()
                    .header("X-App-Version", "1.2.0")
                    .build();
            })
            .errorResponseHandler(response -> {
                if (response.getStatusCode() == 401) {
                    return new AuthenticationException("Token expirado o inválido");
                }
                return null;
            })
            .build();
    }
    
    public User getUserById(String id) throws ApiException {
        return client.get("/users/{id}", Map.of("id", id), User.class).getBody();
    }
    
    public List<User> searchUsers(String query, String role, int limit) throws ApiException {
        return client.request()
            .method(HttpMethod.GET)
            .path("/users")
            .queryParam("q", query)
            .queryParam("role", role)
            .queryParam("limit", limit)
            .execute(new TypeReference<List<User>>() {})
            .getBody();
    }
    
    public User createUser(User user) throws ApiException {
        return client.post("/users", user, User.class).getBody();
    }
    
    public User updateUser(String id, User user) throws ApiException {
        return client.put("/users/{id}", Map.of("id", id), user, User.class).getBody();
    }
    
    public void deleteUser(String id) throws ApiException {
        client.delete("/users/{id}", Map.of("id", id));
    }
    
    public User uploadProfilePhoto(String userId, File photo) throws ApiException {
        return client.request()
            .method(HttpMethod.POST)
            .path("/users/{id}/photo")
            .pathParam("id", userId)
            .file("photo", photo, "image/jpeg")
            .execute(User.class)
            .getBody();
    }
}
```

### Roadmap de Desarrollo Futuro

1. **MVP (3 meses):**
   - Implementación del cliente API genérico
   - Soporte para operaciones HTTP básicas
   - Autenticación básica, Bearer y API Key
   - Serialización/deserialización JSON
   - Caché básica en memoria
   - Manejo básico de errores

2. **Versiones Futuras:**
   - Soporte para GraphQL
   - Mejoras en rendimiento y concurrencia
   - Más implementaciones de clientes HTTP
   - Soporte para WebSockets
   - OAuth 2.0 completo con renovación de tokens
   - Generación automática de clientes a partir de especificaciones OpenAPI/Swagger
   - Herramientas de pruebas y simulación de APIs

### Consideraciones Técnicas

- **Compatibilidad:** Java 8+
- **Dependencias:** Mínimas, con implementaciones pluggables
- **Thread-safety:** Seguro para uso en entornos multihilo
- **Rendimiento:** Optimizado para operaciones frecuentes
- **Seguridad:** Manejo seguro de tokens y credenciales
- **Extensibilidad:** Arquitectura pluggable para adaptarse a necesidades específicas")
    .authenticator(auth)
    .build();
```

**Autenticación con API Key:**
```java
// En cabecera
Authenticator auth = new ApiKeyAuthenticator("X-API-Key", "abcd1234", ApiKeyLocation.HEADER);

// En parámetro de consulta
Authenticator auth = new ApiKeyAuthenticator("api_key", "abcd1234", ApiKeyLocation.QUERY);

ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com")
    .authenticator(auth)
    .build();
```

**OAuth 2.0:**
```java
OAuth2Config config = new OAuth2Config.Builder()
    .tokenUrl("https://auth.example.com/oauth/token")
    .clientId("client-id")
    .clientSecret("client-secret")
    .build();

Authenticator auth = new OAuth2Authenticator(config);

ApiClient client = new ApiClient.Builder()
    .baseUrl("https://api.example.com")
    .authenticator(auth)
    .build();
```

#### 4. Manejo de Respuestas

El sistema facilita el acceso a los datos de respuesta y metadatos.

```java
// Acceso a datos de respuesta
ApiResponse<User> response = client.get("/users/123", User.class);

// Cuerpo de respuesta
User user = response.getBody();

// Código de estado
int statusCode = response.getStatusCode();

// Cabeceras de respuesta
Map<String, List<String>> headers = response.getHeaders();
String contentType = response.getHeader("Content-Type");

// Verificar resultado exitoso
if (response.isSuccessful()) {
    // Operación exitosa
} else {
    // Manejar error
}
```

**Conversión de tipos automática:**
```java
// Lista de objetos
ApiResponse<List<User>> response = client.get("/users", 
    new TypeReference<List<User>>() {});
List<User> users = response.getBody();

// Mapas anidados
ApiResponse<Map<String, Object>> response = client.get("/data",
    new TypeReference<Map<String, Object>>() {});
Map<String, Object> data = response.getBody();
```

#### 5. Manejo de Errores

El sistema proporciona un manejo de