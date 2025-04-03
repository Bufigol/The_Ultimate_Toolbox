# Documentación de Conexión a Bases de Datos

## Herramienta de Conexión a Bases de Datos en The_Ultimate_Toolbox

### Introducción

El módulo de conexión a bases de datos de The_Ultimate_Toolbox proporciona una solución híbrida que combina la simplicidad de uso de un ORM con la flexibilidad de JDBC, permitiendo a los desarrolladores trabajar con bases de datos SQL de manera agnóstica respecto al gestor de base de datos utilizado.

### Arquitectura

La arquitectura del sistema de conexión a bases de datos sigue un enfoque en capas:

```
com.ultimatetoolbox.database/
├── core/               # Interfaces y clases base
│   ├── connection/     # Gestión de conexiones
│   ├── pool/           # Pool de conexiones
│   ├── transaction/    # Gestión de transacciones
├── orm/                # Funcionalidades tipo ORM
│   ├── mapping/        # Mapeo objeto-relacional
│   ├── query/          # Construcción de consultas
│   ├── criteria/       # API de criterios para consultas
├── jdbc/               # Operaciones JDBC simplificadas
│   ├── template/       # Plantillas para operaciones comunes
│   ├── executor/       # Ejecutores de consultas
├── dialect/            # Soporte para diferentes SGBD
│   ├── mysql/          # Soporte MySQL/MariaDB
│   ├── postgresql/     # Soporte PostgreSQL
│   ├── oracle/         # Soporte Oracle
│   ├── sqlserver/      # Soporte SQL Server
│   ├── h2/             # Soporte H2
│   ├── sqlite/         # Soporte SQLite
└── util/               # Utilidades relacionadas con BD
```

### Características Principales (MVP)

#### 1. Gestión de Conexiones

La gestión de conexiones proporciona una forma sencilla de establecer y mantener conexiones a bases de datos.

**Configuración por propiedades:**
```java
DatabaseConfig config = new DatabaseConfig.Builder()
    .url("jdbc:mysql://localhost:3306/mydb")
    .username("user")
    .password("pass")
    .driverClass("com.mysql.cj.jdbc.Driver")
    .poolSize(10)
    .build();

Database db = DatabaseFactory.createDatabase(config);
```

**Configuración por JNDI:**
```java
Database db = DatabaseFactory.createDatabaseFromJndi("java:comp/env/jdbc/myDataSource");
```

#### 2. Pool de Conexiones

El sistema implementa un pool de conexiones eficiente para optimizar el rendimiento:

- Reutilización de conexiones
- Configuración de tamaño mínimo y máximo
- Tiempo de espera configurable
- Validación de conexiones
- Monitorización del estado del pool

#### 3. Operaciones CRUD Simplificadas

**Crear (Insertar):**
```java
// Usando objetos
Usuario usuario = new Usuario("juan", "juan@example.com");
db.save(usuario);

// Usando mapas
Map<String, Object> userData = new HashMap<>();
userData.put("username", "juan");
userData.put("email", "juan@example.com");
db.insert("usuarios", userData);
```

**Leer (Consultar):**
```java
// Buscar por ID
Usuario usuario = db.findById(Usuario.class, 1);

// Buscar todos
List<Usuario> todos = db.findAll(Usuario.class);

// Consulta con condiciones
List<Usuario> activos = db.find(Usuario.class)
    .where("activo = ?", true)
    .orderBy("nombre")
    .list();
```

**Actualizar:**
```java
// Actualizar objeto
usuario.setEmail("nuevo@example.com");
db.update(usuario);

// Actualizar con condiciones
db.update("usuarios")
    .set("activo", false)
    .where("lastLogin < ?", fechaLimite)
    .execute();
```

**Eliminar:**
```java
// Eliminar objeto
db.delete(usuario);

// Eliminar con condiciones
db.delete("usuarios")
    .where("activo = ?", false)
    .execute();
```

#### 4. Consultas Avanzadas

**Query Builder:**
```java
List<Usuario> usuarios = db.query(Usuario.class)
    .select("id", "username", "email")
    .where("rol = ?", "ADMIN")
    .and("activo = ?", true)
    .orderBy("fechaCreacion DESC")
    .limit(10)
    .offset(20)
    .list();
```

**SQL Nativo:**
```java
List<Map<String, Object>> resultado = db.nativeQuery("SELECT * FROM usuarios WHERE rol = ?")
    .param("ADMIN")
    .list();
```

**Agregaciones:**
```java
long count = db.query(Usuario.class)
    .count()
    .where("activo = ?", true)
    .singleResult();

double promedio = db.query("pedidos")
    .avg("monto")
    .where("fecha > ?", fechaInicio)
    .singleResult();
```

#### 5. Transacciones

**Transacciones declarativas:**
```java
db.transaction(() -> {
    Usuario usuario = new Usuario("juan", "juan@example.com");
    db.save(usuario);
    
    Perfil perfil = new Perfil(usuario.getId(), "Juan Pérez");
    db.save(perfil);
    
    // Se confirma automáticamente si no hay excepciones
    // Se revierte automáticamente si hay alguna excepción


### Roadmap de Desarrollo Futuro

1. **MVP (3 meses):**
   - Implementación del pool de conexiones
   - Soporte para operaciones CRUD básicas
   - Mapeo objeto-relacional básico
   - Soporte para 4 gestores de bases de datos principales
   - Implementación de transacciones

2. **Versiones Futuras:**
   - Soporte para más gestores de bases de datos
   - Caché de segundo nivel
   - Mejoras en el rendimiento
   - Migraciones de esquema
   - Consultas más avanzadas y específicas del dialecto
   - Herramientas de profiling y optimización

### Consideraciones Técnicas

- **Compatibilidad:** Java 8+
- **Dependencias:** Drivers JDBC para las bases de datos soportadas
- **Thread-safety:** Seguro para uso en entornos multihilo
- **Rendimiento:** Optimizado para reducir sobrecarga en comparación con ORMs completos
- **Seguridad:** Prevención de inyección SQL mediante consultas parametrizadas
```

**Transacciones manuales:**
```java
Transaction tx = db.beginTransaction();
try {
    // Operaciones con la base de datos
    tx.commit();
} catch (Exception e) {
    tx.rollback();
    throw e;
}
```

### Mapeo Objeto-Relacional

El sistema proporciona un mapeo objeto-relacional ligero mediante anotaciones:

```java
@Table("usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column("nombre_usuario")
    private String username;
    
    @Column
    private String email;
    
    @Column
    private boolean activo;
    
    @Transient
    private String campoTemporal;
    
    // Constructores, getters y setters
}
```

### Soporte para Múltiples Bases de Datos

El sistema detecta automáticamente el dialecto SQL basado en la URL de conexión, o puede especificarse manualmente:

```java
DatabaseConfig config = new DatabaseConfig.Builder()
    .url("jdbc:mysql://localhost:3306/mydb")
    .dialect(Dialect.MYSQL)
    // Otras configuraciones
    .build();
```

Dialectos soportados en el MVP:
- MySQL/MariaDB
- PostgreSQL
- H2
- SQLite

### Características Adicionales

#### 1. Caché de Primer Nivel

Caché dentro de la misma transacción para mejorar el rendimiento:

```java
Usuario usuario1 = db.findById(Usuario.class, 1); // Consulta la BD
Usuario usuario2 = db.findById(Usuario.class, 1); // Usa la caché
```

#### 2. Registros de Consultas

Registro detallado para depuración y optimización:

```java
db.enableQueryLogging(true);
```

#### 3. Metadata de Base de Datos

Acceso a la metadata de la base de datos para introspección:

```java
List<String> tablas = db.getMetadata().getTables();
List<Column> columnas = db.getMetadata().getColumns("usuarios");
```

### Ejemplos de Uso Completos

**Ejemplo 1: Aplicación CRUD básica**
```java
// Configuración
DatabaseConfig config = new DatabaseConfig.Builder()
    .url("jdbc:mysql://localhost:3306/myapp")
    .username("user")
    .password("pass")
    .build();

Database db = DatabaseFactory.createDatabase(config);

// Crear un usuario
Usuario usuario = new Usuario();
usuario.setUsername("nuevo_usuario");
usuario.setEmail("nuevo@example.com");
usuario.setActivo(true);
db.save(usuario);
System.out.println("Usuario creado con ID: " + usuario.getId());

// Buscar usuarios
List<Usuario> usuarios = db.find(Usuario.class)
    .where("activo = ?", true)
    .orderBy("username")
    .list();

System.out.println("Usuarios encontrados: " + usuarios.size());
for (Usuario u : usuarios) {
    System.out.println(u.getUsername() + " - " + u.getEmail());
}

// Actualizar usuario
usuario.setEmail("actualizado@example.com");
db.update(usuario);

// Eliminar usuario
db.delete(usuario);
```

**Ejemplo 2: Transacciones y operaciones complejas**
```java
db.transaction(() -> {
    // Crear pedido
    Pedido pedido = new Pedido();
    pedido.setCliente(clienteId);
    pedido.setFecha(new Date());
    pedido.setEstado("PENDIENTE");
    db.save(pedido);
    
    // Crear detalles del pedido
    for (ProductoCarrito pc : productosCarrito) {
        DetallePedido detalle = new DetallePedido();
        detalle.setPedidoId(pedido.getId());
        detalle.setProductoId(pc.getProductoId());
        detalle.setCantidad(pc.getCantidad());
        detalle.setPrecioUnitario(pc.getPrecio());
        db.save(detalle);
    }
    
    // Actualizar inventario
    for (ProductoCarrito pc : productosCarrito) {
        db.update("productos")
           .set("stock", db.raw("stock - ?", pc.getCantidad()))
           .where("id = ?", pc.getProductoId())
           .execute();
    }
});