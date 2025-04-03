# Documentación de Anotaciones para Validación

## Anotaciones en The_Ultimate_Toolbox

### Introducción

El módulo de anotaciones de The_Ultimate_Toolbox proporciona un conjunto completo de anotaciones para validación de datos que simplifican y unifican el proceso de validación en aplicaciones Java. Este módulo está diseñado para integrar diversas bibliotecas de validación bajo una interfaz común, permitiendo a los desarrolladores utilizar una única anotación independientemente de la implementación subyacente.

### Arquitectura

El sistema de anotaciones está estructurado de la siguiente manera:

```
com.ultimatetoolbox.validation/
├── annotations/       # Anotaciones de validación
├── validators/        # Implementaciones de validadores
├── constraints/       # Restricciones y reglas personalizables
├── messages/          # Mensajes de error localizados
└── util/              # Utilidades de validación
```

### Anotaciones Principales (MVP)

#### 1. Validación de Email (`@ValidEmail`)

Valida que una cadena represente una dirección de correo electrónico válida según estándares RFC.

**Propiedades:**
- `message`: Mensaje de error personalizado
- `regexp`: Expresión regular personalizada (opcional)
- `flags`: Banderas para la expresión regular
- `strict`: Modo de validación estricto/permisivo

**Ejemplo:**
```java
public class Usuario {
    @ValidEmail(message = "El correo electrónico no es válido")
    private String email;
    
    // Getters y setters
}
```

#### 2. Validación de Documento de Identidad (`@ValidId`)

Valida documentos de identidad según el formato específico del país indicado.

**Propiedades:**
- `country`: Código ISO del país (ej. "ES" para España, "CL" para Chile)
- `message`: Mensaje de error personalizado
- `ignoreFormatting`: Ignorar caracteres de formato como guiones o espacios
- `caseSensitive`: Considerar sensibilidad a mayúsculas/minúsculas

**Ejemplo:**
```java
public class Usuario {
    @ValidId(country = "ES", message = "El DNI español no es válido")
    private String documentoEspañol;
    
    @ValidId(country = "CL", message = "El RUT chileno no es válido")
    private String documentoChileno;
    
    // Getters y setters
}
```

#### 3. Validación de Contraseña (`@StrongPassword`)

Valida que una contraseña cumpla con ciertos criterios de seguridad.

**Propiedades:**
- `minLength`: Longitud mínima (por defecto 8)
- `maxLength`: Longitud máxima (por defecto 128)
- `requireUppercase`: Requiere al menos una mayúscula (por defecto true)
- `requireLowercase`: Requiere al menos una minúscula (por defecto true)
- `requireDigit`: Requiere al menos un dígito (por defecto true)
- `requireSpecial`: Requiere al menos un carácter especial (por defecto true)
- `allowedSpecialChars`: Caracteres especiales permitidos
- `message`: Mensaje de error personalizado

**Ejemplo:**
```java
public class Credenciales {
    @StrongPassword(
        minLength = 10,
        requireUppercase = true,
        requireSpecial = true,
        message = "La contraseña debe tener al menos 10 caracteres, incluyendo mayúsculas y caracteres especiales"
    )
    private String contraseña;
    
    // Getters y setters
}
```

#### 4. Validación de Moneda (`@CurrencyAmount`)

Valida y opcionalmente convierte cantidades monetarias.

**Propiedades:**
- `currency`: Moneda base (ej. "USD", "EUR")
- `minValue`: Valor mínimo permitido
- `maxValue`: Valor máximo permitido
- `precision`: Precisión decimal
- `convertTo`: Moneda a la que convertir (opcional)
- `message`: Mensaje de error personalizado

**Ejemplo:**
```java
public class Transacción {
    @CurrencyAmount(
        currency = "USD",
        minValue = "0.01",
        precision = 2,
        message = "El monto debe ser un valor en USD positivo"
    )
    private BigDecimal monto;
    
    // Getters y setters
}
```

#### 5. Otras Anotaciones Útiles

##### `@NotEmpty`
Valida que una colección, mapa, array o cadena no esté vacía.

##### `@Range`
Valida que un número esté dentro de un rango específico.

##### `@Pattern`
Valida que una cadena cumpla con un patrón de expresión regular.

##### `@URL`
Valida que una cadena represente una URL válida.

##### `@Date`
Valida que una cadena o campo represente una fecha válida en el formato especificado.

### Motor de Validación

El sistema incluye un motor de validación unificado que puede validar objetos completos:

```java
ValidationResult result = Validator.validate(usuario);
if (!result.isValid()) {
    List<ValidationError> errors = result.getErrors();
    // Manejar errores
}
```

### Integración con Frameworks Existentes

El sistema puede integrarse con:

- **Bean Validation (JSR 380)**: Compatibilidad con el estándar
- **Hibernate Validator**: Aprovecha la implementación más popular
- **Spring Validation**: Integración con el ecosistema Spring

### Mensajes de Error Internacionalizados

Todos los mensajes de error pueden ser internacionalizados:

```java
ValidationResult result = Validator.validate(usuario, Locale.SPAIN);
```

Los mensajes se cargan desde archivos de propiedades según el locale especificado.

### Validaciones Compuestas

Se pueden crear validaciones compuestas combinando varias anotaciones:

```java
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ValidEmail
@NotEmpty(message = "El email no puede estar vacío")
public @interface BusinessEmail {
    String message() default "Debe ser un email de negocio válido";
    String[] domains() default {"company.com", "business.org"};
}
```

### Validación Programática

Además de las anotaciones, se pueden realizar validaciones programáticas:

```java
ValidationBuilder.forValue("john@example.com")
    .validateEmail()
    .withMessage("Email inválido")
    .validate();
```

### Roadmap de Desarrollo Futuro

1. **MVP (3 meses):**
   - Implementación de las 5 anotaciones principales
   - Motor de validación básico
   - Mensajes de error personalizables
   - Soporte para validación de objetos completos

2. **Versiones Futuras:**
   - Más anotaciones específicas según necesidades de la comunidad
   - Mejor integración con frameworks populares
   - Mejoras en rendimiento y extensibilidad
   - Validación asíncrona para operaciones costosas
   - Validación de grupos y secuencias

### Consideraciones Técnicas

- **Compatibilidad:** Java 8+
- **Rendimiento:** Optimizar para minimizar el impacto en tiempo de ejecución
- **Extensibilidad:** Facilitar la creación de validadores personalizados
- **Mantenibilidad:** Código limpio, bien documentado y con pruebas unitarias completas
