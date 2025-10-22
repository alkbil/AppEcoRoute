package com.example.appecoroute_alcavil.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun EcoRouteMap(
    modifier: Modifier = Modifier,
    initialPosition: LatLng = LatLng(0.0, 0.0),
    puntos: List<LatLng> = emptyList(),
    onMapClick: (LatLng) -> Unit = {},
    onMarkerClick: (LatLng) -> Unit = {}
) {
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = false
            )
        )
    }

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true,
                mapToolbarEnabled = true
            )
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 15f)
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState,
            onMapClick = onMapClick
        ) {
            // Dibujar puntos de la ruta si existen
            if (puntos.isNotEmpty()) {
                // Marcador de inicio
                Marker(
                    state = MarkerState(position = puntos.first()),
                    title = "Inicio",
                    snippet = "Punto de inicio de la ruta",
                    onClick = {
                        onMarkerClick(puntos.first())
                        true
                    }
                )

                // Marcador de fin
                Marker(
                    state = MarkerState(position = puntos.last()),
                    title = "Fin",
                    snippet = "Punto final de la ruta",
                    onClick = {
                        onMarkerClick(puntos.last())
                        true
                    }
                )

                // Línea de la ruta
                Polyline(
                    points = puntos,
                    color = androidx.compose.ui.graphics.Color.Blue.copy(alpha = 0.7f)
                )
            }
        }
    }
}