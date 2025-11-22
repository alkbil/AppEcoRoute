# 📘 Documentación Técnica - EcoRoute

**Versión**: 1.0.0  
**Fecha**: Noviembre 2024  
**Plataforma**: Android  
**Lenguaje**: Kotlin  

---

## 📑 Tabla de Contenidos

1. [Resumen Ejecutivo](#1-resumen-ejecutivo)
2. [Arquitectura del Sistema](#2-arquitectura-del-sistema)
3. [Base de Datos](#3-base-de-datos)
4. [API Externa](#4-api-externa)
5. [Componentes Principales](#5-componentes-principales)
6. [Cálculos y Algoritmos](#6-cálculos-y-algoritmos)
7. [Seguridad](#7-seguridad)
8. [Pruebas](#8-pruebas)
9. [Instalación y Configuración](#9-instalación-y-configuración)
10. [Generación de APK](#10-generación-de-apk)
11. [Dependencias](#11-dependencias)

---

## 1. Resumen Ejecutivo

### 1.1 Descripción del Proyecto

**EcoRoute** es una aplicación móvil Android que promueve la movilidad sostenible mediante el registro GPS de rutas ecológicas (caminata, trote, bicicleta). La aplicación calcula automáticamente el impacto ambiental positivo (CO₂ evitado) y beneficios a la salud (calorías quemadas).

### 1.2 Problemática

El transporte vehicular representa el 24% de las emisiones globales de CO₂. Existe una falta de herramientas que:
- Cuantifiquen el impacto individual positivo
- Motiven el uso de transporte ecológico
- Visualicen beneficios ambientales y de salud

### 1.3 Solución

EcoRoute ofrece:
- ✅ Tracking GPS en tiempo real con OpenStreetMap
- ✅ Cálculo automático de métricas ambientales y de salud
- ✅ Sistema de autenticación local seguro
- ✅ Información meteorológica contextual (API externa)
- ✅ Historial personalizado de rutas
- ✅ Notificaciones motivacionales

### 1.4 Tecnologías Clave

| Categoría | Tecnología |
|-----------|------------|
| **Lenguaje** | Kotlin 1.9.0 |
| **UI** | Jetpack Compose + Material 3 |
| **Arquitectura** | MVVM + Clean Architecture |
| **Base de Datos** | Room 2.6.0 (SQLite) |
| **API REST** | Retrofit 2.9.0 + Gson |
| **Mapas** | OSMDroid 6.1.20 |
| **GPS** | Google Play Services Location 21.3.0 |
| **Async** | Kotlin Coroutines + Flow |

---

## 2. Arquitectura del Sistema

### 2.1 Patrón MVVM (Model-View-ViewModel)

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │ LoginScreen  │  │PerfilScreen  │  │ RutasScreen  │ │
│  │  (Compose)   │  │  (Compose)   │  │  (Compose)   │ │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘ │
└─────────┼──────────────────┼──────────────────┼─────────┘
          │                  │                  │
┌─────────▼──────────────────▼──────────────────▼─────────┐
│                    ViewModel Layer                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │AuthViewModel │  │RutasViewModel│  │LocationVModel│ │
│  │(State + UDF) │  │ (LiveData)   │  │   (Flow)     │ │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘ │
└─────────┼──────────────────┼──────────────────┼─────────┘
          │                  │                  │
┌─────────▼──────────────────▼──────────────────▼─────────┐
│                    Repository Layer                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │AuthRepository│  │EcoRouteRepo  │  │WeatherRepo   │ │
│  │  (Local)     │  │ (Local+GPS)  │  │  (Remote)    │ │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘ │
└─────────┼──────────────────┼──────────────────┼─────────┘
          │                  │                  │
┌─────────▼──────────────────▼──────────────────▼─────────┐
│                      Data Layer                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │  Room DAOs   │  │LocationMgr   │  │ WeatherAPI   │ │
│  │   (SQLite)   │  │    (GPS)     │  │  (Retrofit)  │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
```

### 2.2 Flujo de Datos

#### Flujo de Navegación Principal

```
┌─────────┐     Login     ┌─────────┐    Ver Rutas   ┌──────────┐
│ Splash  │──────────────>│  Login  │───────────────>│  Rutas   │
└─────────┘               └────┬────┘                │   List   │
                               │                     └────┬─────┘
                          Registrar                       │
                               │                          │
                          ┌────▼────┐                     │
                          │Register │                     │
                          └─────────┘                     │
                                                          │
                               ┌──────────────────────────┘
                               │
                   ┌───────────▼────────────┐
                   │                        │
              ┌────▼──────┐          ┌─────▼────┐
              │  Perfil   │          │ Registro │
              │  Usuario  │          │  Ruta    │
              └───────────┘          └──────────┘
```

### 2.3 Capas de la Aplicación

#### 2.3.1 Capa de Presentación (UI)
- **Responsabilidad**: Renderizado de UI y captura de eventos de usuario
- **Tecnología**: Jetpack Compose
- **Componentes**:
  - `LoginScreen`, `RegisterScreen`: Autenticación
  - `PerfilScreen`: Estadísticas de usuario
  - `RegistroRutaScreen`: Tracking GPS con mapa
  - `RutasListScreen`: Historial de rutas
  - `RutaDetailScreen`: Detalles de ruta específica

#### 2.3.2 Capa de ViewModels
- **Responsabilidad**: Gestión de estado y lógica de presentación
- **Patrón**: UDF (Unidirectional Data Flow)
- **Componentes**:
  - `AuthViewModel`: Estado de autenticación
  - `RutasViewModel`: CRUD de rutas
  - `LocationViewModel`: Tracking GPS en tiempo real

#### 2.3.3 Capa de Repositorios
- **Responsabilidad**: Abstracción de fuentes de datos
- **Componentes**:
  - `AuthRepository`: Autenticación local
  - `EcoRouteRepository`: Gestión de rutas y estadísticas
  - `ImageRepository`: Manejo de imágenes
  - `WeatherRepository`: Consumo de API externa

#### 2.3.4 Capa de Datos
- **Responsabilidad**: Acceso a datos persistentes y servicios
- **Componentes**:
  - Room Database (SQLite)
  - DAOs (Data Access Objects)
  - LocationManager (GPS)
  - Retrofit (API REST)

---

## 3. Base de Datos

### 3.1 Diagrama Entidad-Relación

```
┌─────────────────┐
│    USUARIOS     │
│─────────────────│         ┌─────────────────┐
│ id (PK)         │1       N│     RUTAS       │
│ nombre          │◄────────│─────────────────│
│ email (UNIQUE)  │         │ id (PK)         │
│ passwordHash    │         │ nombre          │
│ fotoPerfil      │         │ creadorId (FK)  │1
│ fechaRegistro   │         │ tipo            │├───┐
│ totalKm         │         │ distanciaKm     │    │
│ totalCalorias   │         │ caloriasQuemadas│    │N
│ totalCO2Evitado │         │ co2Evitado      │    │
│ cantidadRutas   │         │ fechaCreacion   │    │
│ notifActivas    │         └─────────────────┘    │
│ recordatorios   │                                │
└─────────────────┘         ┌─────────────────┐   │
        │1                  │   PUNTOS_GPS    │   │
        │                   │─────────────────│   │
        │N                  │ id (PK)         │   │
        │                   │ rutaId (FK)     │◄──┘
┌───────▼─────────┐         │ latitud         │
│    SESIONES     │         │ longitud        │
│─────────────────│         │ timestamp       │
│ id (PK)         │         └─────────────────┘
│ usuarioId (FK)  │
│ recordarme      │         ┌─────────────────┐
│ ultimoAcceso    │         │  COMENTARIOS    │
└─────────────────┘         │─────────────────│
                            │ id (PK)         │
                            │ rutaId (FK)     │
                            │ autorId (FK)    │
                            │ contenido       │
                            │ timestamp       │
                            └─────────────────┘
```

### 3.2 Tablas

#### 3.2.1 Tabla: `usuarios`

```sql
CREATE TABLE usuarios (
    id TEXT PRIMARY KEY NOT NULL,
    nombre TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    passwordHash TEXT NOT NULL,
    fotoPerfil TEXT,
    fechaRegistro INTEGER NOT NULL,
    totalKmRecorridos REAL NOT NULL DEFAULT 0,
    totalCaloriasQuemadas REAL NOT NULL DEFAULT 0,
    totalCO2Evitado REAL NOT NULL DEFAULT 0,
    cantidadRutasCompletadas INTEGER NOT NULL DEFAULT 0,
    notificacionesActivas INTEGER NOT NULL DEFAULT 1,
    recordatoriosActivos INTEGER NOT NULL DEFAULT 0,
    horaRecordatorio INTEGER,
    minutoRecordatorio INTEGER
);
```

**Índices**:
```sql
CREATE UNIQUE INDEX idx_usuarios_email ON usuarios(email);
```

#### 3.2.2 Tabla: `sesiones`

```sql
CREATE TABLE sesiones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuarioId TEXT NOT NULL,
    recordarme INTEGER NOT NULL DEFAULT 0,
    ultimoAcceso INTEGER NOT NULL,
    FOREIGN KEY (usuarioId) REFERENCES usuarios(id) ON DELETE CASCADE
);
```

#### 3.2.3 Tabla: `rutas`

```sql
CREATE TABLE rutas (
    id TEXT PRIMARY KEY NOT NULL,
    nombre TEXT NOT NULL,
    descripcion TEXT,
    tipo TEXT NOT NULL,
    distanciaKm REAL NOT NULL,
    caloriasQuemadas REAL NOT NULL,
    co2Evitado REAL NOT NULL,
    fechaCreacion INTEGER NOT NULL,
    creadorId TEXT NOT NULL,
    FOREIGN KEY (creadorId) REFERENCES usuarios(id) ON DELETE CASCADE
);
```

**Índices**:
```sql
CREATE INDEX idx_rutas_creadorId ON rutas(creadorId);
CREATE INDEX idx_rutas_fechaCreacion ON rutas(fechaCreacion DESC);
```

#### 3.2.4 Tabla: `puntos_gps`

```sql
CREATE TABLE puntos_gps (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    rutaId TEXT NOT NULL,
    latitud REAL NOT NULL,
    longitud REAL NOT NULL,
    timestamp INTEGER NOT NULL,
    FOREIGN KEY (rutaId) REFERENCES rutas(id) ON DELETE CASCADE
);
```

**Índices**:
```sql
CREATE INDEX idx_puntos_gps_rutaId ON puntos_gps(rutaId);
```

#### 3.2.5 Tabla: `comentarios`

```sql
CREATE TABLE comentarios (
    id TEXT PRIMARY KEY NOT NULL,
    rutaId TEXT NOT NULL,
    autorId TEXT NOT NULL,
    contenido TEXT NOT NULL,
    calificacion REAL NOT NULL,
    timestamp INTEGER NOT NULL,
    FOREIGN KEY (rutaId) REFERENCES rutas(id) ON DELETE CASCADE,
    FOREIGN KEY (autorId) REFERENCES usuarios(id) ON DELETE CASCADE
);
```

### 3.3 Operaciones Críticas

#### Actualizar Estadísticas de Usuario
```sql
UPDATE usuarios SET
    totalKmRecorridos = totalKmRecorridos + :distancia,
    totalCaloriasQuemadas = totalCaloriasQuemadas + :calorias,
    totalCO2Evitado = totalCO2Evitado + :co2,
    cantidadRutasCompletadas = cantidadRutasCompletadas + 1
WHERE id = :usuarioId;
```

#### Obtener Rutas por Usuario
```sql
SELECT * FROM rutas 
WHERE creadorId = :usuarioId 
ORDER BY fechaCreacion DESC;
```

---

## 4. API Externa

### 4.1 OpenWeatherMap API

**Documentación**: https://openweathermap.org/current

#### 4.1.1 Endpoint Utilizado

```
GET https://api.openweathermap.org/data/2.5/weather
```

**Parámetros**:
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `lat` | Double | Latitud |
| `lon` | Double | Longitud |
| `appid` | String | API Key |
| `units` | String | `metric` (Celsius) |
| `lang` | String | `es` (Español) |

**Ejemplo de Request**:
```http
GET /data/2.5/weather?lat=-33.4489&lon=-70.6693&appid=YOUR_API_KEY&units=metric&lang=es
```

#### 4.1.2 Respuesta JSON

```json
{
  "coord": {
    "lon": -70.6693,
    "lat": -33.4489
  },
  "weather": [
    {
      "id": 800,
      "main": "Clear",
      "description": "cielo claro",
      "icon": "01d"
    }
  ],
  "main": {
    "temp": 22.5,
    "feels_like": 21.8,
    "temp_min": 20.0,
    "temp_max": 25.0,
    "pressure": 1013,
    "humidity": 65
  },
  "wind": {
    "speed": 3.5,
    "deg": 180
  },
  "clouds": {
    "all": 10
  },
  "sys": {
    "country": "CL",
    "sunrise": 1700820000,
    "sunset": 1700870400
  },
  "name": "Santiago"
}
```

#### 4.1.3 Modelo de Datos Kotlin

```kotlin
data class WeatherResponse(
    @SerializedName("main") val main: MainWeather,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("name") val cityName: String
)

data class MainWeather(
    @SerializedName("temp") val temperature: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("humidity") val humidity: Int
)
```

#### 4.1.4 Implementación

```kotlin
interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "es"
    ): Response<WeatherResponse>
}
```

#### 4.1.5 Uso en la Aplicación

El clima se muestra en `RegistroRutaScreen` mediante el componente `WeatherCard`:

```kotlin
WeatherCard(
    latitude = location.latitude,
    longitude = location.longitude
)
```

**Información Mostrada**:
- Temperatura actual y sensación térmica
- Descripción del clima con emoji contextual
- Humedad relativa
- Velocidad del viento
- Nombre de la ciudad

#### 4.1.6 Configuración de API Key

1. Obtener API Key gratuita en: https://openweathermap.org/appid
2. Editar `WeatherRepository.kt`:
```kotlin
private const val API_KEY = "TU_API_KEY_AQUI"
```

**Límites del plan gratuito**:
- 60 llamadas por minuto
- 1,000,000 llamadas por mes

---

## 5. Componentes Principales

### 5.1 Autenticación

#### 5.1.1 Flujo de Login

```
Usuario ingresa credenciales
         │
         ▼
AuthViewModel.login()
         │
         ▼
AuthRepository.login()
         │
         ├─> UsuarioDao.getByEmail()
         │
         ▼
   ¿Usuario existe?
    │           │
   NO          SÍ
    │           │
    ▼           ▼
 Error    PasswordHasher.verify()
              │
              ▼
        ¿Password correcto?
         │           │
        NO          SÍ
         │           │
         ▼           ▼
      Error     Crear Sesión
                     │
                     ▼
              Navegar a Home
```

#### 5.1.2 Hash de Contraseñas

**Algoritmo**: SHA-256 con salt aleatorio

```kotlin
object PasswordHasher {
    fun hashPassword(password: String): String {
        // 1. Generar salt aleatorio (16 bytes)
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        
        // 2. Concatenar salt + password
        val combined = salt + password.toByteArray()
        
        // 3. Aplicar SHA-256
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(combined)
        
        // 4. Formato: "salt:hash" (ambos en hex)
        return "${salt.toHex()}:${hash.toHex()}"
    }
    
    fun verifyPassword(password: String, storedHash: String): Boolean {
        val parts = storedHash.split(":")
        val salt = parts[0].hexToByteArray()
        val hash = parts[1].hexToByteArray()
        
        val combined = salt + password.toByteArray()
        val digest = MessageDigest.getInstance("SHA-256")
        val computedHash = digest.digest(combined)
        
        return computedHash.contentEquals(hash)
    }
}
```

**Ventajas**:
- Salt único por contraseña previene rainbow tables
- SHA-256 es rápido y seguro para este caso de uso
- No se almacenan contraseñas en texto plano

### 5.2 Tracking GPS

#### 5.2.1 Flujo de Registro de Ruta

```
Usuario inicia tracking
         │
         ▼
LocationViewModel.iniciarTracking()
         │
         ▼
LocationManager.startTracking()
         │
         ├─> Solicitar permisos
         │
         ▼
 FusedLocationProvider
    (cada 5 seg / 10m)
         │
         ▼
LocationViewModel.onLocationUpdate()
         │
         ├─> Agregar punto a lista
         ├─> Calcular distancia (Haversine)
         ├─> Calcular calorías
         └─> Actualizar UI (Flow)
         │
         ▼
Usuario detiene tracking
         │
         ▼
Guardar ruta en Room
         │
         ▼
Actualizar estadísticas usuario
```

#### 5.2.2 LocationManager

```kotlin
class LocationManager(context: Context) {
    private val fusedLocationClient = 
        LocationServices.getFusedLocationProviderClient(context)
    
    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000L // 5 segundos
    ).apply {
        setMinUpdateIntervalMillis(3000L)
        setMinUpdateDistanceMeters(10f) // 10 metros
    }.build()
    
    fun startTracking(callback: (Location) -> Unit) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let(callback)
                }
            },
            Looper.getMainLooper()
        )
    }
}
```

### 5.3 Mapas (OSMDroid)

#### 5.3.1 Componente EcoRouteMapOSM

```kotlin
@Composable
fun EcoRouteMapOSM(
    initialPosition: GeoPoint,
    puntos: List<GeoPoint>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                controller.setCenter(initialPosition)
            }
        },
        update = { mapView ->
            // Limpiar overlays anteriores
            mapView.overlays.clear()
            
            // Dibujar polyline de la ruta
            if (puntos.size >= 2) {
                val polyline = Polyline().apply {
                    setPoints(puntos)
                    outlinePaint.color = Color.BLUE
                    outlinePaint.strokeWidth = 10f
                }
                mapView.overlays.add(polyline)
            }
            
            // Agregar marcador de inicio
            if (puntos.isNotEmpty()) {
                val marker = Marker(mapView).apply {
                    position = puntos.first()
                    title = "Inicio"
                }
                mapView.overlays.add(marker)
            }
            
            mapView.invalidate()
        },
        modifier = modifier
    )
}
```

---

## 6. Cálculos y Algoritmos

### 6.1 Distancia GPS (Haversine)

**Fórmula**:

$$
a = \sin^2\left(\frac{\Delta\phi}{2}\right) + \cos(\phi_1) \cdot \cos(\phi_2) \cdot \sin^2\left(\frac{\Delta\lambda}{2}\right)
$$

$$
c = 2 \cdot \text{atan2}\left(\sqrt{a}, \sqrt{1-a}\right)
$$

$$
d = R \cdot c
$$

Donde:
- $\phi$ = latitud
- $\lambda$ = longitud
- $R$ = radio de la Tierra (6371 km)

**Implementación**:
```kotlin
fun calcularDistancia(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
): Double {
    val R = 6371.0 // Radio de la Tierra en km
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    
    val a = sin(dLat/2).pow(2) +
            cos(Math.toRadians(lat1)) * 
            cos(Math.toRadians(lat2)) *
            sin(dLon/2).pow(2)
    
    val c = 2 * atan2(sqrt(a), sqrt(1-a))
    
    return R * c
}
```

### 6.2 Calorías Quemadas

**Fórmulas por tipo de actividad**:

| Actividad | Calorías/km |
|-----------|-------------|
| Caminata  | 50 cal/km   |
| Trote     | 70 cal/km   |
| Bicicleta | 30 cal/km   |

```kotlin
fun calcularCalorias(distanciaKm: Double, tipo: TipoRuta): Double {
    return when (tipo) {
        TipoRuta.CAMINATA -> distanciaKm * 50
        TipoRuta.TROTE -> distanciaKm * 70
        TipoRuta.BICICLETA -> distanciaKm * 30
    }
}
```

**Ejemplo**:
- 10 km de caminata = 500 calorías
- 10 km de trote = 700 calorías
- 10 km de bicicleta = 300 calorías

### 6.3 CO₂ Evitado

**Fórmula**:

$$
\text{CO}_2 \text{ (kg)} = \text{distancia (km)} \times 0.12
$$

El factor 0.12 kg CO₂/km es el promedio de emisiones de un automóvil de gasolina según la EPA.

```kotlin
fun calcularCO2Evitado(distanciaKm: Double): Double {
    return distanciaKm * 0.12
}
```

**Ejemplo de impacto**:
- 10 km en bicicleta = 1.2 kg CO₂ evitado
- 100 km/mes = 12 kg CO₂ evitado
- 1,200 km/año = 144 kg CO₂ evitado ≈ 7 árboles plantados

---

## 7. Seguridad

### 7.1 Contraseñas

✅ **Hash SHA-256 con salt**
- Salt único de 16 bytes por contraseña
- No se almacenan contraseñas en texto plano
- Formato: `salt:hash` (hex)

✅ **Validación de fortaleza**
- Mínimo 8 caracteres
- Validación en cliente y servidor (futuro)

### 7.2 Sesiones

✅ **Persistencia local**
- Tabla `sesiones` con timestamp de último acceso
- Opción "Recordarme" para mantener sesión
- Logout limpia sesión de Room

### 7.3 Archivos

✅ **FileProvider**
- Imágenes almacenadas en `context.filesDir`
- URIs compartidas de forma segura
- Configuración en `file_paths.xml`

### 7.4 Permisos

✅ **Runtime Permissions**
- Solicitud en tiempo de ejecución
- Explicaciones contextuales al usuario
- Accompanist Permissions para Compose

**Permisos necesarios**:
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

### 7.5 ProGuard (Release)

✅ **Ofuscación de código**
- Nombres de clases y métodos ofuscados
- Reducción de tamaño del APK
- Reglas específicas para librerías críticas

---

## 8. Pruebas

### 8.1 Pruebas Unitarias

**Framework**: JUnit 4

**Cobertura actual**: 4 suites de pruebas, 30+ tests

#### 8.1.1 `PasswordHasherTest`
- ✅ Generación de hashes únicos
- ✅ Verificación de contraseñas
- ✅ Manejo de caracteres especiales
- ✅ Diferentes longitudes de contraseña

#### 8.1.2 `RutaCalculosTest`
- ✅ Cálculo de calorías (3 tipos)
- ✅ Cálculo de CO₂ evitado
- ✅ Casos extremos (distancia 0)
- ✅ Comparación entre tipos de ruta

#### 8.1.3 `DistanciaGPSTest`
- ✅ Fórmula de Haversine
- ✅ Distancias conocidas (Santiago-Valparaíso)
- ✅ Simetría de distancias
- ✅ Coordenadas extremas (polos)

#### 8.1.4 `ValidacionFormulariosTest`
- ✅ Validación de emails
- ✅ Validación de contraseñas
- ✅ Validación de nombres
- ✅ Validación de distancias

### 8.2 Ejecutar Tests

**Comando Gradle**:
```bash
./gradlew test
```

**Ver reporte HTML**:
```
app/build/reports/tests/testDebugUnitTest/index.html
```

**Comando con cobertura**:
```bash
./gradlew testDebugUnitTest jacocoTestReport
```

---

## 9. Instalación y Configuración

### 9.1 Requisitos del Sistema

| Componente | Versión Mínima |
|------------|----------------|
| **Android Studio** | Hedgehog 2023.1.1+ |
| **JDK** | 17 |
| **Gradle** | 8.1.2 |
| **Android SDK** | API 34 |
| **Kotlin** | 1.9.0 |

### 9.2 Configuración del Proyecto

#### Paso 1: Clonar el repositorio
```bash
git clone https://github.com/alkbil/AppEcoRoute.git
cd AppEcoRoute
```

#### Paso 2: Abrir en Android Studio
- File → Open → Seleccionar carpeta del proyecto
- Esperar sincronización de Gradle

#### Paso 3: Configurar API Key de OpenWeatherMap
1. Obtener API Key en: https://openweathermap.org/appid
2. Editar `app/src/main/java/.../data/repository/WeatherRepository.kt`:
```kotlin
private const val API_KEY = "TU_API_KEY_AQUI"
```

#### Paso 4: Sincronizar dependencias
```bash
./gradlew build
```

### 9.3 Ejecutar en Emulador/Dispositivo

#### Debug (desarrollo)
```bash
./gradlew installDebug
```

#### Release (producción)
```bash
./gradlew installRelease
```

---

## 10. Generación de APK

### 10.1 Crear Keystore de Firma

**Comando**:
```bash
keytool -genkey -v -keystore app/ecoroute-release.keystore -alias ecoroute -keyalg RSA -keysize 2048 -validity 10000
```

**Información solicitada**:
- Contraseña del keystore: `EcoRoute2024!`
- Contraseña de la clave: `EcoRoute2024!`
- Nombre y organización: [Tu información]

### 10.2 Configurar Credenciales

Crear archivo `keystore.properties` en la raíz del proyecto:

```properties
storeFile=app/ecoroute-release.keystore
storePassword=EcoRoute2024!
keyAlias=ecoroute
keyPassword=EcoRoute2024!
```

⚠️ **IMPORTANTE**: Agregar a `.gitignore`:
```
keystore.properties
*.keystore
*.jks
```

### 10.3 Generar APK Release

**Comando**:
```bash
./gradlew assembleRelease
```

**Ubicación del APK**:
```
app/build/outputs/apk/release/app-release.apk
```

**Tamaño esperado**: ~15-20 MB

### 10.4 Verificar Firma

```bash
keytool -printcert -jarfile app/build/outputs/apk/release/app-release.apk
```

### 10.5 Instalar APK en Dispositivo

```bash
adb install app/build/outputs/apk/release/app-release.apk
```

---

## 11. Dependencias

### 11.1 Tabla Completa de Dependencias

| Librería | Versión | Propósito |
|----------|---------|-----------|
| **androidx.core:core-ktx** | 1.12.0 | Extensiones Kotlin para Android |
| **androidx.appcompat:appcompat** | 1.6.1 | Compatibilidad con versiones antiguas |
| **androidx.compose:compose-bom** | 2023.10.00 | Bill of Materials de Compose |
| **androidx.compose.ui:ui** | BOM | Componentes UI de Compose |
| **androidx.compose.material3:material3** | BOM | Material Design 3 |
| **androidx.navigation:navigation-compose** | 2.7.5 | Navegación en Compose |
| **androidx.lifecycle:lifecycle-viewmodel-compose** | 2.8.0-alpha01 | ViewModels para Compose |
| **androidx.room:room-runtime** | 2.6.0 | Base de datos SQLite |
| **org.jetbrains.kotlinx:kotlinx-coroutines-android** | 1.7.3 | Coroutines para async |
| **androidx.datastore:datastore-preferences** | 1.0.0 | Almacenamiento de preferencias |
| **org.osmdroid:osmdroid-android** | 6.1.20 | Mapas OpenStreetMap |
| **com.google.android.gms:play-services-location** | 21.3.0 | Servicios de ubicación |
| **com.google.accompanist:accompanist-permissions** | 0.37.3 | Gestión de permisos en Compose |
| **com.squareup.retrofit2:retrofit** | 2.9.0 | Cliente HTTP REST |
| **com.squareup.retrofit2:converter-gson** | 2.9.0 | Serialización JSON |
| **com.google.code.gson:gson** | 2.10.1 | Parser JSON |
| **com.squareup.okhttp3:logging-interceptor** | 4.12.0 | Logs de HTTP |
| **androidx.work:work-runtime-ktx** | 2.9.0 | Tareas en segundo plano |
| **io.coil-kt:coil-compose** | 2.5.0 | Carga de imágenes |
| **junit:junit** | 4.13.2 | Framework de testing |
| **org.mockito:mockito-core** | 5.7.0 | Mocking para tests |

### 11.2 Archivo `build.gradle.kts` (Resumen)

```kotlin
dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.12.0")
    
    // Compose
    val composeBom = platform("androidx.compose:compose-bom:2023.10.00")
    implementation(composeBom)
    implementation("androidx.compose.material3:material3")
    
    // Room
    val roomVersion = "2.6.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.7.0")
}
```

---

## 📊 Resumen de Métricas

### Líneas de Código
- **Kotlin**: ~5,000 líneas
- **XML**: ~500 líneas
- **Tests**: ~800 líneas

### Archivos
- **Total**: 50+ archivos
- **Screens**: 6 pantallas
- **ViewModels**: 3
- **Repositorios**: 4
- **DAOs**: 5
- **Tests**: 4 suites

### Cobertura de Tests
- **Total Tests**: 30+
- **Tests Passing**: 100%
- **Clases Críticas Cubiertas**: 80%

---

## 🔗 Enlaces Útiles

- **Repositorio GitHub**: https://github.com/alkbil/AppEcoRoute
- **OpenWeatherMap API**: https://openweathermap.org/api
- **Jetpack Compose**: https://developer.android.com/jetpack/compose
- **Room Database**: https://developer.android.com/training/data-storage/room
- **OSMDroid**: https://github.com/osmdroid/osmdroid

---

## 👥 Equipo de Desarrollo

**Desarrolladores**: [Tu Nombre] y [Nombre del Compañero]  
**Curso**: Programación de Aplicaciones Móviles  
**Institución**: [Tu Institución]  
**Fecha**: Noviembre 2024

---

## 📝 Notas Finales

Esta documentación técnica cubre los aspectos fundamentales de la aplicación EcoRoute. Para consultas específicas o problemas técnicos, referirse al código fuente en el repositorio GitHub.

**Versión del Documento**: 1.0.0  
**Última Actualización**: Noviembre 18, 2024
