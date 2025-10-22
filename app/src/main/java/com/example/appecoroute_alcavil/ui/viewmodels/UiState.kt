package com.example.appecoroute_alcavil.ui.viewmodels

import com.example.appecoroute_alcavil.data.model.Ruta

data class RutasUiState(
    val rutas: List<Ruta> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class RutaDetalleUiState(
    val ruta: Ruta? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class NuevaRutaUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val rutaGuardada: Boolean = false
)