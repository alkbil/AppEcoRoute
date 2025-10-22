package com.example.appecoroute_alcavil.ui.viewmodels

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appecoroute_alcavil.data.location.LocationManager
import com.example.appecoroute_alcavil.data.model.PuntoGPS
import com.example.appecoroute_alcavil.data.model.TipoRuta
import com.example.appecoroute_alcavil.ui.viewmodels.TrackingStats
import com.example.appecoroute_alcavil.utils.RouteUtils
import org.osmdroid.util.GeoPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class LocationState(
    val currentLocation: GeoPoint? = null,
    val isTracking: Boolean = false,
    val error: String? = null,
    val trackingPoints: List<GeoPoint> = emptyList(),
    val trackingStats: TrackingStats = TrackingStats(),
    val startTime: Long? = null,
    val tipoActividad: TipoRuta = TipoRuta.CAMINATA
)

class LocationViewModel(
    private val locationManager: LocationManager
) : ViewModel() {
    
    private val _locationState = MutableStateFlow(LocationState())
    val locationState: StateFlow<LocationState> = _locationState

    private var isTrackingEnabled = false

    init {
        obtenerUltimaUbicacion()
    }

    private fun obtenerUltimaUbicacion() {
        viewModelScope.launch {
            try {
                val ubicacion = locationManager.getUltimaUbicacion()
                ubicacion?.let {
                    _locationState.value = _locationState.value.copy(
                        currentLocation = GeoPoint(it.latitud, it.longitud)
                    )
                }
            } catch (e: Exception) {
                _locationState.value = _locationState.value.copy(
                    error = "No se pudo obtener la ubicación: ${e.message}"
                )
            }
        }
    }

    fun iniciarTracking() {
        if (isTrackingEnabled) return
        
        try {
            isTrackingEnabled = true
            _locationState.value = _locationState.value.copy(
                isTracking = true,
                startTime = System.currentTimeMillis(),
                trackingPoints = emptyList(),
                trackingStats = TrackingStats(),
                error = null
            )
            
            viewModelScope.launch {
            locationManager.locationUpdates()
                .catch { e ->
                    _locationState.value = _locationState.value.copy(
                        error = "Error en el tracking: ${e.message}",
                        isTracking = false
                    )
                    isTrackingEnabled = false
                }
                .collect { punto ->
                    val nuevoPunto = GeoPoint(punto.latitud, punto.longitud)
                    val nuevasPuntos = _locationState.value.trackingPoints + nuevoPunto
                    
                    // Calcular estadísticas actualizadas
                    val distanciaTotal = if (nuevasPuntos.size >= 2) {
                        RouteUtils.calcularDistanciaTotal(nuevasPuntos)
                    } else 0.0

                    val tiempoTranscurrido = (_locationState.value.startTime?.let { 
                        System.currentTimeMillis() - it 
                    } ?: 0L) / 1000.0 // Convertir a segundos

                    val velocidadPromedio = if (tiempoTranscurrido > 0) {
                        (distanciaTotal / tiempoTranscurrido) * 3.6 // Convertir a km/h
                    } else 0.0

                    val caloriasQuemadas = RouteUtils.calcularCalorias(
                        distanciaTotal,
                        _locationState.value.tipoActividad
                    )

                    val co2Ahorrado = RouteUtils.calcularCO2Evitado(distanciaTotal)

                    val nuevasStats = TrackingStats(
                        distanciaRecorrida = distanciaTotal,
                        tiempoTranscurrido = tiempoTranscurrido.toLong(),
                        velocidadPromedio = velocidadPromedio,
                        caloriasQuemadas = caloriasQuemadas,
                        co2Ahorrado = co2Ahorrado
                    )

                    _locationState.value = _locationState.value.copy(
                        currentLocation = nuevoPunto,
                        trackingPoints = nuevasPuntos,
                        trackingStats = nuevasStats
                    )
                }
            }
        } catch (e: Exception) {
            _locationState.value = _locationState.value.copy(
                error = "Error al iniciar el tracking: ${e.message}",
                isTracking = false
            )
            isTrackingEnabled = false
        }
    }

    fun detenerTracking() {
        isTrackingEnabled = false
        _locationState.value = _locationState.value.copy(
            isTracking = false,
            // Mantenemos las estadísticas y los puntos para mostrar el resumen
            startTime = null // Reseteamos el tiempo de inicio
        )
    }

    fun limpiarRuta() {
        _locationState.value = _locationState.value.copy(
            trackingPoints = emptyList(),
            trackingStats = TrackingStats(), // Reseteamos las estadísticas
            startTime = null // Reseteamos el tiempo de inicio
        )
    }

    fun startLocationUpdates(tipoActividad: TipoRuta) {
        _locationState.value = _locationState.value.copy(
            tipoActividad = tipoActividad
        )
        // Actualizar calorías con el nuevo tipo de actividad
        if (_locationState.value.trackingPoints.size >= 2) {
            val distanciaTotal = RouteUtils.calcularDistanciaTotal(_locationState.value.trackingPoints)
            val tiempoTranscurrido = (_locationState.value.startTime?.let { 
                (System.currentTimeMillis() - it) / 1000.0 
            } ?: 0.0).toInt()
            
            val caloriasQuemadas = RouteUtils.calcularCalorias(
                distanciaTotal,
                tipoActividad
            )

            _locationState.value = _locationState.value.copy(
                trackingStats = _locationState.value.trackingStats.copy(
                    caloriasQuemadas = caloriasQuemadas
                )
            )
        }
    }

    class Factory(private val locationManager: LocationManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
                return LocationViewModel(locationManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}