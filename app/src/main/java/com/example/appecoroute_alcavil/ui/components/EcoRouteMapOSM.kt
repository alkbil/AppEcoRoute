package com.example.appecoroute_alcavil.ui.components

import android.view.MotionEvent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import com.example.appecoroute_alcavil.utils.MapUtils

@Composable
fun EcoRouteMapOSM(
    modifier: Modifier = Modifier,
    initialPosition: GeoPoint = GeoPoint(0.0, 0.0),
    puntos: List<GeoPoint> = emptyList(),
    onMapClick: (GeoPoint) -> Unit = {},
    onMarkerClick: (GeoPoint) -> Unit = {}
) {
    val context = LocalContext.current
    var mapView: MapView? by remember { mutableStateOf(null) }

    DisposableEffect(Unit) {
        MapUtils.initializeOSMDroid(context)
        onDispose {
            mapView?.onDetach()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).also { map ->
                MapUtils.configureMapView(map, ctx)
                map.controller.setCenter(initialPosition)
                map.controller.setZoom(15.0)
                mapView = map
            }
        },
        update = { map ->
            // Limpiar overlays existentes excepto el de ubicación
            map.overlays.removeAll { it !is org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay }
            
            if (puntos.isNotEmpty()) {
                // Agregar marcadores
                Marker(map).apply {
                    position = puntos.first()
                    title = "Inicio"
                    snippet = "Punto de inicio de la ruta"
                    setOnMarkerClickListener { marker, _ ->
                        onMarkerClick(marker.position)
                        true
                    }
                    map.overlays.add(this)
                }

                Marker(map).apply {
                    position = puntos.last()
                    title = "Fin"
                    snippet = "Punto final de la ruta"
                    setOnMarkerClickListener { marker, _ ->
                        onMarkerClick(marker.position)
                        true
                    }
                    map.overlays.add(this)
                }

                // Agregar línea de la ruta
                Polyline().apply {
                    setPoints(puntos)
                    outlinePaint.color = android.graphics.Color.BLUE
                    outlinePaint.strokeWidth = 8f
                    map.overlays.add(this)
                }
            }

            // Manejar clicks en el mapa
            map.overlays.add(object : org.osmdroid.views.overlay.Overlay() {
                override fun onSingleTapConfirmed(e: MotionEvent, mapView: MapView): Boolean {
                    val projection = mapView.projection
                    val geoPoint = projection.fromPixels(e.x.toInt(), e.y.toInt())
                    onMapClick(GeoPoint(geoPoint.latitude, geoPoint.longitude))
                    return true
                }
            })

            map.invalidate()
        }
    )
}