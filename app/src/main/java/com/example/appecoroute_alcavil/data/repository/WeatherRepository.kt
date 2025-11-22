package com.example.appecoroute_alcavil.data.repository

import android.util.Log
import com.example.appecoroute_alcavil.data.api.WeatherApi
import com.example.appecoroute_alcavil.data.api.WeatherInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Repositorio para gestionar las llamadas a la API de clima
 * 
 * IMPORTANTE: Para usar esta API necesitas:
 * 1. Registrarte en: https://openweathermap.org/api
 * 2. Obtener una API Key gratuita
 * 3. Reemplazar API_KEY con tu clave
 * 
 * La API gratuita permite:
 * - 60 llamadas por minuto
 * - 1,000,000 llamadas por mes
 */
class WeatherRepository {
    
    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        
        // API Key de OpenWeatherMap (activada y lista para usar)
        // Obtenida de: https://openweathermap.org/appid
        private const val API_KEY = "659cdca3262397b607ed41f74ede25fb"
        
        private const val TAG = "WeatherRepository"
    }
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val api = retrofit.create(WeatherApi::class.java)
    
    /**
     * Obtiene el clima actual para una ubicación específica
     * @param latitude Latitud de la ubicación
     * @param longitude Longitud de la ubicación
     * @return Result con WeatherInfo o error
     */
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): Result<WeatherInfo> {
        return try {
            Log.d(TAG, "Solicitando clima para: lat=$latitude, lon=$longitude")
            
            if (API_KEY == "TU_API_KEY_AQUI") {
                Log.w(TAG, "⚠️ API_KEY no configurada. Usa una clave real de OpenWeatherMap")
                return Result.failure(Exception("API Key no configurada. Obtén una en: https://openweathermap.org/appid"))
            }
            
            val response = api.getCurrentWeather(
                latitude = latitude,
                longitude = longitude,
                apiKey = API_KEY
            )
            
            if (response.isSuccessful && response.body() != null) {
                val weatherInfo = WeatherInfo.fromResponse(response.body()!!)
                Log.d(TAG, "Clima obtenido: ${weatherInfo.temperature}°C, ${weatherInfo.description}")
                Result.success(weatherInfo)
            } else {
                val errorMsg = "Error ${response.code()}: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener clima", e)
            Result.failure(e)
        }
    }
    
    /**
     * Verifica si la API está configurada correctamente
     */
    fun isApiConfigured(): Boolean {
        return API_KEY != "TU_API_KEY_AQUI" && API_KEY.isNotEmpty()
    }
}
