package com.example.appecoroute_alcavil.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Entidad Usuario para la base de datos Room
 * Representa un usuario registrado en la aplicación
 */
@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["email"], unique = true)] // Email único
)
data class Usuario(
    @PrimaryKey 
    val id: String = UUID.randomUUID().toString(),
    
    val nombre: String,
    
    val email: String,
    
    val passwordHash: String, // NUNCA guardar contraseña en texto plano
    
    val fotoPerfil: String? = null, // URI de la imagen de perfil
    
    val fechaRegistro: Long = System.currentTimeMillis(),
    
    // Estadísticas del usuario
    var totalKmRecorridos: Double = 0.0,
    
    var totalCaloriasQuemadas: Double = 0.0,
    
    var totalCO2Evitado: Double = 0.0,
    
    var cantidadRutasCompletadas: Int = 0,
    
    // Configuraciones
    var notificacionesActivas: Boolean = true,
    
    var recordatoriosActivos: Boolean = true,
    
    var horaRecordatorio: Int = 18, // 6:00 PM por defecto
    
    var minutoRecordatorio: Int = 0
) {
    /**
     * Actualiza las estadísticas del usuario al completar una ruta
     */
    fun actualizarEstadisticas(
        distanciaKm: Double,
        calorias: Double,
        co2: Double
    ): Usuario {
        return this.copy(
            totalKmRecorridos = totalKmRecorridos + distanciaKm,
            totalCaloriasQuemadas = totalCaloriasQuemadas + calorias,
            totalCO2Evitado = totalCO2Evitado + co2,
            cantidadRutasCompletadas = cantidadRutasCompletadas + 1
        )
    }
    
    /**
     * Obtiene las iniciales del nombre para mostrar en avatar
     */
    fun getIniciales(): String {
        return nombre.split(" ")
            .mapNotNull { it.firstOrNull()?.toString() }
            .take(2)
            .joinToString("")
            .uppercase()
    }
}