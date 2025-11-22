package com.example.appecoroute_alcavil.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface de Retrofit para la API de OpenWeatherMap
 * Documentación: https://openweathermap.org/current
 */
interface WeatherApi {
    
    /**
     * Obtiene el clima actual para una ubicación específica
     * @param lat Latitud
     * @param lon Longitud
     * @param appid API Key de OpenWeatherMap
     * @param units Sistema de unidades (metric, imperial, standard)
     * @param lang Idioma de la respuesta (es para español)
     */
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "es"
    ): Response<WeatherResponse>
}
