package com.example.appecoroute_alcavil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appecoroute_alcavil.data.model.TipoRuta
import com.example.appecoroute_alcavil.ui.components.TipoRutaSelector
import com.example.appecoroute_alcavil.ui.viewmodels.RutasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRutaScreen(
    viewModel: RutasViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nombre by remember { mutableStateOf("") }
    var distancia by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tipoRuta by remember { mutableStateOf(TipoRuta.CAMINATA) }
    var showError by remember { mutableStateOf(false) }

    val uiState by viewModel.nuevaRutaUiState.collectAsState()

    LaunchedEffect(uiState.rutaGuardada) {
        if (uiState.rutaGuardada) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Ruta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la ruta") },
                isError = showError && nombre.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = distancia,
                onValueChange = { distancia = it },
                label = { Text("Distancia (km)") },
                isError = showError && distancia.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TipoRutaSelector(
                selectedTipo = tipoRuta,
                onTipoSelected = { tipoRuta = it },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            
            if (showError) {
                Text(
                    text = "Por favor completa todos los campos requeridos",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    if (nombre.isBlank() || distancia.isBlank()) {
                        showError = true
                        return@Button
                    }
                    
                    try {
                        val distanciaNum = distancia.toDouble()
                        viewModel.guardarRuta(
                            nombre = nombre,
                            tipo = tipoRuta,
                            distancia = distanciaNum,
                            descripcion = descripcion
                        )
                    } catch (e: NumberFormatException) {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Ruta")
            }
        }
    }
}