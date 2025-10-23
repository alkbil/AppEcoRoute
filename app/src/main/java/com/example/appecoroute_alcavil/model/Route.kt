package com.example.appecoroute_alcavil.model

data class Route(
    val id: String,
    val name: String,
    val type: RouteType,
    val distanceKm: Double,
    val rating: Float = 0f
)

enum class RouteType { WALK, BIKE, RUN }
