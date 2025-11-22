package com.example.appecoroute_alcavil.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appecoroute_alcavil.data.api.WeatherInfo
import com.example.appecoroute_alcavil.data.repository.WeatherRepository
import kotlinx.coroutines.launch

/**
 * Componente para mostrar información del clima
 * Consume la API de OpenWeatherMap
 */
@Composable
fun WeatherCard(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier,
    onWeatherLoaded: ((WeatherInfo) -> Unit)? = null
) {
    val repository = remember { WeatherRepository() }
    val scope = rememberCoroutineScope()
    
    var weatherInfo by remember { mutableStateOf<WeatherInfo?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // Cargar clima al iniciar
    LaunchedEffect(latitude, longitude) {
        if (!repository.isApiConfigured()) {
            errorMessage = "⚠️ API no configurada"
            return@LaunchedEffect
        }
        
        isLoading = true
        errorMessage = null
        
        repository.getCurrentWeather(latitude, longitude)
            .onSuccess { weather ->
                weatherInfo = weather
                onWeatherLoaded?.invoke(weather)
                isLoading = false
            }
            .onFailure { error ->
                errorMessage = error.message ?: "Error desconocido"
                isLoading = false
            }
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Text("Cargando clima...")
                    }
                }
            }
            
            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🌤️ Clima",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = errorMessage!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            weatherInfo != null -> {
                WeatherContent(weatherInfo = weatherInfo!!)
            }
        }
    }
}

@Composable
private fun WeatherContent(weatherInfo: WeatherInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Encabezado con emoji y temperatura
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = weatherInfo.getWeatherEmoji(),
                    style = MaterialTheme.typography.headlineMedium
                )
                Column {
                    Text(
                        text = weatherInfo.getTemperatureFormatted(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = weatherInfo.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = weatherInfo.cityName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Información detallada
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherDetailItem(
                icon = { Icon(Icons.Default.Cloud, "Sensación") },
                label = weatherInfo.getFeelsLikeFormatted()
            )
            
            WeatherDetailItem(
                icon = { Icon(Icons.Default.Water, "Humedad") },
                label = weatherInfo.getHumidityFormatted()
            )
            
            WeatherDetailItem(
                icon = { Icon(Icons.Default.Air, "Viento") },
                label = weatherInfo.getWindSpeedFormatted()
            )
        }
    }
}

@Composable
private fun WeatherDetailItem(
    icon: @Composable () -> Unit,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
