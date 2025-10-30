# 🚴‍♂️ EcoRoute - Aplicación de Rutas Ecológicas

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)

**Registra tus rutas ecológicas, ahorra CO₂ y mantente activo** 🌱

[Características](#-características) • [Tecnologías](#️-tecnologías) • [Instalación](#-instalación) • [Uso](#-uso) • [Arquitectura](#-arquitectura)

</div>

---

## 📱 Descripción

**EcoRoute** es una aplicación móvil Android que te permite registrar tus rutas a pie, en bicicleta o corriendo, calculando automáticamente:

- 📏 **Distancia recorrida** en kilómetros
- 🔥 **Calorías quemadas** según el tipo de actividad
- 🌍 **CO₂ evitado** al no usar vehículos motorizados
- ⏱️ **Tiempo y velocidad promedio**

Visualiza tus rutas en un mapa interactivo, consulta tus estadísticas personales y contribuye al medio ambiente mientras te mantienes en forma.

---

## ✨ Características

### 🔐 Sistema de Autenticación
- Registro de usuarios con validación de email
- Login seguro con hash de contraseñas (SHA-256 + salt)
- Sesiones persistentes con opción "Recordarme"
- Perfil de usuario con foto personalizable

### 📍 Seguimiento GPS en Tiempo Real
- Registro de rutas con seguimiento GPS continuo
- Almacenamiento de puntos GPS en base de datos local
- Cálculo automático de distancia, velocidad y tiempo
- Pausa y reanudación de seguimiento

### 🗺️ Visualización de Rutas
- Mapas interactivos con OpenStreetMap (OSMDroid)
- Visualización del recorrido completo en detalle
- Marcadores de inicio y fin de ruta
- Polylines mostrando el camino exacto recorrido

### 📊 Estadísticas Personales
- Total de kilómetros recorridos
- Total de calorías quemadas
- Total de CO₂ evitado
- Cantidad de rutas completadas
- Visualización en perfil de usuario

### 📸 Gestión de Fotos
- Captura de fotos con cámara nativa
- Selección de fotos desde galería
- Almacenamiento seguro con FileProvider
- Foto de perfil personalizable

### 🔔 Sistema de Notificaciones
- Notificación de bienvenida al registrarse
- Notificación al completar una ruta
- Notificaciones de logros alcanzados
- Recordatorios diarios de actividad programables
- 3 canales de notificación configurables

### 💾 Base de Datos Local
- Room Database con SQLite
- Almacenamiento offline completo
- Sincronización reactiva con Flows
- Cascada de eliminación en relaciones

---

## 🛠️ Tecnologías

### Lenguaje y Framework
- **Kotlin** - Lenguaje principal
- **Jetpack Compose** - UI moderna y declarativa
- **Material 3** - Diseño moderno y consistente

### Arquitectura
- **MVVM** (Model-View-ViewModel)
- **Repository Pattern**
- **Clean Architecture**
- **Flows** para programación reactiva

### Jetpack Components
| Componente | Uso |
|------------|-----|
| **Room** | Base de datos local SQLite |
| **Navigation Compose** | Navegación entre pantallas |
| **ViewModel** | Gestión de estado UI |
| **LiveData/Flow** | Observación reactiva de datos |
| **DataStore** | Preferencias de usuario |
| **WorkManager** | Tareas en background (notificaciones) |

### Librerías Principales

```gradle
// Maps
implementation("org.osmdroid:osmdroid-android:6.1.20")

// Location Services
implementation("com.google.android.gms:play-services-location:21.3.0")

// Permissions
implementation("com.google.accompanist:accompanist-permissions:0.37.3")

// Image Loading
implementation("io.coil-kt:coil-compose:2.5.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

---

## 📥 Instalación

### Requisitos Previos
- Android Studio Hedgehog | 2023.1.1 o superior
- JDK 17
- Android SDK API 24+ (Android 7.0 Nougat o superior)
- Dispositivo físico o emulador con GPS

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/alkbil/AppEcoRoute.git
cd AppEcoRoute
```

2. **Abrir en Android Studio**
   - File → Open → Seleccionar carpeta del proyecto
   - Esperar a que Gradle sincronice las dependencias

3. **Configurar permisos** (ya incluidos en AndroidManifest.xml)
   - Ubicación (GPS)
   - Cámara
   - Almacenamiento/Galería
   - Notificaciones

4. **Compilar y ejecutar**
```bash
./gradlew assembleDebug
```
O directamente desde Android Studio: Run → Run 'app'

---

## 🚀 Uso

### Primer Inicio

1. **Registro de Usuario**
   - Abre la app
   - Haz clic en "Crear Cuenta"
   - Completa: Nombre, Email, Contraseña
   - Requisitos de contraseña:
     - Mínimo 6 caracteres
     - Al menos 1 número
     - Al menos 1 letra

2. **Login**
   - Ingresa email y contraseña
   - Activa "Recordarme" para sesión persistente (7 días)

### Registrar una Ruta

1. **Iniciar Seguimiento**
   - Pantalla principal → Botón "Registrar Ruta"
   - Selecciona tipo de actividad: Caminata / Trote / Bicicleta
   - Presiona "Iniciar" cuando estés listo

2. **Durante el Recorrido**
   - El GPS registra tu posición cada 5 segundos
   - Puedes ver en tiempo real:
     - Distancia recorrida
     - Velocidad actual
     - Tiempo transcurrido
   - Usa "Pausar" si necesitas detenerte

3. **Finalizar Ruta**
   - Presiona "Detener"
   - Ingresa nombre y descripción de la ruta
   - Presiona "Guardar"
   - ¡Recibirás una notificación con tus estadísticas!

### Ver Rutas Guardadas

- **Lista de Rutas**: Pantalla principal muestra todas tus rutas
- **Detalle de Ruta**: Toca cualquier ruta para ver:
  - Mapa con el recorrido completo
  - Estadísticas detalladas
  - Fecha y duración
  - Eliminar ruta (botón en detalle)

### Perfil de Usuario

1. **Acceder al Perfil**
   - Pantalla principal → Ícono de persona (esquina superior derecha)

2. **Funcionalidades**
   - Ver/cambiar foto de perfil (cámara o galería)
   - Consultar estadísticas totales
   - Configurar notificaciones
   - Configurar recordatorios de actividad
   - Cerrar sesión

---

## 🏗️ Arquitectura

### Estructura del Proyecto

```
app/src/main/java/com/example/appecoroute_alcavil/
│
├── data/                           # Capa de Datos
│   ├── location/                   # Gestión de GPS
│   │   └── LocationManager.kt      # Servicio de ubicación
│   ├── model/                      # Entidades Room
│   │   ├── Usuario.kt              # Tabla usuarios
│   │   ├── Sesion.kt               # Tabla sesiones
│   │   ├── Ruta.kt                 # Tabla rutas
│   │   ├── PuntoGPSEntity.kt       # Tabla puntos GPS
│   │   └── Comentario.kt           # Tabla comentarios
│   └── repository/                 # Repositorios
│       ├── EcoRouteDatabase.kt     # Database principal
│       ├── EcoRouteRepository.kt   # Repo de rutas
│       ├── AuthRepository.kt       # Repo de autenticación
│       └── ImageRepository.kt      # Repo de imágenes
│
├── ui/                             # Capa de Presentación
│   ├── screens/                    # Pantallas Compose
│   │   ├── LoginScreen.kt          # Login
│   │   ├── RegisterScreen.kt       # Registro
│   │   ├── RutasListScreen.kt      # Lista de rutas
│   │   ├── RutaDetailScreen.kt     # Detalle de ruta
│   │   ├── RegistroRutaScreen.kt   # Registro GPS
│   │   └── PerfilScreen.kt         # Perfil de usuario
│   ├── components/                 # Componentes reutilizables
│   │   ├── EcoRouteMapOSM.kt       # Mapa OSM
│   │   ├── PhotoPickerDialog.kt    # Selector de fotos
│   │   └── RatingBar.kt            # Componente de rating
│   ├── viewmodels/                 # ViewModels
│   │   ├── AuthViewModel.kt        # VM de autenticación
│   │   ├── RutasViewModel.kt       # VM de rutas
│   │   └── LocationViewModel.kt    # VM de ubicación
│   ├── theme/                      # Tema Material 3
│   ├── EcoRouteNavigation.kt       # Navegación
│   └── Screen.kt                   # Definición de rutas
│
├── utils/                          # Utilidades
│   ├── PasswordHasher.kt           # Hash de contraseñas
│   └── MapUtils.kt                 # Utilidades de mapas
│
├── notifications/                  # Sistema de notificaciones
│   ├── NotificationHelper.kt       # Gestión de notificaciones
│   └── ReminderWorker.kt           # Worker de recordatorios
│
└── MainActivity.kt                 # Activity principal
```

### Base de Datos - Room

**Versión**: 3

#### Tablas

**usuarios**
- `id` (PK, UUID)
- `nombre`, `email` (unique), `passwordHash`
- `fotoPerfil` (URI)
- `fechaRegistro` (Long)
- Estadísticas: `totalKmRecorridos`, `totalCaloriasQuemadas`, `totalCO2Evitado`, `cantidadRutasCompletadas`
- Configuración: `notificacionesActivas`, `recordatoriosActivos`, `horaRecordatorio`

**sesiones**
- `id` (PK, fixed = 1)
- `usuarioId` (FK → usuarios)
- `recordarme` (Boolean)
- `ultimoAcceso` (Long)

**rutas**
- `id` (PK, autoincrement)
- `creadorId` (FK → usuarios)
- `nombre`, `descripcion`, `tipo`
- `distancia`, `duracion`, `velocidadPromedio`
- `caloriasQuemadas`, `co2Ahorrado`
- `fecha` (Long)
- `fotos` (String, comma-separated URIs)

**puntos_gps**
- `id` (PK, autoincrement)
- `rutaId` (FK → rutas, cascade delete)
- `latitud`, `longitud`
- `timestamp` (Long)

**comentarios**
- `id` (PK, autoincrement)
- `rutaId` (FK → rutas)
- `autorId` (FK → usuarios)
- `texto`, `rating`
- `fecha` (Long)

### Flujo de Datos

```
Usuario → ViewModel → Repository → DAO → Room Database
                ↓
            UI State (Flow)
                ↓
        Compose Screens (recomposición automática)
```

---

## 🔒 Seguridad

- **Contraseñas**: Hash SHA-256 con salt aleatorio de 16 bytes
- **Sesiones**: Validación de expiración (7 días sin "recordarme")
- **Imágenes**: FileProvider para compartir URIs de forma segura
- **Permisos**: Solicitud dinámica en tiempo de ejecución
- **Base de datos**: Almacenamiento local encriptado (SQLite)

---

## 🎨 Capturas de Pantalla

_(Puedes agregar capturas aquí cuando tengas la app corriendo)_

| Login | Lista de Rutas | Detalle de Ruta |
|-------|----------------|-----------------|
| ![Login](screenshots/login.png) | ![Lista](screenshots/lista.png) | ![Detalle](screenshots/detalle.png) |

| Registro GPS | Perfil | Mapa |
|--------------|--------|------|
| ![Registro](screenshots/registro.png) | ![Perfil](screenshots/perfil.png) | ![Mapa](screenshots/mapa.png) |

---

## 📝 Permisos Requeridos

```xml
<!-- Ubicación -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- Cámara y Galería -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />

<!-- Notificaciones -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<!-- Internet (para mapas OSM) -->
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 🐛 Problemas Conocidos

- En Android 14+, se requiere permiso explícito de notificaciones
- OSMDroid requiere conexión a internet para cargar tiles del mapa
- En algunos dispositivos, el GPS puede tardar en inicializar

---

## 🛣️ Roadmap

### Versión 1.1 (Próximamente)
- [ ] Exportar rutas a GPX/KML
- [ ] Compartir rutas en redes sociales
- [ ] Desafíos y logros personalizados
- [ ] Modo oscuro completo

### Versión 1.2
- [ ] Sincronización en la nube (Firebase)
- [ ] Rutas compartidas por la comunidad
- [ ] Tabla de clasificación (leaderboard)
- [ ] Integración con wearables

### Versión 2.0
- [ ] Planificación de rutas antes de salir
- [ ] Predicción de calorías y CO₂
- [ ] Análisis de terreno y elevación
- [ ] Widget para pantalla de inicio

---

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! Si quieres colaborar:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add: Amazing feature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

### Guía de Estilo
- Seguir convenciones de Kotlin
- Usar Jetpack Compose para UI
- Documentar funciones públicas con KDoc
- Escribir tests para lógica de negocio

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

---

## 👤 Autor

**alkbil**
- GitHub: [@alkbil](https://github.com/alkbil)
- Proyecto: [AppEcoRoute](https://github.com/alkbil/AppEcoRoute)

---

## 🙏 Agradecimientos

- **OpenStreetMap** - Mapas de código abierto
- **Material Design** - Diseño UI/UX
- **Android Jetpack** - Componentes modernos
- **Kotlin Community** - Lenguaje y soporte

---

## 📚 Documentación Adicional

- [Sistema de Autenticación](SISTEMA_AUTENTICACION.md) - Documentación detallada del sistema de login/registro
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [OSMDroid](https://github.com/osmdroid/osmdroid)

---

<div align="center">

**Hecho con ❤️ y ♻️ para un mundo más verde**

⭐ Si te gusta el proyecto, dale una estrella en GitHub!

</div>
