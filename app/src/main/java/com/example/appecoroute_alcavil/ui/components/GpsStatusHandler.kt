package com.example.appecoroute_alcavil.ui.components

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun GpsStatusHandler(
    isGpsEnabled: Boolean,
    onGpsEnabled: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (!isGpsEnabled) {
            GpsDisabledContent(onGpsEnabled = onGpsEnabled)
        } else {
            onGpsEnabled()
            content()
        }
    }
}

@Composable
private fun GpsDisabledContent(onGpsEnabled: () -> Unit) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "GPS desactivado",
            style = MaterialTheme.typography.bodyLarge
        )
        Button(
            onClick = {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
        ) {
            Text("Activar GPS")
        }
    }
}