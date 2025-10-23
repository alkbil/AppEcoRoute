package com.example.appecoroute_alcavil.ui.viewmodels

data class TrackingStats(
    val distanciaRecorrida: Double = 0.0,
    val tiempoTranscurrido: Long = 0L,
    val velocidadPromedio: Double = 0.0,
    val caloriasQuemadas: Double = 0.0,
    val co2Ahorrado: Double = 0.0
)