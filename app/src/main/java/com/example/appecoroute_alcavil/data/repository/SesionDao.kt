package com.example.appecoroute_alcavil.data.repository

import androidx.room.*
import com.example.appecoroute_alcavil.data.model.Sesion
import com.example.appecoroute_alcavil.data.model.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones CRUD de Sesión
 */
@Dao
interface SesionDao {
    
    /**
     * Obtiene la sesión activa (solo puede haber una con id = 1)
     */
    @Query("SELECT * FROM sesiones WHERE id = 1")
    suspend fun getSesionActiva(): Sesion?
    
    /**
     * Obtiene el usuario de la sesión activa con Flow reactivo
     */
    @Query("""
        SELECT u.* FROM usuarios u
        INNER JOIN sesiones s ON u.id = s.usuarioId
        WHERE s.id = 1
    """)
    fun getUsuarioActivo(): Flow<Usuario?>
    
    /**
     * Inserta o actualiza la sesión activa
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSesion(sesion: Sesion)
    
    /**
     * Actualiza el último acceso de la sesión
     */
    @Query("UPDATE sesiones SET ultimoAcceso = :timestamp WHERE id = 1")
    suspend fun actualizarUltimoAcceso(timestamp: Long)
    
    /**
     * Elimina la sesión activa (cierre de sesión)
     */
    @Query("DELETE FROM sesiones WHERE id = 1")
    suspend fun eliminarSesion()
    
    /**
     * Verifica si existe una sesión activa
     */
    @Query("SELECT EXISTS(SELECT 1 FROM sesiones WHERE id = 1)")
    suspend fun existeSesion(): Boolean
}
