package com.example.appecoroute_alcavil.data.model

import org.osmdroid.util.GeoPoint

data class PuntoGPS(
    val latitud: Double,
    val longitud: Double,
    val timestamp: Long,
    val altitud: Double? = null,
    val precision: Float? = null
) {
    fun toGeoPoint(): GeoPoint = GeoPoint(latitud, longitud)
    
    companion object {
        fun fromGeoPoint(geoPoint: GeoPoint, timestamp: Long = System.currentTimeMillis()): PuntoGPS {
            return PuntoGPS(
                latitud = geoPoint.latitude,
                longitud = geoPoint.longitude,
                timestamp = timestamp
            )
        }
    }
}