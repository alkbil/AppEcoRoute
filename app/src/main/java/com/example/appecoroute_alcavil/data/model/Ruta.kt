package com.example.appecoroute_alcavil.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rutas")
data class Ruta(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val tipo: TipoRuta,
    val distancia: Double, // en kilómetros
    val calificacion: Float = 0f, // 1 a 5 estrellas
    val descripcion: String = "",
    val duracionMinutos: Int = 0,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val creadorId: String = "", // ID del usuario que creó la ruta
    val fotos: String = "", // URIs de fotos separadas por comas
    val caloriasPorKm: Double = when(tipo) {
        TipoRuta.CAMINATA -> 60.0
        TipoRuta.BICICLETA -> 40.0
        TipoRuta.TROTE -> 100.0
    },
    val co2AhorradoPorKm: Double = when(tipo) {
        TipoRuta.CAMINATA -> 0.2
        TipoRuta.BICICLETA -> 0.2
        TipoRuta.TROTE -> 0.2
    }
) {
    fun calcularCaloriasQuemadas(): Double = distancia * caloriasPorKm
    
    fun calcularCo2Ahorrado(): Double = distancia * co2AhorradoPorKm
    
    /**
     * Obtiene la lista de fotos de la ruta parseadas
     */
    fun obtenerListaFotos(): List<String> {
        return if (fotos.isNullOrBlank()) emptyList() 
        else fotos.split(",").filter { it.isNotBlank() }
    }
    
    /**
     * Agrega una nueva foto a la ruta
     */
    fun agregarFoto(uri: String): Ruta {
        val fotosActuales = obtenerListaFotos().toMutableList()
        fotosActuales.add(uri)
        return this.copy(fotos = fotosActuales.joinToString(","))
    }
}