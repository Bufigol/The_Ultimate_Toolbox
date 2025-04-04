# Documentación de Modelos

## Estructura de Modelos en The_Ultimate_Toolbox

### Introducción

El módulo de modelos de The_Ultimate_Toolbox proporciona estructuras de datos predefinidas para objetos comúnmente utilizados en el desarrollo Java. Estos modelos están diseñados para ser extensibles, serializables y fáciles de usar, eliminando la necesidad de que los desarrolladores creen estas estructuras desde cero.

### Arquitectura

Los modelos siguen un enfoque basado en interfaces con implementaciones concretas, permitiendo personalización y extensibilidad. La estructura de paquetes será:

```tree
com.ultimatetoolbox.models/
├── core/             # Interfaces y clases base
├── common/           # Implementaciones estándar
│   ├── json/         # Modelos relacionados con JSON
│   ├── user/         # Modelos relacionados con usuarios
│   ├── email/        # Modelos relacionados con emails
│   ├── http/         # Modelos para comunicación HTTP
│   └── ... (otros)
└── util/             # Utilidades de conversión y manipulación
```

### Modelos Principales (MVP)

#### 1. Modelos JSON (`com.ultimatetoolbox.models.common.json`)

- **JsonObject:** Representación de un objeto JSON con métodos para acceder y modificar propiedades.
- **JsonArray:** Representación de un array JSON con métodos para manipular elementos.
- **JsonElement:** Interfaz base para cualquier elemento JSON.
- **JsonParser:** Utilidad para análisis y generación de JSON.

**Ejemplo de uso:**

```java
JsonObject user = new JsonObject();
user.addProperty("name", "Juan");
user.addProperty("age", 30);
user.addProperty("active", true);

JsonArray hobbies = new JsonArray();
hobbies.add("programming");
hobbies.add("reading");
user.add("hobbies", hobbies);

String json = user.toString(); // Convierte a String JSON
JsonObject parsed = JsonParser.parse(json); // Analiza desde String
```

#### 2. Modelos de Usuario (`com.ultimatetoolbox.models.common.user`)

- **User:** Clase base con propiedades comunes de usuario (id, nombre, email, contraseña hash, etc.).
- **UserRole:** Enumeración de roles comunes.
- **UserCredentials:** Modelo para credenciales de autenticación.
- **UserProfile:** Perfil extendido con información adicional.

**Ejemplo de uso:**

```java
User user = new User.Builder()
        .withId(UUID.randomUUID())
        .withUsername("juan123")
        .withEmail("juan@example.com")
        .withPasswordHash(passwordService.hash("securePass123"))
        .withRoles(Arrays.asList(UserRole.USER))
        .build();
```

#### 3. Modelos de Email (`com.ultimatetoolbox.models.common.email`)

- **EmailAddress:** Clase que representa una dirección de correo válida.
- **EmailMessage:** Mensaje de correo con remitente, destinatario(s), asunto, cuerpo, etc.
- **EmailAttachment:** Adjuntos para mensajes de correo.
- **EmailTemplate:** Plantillas para mensajes de correo.

**Ejemplo de uso:**

```java
EmailMessage message = new EmailMessage.Builder()
        .from(new EmailAddress("sender@example.com"))
        .to(new EmailAddress("recipient@example.com"))
        .subject("Prueba de Email")
        .body("Este es el cuerpo del mensaje")
        .addAttachment(new EmailAttachment("informe.pdf", byteData))
        .build();
```

#### 4. Modelos HTTP (`com.ultimatetoolbox.models.common.http`)

- **HttpRequest:** Solicitud HTTP con métodos, encabezados y cuerpo.
- **HttpResponse:** Respuesta HTTP con código de estado, encabezados y cuerpo.
- **HttpHeader:** Representación de encabezados HTTP.
- **HttpParameter:** Parámetros para solicitudes.

**Ejemplo de uso:**

```java
HttpRequest request = new HttpRequest.Builder()
        .url("https://api.example.com/users")
        .method(HttpMethod.POST)
        .addHeader("Content-Type", "application/json")
        .body("{\"name\":\"Juan\",\"email\":\"juan@example.com\"}")
        .build();
```

#### 5. Modelos de Moneda (`com.ultimatetoolbox.models.common.currency`)

- **Currency:** Representación de moneda con código, símbolo y nombre.
- **MoneyAmount:** Cantidad monetaria con valor y moneda.
- **ExchangeRate:** Tasa de cambio entre monedas.

**Ejemplo de uso:**

```java
Currency usd = Currency.USD;
Currency eur = Currency.EUR;
MoneyAmount amount = new MoneyAmount(100.50, usd);
MoneyAmount converted = amount.convertTo(eur, ExchangeRate.getRate(usd, eur));
```

### Características Comunes

Todos los modelos incluirán:

- **Validación integrada:** Verificación de propiedades según reglas definidas.
- **Serialización/Deserialización:** Convertir a/desde JSON, XML, etc.
- **Builder Pattern:** Para creación fluida de instancias.
- **ToString()/Equals()/HashCode():** Implementaciones adecuadas.
- **Inmutabilidad (cuando sea apropiado):** Para seguridad en entornos concurrentes.
- **Documentación Javadoc:** Completa y descriptiva.

### Extensibilidad

Los desarrolladores podrán extender los modelos de varias maneras:

1. **Herencia directa:** Extender las clases base para añadir funcionalidades.
2. **Composición:** Utilizar modelos existentes como propiedades en nuevos modelos.
3. **Adaptadores:** Convertir entre modelos personalizados y estándar.

### Roadmap de Desarrollo Futuro

1. **MVP (3 meses):**
   - Implementación básica de los 5 tipos de modelo mencionados.
   - Soporte para serialización/deserialización JSON.
   - Validación básica de propiedades.

2. **Versiones Futuras:**
   - Modelos adicionales según las necesidades de la comunidad.
   - Mejoras en rendimiento y características.
   - Serialización/deserialización para más formatos (XML, YAML, etc.).
   - Integración más estrecha con otros módulos de la biblioteca.

### Consideraciones Técnicas

- **Compatibilidad:** Java 8+
- **Dependencias mínimas:** Reducir al mínimo las dependencias externas
- **Thread-safety:** Garantizar seguridad en entornos concurrentes
- **Rendimiento:** Optimizar operaciones comunes
