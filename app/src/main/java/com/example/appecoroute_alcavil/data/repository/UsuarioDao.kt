package com.example.appecoroute_alcavil.data.repository

import androidx.room.*
import com.example.appecoroute_alcavil.data.model.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones CRUD de Usuario
 */
@Dao
interface UsuarioDao {
    
    /**
     * Obtiene todos los usuarios (para propósitos administrativos)
     */
    @Query("SELECT * FROM usuarios")
    fun getAllUsuarios(): Flow<List<Usuario>>
    
    /**
     * Obtiene un usuario por su ID con Flow (para observar cambios)
     */
    @Query("SELECT * FROM usuarios WHERE id = :userId")
    fun getUsuario(userId: String): Flow<Usuario?>
    
    /**
     * Obtiene un usuario por su ID (suspendable, sin Flow)
     */
    @Query("SELECT * FROM usuarios WHERE id = :usuarioId")
    suspend fun getUsuarioById(usuarioId: String): Usuario?
    
    /**
     * Obtiene un usuario por su email
     */
    @Query("SELECT * FROM usuarios WHERE email = :email")
    suspend fun getUsuarioByEmail(email: String): Usuario?
    
    /**
     * Verifica si existe un usuario con el email dado
     */
    @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE email = :email)")
    suspend fun existeEmail(email: String): Boolean
    
    /**
     * Inserta un nuevo usuario
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUsuario(usuario: Usuario): Long
    
    /**
     * Actualiza un usuario existente
     */
    @Update
    suspend fun updateUsuario(usuario: Usuario)
    
    /**
     * Elimina un usuario
     */
    @Delete
    suspend fun deleteUsuario(usuario: Usuario)
    
    /**
     * Actualiza solo la foto de perfil del usuario
     */
    @Query("UPDATE usuarios SET fotoPerfil = :uri WHERE id = :usuarioId")
    suspend fun updateFotoPerfil(usuarioId: String, uri: String?)
    
    /**
     * Actualiza las estadísticas del usuario
     */
    @Query("""
        UPDATE usuarios 
        SET totalKmRecorridos = totalKmRecorridos + :distancia,
            totalCaloriasQuemadas = totalCaloriasQuemadas + :calorias,
            totalCO2Evitado = totalCO2Evitado + :co2,
            cantidadRutasCompletadas = cantidadRutasCompletadas + 1
        WHERE id = :usuarioId
    """)
    suspend fun actualizarEstadisticas(
        usuarioId: String,
        distancia: Double,
        calorias: Double,
        co2: Double
    )
    
    /**
     * Actualiza las preferencias de notificaciones
     */
    @Query("""
        UPDATE usuarios 
        SET notificacionesActivas = :activas,
            recordatoriosActivos = :recordatorios,
            horaRecordatorio = :hora,
            minutoRecordatorio = :minuto
        WHERE id = :usuarioId
    """)
    suspend fun actualizarPreferenciasNotificaciones(
        usuarioId: String,
        activas: Boolean,
        recordatorios: Boolean,
        hora: Int,
        minuto: Int
    )
}