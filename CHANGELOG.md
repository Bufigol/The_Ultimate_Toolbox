# Changelog

Todos los cambios notables de este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/lang/es/).

## [Unreleased]

### Por implementar
- Más utilidades matemáticas avanzadas
- Mejoras en manejo de APIs
- Expansión de utilidades de validación
- Documentación completa de API

---

## [0.1.0] - 2026-02-23

### 🎉 Primera versión pública

Esta es la primera versión pública de **The Ultimate Toolbox**. El proyecto está en desarrollo activo y la API puede cambiar en versiones futuras.

### Added

#### Módulos principales
- **API Utilities** (`com.the_ultimate_toolbox.api`): Utilidades para interacción con servicios web y APIs
- **Database Utilities** (`com.the_ultimate_toolbox.database`): Herramientas para interacción con bases de datos
- **File Operations** (`com.the_ultimate_toolbox.files`): Operaciones de lectura, escritura y manejo de archivos
- **Input Handling** (`com.the_ultimate_toolbox.input`): Manejo de entrada de usuario por teclado
- **Logging Utilities** (`com.the_ultimate_toolbox.logging`): Sistema de logging con Log4j 2
- **Math Toolbox** (`com.the_ultimate_toolbox.math`): Operaciones matemáticas y algoritmos
- **Data Models** (`com.the_ultimate_toolbox.models`): Modelos de datos y POJOs
- **General Utilities** (`com.the_ultimate_toolbox.util`): Utilidades de propósito general
  - Manejo de configuración
  - Sistema de caché
  - Serialización JSON
- **Validation Tools** (`com.the_ultimate_toolbox.validation`): Herramientas de validación de datos
- **Error Handling** (`com.the_ultimate_toolbox.error`): Excepciones personalizadas y manejo de errores

#### Dependencias incluidas
- Apache Commons Net 3.11.1
- Jsoup 1.15.3
- ZXing (generación de códigos QR) 3.5.2
- MySQL Connector/J 9.4.0
- Jackson (JSON processing) 2.17.2
- Log4j 2.23.1
- JUnit 5 para testing

#### Configuración
- Configuración de Maven para publicación en GitHub Packages
- Soporte para Java 21+
- Generación automática de JAR con sources y Javadoc

### Notes

⚠️ **Versión en desarrollo**: Esta es una versión inicial y la API puede sufrir cambios significativos en futuras versiones. Se recomienda usarla solo para desarrollo y testing, no en producción.

📝 **Contribuciones**: El proyecto acepta contribuciones siguiendo el flujo de trabajo documentado en `CONTRIBUTING.md` y `GEMINI.md`.

---

## Cómo usar este proyecto

Para agregar esta dependencia a tu proyecto Maven:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/Bufigol/The_Ultimate_Toolbox</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.theultimatetoolbox</groupId>
        <artifactId>the-ultimate-toolbox</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

**Nota**: Necesitas configurar autenticación con GitHub en tu `~/.m2/settings.xml`. Ver [README.md](README.md) para más detalles.

---

[Unreleased]: https://github.com/Bufigol/The_Ultimate_Toolbox/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/Bufigol/The_Ultimate_Toolbox/releases/tag/v0.1.0
