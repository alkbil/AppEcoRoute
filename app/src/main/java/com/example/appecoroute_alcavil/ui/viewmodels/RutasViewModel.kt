package com.example.appecoroute_alcavil.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appecoroute_alcavil.data.model.Ruta
import com.example.appecoroute_alcavil.data.model.TipoRuta
import com.example.appecoroute_alcavil.data.repository.EcoRouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RutasViewModel(private val repository: EcoRouteRepository) : ViewModel() {
    
    private val _rutasUiState = MutableStateFlow(RutasUiState(isLoading = true))
    val rutasUiState: StateFlow<RutasUiState> = _rutasUiState

    private val _rutaDetalleUiState = MutableStateFlow(RutaDetalleUiState(isLoading = true))
    val rutaDetalleUiState: StateFlow<RutaDetalleUiState> = _rutaDetalleUiState

    private val _nuevaRutaUiState = MutableStateFlow(NuevaRutaUiState())
    val nuevaRutaUiState: StateFlow<NuevaRutaUiState> = _nuevaRutaUiState

    init {
        cargarRutas()
    }

    fun cargarRutas() {
        viewModelScope.launch {
            _rutasUiState.value = RutasUiState(isLoading = true)
            repository.getAllRutas()
                .catch { e ->
                    _rutasUiState.value = RutasUiState(error = e.message)
                }
                .collect { rutas ->
                    _rutasUiState.value = RutasUiState(rutas = rutas)
                }
        }
    }

    fun cargarRuta(id: Long) {
        viewModelScope.launch {
            _rutaDetalleUiState.value = RutaDetalleUiState(isLoading = true)
            try {
                val ruta = repository.getRutaById(id)
                _rutaDetalleUiState.value = RutaDetalleUiState(ruta = ruta)
            } catch (e: Exception) {
                _rutaDetalleUiState.value = RutaDetalleUiState(error = e.message)
            }
        }
    }

    fun guardarRuta(
        nombre: String,
        tipo: TipoRuta,
        distancia: Double,
        descripcion: String = "",
        puntosGPS: List<org.osmdroid.util.GeoPoint> = emptyList(),
        usuarioId: String? = null
    ) {
        viewModelScope.launch {
            _nuevaRutaUiState.value = NuevaRutaUiState(isLoading = true)
            try {
                val ruta = Ruta(
                    nombre = nombre,
                    tipo = tipo,
                    distancia = distancia,
                    descripcion = descripcion,
                    creadorId = usuarioId ?: "default_user"
                )
                val rutaId = repository.insertRuta(ruta)
                
                // Guardar los puntos GPS si existen
                if (puntosGPS.isNotEmpty()) {
                    val puntosEntity = puntosGPS.map { punto ->
                        com.example.appecoroute_alcavil.data.model.PuntoGPSEntity(
                            rutaId = rutaId,
                            latitud = punto.latitude,
                            longitud = punto.longitude,
                            timestamp = System.currentTimeMillis()
                        )
                    }
                    repository.insertPuntos(puntosEntity)
                }
                
                // Actualizar estadísticas del usuario si tiene ID
                if (usuarioId != null) {
                    repository.actualizarEstadisticasUsuario(
                        usuarioId = usuarioId,
                        kmAdicionales = distancia,
                        caloriasAdicionales = ruta.calcularCaloriasQuemadas(),
                        co2Adicional = ruta.calcularCo2Ahorrado()
                    )
                }
                
                _nuevaRutaUiState.value = NuevaRutaUiState(rutaGuardada = true)
            } catch (e: Exception) {
                _nuevaRutaUiState.value = NuevaRutaUiState(error = e.message)
            }
        }
    }

    fun calificarRuta(ruta: Ruta, calificacion: Float) {
        viewModelScope.launch {
            try {
                val rutaActualizada = ruta.copy(calificacion = calificacion)
                repository.updateRuta(rutaActualizada)
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun eliminarRuta(ruta: Ruta) {
        viewModelScope.launch {
            try {
                repository.deleteRuta(ruta)
                cargarRutas() // Recargar la lista después de eliminar
            } catch (e: Exception) {
                _rutasUiState.value = _rutasUiState.value.copy(error = e.message)
            }
        }
    }

    fun getPuntosGPSDeRuta(rutaId: Long): Flow<List<org.osmdroid.util.GeoPoint>> {
        return repository.getPuntosPorRuta(rutaId)
            .catch { e ->
                // Manejar error silenciosamente o loguear
                emit(emptyList())
            }
            .map { puntosEntity ->
                puntosEntity.map { punto ->
                    org.osmdroid.util.GeoPoint(punto.latitud, punto.longitud)
                }
            }
    }

    class Factory(private val repository: EcoRouteRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RutasViewModel::class.java)) {
                return RutasViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}