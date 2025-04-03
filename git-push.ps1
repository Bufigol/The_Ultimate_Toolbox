# Script para hacer commit y push de los cambios a GitHub

# Ver el estado actual
Write-Output "Estado actual del repositorio:"
git status

# Añadir todos los cambios
Write-Output "Añadiendo todos los cambios..."
git add .

# Hacer commit
$commitMessage = "Reestructuración del proyecto según la documentación"
Write-Output "Haciendo commit con mensaje: $commitMessage"
git commit -m $commitMessage

# Push a GitHub
Write-Output "Haciendo push a GitHub..."
git push origin develop

Write-Output "Proceso completado. Verifica en GitHub que los cambios se hayan subido correctamente." 