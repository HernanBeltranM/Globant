param(
    [string]$EnvFile = ".env",
    [string]$MainClass = "Principal.Main",
    [switch]$Compile
)

$ErrorActionPreference = "Stop"

if (-not (Test-Path $EnvFile)) {
    Write-Error "No se encontro el archivo de entorno: $EnvFile"
}

$opCommand = Get-Command op -ErrorAction SilentlyContinue
if (-not $opCommand) {
    Write-Error "No se encontro 1Password CLI (op) en PATH. Instala op e inicia sesion con: op signin"
}

if ($Compile) {
    $javacCommand = Get-Command javac -ErrorAction SilentlyContinue
    if (-not $javacCommand) {
        Write-Error "No se encontro javac en PATH. Configura el JDK o ejecuta sin -Compile si ya compilaste."
    }

    if (Test-Path "out") {
        Remove-Item -Recurse -Force "out"
    }

    New-Item -ItemType Directory -Path "out" | Out-Null
    & javac -d out src/modelo/*.java src/servicio/*.java src/Principal/*.java
}

$javaCommand = Get-Command java -ErrorAction SilentlyContinue
if (-not $javaCommand) {
    Write-Error "No se encontro java en PATH. Configura el JDK/JRE antes de ejecutar."
}

Write-Host "Ejecutando con variables inyectadas por 1Password..." -ForegroundColor Cyan
& op run --env-file $EnvFile -- java -cp out $MainClass
