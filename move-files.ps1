# Script para mover archivos desde theultimatetoolbox a ultimatetoolbox

# Mapeamos las carpetas de origen a destino
$mappings = @{
    "generadores" = "util";
    "modelage" = "models\core";
    "services" = "api";
    "utils\configurationFile" = "util";
    "utils\errorHandler" = "error\types";
    "utils\fileManagment" = "files\util";
    "utils\input" = "input\core";
    "controllers" = "api\core";
    "models" = "models\common";
}

# Ruta base de origen y destino
$sourceBase = "src\main\java\com\theultimatetoolbox"
$targetBase = "src\main\java\com\ultimatetoolbox"

# Función para crear la carpeta de destino si no existe
function Ensure-Directory {
    param (
        [string]$directory
    )
    
    if (-not (Test-Path $directory)) {
        New-Item -Path $directory -ItemType Directory -Force | Out-Null
        Write-Output "Creado directorio: $directory"
    }
}

# Función para copiar archivos
function Copy-Files {
    param (
        [string]$source,
        [string]$destination
    )
    
    # Comprobar si la carpeta de origen existe
    if (-not (Test-Path $source)) {
        Write-Output "La carpeta de origen no existe: $source"
        return
    }
    
    # Crear la carpeta de destino si no existe
    Ensure-Directory $destination
    
    # Obtener todos los archivos en la carpeta de origen
    $files = Get-ChildItem -Path $source -File
    
    # Copiar cada archivo
    foreach ($file in $files) {
        if ($file.Name -notlike "*.java~") {  # Excluir archivos temporales
            Copy-Item -Path $file.FullName -Destination $destination -Force
            Write-Output "Copiado: $($file.FullName) -> $destination\$($file.Name)"
        }
    }
}

# Mover el archivo ComprobadoresIdentificadores.java (si existe)
$comprobadoresFile = Join-Path $sourceBase "ComprobadoresIdentificadores.java~"
if (Test-Path $comprobadoresFile) {
    $targetDir = Join-Path $targetBase "validation\validators"
    Ensure-Directory $targetDir
    # Renombrar al copiar eliminando el ~ al final
    $targetFile = Join-Path $targetDir "ComprobadoresIdentificadores.java"
    Copy-Item -Path $comprobadoresFile -Destination $targetFile -Force
    Write-Output "Copiado y renombrado: $comprobadoresFile -> $targetFile"
}

# Mapeo especial para carpetas anidadas en services
$servicesDir = Join-Path $sourceBase "services"
if (Test-Path $servicesDir) {
    # Obtener las subcarpetas de services
    $subServices = Get-ChildItem -Path $servicesDir -Directory -ErrorAction SilentlyContinue
    foreach ($subService in $subServices) {
        $subSourceDir = Join-Path $servicesDir $subService.Name
        $subTargetDir = Join-Path $targetBase "api\$($subService.Name)"
        
        # Crear la carpeta de destino
        Ensure-Directory $subTargetDir
        
        # Copiar los archivos
        Copy-Files -source $subSourceDir -destination $subTargetDir
    }
}

# Procesar cada mapeo de carpeta
foreach ($mapping in $mappings.GetEnumerator()) {
    $sourceDir = Join-Path $sourceBase $mapping.Key
    $targetDir = Join-Path $targetBase $mapping.Value
    
    # Solo copia directorios que existen
    if (Test-Path $sourceDir) {
        Copy-Files -source $sourceDir -destination $targetDir
        
        # Procesar subcarpetas (si existen)
        $subDirs = Get-ChildItem -Path $sourceDir -Directory -ErrorAction SilentlyContinue
        foreach ($subDir in $subDirs) {
            $subSourceDir = Join-Path $sourceDir $subDir.Name
            $subTargetDir = Join-Path $targetDir $subDir.Name
            
            Copy-Files -source $subSourceDir -destination $subTargetDir
        }
    }
}

Write-Output "Proceso de copia completado. Verifica los archivos antes de eliminar la carpeta original." 