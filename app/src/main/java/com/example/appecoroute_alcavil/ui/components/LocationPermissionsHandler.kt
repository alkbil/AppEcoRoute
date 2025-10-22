package com.example.appecoroute_alcavil.ui.components

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionsHandler(
    onPermissionsGranted: () -> Unit,
    content: @Composable () -> Unit
) {
    val permissions = remember {
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
    
    val permissionState = rememberMultiplePermissionsState(permissions = permissions)

    if (permissionState.allPermissionsGranted) {
        onPermissionsGranted()
        content()
    } else {
        PermissionRequestContent(onRequestPermissions = {
            permissionState.launchMultiplePermissionRequest()
        })
    }
}

@Composable
private fun PermissionRequestContent(onRequestPermissions: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Se requieren permisos de ubicación para registrar rutas",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(onClick = onRequestPermissions) {
                Text("Conceder Permisos")
            }
        }
    }
}