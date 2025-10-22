package com.example.appecoroute_alcavil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appecoroute_alcavil.data.model.TipoRuta
import com.example.appecoroute_alcavil.ui.components.EcoRouteMapOSM
import com.example.appecoroute_alcavil.ui.viewmodels.RutasViewModel
import org.osmdroid.util.GeoPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutaDetailScreen(
    rutaId: Long,
    viewModel: RutasViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rutaDetalleState by viewModel.rutaDetalleUiState.collectAsState()
    val puntosGPS by viewModel.getPuntosGPSDeRuta(rutaId).collectAsState(initial = emptyList())
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(rutaId) {
        viewModel.cargarRuta(rutaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Ruta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (rutaDetalleState.ruta != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar ruta",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        when {
            rutaDetalleState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            rutaDetalleState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${rutaDetalleState.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            rutaDetalleState.ruta != null -> {
                val ruta = rutaDetalleState.ruta!!
                
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    // Mapa con la ruta
                    if (puntosGPS.isNotEmpty()) {
                        EcoRouteMapOSM(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            initialPosition = puntosGPS.firstOrNull() ?: GeoPoint(0.0, 0.0),
                            puntos = puntosGPS
                        )
                    }
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Nombre de la ruta
                        Text(
                            text = ruta.nombre,
                            style = MaterialTheme.typography.headlineMedium
                        )

                    // Tipo de ruta
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (ruta.tipo) {
                                    TipoRuta.CAMINATA -> Icons.Default.DirectionsWalk
                                    TipoRuta.BICICLETA -> Icons.Default.DirectionsBike
                                    TipoRuta.TROTE -> Icons.Default.DirectionsRun
                                },
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = when (ruta.tipo) {
                                    TipoRuta.CAMINATA -> "Caminata"
                                    TipoRuta.BICICLETA -> "Bicicleta"
                                    TipoRuta.TROTE -> "Trote"
                                },
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }

                    // Estadísticas
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Estadísticas",
                                style = MaterialTheme.typography.titleMedium
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Distancia",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = String.format("%.2f km", ruta.distancia),
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                                
                                Column {
                                    Text(
                                        text = "Calorías",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = String.format("%.0f cal", ruta.calcularCaloriasQuemadas()),
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "CO₂ Ahorrado",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = String.format("%.2f kg", ruta.calcularCo2Ahorrado()),
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                                
                                if (ruta.duracionMinutos > 0) {
                                    Column {
                                        Text(
                                            text = "Duración",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "${ruta.duracionMinutos} min",
                                            style = MaterialTheme.typography.headlineSmall
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Calificación
                    if (ruta.calificacion > 0) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = String.format("%.1f / 5.0", ruta.calificacion),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }

                    // Descripción
                    if (ruta.descripcion.isNotBlank()) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Descripción",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = ruta.descripcion,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    }
                }
            }
        }
        
        // Diálogo de confirmación para eliminar
        if (showDeleteDialog && rutaDetalleState.ruta != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar Ruta") },
                text = { Text("¿Estás seguro de que deseas eliminar la ruta '${rutaDetalleState.ruta!!.nombre}'? Esta acción no se puede deshacer.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.eliminarRuta(rutaDetalleState.ruta!!)
                            showDeleteDialog = false
                            onNavigateBack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
