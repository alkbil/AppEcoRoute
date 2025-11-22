# 🔐 Guía Rápida: Generar APK Firmada

## ⚡ Método Rápido (1 Comando)

Abre PowerShell en la raíz del proyecto y ejecuta:

```powershell
.\generar-apk-firmada.ps1
```

**¡Eso es todo!** El script hará todo automáticamente:
- ✅ Verifica que el keystore existe
- ✅ Limpia builds anteriores
- ✅ Genera la APK firmada
- ✅ Verifica la firma
- ✅ Muestra el tamaño y ubicación

---

## 📱 Método Manual

Si prefieres hacerlo paso a paso:

### 1. Limpiar y generar APK

```powershell
.\gradlew.bat clean assembleRelease
```

### 2. Ubicación del APK

```
app\build\outputs\apk\release\app-release.apk
```

### 3. Verificar firma

```powershell
keytool -printcert -jarfile app\build\outputs\apk\release\app-release.apk
```

---

## 📲 Instalar en Dispositivo

### Paso 1: Conectar dispositivo

1. Conecta tu teléfono Android por USB
2. Habilita "Depuración USB" en opciones de desarrollador
3. Acepta la autorización en el teléfono

### Paso 2: Verificar conexión

```powershell
adb devices
```

Debe mostrar:
```
List of devices attached
ABC123DEF456    device
```

### Paso 3: Instalar APK

```powershell
adb install app\build\outputs\apk\release\app-release.apk
```

O para reinstalar (si ya está instalada):

```powershell
adb install -r app\build\outputs\apk\release\app-release.apk
```

---

## 🎯 Desde Android Studio

1. **Menu**: Build → Generate Signed Bundle / APK
2. Selecciona: **APK**
3. Click: **Next**
4. Configuración del keystore:
   - **Key store path**: Busca `app\ecoroute-release.keystore`
   - **Key store password**: `EcoRoute2024!`
   - **Key alias**: `ecoroute`
   - **Key password**: `EcoRoute2024!`
5. Click: **Next**
6. Selecciona: **release**
7. Marca: ☑ **V2 (Full APK Signature)**
8. Click: **Finish**

Android Studio generará el APK y te mostrará un popup con la ubicación.

---

## 🔍 Información del Keystore

Tu proyecto ya tiene configurado:

- **Archivo keystore**: `app\ecoroute-release.keystore`
- **Alias**: `ecoroute`
- **Contraseña**: `EcoRoute2024!`
- **Validez**: 10,000 días (~27 años)
- **Algoritmo**: RSA 2048 bits

---

## ⚠️ Importante: Seguridad

**NUNCA** compartas estos archivos públicamente:
- ❌ `ecoroute-release.keystore`
- ❌ `keystore.properties`

Estos archivos ya están en `.gitignore` para evitar que se suban a GitHub.

**Si pierdes el keystore**, no podrás actualizar la app en Google Play Store. ¡Guárdalo en un lugar seguro!

---

## 📊 Verificar APK Generada

### Ver tamaño del APK

```powershell
Get-Item app\build\outputs\apk\release\app-release.apk | Select-Object Name, @{Name="Size(MB)";Expression={[math]::Round($_.Length / 1MB, 2)}}
```

### Ver información de firma

```powershell
keytool -printcert -jarfile app\build\outputs\apk\release\app-release.apk
```

### Abrir carpeta del APK

```powershell
explorer app\build\outputs\apk\release
```

---

## 🐛 Solución de Problemas

### Error: "keytool no se reconoce"

**Solución**: Agrega Java al PATH

```powershell
$env:PATH += ";C:\Program Files\Java\jdk-17\bin"
```

### Error: "Keystore not found"

**Solución**: Verifica que el keystore existe

```powershell
Test-Path app\ecoroute-release.keystore
```

Si devuelve `False`, necesitas crear el keystore primero (ver sección siguiente).

### Error: Build falla

**Solución**: Limpia el proyecto primero

```powershell
.\gradlew.bat clean
.\gradlew.bat assembleRelease
```

---

## 🔧 Crear Keystore (Solo si no existe)

Si por alguna razón necesitas crear un nuevo keystore:

```powershell
keytool -genkey -v -keystore app\ecoroute-release.keystore -alias ecoroute -keyalg RSA -keysize 2048 -validity 10000
```

Cuando te pida información:
- **Contraseña**: `EcoRoute2024!`
- **Nombre**: Tu nombre
- **Organización**: EcoRoute
- **Ciudad**: Tu ciudad
- **País**: CL (o tu código de país)

---

## 📦 Compartir APK

Una vez generada, puedes compartir el APK de varias formas:

### 1. Por USB
Copia `app-release.apk` a tu teléfono y ábrelo para instalar

### 2. Por correo/Drive
Sube el APK a Google Drive o envíalo por correo

### 3. Por ADB
```powershell
adb install app\build\outputs\apk\release\app-release.apk
```

---

## ✅ Checklist de Verificación

Antes de distribuir tu APK, verifica:

- [ ] APK generada exitosamente
- [ ] Firma verificada con keytool
- [ ] Tamaño del APK razonable (< 30 MB)
- [ ] APK instalada y probada en dispositivo real
- [ ] App se ejecuta sin crashes
- [ ] Todas las funcionalidades funcionan:
  - [ ] Login/Registro
  - [ ] GPS tracking
  - [ ] Clima (API funciona)
  - [ ] Guardar rutas
  - [ ] Ver historial
  - [ ] Notificaciones

---

## 🚀 Próximos Pasos

Una vez que tengas tu APK firmada funcionando:

1. **Prueba exhaustiva**: Instala en varios dispositivos
2. **Google Play Store**: Prepara para publicación
3. **Bundle AAB**: Considera generar Android App Bundle para Play Store
4. **Versioning**: Incrementa `versionCode` y `versionName` para actualizaciones

---

## 📚 Recursos Adicionales

- [Documentación completa](scripts_generacion_apk.md)
- [Android App Signing](https://developer.android.com/studio/publish/app-signing)
- [Gradle Build](https://developer.android.com/build)

---

**Última actualización**: Noviembre 19, 2024
