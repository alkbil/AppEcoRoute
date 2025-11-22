package com.example.appecoroute_alcavil.data.api

import com.google.gson.annotations.SerializedName

/**
 * Modelo de respuesta de la API de OpenWeatherMap
 */
data class WeatherResponse(
    @SerializedName("coord")
    val coord: Coordinates,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("main")
    val main: MainWeather,
    @SerializedName("wind")
    val wind: Wind,
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("dt")
    val timestamp: Long,
    @SerializedName("sys")
    val sys: Sys,
    @SerializedName("timezone")
    val timezone: Int,
    @SerializedName("name")
    val cityName: String
)

data class Coordinates(
    @SerializedName("lon")
    val longitude: Double,
    @SerializedName("lat")
    val latitude: Double
)

data class Weather(
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
)

data class MainWeather(
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("humidity")
    val humidity: Int
)

data class Wind(
    @SerializedName("speed")
    val speed: Double,
    @SerializedName("deg")
    val direction: Int
)

data class Clouds(
    @SerializedName("all")
    val cloudiness: Int
)

data class Sys(
    @SerializedName("country")
    val country: String,
    @SerializedName("sunrise")
    val sunrise: Long,
    @SerializedName("sunset")
    val sunset: Long
)

/**
 * Modelo simplificado para UI
 */
data class WeatherInfo(
    val temperature: Double,
    val feelsLike: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val icon: String,
    val cityName: String
) {
    fun getTemperatureFormatted(): String = "${temperature.toInt()}°C"
    fun getFeelsLikeFormatted(): String = "Sensación: ${feelsLike.toInt()}°C"
    fun getHumidityFormatted(): String = "Humedad: $humidity%"
    fun getWindSpeedFormatted(): String = "Viento: ${windSpeed} m/s"
    
    fun getWeatherEmoji(): String = when {
        description.contains("despejado", ignoreCase = true) -> "☀️"
        description.contains("nubes", ignoreCase = true) -> "☁️"
        description.contains("lluvia", ignoreCase = true) -> "🌧️"
        description.contains("tormenta", ignoreCase = true) -> "⛈️"
        description.contains("nieve", ignoreCase = true) -> "❄️"
        description.contains("niebla", ignoreCase = true) -> "🌫️"
        else -> "🌤️"
    }
    
    companion object {
        fun fromResponse(response: WeatherResponse): WeatherInfo {
            return WeatherInfo(
                temperature = response.main.temperature,
                feelsLike = response.main.feelsLike,
                description = response.weather.firstOrNull()?.description?.capitalize() ?: "No disponible",
                humidity = response.main.humidity,
                windSpeed = response.wind.speed,
                icon = response.weather.firstOrNull()?.icon ?: "",
                cityName = response.cityName
            )
        }
    }
}
