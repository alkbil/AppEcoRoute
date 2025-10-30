package com.example.appecoroute_alcavil.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Sesión para mantener la sesión activa del usuario
 * Solo existirá una sesión a la vez (id = 1)
 */
@Entity(tableName = "sesiones")
data class Sesion(
    @PrimaryKey 
    val id: Int = 1, // Solo habrá una sesión a la vez
    
    val usuarioId: String, // ID del usuario logueado
    
    val recordarme: Boolean = false, // Si el usuario eligió "Recordarme"
    
    val ultimoAcceso: Long = System.currentTimeMillis() // Última vez que se abrió la app
) {
    /**
     * Verifica si la sesión sigue siendo válida
     * Una sesión sin "recordarme" expira después de 7 días
     */
    fun esValida(): Boolean {
        if (recordarme) return true
        
        val diasDesdeUltimoAcceso = (System.currentTimeMillis() - ultimoAcceso) / (1000 * 60 * 60 * 24)
        return diasDesdeUltimoAcceso < 7
    }
    
    /**
     * Actualiza el último acceso a la sesión
     */
    fun actualizarAcceso(): Sesion {
        return this.copy(ultimoAcceso = System.currentTimeMillis())
    }
}
