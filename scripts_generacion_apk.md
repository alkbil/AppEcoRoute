# 🛠️ Scripts para Generación de APK - EcoRoute

Este documento contiene todos los comandos necesarios para generar el APK firmado de la aplicación.

---

## 📋 Índice

1. [Pre-requisitos](#1-pre-requisitos)
2. [Configurar API Key del Clima](#2-configurar-api-key-del-clima)
3. [Generar Keystore de Firma](#3-generar-keystore-de-firma)
4. [Crear Archivo de Credenciales](#4-crear-archivo-de-credenciales)
5. [Ejecutar Tests](#5-ejecutar-tests)
6. [Generar APK Release](#6-generar-apk-release)
7. [Verificar APK](#7-verificar-apk)
8. [Instalar en Dispositivo](#8-instalar-en-dispositivo)

---

## 1. Pre-requisitos

### Verificar Instalación de Java/JDK

**PowerShell**:
```powershell
java -version
```

**Salida esperada**:
```
java version "17.x.x"
Java(TM) SE Runtime Environment
```

### Verificar Android SDK

**PowerShell**:
```powershell
$env:ANDROID_HOME
```

**Debe mostrar la ruta**, ejemplo:
```
C:\Users\TuUsuario\AppData\Local\Android\Sdk
```

---

## 2. Configurar API Key del Clima

### Paso 1: Obtener API Key Gratuita

1. Visitar: https://openweathermap.org/appid
2. Crear cuenta gratuita
3. Ir a "API Keys" en tu perfil
4. Copiar tu API Key (formato: `a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6`)

### Paso 2: Editar WeatherRepository.kt

**Ubicación del archivo**:
```
app/src/main/java/com/example/appecoroute_alcavil/data/repository/WeatherRepository.kt
```

**Buscar la línea 14**:
```kotlin
private const val API_KEY = "TU_API_KEY_AQUI"
```

**Reemplazar con tu API Key**:
```kotlin
private const val API_KEY = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
```

---

## 3. Generar Keystore de Firma

### Comando para Crear Keystore

**PowerShell** (ejecutar desde la raíz del proyecto):

```powershell
keytool -genkey -v -keystore app\ecoroute-release.keystore -alias ecoroute -keyalg RSA -keysize 2048 -validity 10000
```

### Información a Proporcionar

El comando solicitará la siguiente información:

```
Ingrese la contraseña del almacén de claves: EcoRoute2024!
Vuelva a escribir la nueva contraseña: EcoRoute2024!

¿Cuál es su nombre y apellido?
  [Unknown]: Juan Perez

¿Cuál es el nombre de su unidad de organización?
  [Unknown]: Desarrollo

¿Cuál es el nombre de su organización?
  [Unknown]: EcoRoute

¿Cuál es el nombre de su ciudad o localidad?
  [Unknown]: Santiago

¿Cuál es el nombre de su estado o provincia?
  [Unknown]: Metropolitana

¿Cuál es el código de país de dos letras de la unidad?
  [Unknown]: CL

¿Es correcto CN=Juan Perez, OU=Desarrollo, O=EcoRoute, L=Santiago, ST=Metropolitana, C=CL?
  [no]: si

Ingrese la contraseña de clave para <ecoroute>
  (INTRO si es la misma contraseña que la del almacén de claves): EcoRoute2024!
```

### Verificar Creación del Keystore

```powershell
Test-Path app\ecoroute-release.keystore
```

**Debe mostrar**: `True`

---

## 4. Crear Archivo de Credenciales

### Crear `keystore.properties`

**PowerShell** (desde la raíz del proyecto):

```powershell
@"
storeFile=app/ecoroute-release.keystore
storePassword=EcoRoute2024!
keyAlias=ecoroute
keyPassword=EcoRoute2024!
"@ | Out-File -Encoding UTF8 keystore.properties
```

### Verificar Contenido

```powershell
Get-Content keystore.properties
```

**Salida esperada**:
```
storeFile=app/ecoroute-release.keystore
storePassword=EcoRoute2024!
keyAlias=ecoroute
keyPassword=EcoRoute2024!
```

### ⚠️ IMPORTANTE: Seguridad

**NO subir estos archivos a GitHub**. Verificar `.gitignore`:

```powershell
Add-Content .gitignore "`nkeystore.properties"
Add-Content .gitignore "*.keystore"
Add-Content .gitignore "*.jks"
```

---

## 5. Ejecutar Tests

### Ejecutar Todos los Tests

**PowerShell**:
```powershell
.\gradlew.bat test
```

**Alternativa con limpieza previa**:
```powershell
.\gradlew.bat clean test
```

### Ver Reporte HTML de Tests

**PowerShell**:
```powershell
Start-Process app\build\reports\tests\testDebugUnitTest\index.html
```

### Generar Reporte de Cobertura

**PowerShell**:
```powershell
.\gradlew.bat testDebugUnitTest jacocoTestReport
```

**Abrir reporte de cobertura**:
```powershell
Start-Process app\build\reports\jacoco\jacocoTestReport\html\index.html
```

---

## 6. Generar APK Release

### Comando Principal

**PowerShell**:
```powershell
.\gradlew.bat assembleRelease
```

### Comando con Limpieza Previa (Recomendado)

```powershell
.\gradlew.bat clean assembleRelease
```

### Salida Esperada

```
BUILD SUCCESSFUL in Xs
XX actionable tasks: XX executed
```

### Ubicación del APK Generado

```
app\build\outputs\apk\release\app-release.apk
```

### Verificar Existencia del APK

```powershell
Test-Path app\build\outputs\apk\release\app-release.apk
```

**Debe mostrar**: `True`

### Ver Tamaño del APK

```powershell
Get-Item app\build\outputs\apk\release\app-release.apk | Select-Object Name, @{Name="Size(MB)";Expression={[math]::Round($_.Length / 1MB, 2)}}
```

**Tamaño esperado**: 15-20 MB

---

## 7. Verificar APK

### Verificar Firma del APK

**PowerShell**:
```powershell
keytool -printcert -jarfile app\build\outputs\apk\release\app-release.apk
```

**Salida esperada** (parcial):
```
Signer #1:

Signature:

Owner: CN=Juan Perez, OU=Desarrollo, O=EcoRoute, L=Santiago, ST=Metropolitana, C=CL
Issuer: CN=Juan Perez, OU=Desarrollo, O=EcoRoute, L=Santiago, ST=Metropolitana, C=CL
Serial number: xxxxxxxxxx
Valid from: ...
Valid until: ...
Certificate fingerprints:
	 SHA256: ...
```

### Verificar Contenido del APK

**PowerShell**:
```powershell
Add-Type -Assembly System.IO.Compression.FileSystem
$zip = [System.IO.Compression.ZipFile]::OpenRead("app\build\outputs\apk\release\app-release.apk")
$zip.Entries | Select-Object Name, Length | Format-Table
$zip.Dispose()
```

---

## 8. Instalar en Dispositivo

### Pre-requisitos

1. Habilitar "Depuración USB" en el dispositivo Android
2. Conectar dispositivo por USB
3. Aceptar autorización en el dispositivo

### Verificar Conexión del Dispositivo

**PowerShell**:
```powershell
adb devices
```

**Salida esperada**:
```
List of devices attached
ABC123DEF456    device
```

### Instalar APK

**PowerShell**:
```powershell
adb install app\build\outputs\apk\release\app-release.apk
```

**Salida esperada**:
```
Performing Streamed Install
Success
```

### Desinstalar Versión Anterior (si existe)

```powershell
adb uninstall com.example.appecoroute_alcavil
```

### Reinstalar (Desinstalar + Instalar)

```powershell
adb install -r app\build\outputs\apk\release\app-release.apk
```

### Iniciar la App desde ADB

```powershell
adb shell am start -n com.example.appecoroute_alcavil/.MainActivity
```

---

## 🚀 Script Todo-en-Uno

### Script Completo de Generación

**Guardar como `generar-apk.ps1`**:

```powershell
# Script de Generación de APK - EcoRoute
Write-Host "==================================" -ForegroundColor Cyan
Write-Host "  GENERACION DE APK - ECOROUTE  " -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

# 1. Verificar keystore
Write-Host "[1/5] Verificando keystore..." -ForegroundColor Yellow
if (-Not (Test-Path "app\ecoroute-release.keystore")) {
    Write-Host "ERROR: No se encuentra el keystore" -ForegroundColor Red
    Write-Host "Ejecute el comando de generacion de keystore primero" -ForegroundColor Red
    exit 1
}
Write-Host "OK: Keystore encontrado" -ForegroundColor Green
Write-Host ""

# 2. Verificar keystore.properties
Write-Host "[2/5] Verificando credenciales..." -ForegroundColor Yellow
if (-Not (Test-Path "keystore.properties")) {
    Write-Host "ERROR: No se encuentra keystore.properties" -ForegroundColor Red
    exit 1
}
Write-Host "OK: Credenciales configuradas" -ForegroundColor Green
Write-Host ""

# 3. Ejecutar tests
Write-Host "[3/5] Ejecutando tests unitarios..." -ForegroundColor Yellow
.\gradlew.bat test --quiet
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Tests fallaron" -ForegroundColor Red
    Write-Host "Revise el reporte en: app\build\reports\tests\testDebugUnitTest\index.html" -ForegroundColor Yellow
    exit 1
}
Write-Host "OK: Todos los tests pasaron" -ForegroundColor Green
Write-Host ""

# 4. Limpiar build anterior
Write-Host "[4/5] Limpiando builds anteriores..." -ForegroundColor Yellow
.\gradlew.bat clean --quiet
Write-Host "OK: Build limpio" -ForegroundColor Green
Write-Host ""

# 5. Generar APK release
Write-Host "[5/5] Generando APK firmado..." -ForegroundColor Yellow
.\gradlew.bat assembleRelease
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Fallo al generar APK" -ForegroundColor Red
    exit 1
}
Write-Host ""

# Resumen final
Write-Host "==================================" -ForegroundColor Cyan
Write-Host "  APK GENERADO EXITOSAMENTE  " -ForegroundColor Green
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Ubicacion: app\build\outputs\apk\release\app-release.apk" -ForegroundColor Yellow

# Mostrar tamaño del APK
$apk = Get-Item "app\build\outputs\apk\release\app-release.apk"
$sizeMB = [math]::Round($apk.Length / 1MB, 2)
Write-Host "Tamaño: $sizeMB MB" -ForegroundColor Yellow
Write-Host ""

Write-Host "Comandos disponibles:" -ForegroundColor Cyan
Write-Host "  - Verificar firma: keytool -printcert -jarfile app\build\outputs\apk\release\app-release.apk" -ForegroundColor White
Write-Host "  - Instalar en dispositivo: adb install app\build\outputs\apk\release\app-release.apk" -ForegroundColor White
```

### Ejecutar el Script

```powershell
.\generar-apk.ps1
```

---

## 🐛 Solución de Problemas

### Error: "keytool no se reconoce como comando"

**Solución**: Agregar JDK al PATH

```powershell
$env:PATH += ";C:\Program Files\Java\jdk-17\bin"
```

### Error: "ANDROID_HOME no está definido"

**Solución**: Definir variable de entorno

```powershell
$env:ANDROID_HOME = "C:\Users\$env:USERNAME\AppData\Local\Android\Sdk"
```

### Error: "gradlew.bat no se encuentra"

**Solución**: Ejecutar desde la raíz del proyecto

```powershell
cd C:\Users\Pc\AndroidStudioProjects\AppEcoRoute_Alcavil
```

### Error: Build falla por falta de memoria

**Solución**: Aumentar memoria de Gradle

Editar `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=1024m
```

### APK no instala en dispositivo

**Causas posibles**:
1. Versión anterior instalada → Desinstalar primero
2. Firma corrupta → Regenerar keystore
3. Android version incompatible → Verificar minSdk (24)

**Solución**:
```powershell
adb uninstall com.example.appecoroute_alcavil
adb install -r app\build\outputs\apk\release\app-release.apk
```

---

## 📊 Checklist de Verificación

- [ ] Keystore generado (`app\ecoroute-release.keystore`)
- [ ] Archivo `keystore.properties` creado
- [ ] API Key de OpenWeatherMap configurada
- [ ] Tests unitarios pasan 100%
- [ ] APK generado exitosamente
- [ ] Firma del APK verificada
- [ ] Tamaño del APK < 25 MB
- [ ] APK instalado en dispositivo de prueba
- [ ] App se ejecuta sin crashes
- [ ] Funcionalidad de clima muestra datos
- [ ] GPS tracking funciona correctamente

---

## 📚 Referencias

- **Android Signing**: https://developer.android.com/studio/publish/app-signing
- **Gradle Build**: https://developer.android.com/build
- **ProGuard**: https://developer.android.com/studio/build/shrink-code
- **ADB Commands**: https://developer.android.com/tools/adb

---

**Última actualización**: Noviembre 18, 2024
