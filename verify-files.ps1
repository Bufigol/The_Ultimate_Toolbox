# Script para verificar que los archivos se han copiado correctamente

# Función para contar archivos en un directorio y subdirectorios
function Count-Files {
    param (
        [string]$directory
    )
    
    $count = 0
    if (Test-Path $directory) {
        $files = Get-ChildItem -Path $directory -File -Recurse | Where-Object { $_.Name -notlike "*.java~" }
        $count = $files.Count
    }
    return $count
}

# Contar archivos en la carpeta de origen
$sourceBase = "src\main\java\com\theultimatetoolbox"
$sourceCount = Count-Files -directory $sourceBase

# Contar archivos en la carpeta de destino (solo archivos java, excluyendo package-info.java)
$targetBase = "src\main\java\com\ultimatetoolbox"
$allFilesTarget = Get-ChildItem -Path $targetBase -File -Recurse | Where-Object { $_.Name -notlike "*.java~" }
$packageInfoCount = ($allFilesTarget | Where-Object { $_.Name -eq "package-info.java" }).Count
$javaFilesTarget = $allFilesTarget | Where-Object { $_.Name -ne "package-info.java" }
$targetCount = $javaFilesTarget.Count

Write-Output "=== VERIFICACIÓN DE ARCHIVOS ==="
Write-Output ""
Write-Output "Conteo de archivos:"
Write-Output "- Carpeta origen (theultimatetoolbox): $sourceCount archivos .java (excluyendo backups)"
Write-Output "- Carpeta destino (ultimatetoolbox): $targetCount archivos .java (excluyendo package-info.java)"
Write-Output "- Archivos package-info.java en destino: $packageInfoCount"
Write-Output ""

# Mostrar los archivos Java (no package-info) en la carpeta destino
Write-Output "=== ARCHIVOS JAVA COPIADOS A LA CARPETA DESTINO ==="
$javaFilesTarget | Sort-Object -Property FullName | ForEach-Object {
    $relativePath = $_.FullName.Replace("$([System.IO.Path]::GetFullPath($targetBase))\", "")
    Write-Output "- $relativePath"
}

# Verificar si hay archivos en la carpeta de origen que no se hayan copiado
Write-Output ""
Write-Output "=== ARCHIVOS EN CARPETA ORIGEN ==="
$sourceFiles = Get-ChildItem -Path $sourceBase -File -Recurse | Where-Object { $_.Name -notlike "*.java~" }
$sourceFiles | Sort-Object -Property FullName | ForEach-Object {
    $relativePath = $_.FullName.Replace("$([System.IO.Path]::GetFullPath($sourceBase))\", "")
    Write-Output "- $relativePath"
}

# Determinar si todos los archivos se copiaron correctamente
if ($sourceCount -le $targetCount) {
    Write-Output ""
    Write-Output "✅ VERIFICACIÓN COMPLETADA: Todos los archivos parecen haberse copiado correctamente."
} else {
    Write-Output ""
    Write-Output "⚠️ ATENCIÓN: Puede haber archivos que no se copiaron correctamente. Por favor, verifica manualmente."
} 