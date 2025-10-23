package com.example.appecoroute_alcavil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appecoroute_alcavil.data.model.Ruta
import com.example.appecoroute_alcavil.ui.components.ErrorMessage
import com.example.appecoroute_alcavil.ui.components.LoadingSpinner
import com.example.appecoroute_alcavil.ui.components.RatingBar
import com.example.appecoroute_alcavil.ui.viewmodels.RutasViewModel

@Composable
fun RutasListScreen(
    viewModel: RutasViewModel,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToRegistro: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.rutasUiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToRegistro,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Registrar Ruta") }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> LoadingSpinner()
            uiState.error != null -> ErrorMessage(
                message = uiState.error ?: "Error desconocido",
                onRetry = viewModel::cargarRutas
            )
            else -> RutasList(
                rutas = uiState.rutas,
                onRutaClick = onNavigateToDetail,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun RutasList(
    rutas: List<Ruta>,
    onRutaClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(rutas) { ruta ->
            RutaItem(
                ruta = ruta,
                onClick = { onRutaClick(ruta.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RutaItem(
    ruta: Ruta,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = ruta.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tipo: ${ruta.tipo.name}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Distancia: ${ruta.distancia} km",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            RatingBar(
                rating = ruta.calificacion,
                onRatingChanged = { /* No hacer nada aquí */ }
            )
        }
    }
}