package com.example.appecoroute_alcavil.repository

import com.example.appecoroute_alcavil.model.Route
import com.example.appecoroute_alcavil.model.RouteType
import kotlinx.coroutines.delay

class RouteRepository {
    private val sample = listOf(
        Route("1", "Parque Verde", RouteType.WALK, 2.3, 4.5f),
        Route("2", "Ciclovía Central", RouteType.BIKE, 5.0, 4.0f),
        Route("3", "Ruta Costera", RouteType.RUN, 3.7, 4.8f)
    )

    suspend fun getRoutes(): List<Route> {
        delay(500) // simula latencia
        return sample
    }
}
