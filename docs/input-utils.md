# Documentación de Utilidades de Entrada por Teclado

## Utilidades de Entrada por Teclado en The_Ultimate_Toolbox

### Introducción

El módulo de utilidades de entrada por teclado de The_Ultimate_Toolbox proporciona una solución robusta, flexible y fácil de usar para manejar la entrada de datos por consola en aplicaciones Java. Este módulo está diseñado para simplificar la obtención de datos del usuario, proporcionando validación automática, conversión de tipos y manejo de errores.

### Arquitectura

La arquitectura del módulo de utilidades de entrada por teclado está estructurada de la siguiente manera:

```
com.ultimatetoolbox.input/
├── core/                # Núcleo del sistema de entrada
│   ├── InputHandler.java         # Interfaz principal
│   ├── ConsoleInput.java         # Implementación para consola
│   ├── InputResult.java          # Resultado de operaciones de entrada
│   ├── InputValidation.java      # Validación de entrada
├── readers/             # Lectores por tipo de dato
│   ├── StringReader.java         # Lector de cadenas
│   ├── NumberReader.java         # Lector de números
│   ├── BooleanReader.java        # Lector de booleanos
│   ├── DateReader.java           # Lector de fechas
│   ├── EnumReader.java           # Lector de enumeraciones
├── validators/          # Validadores de entrada
│   ├── InputValidator.java       # Interfaz para validadores
│   ├── StringValidator.java      # Validador de cadenas
│   ├── NumberValidator.java      # Validador de números
│   ├── DateValidator.java        # Validador de fechas
│   ├── RegexValidator.java       # Validador por expresiones regulares
├── formatters/          # Formateadores de entrada/salida
│   ├── InputFormatter.java       # Interfaz para formateadores
│   ├── StringFormatter.java      # Formateador de cadenas
│   ├── NumberFormatter.java      # Formateador de números
│   ├── DateFormatter.java        # Formateador de fechas
├── prompts/             # Generadores de prompts
│   ├── Prompt.java               # Interfaz para prompts
│   ├── TextPrompt.java           # Prompt de texto simple
│   ├── MenuPrompt.java           # Prompt de menú
│   ├── ConfirmationPrompt.java   # Prompt de confirmación
└── util/                # Utilidades
    ├── InputUtils.java           # Utilidades generales
    ├── TerminalUtils.java        # Utilidades para terminal
    ├── ColorOutput.java          # Salida con colores
```

### Características Principales (MVP)

#### 1. Lectura Básica por Tipo de Dato

El sistema proporciona métodos específicos para leer diferentes tipos de datos.

**Lectura de cadenas:**
```java
// Lectura simple
String name = ConsoleInput.readString("Introduce tu nombre: ");

// Con validación
String email = ConsoleInput.readString("Introduce tu email: ", 
    input -> input.contains("@"), 
    "Email no válido. Debe contener @");
```

**Lectura de números:**
```java
// Enteros
int age = ConsoleInput.readInt("Introduce tu edad: ");

// Con límites
int quantity = ConsoleInput.readInt("Cantidad (1-100): ", 1, 100);

// Números decimales
double price = ConsoleInput.readDouble("Introduce el precio: ");

// Con formato
BigDecimal amount = ConsoleInput.readBigDecimal(
    "Importe: ", 
    "###,##0.00"
);
```

**Lectura de booleanos:**
```java
// Simple (acepta s/n, y/n, true/false)
boolean confirmed = ConsoleInput.readBoolean("¿Confirmar? (s/n): ");

// Con opciones personalizadas
boolean agreed = ConsoleInput.readBoolean(
    "¿Estás de acuerdo? ", 
    Arrays.asList("si", "sí", "ok"), 
    Arrays.asList("no", "nope", "cancel")
);
```

**Lectura de fechas:**
```java
// Con formato por defecto
LocalDate date = ConsoleInput.readDate("Fecha de nacimiento: ");

// Con formato personalizado
LocalDate eventDate = ConsoleInput.readDate(
    "Fecha del evento (dd/MM/yyyy): ", 
    "dd/MM/yyyy"
);

// Con rango
LocalDate appointmentDate = ConsoleInput.readDate(
    "Fecha de cita: ",
    LocalDate.now(),
    LocalDate.now().plusMonths(1)
);
```

#### 2. Validación y Reintento Automático

El sistema valida automáticamente la entrada y solicita reintentos cuando sea necesario.

**Validación con reintento:**
```java
String password = ConsoleInput.readString(
    "Contraseña (mín. 8 caracteres): ",
    input -> input.length() >= 8,
    "La contraseña debe tener al menos 8 caracteres"
);
```

**Validación con expresiones regulares:**
```java
String postalCode = ConsoleInput.readString(
    "Código postal: ",
    new RegexValidator("\\d{5}", "El código postal debe tener 5 dígitos")
);
```

**Validación personalizada:**
```java
InputValidator<String> passwordValidator = input -> {
    if (input.length() < 8) {
        return ValidationResult.error("Mínimo 8 caracteres");
    }
    if (!input.matches(".*[A-Z].*")) {
        return ValidationResult.error("Debe incluir mayúsculas");
    }
    if (!input.matches(".*[a-z].*")) {
        return ValidationResult.error("Debe incluir minúsculas");
    }
    if (!input.matches(".*\\d.*")) {
        return ValidationResult.error("Debe incluir números");
    }
    return ValidationResult.valid();
};

String securePassword = ConsoleInput.readString(
    "Contraseña segura: ",
    passwordValidator
);
```

#### 3. Menús Interactivos

El sistema facilita la creación de menús de opciones.

**Menú simple:**
```java
String[] options = {"Ver clientes", "Añadir cliente", "Eliminar cliente", "Salir"};
int option = ConsoleInput.readMenu("Seleccione una opción:", options);

switch (option) {
    case 0: viewClients(); break;
    case 1: addClient(); break;
    case 2: deleteClient(); break;
    case 3: exit(); break;
}
```

**Menú con objetos:**
```java
List<Product> products = getProducts();
Product selected = ConsoleInput.readMenu(
    "Seleccione un producto:",
    products,
    Product::getName
);

System.out.println("Producto seleccionado: " + selected.getName());
```

**Menú con múltiples columnas:**
```java
MenuBuilder<Product> menuBuilder = new MenuBuilder<Product>()
    .title("Catálogo de Productos")
    .addColumn("ID", Product::getId)
    .addColumn("Nombre", Product::getName)
    .addColumn("Precio", p -> String.format("$%.2f", p.getPrice()))
    .items(products);

Product selected = ConsoleInput.readMenu(menuBuilder);
```

#### 4. Entrada con Formato y Colores

El sistema permite dar formato y color a la entrada/salida para mejorar la experiencia.

**Salida con colores:**
```java
// Mensajes con color
ConsoleInput.printInfo("Información importante");
ConsoleInput.printWarning("Advertencia: operación lenta");
ConsoleInput.printError("Error: archivo no encontrado");
ConsoleInput.printSuccess("Operación completada con éxito");

// Prompts con color
String name = ConsoleInput.readString(
    ColorOutput.green("Nombre: ")
);
```

**Formateo personalizado:**
```java
// Entrada de fecha con formato personalizado
LocalDate date = ConsoleInput.readDate(
    "Fecha de nacimiento: ",
    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
    date -> ColorOutput.format(date, "dd '%s' MMMM '%s' yyyy", "de", "de")
);

// Entrada de número con formato monetario
BigDecimal amount = ConsoleInput.readBigDecimal(
    "Importe: ",
    value -> String.format("$%,.2f", value)
);
```

#### 5. Entrada de Datos Complejos

El sistema facilita la entrada de estructuras de datos completas.

**Entrada de objetos:**
```java
// Definición de la entrada
Person person = ConsoleInput.readObject(
    Person.class,
    Arrays.asList(
        new ObjectField<>("name", "Nombre: ", String.class),
        new ObjectField<>("age", "Edad: ", Integer.class, 18, 100),
        new ObjectField<>("email", "Email: ", String.class, 
            new RegexValidator(".*@.*\\..*", "Email no válido"))
    )
);

System.out.println("Persona creada: " + person);
```

**Entrada de listas:**
```java
// Leer múltiples elementos
List<String> tags = ConsoleInput.readList(
    "Introduce etiquetas (línea vacía para terminar): ",
    String.class
);

// Leer cantidad específica
List<Double> scores = ConsoleInput.readList(
    "Introduce 5 puntuaciones: ",
    Double.class,
    5
);
```

**Entrada de mapas:**
```java
// Leer pares clave-valor
Map<String, String> properties = ConsoleInput.readMap(
    "Introduce propiedades (formato 'clave=valor', línea vacía para terminar): ",
    input -> {
        String[] parts = input.split("=", 2);
        if (parts.length != 2) {
            return null;
        }
        return new AbstractMap.SimpleEntry<>(parts[0].trim(), parts[1].trim());
    }
);
```

#### 6. Entrada Multilinea y Especial

El sistema proporciona métodos para tipos especiales de entrada.

**Entrada multilinea:**
```java
String description = ConsoleInput.readMultiLine(
    "Descripción (línea vacía para terminar):\n"
);
```

**Entrada de contraseñas (oculta):**
```java
String password = ConsoleInput.readPassword("Contraseña: ");
```

**Entrada con autocompletado:**
```java
List<String> commands = Arrays.asList("help", "status", "add", "delete", "exit");
String command = ConsoleInput.readWithCompletion(
    "Comando: ",
    commands
);
```

**Entrada con tiempo límite:**
```java
String response = ConsoleInput.readWithTimeout(
    "Responde rápido (5 segundos): ",
    5,
    TimeUnit.SECONDS,
    "¡Tiempo agotado!"
);
```

### Integración con Validaciones

El sistema se integra perfectamente con el módulo de validaciones de The_Ultimate_Toolbox.

```java
// Usar anotaciones de validación
@ValidEmail
String email = ConsoleInput.readValidatedString(
    "Email: ",
    ValidEmail.class
);

// Usar validadores del sistema de validación
@StrongPassword(minLength = 10, requireSpecial = true)
String password = ConsoleInput.readValidatedString(
    "Contraseña segura: ",
    StrongPassword.class
);
```

### Entrada por Lotes

El sistema permite configurar múltiples entradas en secuencia.

```java
InputForm form = new InputForm()
    .addField("nombre", "Nombre: ", String.class)
    .addField("edad", "Edad: ", Integer.class, 1, 120)
    .addField("email", "Email: ", String.class, 
        new RegexValidator(".*@.*\\..*", "Email no válido"))
    .addField("fechaNacimiento", "Fecha de nacimiento: ", LocalDate.class)
    .addField("aceptaTerminos", "¿Acepta los términos? (s/n): ", Boolean.class);

Map<String, Object> data = ConsoleInput.readForm(form);

String nombre = (String) data.get("nombre");
Integer edad = (Integer) data.get("edad");
// ...
```

### Personalización de la Interfaz

El sistema permite personalizar la apariencia de la interfaz.

```java
// Configurar estilo global
InputStyle style = new InputStyle.Builder()
    .promptColor(Color.CYAN)
    .errorColor(Color.RED)
    .infoColor(Color.BLUE)
    .highlightColor(Color.GREEN)
    .borderStyle(BorderStyle.DOUBLE)
    .build();

ConsoleInput.setGlobalStyle(style);

// Crear banner
ConsoleInput.printBanner("SISTEMA DE GESTIÓN", BorderStyle.DOUBLE);

// Limpiar pantalla
ConsoleInput.clearScreen();

// Mostrar progreso
for (int i = 0; i <= 100; i += 10) {
    ConsoleInput.updateProgressBar("Procesando...", i, 100);
    Thread.sleep(200);
}
```

### Ejemplos de Uso Completos

**Ejemplo 1: Registro de usuario**
```java
public class UserRegistration {
    public static void main(String[] args) {
        ConsoleInput.printBanner("REGISTRO DE USUARIO");
        
        // Recolectar datos de usuario
        String username = ConsoleInput.readString(
            "Nombre de usuario: ",
            input -> input.length() >= 4,
            "El nombre debe tener al menos 4 caracteres"
        );
        
        String password = ConsoleInput.readPassword(
            "Contraseña: ",
            input -> {
                if (input.length() < 8) return "Mínimo 8 caracteres";
                if (!input.matches(".*[A-Z].*")) return "Debe incluir mayúsculas";
                if (!input.matches(".*\\d.*")) return "Debe incluir números";
                return null; // Sin error
            }
        );
        
        String confirmPassword = ConsoleInput.readPassword(
            "Confirmar contraseña: ",
            input -> input.equals(password) ? null : "Las contraseñas no coinciden"
        );
        
        String email = ConsoleInput.readString(
            "Email: ",
            new RegexValidator("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$", "Email no válido")
        );
        
        LocalDate birthDate = ConsoleInput.readDate(
            "Fecha de nacimiento (dd/MM/yyyy): ",
            "dd/MM/yyyy",
            date -> date.isBefore(LocalDate.now().minusYears(18)),
            "Debes ser mayor de 18 años"
        );
        
        // Mostrar menú de países
        String[] countries = {"España", "México", "Argentina", "Colombia", "Chile", "Otro"};
        int countryIndex = ConsoleInput.readMenu("País de residencia:", countries);
        String country = countries[countryIndex];
        
        // Confirmación final
        ConsoleInput.printInfo("\nREVISE SUS DATOS:");
        ConsoleInput.print("Usuario: " + username);
        ConsoleInput.print("Email: " + email);
        ConsoleInput.print("Fecha de nacimiento: " + birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        ConsoleInput.print("País: " + country);
        
        boolean confirm = ConsoleInput.readBoolean("¿Son correctos los datos? (s/n): ");
        
        if (confirm) {
            // Simulación de registro
            ConsoleInput.printSuccess("¡Usuario registrado correctamente!");
        } else {
            ConsoleInput.printWarning("Registro cancelado por el usuario.");
        }
    }
}
```

**Ejemplo 2: Aplicación de gestión simple**
```java
public class SimpleApp {
    public static void main(String[] args) {
        boolean running = true;
        
        while (running) {
            ConsoleInput.clearScreen();
            ConsoleInput.printBanner("GESTOR DE TAREAS");
            
            String[] menuOptions = {
                "Ver tareas",
                "Añadir tarea",
                "Marcar tarea como completada",
                "Eliminar tarea",
                "Salir"
            };
            
            int option = ConsoleInput.readMenu("Seleccione una opción:", menuOptions);
            
            switch (option) {
                case 0:
                    viewTasks();
                    break;
                case 1:
                    addTask();
                    break;
                case 2:
                    completeTask();
                    break;
                case 3:
                    deleteTask();
                    break;
                case 4:
                    running = false;
                    ConsoleInput.printInfo("Gracias por usar la aplicación.");
                    break;
            }
            
            if (running) {
                ConsoleInput.readString("Presione ENTER para continuar...");
            }
        }
    }
    
    private static void viewTasks() {
        // Simulación
        ConsoleInput.printInfo("LISTA DE TAREAS:");
        ConsoleInput.print("1. [✓] Comprar víveres");
        ConsoleInput.print("2. [ ] Llamar al médico");
        ConsoleInput.print("3. [ ] Preparar presentación");
    }
    
    private static void addTask() {
        String task = ConsoleInput.readString("Descripción de la tarea: ");
        LocalDate dueDate = ConsoleInput.readDate("Fecha límite (opcional, Enter para omitir): ", true);
        
        // Simulación
        ConsoleInput.printSuccess("Tarea añadida: " + task + 
            (dueDate != null ? " (Vence: " + dueDate + ")" : ""));
    }
    
    private static void completeTask() {
        int taskId = ConsoleInput.readInt("ID de la tarea a completar: ", 1, 100);
        
        // Simulación
        ConsoleInput.printSuccess("Tarea #" + taskId + " marcada como completada.");
    }
    
    private static void deleteTask() {
        int taskId = ConsoleInput.readInt("ID de la tarea a eliminar: ", 1, 100);
        boolean confirm = ConsoleInput.readBoolean("¿Estás seguro? (s/n): ");
        
        if (confirm) {
            // Simulación
            ConsoleInput.printSuccess("Tarea #" + taskId + " eliminada.");
        } else {
            ConsoleInput.printInfo("Operación cancelada.");
        }
    }
}
```

### Roadmap de Desarrollo Futuro

1. **MVP (3 meses):**
   - Implementación de lectores básicos por tipo de dato
   - Sistema de validación integrado
   - Menús interactivos simples
   - Soporte para colores básicos
   - Entrada de estructuras simples

2. **Versiones Futuras:**
   - Interfaz de entrada gráfica para aplicaciones de escritorio
   - Más opciones de formateo y presentación
   - Soporte para autocompletado avanzado
   - Historial de comandos y navegación
   - Widgets interactivos en consola (calendarios, selectores)
   - Internacionalización de prompts y mensajes
   - Adaptación para diferentes entornos (web, móvil)

### Consideraciones Técnicas

- **Compatibilidad:** Java 8+
- **Dependencias:** Mínimas, posiblemente JLine para funcionalidad avanzada de consola
- **Portabilidad:** Funcionalidad básica garantizada en todas las plataformas
- **Rendimiento:** Optimizado para interacción en tiempo real
- **Internacionalización:** Soporte para diferentes idiomas y formatos
- **Accesibilidad:** Consideraciones para usuarios con necesidades especiales
