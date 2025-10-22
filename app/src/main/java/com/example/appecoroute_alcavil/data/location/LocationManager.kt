package com.example.appecoroute_alcavil.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.example.appecoroute_alcavil.data.model.PuntoGPS
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LocationManager(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    suspend fun getUltimaUbicacion(): PuntoGPS? {
        return try {
            val location = fusedLocationClient.lastLocation.await()
            location?.let {
                PuntoGPS(
                    latitud = it.latitude,
                    longitud = it.longitude,
                    timestamp = it.time,
                    altitud = if (it.hasAltitude()) it.altitude else null,
                    precision = if (it.hasAccuracy()) it.accuracy else null
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    @SuppressLint("MissingPermission")
    fun locationUpdates(intervalMillis: Long = 5000): Flow<PuntoGPS> = callbackFlow {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, intervalMillis)
            .setMinUpdateDistanceMeters(10f)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(location.toPuntoGPS())
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            context.mainLooper
        ).addOnFailureListener { e ->
            close(e)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun Location.toPuntoGPS(): PuntoGPS = PuntoGPS(
        latitud = latitude,
        longitud = longitude,
        timestamp = time,
        altitud = if (hasAltitude()) altitude else null,
        precision = if (hasAccuracy()) accuracy else null
    )
}