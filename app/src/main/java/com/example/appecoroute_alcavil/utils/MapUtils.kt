package com.example.appecoroute_alcavil.utils

import android.content.Context
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

object MapUtils {
    fun initializeOSMDroid(context: Context) {
        Configuration.getInstance().let { config ->
            config.load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
            config.userAgentValue = context.packageName
            config.tileFileSystemCacheMaxBytes = 100L * 1024 * 1024 // 100MB cache
            config.tileDownloadThreads = 4
            config.tileFileSystemThreads = 4
        }
    }

    fun configureMapView(mapView: MapView, context: Context) {
        mapView.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            isTilesScaledToDpi = true
            
            // Configurar overlay de ubicación
            val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), this)
            locationOverlay.enableMyLocation()
            overlays.add(locationOverlay)
        }
    }
}