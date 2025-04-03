# Script para crear la estructura de directorios para The_Ultimate_Toolbox

# Modelo
$moduleDir = "src\main\java\com\ultimatetoolbox"
if (-not (Test-Path $moduleDir)) {
    New-Item -Path $moduleDir -ItemType Directory -Force
}

# Models
New-Item -Path "$moduleDir\models\core" -ItemType Directory -Force
New-Item -Path "$moduleDir\models\common" -ItemType Directory -Force
New-Item -Path "$moduleDir\models\util" -ItemType Directory -Force

# Validation
New-Item -Path "$moduleDir\validation\annotations" -ItemType Directory -Force
New-Item -Path "$moduleDir\validation\validators" -ItemType Directory -Force
New-Item -Path "$moduleDir\validation\constraints" -ItemType Directory -Force
New-Item -Path "$moduleDir\validation\messages" -ItemType Directory -Force

# Database
New-Item -Path "$moduleDir\database\core" -ItemType Directory -Force
New-Item -Path "$moduleDir\database\orm" -ItemType Directory -Force
New-Item -Path "$moduleDir\database\jdbc" -ItemType Directory -Force
New-Item -Path "$moduleDir\database\dialect" -ItemType Directory -Force
New-Item -Path "$moduleDir\database\util" -ItemType Directory -Force

# Error
New-Item -Path "$moduleDir\error\core" -ItemType Directory -Force
New-Item -Path "$moduleDir\error\types" -ItemType Directory -Force
New-Item -Path "$moduleDir\error\handler" -ItemType Directory -Force
New-Item -Path "$moduleDir\error\i18n" -ItemType Directory -Force
New-Item -Path "$moduleDir\error\util" -ItemType Directory -Force

# Logging
New-Item -Path "$moduleDir\logging\core" -ItemType Directory -Force
New-Item -Path "$moduleDir\logging\appenders" -ItemType Directory -Force
New-Item -Path "$moduleDir\logging\formatters" -ItemType Directory -Force
New-Item -Path "$moduleDir\logging\adapters" -ItemType Directory -Force
New-Item -Path "$moduleDir\logging\config" -ItemType Directory -Force
New-Item -Path "$moduleDir\logging\util" -ItemType Directory -Force

# Files
New-Item -Path "$moduleDir\files\core" -ItemType Directory -Force
New-Item -Path "$moduleDir\files\readers" -ItemType Directory -Force
New-Item -Path "$moduleDir\files\writers" -ItemType Directory -Force
New-Item -Path "$moduleDir\files\watchers" -ItemType Directory -Force
New-Item -Path "$moduleDir\files\batch" -ItemType Directory -Force
New-Item -Path "$moduleDir\files\util" -ItemType Directory -Force

# Math
New-Item -Path "$moduleDir\math\core" -ItemType Directory -Force
New-Item -Path "$moduleDir\math\statistics" -ItemType Directory -Force
New-Item -Path "$moduleDir\math\conversion" -ItemType Directory -Force
New-Item -Path "$moduleDir\math\currency" -ItemType Directory -Force
New-Item -Path "$moduleDir\math\geometry" -ItemType Directory -Force
New-Item -Path "$moduleDir\math\finance" -ItemType Directory -Force
New-Item -Path "$moduleDir\math\util" -ItemType Directory -Force

# Input
New-Item -Path "$moduleDir\input\core" -ItemType Directory -Force
New-Item -Path "$moduleDir\input\readers" -ItemType Directory -Force
New-Item -Path "$moduleDir\input\validators" -ItemType Directory -Force
New-Item -Path "$moduleDir\input\formatters" -ItemType Directory -Force
New-Item -Path "$moduleDir\input\prompts" -ItemType Directory -Force

# API
New-Item -Path "$moduleDir\api\core" -ItemType Directory -Force
New-Item -Path "$moduleDir\api\client" -ItemType Directory -Force
New-Item -Path "$moduleDir\api\auth" -ItemType Directory -Force
New-Item -Path "$moduleDir\api\response" -ItemType Directory -Force
New-Item -Path "$moduleDir\api\cache" -ItemType Directory -Force
New-Item -Path "$moduleDir\api\error" -ItemType Directory -Force
New-Item -Path "$moduleDir\api\util" -ItemType Directory -Force

# Util
New-Item -Path "$moduleDir\util\text" -ItemType Directory -Force
New-Item -Path "$moduleDir\util\date" -ItemType Directory -Force
New-Item -Path "$moduleDir\util\crypto" -ItemType Directory -Force
New-Item -Path "$moduleDir\util\collection" -ItemType Directory -Force
New-Item -Path "$moduleDir\util\xml" -ItemType Directory -Force

# Recursos
New-Item -Path "src\main\resources\i18n\errors" -ItemType Directory -Force
New-Item -Path "src\main\resources\i18n\messages" -ItemType Directory -Force
New-Item -Path "src\main\resources\i18n\validation" -ItemType Directory -Force
New-Item -Path "src\main\resources\config" -ItemType Directory -Force

# Test
New-Item -Path "src\test\java\com\ultimatetoolbox\models" -ItemType Directory -Force
New-Item -Path "src\test\java\com\ultimatetoolbox\validation" -ItemType Directory -Force
New-Item -Path "src\test\java\com\ultimatetoolbox\database" -ItemType Directory -Force
New-Item -Path "src\test\java\com\ultimatetoolbox\error" -ItemType Directory -Force
New-Item -Path "src\test\java\com\ultimatetoolbox\logging" -ItemType Directory -Force
New-Item -Path "src\test\java\com\ultimatetoolbox\files" -ItemType Directory -Force
New-Item -Path "src\test\java\com\ultimatetoolbox\math" -ItemType Directory -Force
New-Item -Path "src\test\java\com\ultimatetoolbox\input" -ItemType Directory -Force
New-Item -Path "src\test\java\com\ultimatetoolbox\api" -ItemType Directory -Force
New-Item -Path "src\test\java\com\ultimatetoolbox\util" -ItemType Directory -Force
New-Item -Path "src\test\resources\test-data" -ItemType Directory -Force
New-Item -Path "src\test\resources\config" -ItemType Directory -Force

# Scripts
New-Item -Path "scripts\build" -ItemType Directory -Force
New-Item -Path "scripts\ci" -ItemType Directory -Force
New-Item -Path "scripts\release" -ItemType Directory -Force

# GitHub
New-Item -Path ".github\workflows" -ItemType Directory -Force
New-Item -Path ".github\ISSUE_TEMPLATE" -ItemType Directory -Force

# Crear archivo de configuración de ejemplo
$configContent = @"
# Configuración por defecto para logging
log.level=INFO
log.file=logs/application.log
log.format=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
"@
New-Item -Path "src\main\resources\config\default-logging.properties" -ItemType File -Force -Value $configContent

# Crear archivo de plantilla de base de datos
$dbTemplateContent = @"
<database-templates>
    <template name="mysql">
        <url>jdbc:mysql://localhost:3306/mydatabase</url>
        <driver>com.mysql.cj.jdbc.Driver</driver>
        <properties>
            <property name="useSSL" value="false" />
            <property name="serverTimezone" value="UTC" />
        </properties>
    </template>
    <template name="postgresql">
        <url>jdbc:postgresql://localhost:5432/mydatabase</url>
        <driver>org.postgresql.Driver</driver>
    </template>
    <template name="h2">
        <url>jdbc:h2:mem:testdb</url>
        <driver>org.h2.Driver</driver>
    </template>
</database-templates>
"@
New-Item -Path "src\main\resources\config\database-templates.xml" -ItemType File -Force -Value $dbTemplateContent

# Crear archivo de configuración de test
$testConfigContent = @"
# Configuración para tests
test.database.url=jdbc:h2:mem:testdb
test.database.username=sa
test.database.password=
"@
New-Item -Path "src\test\resources\config\test-config.properties" -ItemType File -Force -Value $testConfigContent

Write-Output "Estructura de directorios creada exitosamente" 