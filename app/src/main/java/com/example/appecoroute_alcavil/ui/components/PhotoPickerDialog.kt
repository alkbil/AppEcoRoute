package com.example.appecoroute_alcavil.ui.components

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.appecoroute_alcavil.data.repository.ImageRepository
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhotoPickerDialog(
    onDismiss: () -> Unit,
    onPhotoSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val imageRepository = remember { ImageRepository(context) }
    
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    var shouldOpenCamera by remember { mutableStateOf(false) }
    
    // Permisos necesarios
    val cameraPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA
        )
    )
    
    // Launcher para la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            val savedPath = imageRepository.saveImage(tempCameraUri!!, "PERFIL")
            onPhotoSelected(savedPath)
            onDismiss()
        }
        shouldOpenCamera = false
        tempCameraUri = null
    }
    
    // Launcher para la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val savedPath = imageRepository.saveImage(it, "PERFIL")
            onPhotoSelected(savedPath)
            onDismiss()
        }
    }
    
    // Efecto para abrir la cámara cuando se conceden los permisos
    LaunchedEffect(cameraPermissionState.allPermissionsGranted, shouldOpenCamera) {
        if (cameraPermissionState.allPermissionsGranted && shouldOpenCamera) {
            tempCameraUri = imageRepository.createTempImageFile()
            tempCameraUri?.let { 
                cameraLauncher.launch(it)
            }
            shouldOpenCamera = false
        }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar foto") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Opción de cámara
                OutlinedButton(
                    onClick = {
                        if (cameraPermissionState.allPermissionsGranted) {
                            try {
                                tempCameraUri = imageRepository.createTempImageFile()
                                cameraLauncher.launch(tempCameraUri!!)
                            } catch (e: Exception) {
                                // Error al abrir la cámara
                            }
                        } else {
                            shouldOpenCamera = true
                            cameraPermissionState.launchMultiplePermissionRequest()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Tomar foto")
                }
                
                // Opción de galería
                OutlinedButton(
                    onClick = {
                        galleryLauncher.launch("image/*")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Elegir de galería")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
