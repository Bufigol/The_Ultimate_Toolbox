# Script para crear archivos package-info.java en cada módulo y submódulo

$baseDir = "src\main\java\com\ultimatetoolbox"
$modulesDirs = Get-ChildItem -Path $baseDir -Directory

foreach ($moduleDir in $modulesDirs) {
    $moduleName = $moduleDir.Name
    $packagePath = "com.ultimatetoolbox.$moduleName"
    
    # Crear package-info.java en el directorio base del módulo
    $packageInfoContent = @"
/**
 * Módulo $moduleName de The Ultimate Toolbox.
 *
 * <p>Este paquete contiene clases para el subsistema de $moduleName.</p>
 *
 * @author The Ultimate Toolbox Team
 * @version 1.0
 * @since 1.0
 */
package $packagePath;
"@
    
    New-Item -Path "$baseDir\$moduleName\package-info.java" -ItemType File -Force -Value $packageInfoContent
    
    # Crear package-info.java en subdirectorios
    $subDirs = Get-ChildItem -Path "$baseDir\$moduleName" -Directory
    
    foreach ($subDir in $subDirs) {
        $subDirName = $subDir.Name
        $subPackagePath = "$packagePath.$subDirName"
        
        $subPackageInfoContent = @"
/**
 * Submódulo $subDirName del módulo $moduleName de The Ultimate Toolbox.
 *
 * <p>Este paquete contiene clases específicas para el componente $subDirName de $moduleName.</p>
 *
 * @author The Ultimate Toolbox Team
 * @version 1.0
 * @since 1.0
 */
package $subPackagePath;
"@
        
        New-Item -Path "$baseDir\$moduleName\$subDirName\package-info.java" -ItemType File -Force -Value $subPackageInfoContent
    }
}

Write-Output "Archivos package-info.java creados exitosamente" 