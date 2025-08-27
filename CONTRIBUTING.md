# Guía de Contribución

## Proceso de Contribución

### 1. Configuración Inicial

1. Fork el repositorio
2. Clona tu fork localmente
3. Configura el remoto upstream:

   ```bash
   git remote add upstream https://github.com/original/The_Ultimate_Toolbox.git
   ```

### 2. Desarrollo

1. Asegúrate de estar en la rama `development`:

   ```bash
   git checkout development
   git pull upstream development
   ```

2. Crea una nueva rama feature:

   ```bash
   git checkout -b feature/nombre-de-tu-feature
   ```

3. Realiza tus cambios y commits:

   ```bash
   git add .
   git commit -m "Descripción clara de tus cambios"
   ```

### 3. Estándares de Código

- Sigue las convenciones de código Java
- Añade Javadoc para todas las clases y métodos públicos
- Escribe pruebas unitarias para nuevas funcionalidades
- Mantén el código limpio y bien organizado
- Usa nombres descriptivos para variables y métodos

### 4. Pull Request

1. Push tus cambios a tu fork:

   ```bash
   git push origin feature/nombre-de-tu-feature
   ```

2. Crea un Pull Request desde tu rama feature a `development`
3. Describe tus cambios en detalle
4. Incluye ejemplos de uso si es posible
5. Espera la revisión y feedback

### 5. Revisión

- Los revisores pueden solicitar cambios
- Responde a los comentarios y realiza las modificaciones necesarias
- Mantén la discusión profesional y constructiva

### 6. Merge

- Una vez aprobado, tu código será fusionado en `development`
- Los cambios en `development` serán eventualmente fusionados en `master`

## Preguntas Frecuentes

- ¿Cómo nombro mi rama feature? Usa el formato: `feature/descripcion-corta`
- ¿Qué incluir en el mensaje de commit? Describe el qué y el por qué
- ¿Cuándo crear un nuevo PR? Cuando tengas una característica completa y probada

## Recursos Adicionales

- [Java Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)
- [Javadoc Guide](https://www.oracle.com/java/technologies/javase/javadoc-command-line.html)
- [JUnit Documentation](https://junit.org/junit5/docs/current/user-guide/)
