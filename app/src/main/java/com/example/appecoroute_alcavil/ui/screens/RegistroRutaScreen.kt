package com.example.appecoroute_alcavil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.widget.Toast
import com.example.appecoroute_alcavil.ui.components.EcoRouteMap
import com.example.appecoroute_alcavil.ui.components.EcoRouteMapOSM
import com.example.appecoroute_alcavil.ui.components.GpsStatusHandler
import com.example.appecoroute_alcavil.ui.components.LocationPermissionsHandler
import com.example.appecoroute_alcavil.ui.viewmodel.AuthViewModel
import com.example.appecoroute_alcavil.ui.viewmodels.LocationViewModel
import com.example.appecoroute_alcavil.ui.viewmodels.RutasViewModel
import com.example.appecoroute_alcavil.utils.isGpsEnabled
import com.example.appecoroute_alcavil.data.model.TipoRuta
import org.osmdroid.util.GeoPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroRutaScreen(
    locationViewModel: LocationViewModel,
    viewModel: RutasViewModel,
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val locationState by locationViewModel.locationState.collectAsState()
    val usuarioActual by authViewModel.usuarioActual.collectAsState()
    var showGuardarDialog by remember { mutableStateOf(false) }
    var showTipoRutaDialog by remember { mutableStateOf(false) }
    var selectedTipoRuta by remember { mutableStateOf(TipoRuta.CAMINATA) }
    var nombreRuta by remember { mutableStateOf("") }
    var descripcionRuta by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Ruta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            if (locationState.isTracking) {
                FloatingActionButton(
                    onClick = { showTipoRutaDialog = true }
                ) {
                    Icon(Icons.Default.Save, contentDescription = "Guardar ruta")
                }
            }
        }
    ) { padding ->
        LocationPermissionsHandler(
            onPermissionsGranted = { /* Los permisos ya están concedidos */ }
        ) {
            GpsStatusHandler(
                isGpsEnabled = isGpsEnabled(context),
                onGpsEnabled = { /* El GPS ya está activado */ }
            ) {
                Box(
                    modifier = modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    // Mapa
                    EcoRouteMapOSM(
                        modifier = Modifier.fillMaxSize(),
                        initialPosition = locationState.currentLocation 
                            ?: GeoPoint(0.0, 0.0),
                        puntos = locationState.trackingPoints
                    )

                    // Panel de control
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Botón de tracking
                            Button(
                                onClick = {
                                    if (locationState.isTracking) {
                                        locationViewModel.detenerTracking()
                                    } else {
                                        locationViewModel.iniciarTracking()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    if (locationState.isTracking) 
                                        Icons.Default.Stop 
                                    else 
                                        Icons.Default.PlayArrow,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    if (locationState.isTracking) 
                                        "Detener Tracking" 
                                    else 
                                        "Iniciar Tracking"
                                )
                            }

                            // Estadísticas básicas
                            if (locationState.trackingPoints.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Puntos registrados: ${locationState.trackingPoints.size}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showTipoRutaDialog) {
            AlertDialog(
                onDismissRequest = { showTipoRutaDialog = false },
                title = { Text("Selecciona el tipo de actividad") },
                text = {
                    Column {
                        TipoRuta.values().forEach { tipo ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = tipo == selectedTipoRuta,
                                    onClick = { selectedTipoRuta = tipo }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    when (tipo) {
                                        TipoRuta.CAMINATA -> "Caminata"
                                        TipoRuta.BICICLETA -> "Bicicleta"
                                        TipoRuta.TROTE -> "Trote"
                                    }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { 
                            showTipoRutaDialog = false
                            showGuardarDialog = true
                        }
                    ) {
                        Text("Continuar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showTipoRutaDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (showGuardarDialog) {
            AlertDialog(
                onDismissRequest = { showGuardarDialog = false },
                title = { Text("Guardar Ruta") },
                text = { 
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Tipo de actividad: ${
                                when (selectedTipoRuta) {
                                    TipoRuta.CAMINATA -> "Caminata"
                                    TipoRuta.BICICLETA -> "Bicicleta"
                                    TipoRuta.TROTE -> "Trote"
                                }
                            }",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = nombreRuta,
                            onValueChange = { nombreRuta = it },
                            label = { Text("Nombre de la ruta") },
                            placeholder = { Text("Ej: Ruta al parque") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = descripcionRuta,
                            onValueChange = { descripcionRuta = it },
                            label = { Text("Descripción (opcional)") },
                            placeholder = { Text("Ej: Caminata matutina por el parque") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val stats = locationState.trackingStats
                        Text(
                            "Distancia: ${String.format("%.2f", stats.distanciaRecorrida)} km",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "Calorías: ${String.format("%.1f", stats.caloriasQuemadas)} cal",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showGuardarDialog = false
                            if (locationState.trackingPoints.size >= 2) {
                                if (nombreRuta.isBlank()) {
                                    Toast.makeText(
                                        context,
                                        "Por favor, ingresa un nombre para la ruta",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    showGuardarDialog = true
                                    return@Button
                                }
                                
                                val stats = locationState.trackingStats
                                val descripcionFinal = if (descripcionRuta.isNotBlank()) {
                                    descripcionRuta
                                } else {
                                    "Distancia: ${String.format("%.2f", stats.distanciaRecorrida)} km, " +
                                    "Calorías: ${String.format("%.1f", stats.caloriasQuemadas)} cal"
                                }
                                
                                viewModel.guardarRuta(
                                    nombre = nombreRuta,
                                    tipo = selectedTipoRuta,
                                    distancia = stats.distanciaRecorrida,
                                    descripcion = descripcionFinal,
                                    puntosGPS = locationState.trackingPoints,
                                    usuarioId = usuarioActual?.id
                                )
                                
                                // Limpiar campos para la próxima vez
                                nombreRuta = ""
                                descripcionRuta = ""
                                
                                locationViewModel.detenerTracking()
                                locationViewModel.limpiarRuta()
                                onNavigateBack()
                            } else {
                                Toast.makeText(
                                    context,
                                    "No hay suficientes puntos para guardar la ruta. Por favor, asegúrate de moverte con el GPS activo.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        enabled = nombreRuta.isNotBlank()
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    Button(onClick = { 
                        showGuardarDialog = false
                        nombreRuta = ""
                        descripcionRuta = ""
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}