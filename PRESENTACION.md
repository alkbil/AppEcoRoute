# 🌿 AppEcoRoute - Presentación del Proyecto

## 📋 Índice de la Presentación
1. [Introducción](#1-introducción)
2. [Problemática](#2-problemática)
3. [Solución Propuesta](#3-solución-propuesta)
4. [Arquitectura del Sistema](#4-arquitectura-del-sistema)
5. [Funcionalidades Principales](#5-funcionalidades-principales)
6. [Demo en Vivo](#6-demo-en-vivo)
7. [Tecnologías Utilizadas](#7-tecnologías-utilizadas)
8. [Impacto Ambiental](#8-impacto-ambiental)
9. [Roadmap Futuro](#9-roadmap-futuro)
10. [Conclusiones](#10-conclusiones)

---

## 1. Introducción

### 🎯 ¿Qué es AppEcoRoute?
**AppEcoRoute** es una aplicación móvil Android que promueve la movilidad sostenible mediante el registro y seguimiento de rutas ecológicas (caminatas, ciclismo, transporte público).

### 👥 Audiencia Objetivo
- Personas comprometidas con el medio ambiente
- Usuarios que buscan alternativas de transporte sostenible
- Comunidades que desean reducir su huella de carbono

### 📊 Propuesta de Valor
- **Gamificación**: Métricas visuales de impacto ambiental
- **Conciencia Ecológica**: Cuantificación real de CO₂ ahorrado
- **Salud Personal**: Registro de calorías quemadas y distancia recorrida

---

## 2. Problemática

### 🚗 Situación Actual
- El transporte vehicular representa **24% de las emisiones globales de CO₂**
- Falta de herramientas para medir el impacto individual positivo
- Baja motivación para adoptar alternativas de transporte sostenible

### ❓ Preguntas Clave
- ¿Cómo motivar a las personas a usar transporte ecológico?
- ¿Cómo visualizar el impacto ambiental personal?
- ¿Cómo hacer el seguimiento accesible y sencillo?

---

## 3. Solución Propuesta

### ✨ AppEcoRoute Ofrece:

#### 📍 Registro GPS en Tiempo Real
- Tracking automático de rutas
- Mapas interactivos con OpenStreetMap
- Visualización de recorridos completados

#### 📊 Métricas Ambientales
- **CO₂ ahorrado** (comparado con vehículos tradicionales)
- **Calorías quemadas** (beneficio a la salud)
- **Distancia recorrida** (estadísticas de actividad)

#### 🔐 Sistema de Usuario
- Autenticación segura con hash SHA-256
- Perfil personalizado con foto
- Historial completo de rutas

#### 🔔 Motivación Continua
- Notificaciones personalizables
- Recordatorios para mantener el hábito
- Estadísticas acumulativas

---

## 4. Arquitectura del Sistema

### 🏗️ Patrón MVVM (Model-View-ViewModel)

```
┌─────────────────────────────────────────┐
│            UI Layer (Compose)           │
│  LoginScreen | MapScreen | ProfileScreen│
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│         ViewModel Layer                 │
│  AuthViewModel | RutasViewModel |       │
│  LocationViewModel                      │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│        Repository Layer                 │
│  AuthRepository | EcoRouteRepository    │
│  ImageRepository                        │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│         Data Layer (Room DB)            │
│  Usuario | Sesion | Ruta |              │
│  PuntoGPS | Comentario                  │
└─────────────────────────────────────────┘
```

### 🗄️ Base de Datos (Room)

**5 Tablas Principales:**
1. **usuarios** - Información de usuarios y estadísticas
2. **sesiones** - Gestión de autenticación persistente
3. **rutas** - Datos de rutas completadas
4. **puntos_gps** - Coordenadas de tracking
5. **comentarios** - Interacción social (futuro)

---

## 5. Funcionalidades Principales

### 🔐 1. Sistema de Autenticación

**Características:**
- Registro con validación de campos
- Login seguro con hash SHA-256
- Sesiones persistentes
- Cierre de sesión

**Seguridad:**
```kotlin
// Hash de contraseñas con SHA-256
PasswordHasher.hashPassword(password)
```

**Demo:**
- Mostrar pantalla de registro
- Crear cuenta de prueba
- Login exitoso

---

### 📸 2. Perfil de Usuario

**Características:**
- Foto de perfil personalizada
- Captura desde cámara o galería
- Estadísticas en tiempo real:
  - Total de rutas completadas
  - Kilómetros acumulados
  - CO₂ ahorrado
  - Calorías quemadas

**Tecnología:**
```kotlin
// FileProvider para compartir imágenes de forma segura
FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
```

**Demo:**
- Mostrar perfil
- Actualizar foto de perfil
- Explicar estadísticas

---

### 🗺️ 3. Registro de Rutas GPS

**Características:**
- Tracking en tiempo real
- Mapa interactivo con OSMDroid
- Tipos de ruta: Caminata, Bicicleta, Transporte Público
- Cálculo automático de métricas

**Flujo de Trabajo:**
1. Usuario inicia registro de ruta
2. GPS captura coordenadas cada segundo
3. Se calcula distancia y calorías en tiempo real
4. Usuario completa y guarda la ruta
5. Estadísticas se actualizan automáticamente

**Cálculos:**
```kotlin
// CO₂ ahorrado (kg) = distancia (km) × 0.12
val co2Ahorrado = distanciaKm * 0.12

// Calorías (según tipo de actividad)
val calorias = when(tipo) {
    CAMINATA -> distanciaKm * 65
    BICICLETA -> distanciaKm * 50
    TRANSPORTE_PUBLICO -> distanciaKm * 5
}
```

**Demo:**
- Iniciar registro de ruta
- Mostrar tracking en mapa
- Completar y guardar ruta
- Ver actualización de estadísticas

---

### 📱 4. Historial de Rutas

**Características:**
- Lista de todas las rutas completadas
- Filtros por tipo de ruta
- Detalles de cada ruta:
  - Fecha y hora
  - Distancia
  - Tipo de actividad
  - Métricas calculadas

**Demo:**
- Mostrar lista de rutas
- Abrir detalle de una ruta
- Explicar datos almacenados

---

### 🔔 5. Sistema de Notificaciones

**Características:**
- Recordatorios personalizables
- Hora configurable
- WorkManager para ejecución confiable
- Motivación para mantener el hábito

**Implementación:**
```kotlin
// WorkManager para notificaciones programadas
PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
```

**Demo:**
- Configurar recordatorio
- Explicar sistema de notificaciones

---

## 6. Demo en Vivo

### 🎬 Guion de Demostración (5-7 minutos)

#### Paso 1: Login/Registro (1 min)
1. Abrir la app
2. Crear cuenta nueva o usar cuenta existente
3. Explicar seguridad de contraseñas

#### Paso 2: Perfil (1 min)
1. Mostrar pantalla de perfil
2. Actualizar foto de perfil (cámara)
3. Explicar estadísticas actuales

#### Paso 3: Registrar Ruta (2-3 min)
1. Ir a "Registrar Ruta"
2. Iniciar tracking GPS
3. Caminar/simular movimiento
4. Mostrar mapa actualizándose en tiempo real
5. Completar ruta
6. Ver métricas calculadas

#### Paso 4: Historial (1 min)
1. Mostrar lista de rutas
2. Abrir detalle de una ruta
3. Explicar datos almacenados

#### Paso 5: Actualización de Estadísticas (1 min)
1. Volver al perfil
2. Mostrar estadísticas actualizadas
3. Explicar impacto acumulativo

---

## 7. Tecnologías Utilizadas

### 📱 Frontend
- **Jetpack Compose** - UI declarativa moderna
- **Material Design 3** - Diseño coherente
- **Navigation Compose** - Navegación fluida

### 🗄️ Base de Datos
- **Room Database** - Persistencia local
- **Kotlin Coroutines** - Operaciones asíncronas
- **Flow** - Programación reactiva

### 🗺️ Mapas y Localización
- **OSMDroid** - Mapas de OpenStreetMap
- **Google Location Services** - GPS de alta precisión
- **Accompanist Permissions** - Gestión de permisos

### 🔒 Seguridad
- **SHA-256** - Hash de contraseñas
- **FileProvider** - Compartir archivos seguro
- **Room Encryption** (futuro)

### 🔔 Notificaciones
- **WorkManager** - Tareas programadas
- **NotificationManager** - Notificaciones del sistema

### 🏗️ Arquitectura
- **MVVM** - Separación de responsabilidades
- **Repository Pattern** - Abstracción de datos
- **Dependency Injection** - ViewModels

---

## 8. Impacto Ambiental

### 🌍 Cálculos de Impacto

#### Emisiones de CO₂ por Transporte
| Tipo de Transporte | kg CO₂/km |
|-------------------|-----------|
| Auto gasolina     | 0.12      |
| Auto diésel       | 0.10      |
| Moto              | 0.08      |
| **Bicicleta**     | **0.00**  |
| **Caminata**      | **0.00**  |

#### Ejemplo Real
Si un usuario reemplaza **10 km diarios** en auto por bicicleta:
- **CO₂ ahorrado por día**: 1.2 kg
- **CO₂ ahorrado por mes**: 36 kg
- **CO₂ ahorrado por año**: 438 kg
- **Equivalente**: Plantar ~20 árboles

### 💪 Beneficios a la Salud
- Caminar 10 km = ~650 calorías quemadas
- Ciclismo 10 km = ~500 calorías quemadas
- Mejora cardiovascular
- Reducción de estrés

---

## 9. Roadmap Futuro

### 🚀 Fase 2 - Social (Próximos 3 meses)
- [ ] Compartir rutas con amigos
- [ ] Comentarios y likes en rutas
- [ ] Tabla de clasificación (leaderboard)
- [ ] Desafíos comunitarios

### 🎯 Fase 3 - Gamificación (6 meses)
- [ ] Sistema de logros y badges
- [ ] Niveles de usuario
- [ ] Recompensas por hitos
- [ ] Competencias mensuales

### 🌐 Fase 4 - Expansión (12 meses)
- [ ] Backend en la nube (Firebase/AWS)
- [ ] Sincronización multi-dispositivo
- [ ] Versión iOS
- [ ] Panel web de estadísticas

### 🤖 Fase 5 - Inteligencia (18 meses)
- [ ] Recomendaciones de rutas con ML
- [ ] Predicción de impacto ambiental
- [ ] Análisis de patrones de movilidad
- [ ] Integración con apps de transporte público

---

## 10. Conclusiones

### ✅ Logros Alcanzados
1. ✅ Sistema completo de autenticación segura
2. ✅ Tracking GPS preciso en tiempo real
3. ✅ Cálculo automático de impacto ambiental
4. ✅ Base de datos robusta con Room
5. ✅ UI moderna con Jetpack Compose
6. ✅ Sistema de notificaciones funcional

### 🎯 Valor Agregado
- **Conciencia Ambiental**: Visualización tangible del impacto
- **Motivación Personal**: Gamificación de hábitos sostenibles
- **Salud y Bienestar**: Tracking de actividad física
- **Escalabilidad**: Arquitectura preparada para crecimiento

### 🌱 Impacto Potencial
Si **1,000 usuarios** usan la app y ahorran **5 km/día** cada uno:
- **CO₂ evitado**: 219 toneladas/año
- **Equivalente**: ~10,000 árboles plantados

### 💡 Mensaje Final
> "AppEcoRoute no solo registra rutas, **crea consciencia y motiva cambios reales** hacia un futuro más sostenible."

---

## 📊 Datos de Desarrollo

### 📈 Estadísticas del Proyecto
- **Líneas de código**: ~5,000+
- **Archivos**: 50+
- **Tiempo de desarrollo**: 4 semanas
- **Commits en GitHub**: 2+
- **Tecnologías integradas**: 15+

### 👨‍💻 Equipo de Desarrollo
- **Desarrollador Principal**: [Tu Nombre]
- **Repositorio**: [github.com/alkbil/AppEcoRoute](https://github.com/alkbil/AppEcoRoute)

---

## 🙋 Preguntas Frecuentes (Preparadas)

### Q: ¿Cómo se calcula el CO₂ ahorrado?
**A:** Usamos el estándar de 0.12 kg CO₂/km de autos de gasolina. Comparamos contra transporte ecológico (0 emisiones).

### Q: ¿Funciona sin internet?
**A:** Sí, todos los datos se almacenan localmente en Room Database. Solo necesitas GPS activo.

### Q: ¿Los datos son seguros?
**A:** Las contraseñas se hashean con SHA-256. Los datos se almacenan en el dispositivo del usuario.

### Q: ¿Por qué Jetpack Compose?
**A:** Es el futuro de Android UI, más conciso que XML y totalmente declarativo.

### Q: ¿Planes de monetización?
**A:** Versión gratuita con features completas. Premium futuro con análisis avanzados y sincronización en la nube.

---

## 📝 Notas para la Presentación

### 🎤 Consejos de Oratoria
1. **Inicio fuerte**: Comienza con una estadística impactante sobre emisiones
2. **Demo fluida**: Practica el flujo de la demo antes
3. **Conecta con la audiencia**: Pregunta quién usa transporte público
4. **Muestra pasión**: Explica por qué te importa el medio ambiente
5. **Cierre inspirador**: Visión de impacto colectivo

### ⏱️ Distribución de Tiempo (Presentación 15 min)
- Introducción: 2 min
- Problemática: 2 min
- Solución y Arquitectura: 3 min
- **Demo en Vivo**: 5 min ⭐
- Tecnologías: 1 min
- Impacto y Futuro: 2 min
- Conclusiones: 1 min

### 🎯 Puntos Clave a Enfatizar
1. **Impacto Real**: No es solo una app, es una herramienta de cambio
2. **Tecnología Moderna**: Stack actualizado y escalable
3. **Arquitectura Sólida**: MVVM bien implementado
4. **Experiencia de Usuario**: UI intuitiva y fluida
5. **Visión a Futuro**: Roadmap claro y ambicioso

### 📱 Preparación Técnica
- [ ] Cargar batería del dispositivo al 100%
- [ ] Tener cuenta de prueba creada
- [ ] Algunas rutas ya registradas para mostrar historial
- [ ] GPS y permisos activados
- [ ] Modo No Molestar activado (para evitar notificaciones)
- [ ] Backup de APK en caso de problemas

---

## 🎨 Recursos Visuales Sugeridos

### Para Diapositivas
1. **Logo/Icono** de la app
2. **Screenshots** de pantallas principales
3. **Diagrama** de arquitectura MVVM
4. **Gráficos** de impacto ambiental
5. **Tabla** de tecnologías usadas
6. **Mockups** de features futuras

### Para la Demo
- Tener la app lista en el dispositivo
- Proyectar pantalla del móvil
- Backup de video de la demo (por si falla GPS)

---

## 🌟 Frase de Cierre

> "En un mundo donde cada acción cuenta, AppEcoRoute convierte tus pasos hacia un futuro sostenible en datos concretos que inspiran a seguir adelante. **El cambio comienza con un paso, un kilómetro, una ruta a la vez.**"

---

## 📚 Referencias y Enlaces

- **Repositorio GitHub**: https://github.com/alkbil/AppEcoRoute
- **Documentación Técnica**: Ver `SISTEMA_AUTENTICACION.md` y `README.md`
- **Datos de Emisiones**: EPA (Environmental Protection Agency)
- **OpenStreetMap**: https://www.openstreetmap.org
- **Jetpack Compose**: https://developer.android.com/jetpack/compose

---

**¡Buena suerte en tu presentación! 🚀🌿**
