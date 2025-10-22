package com.example.appecoroute_alcavil.utils

import org.osmdroid.util.GeoPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import com.example.appecoroute_alcavil.data.model.TipoRuta

object RouteUtils {
    /**
     * Calcula la distancia en kilómetros entre dos puntos usando la fórmula de Haversine
     */
    fun calcularDistancia(punto1: GeoPoint, punto2: GeoPoint): Double {
        val radioTierra = 6371.0 // Radio de la Tierra en km

        val lat1 = Math.toRadians(punto1.latitude)
        val lat2 = Math.toRadians(punto2.latitude)
        val deltaLat = Math.toRadians(punto2.latitude - punto1.latitude)
        val deltaLon = Math.toRadians(punto2.longitude - punto1.longitude)

        val a = sin(deltaLat / 2).pow(2) +
                cos(lat1) * cos(lat2) *
                sin(deltaLon / 2).pow(2)
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return radioTierra * c
    }

    /**
     * Calcula la distancia total de una ruta en kilómetros
     */
    fun calcularDistanciaTotal(puntos: List<GeoPoint>): Double {
        if (puntos.size < 2) return 0.0
        
        return puntos.zipWithNext { punto1, punto2 ->
            calcularDistancia(punto1, punto2)
        }.sum()
    }

    /**
     * Calcula la velocidad promedio en km/h
     */
    fun calcularVelocidadPromedio(
        distanciaKm: Double,
        tiempoMinutos: Int
    ): Double {
        if (tiempoMinutos <= 0) return 0.0
        return (distanciaKm * 60) / tiempoMinutos
    }

    /**
     * Calcula las calorías quemadas según el tipo de actividad y distancia
     */
    fun calcularCalorias(
        distanciaKm: Double,
        tipoActividad: TipoRuta,
        pesoKg: Double = 70.0
    ): Double {
        val metValue = when (tipoActividad) {
            TipoRuta.CAMINATA -> 3.5  // Caminata moderada
            TipoRuta.TROTE -> 7.0     // Trote ligero
            TipoRuta.BICICLETA -> 5.0 // Ciclismo moderado
        }

        // Fórmula: MET * peso(kg) * tiempo(horas)
        val velocidadPromedio = when(tipoActividad) {
            TipoRuta.CAMINATA -> 5.0   // 5 km/h
            TipoRuta.TROTE -> 10.0     // 10 km/h
            TipoRuta.BICICLETA -> 15.0 // 15 km/h
        }
        val tiempoHoras = (distanciaKm / velocidadPromedio)
        return metValue * pesoKg * tiempoHoras
    }

    /**
     * Calcula el CO2 evitado en kg
     */
    fun calcularCO2Evitado(distanciaKm: Double): Double {
        // Promedio de emisiones de CO2 de un auto en kg/km
        val emisionesAutoKgPorKm = 0.2
        return distanciaKm * emisionesAutoKgPorKm
    }
}
