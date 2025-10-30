package com.example.appecoroute_alcavil.data.repository

import com.example.appecoroute_alcavil.data.model.Comentario
import com.example.appecoroute_alcavil.data.model.PuntoGPSEntity
import com.example.appecoroute_alcavil.data.model.Ruta
import com.example.appecoroute_alcavil.data.model.Usuario
import kotlinx.coroutines.flow.Flow

class EcoRouteRepository(
    private val rutaDao: RutaDao,
    private val comentarioDao: ComentarioDao,
    private val usuarioDao: UsuarioDao,
    private val puntoGPSDao: PuntoGPSDao
) {
    // Rutas
    fun getAllRutas(): Flow<List<Ruta>> = rutaDao.getAllRutas()
    
    fun getTopRutas(): Flow<List<Ruta>> = rutaDao.getTopRutas()
    
    suspend fun getRutaById(id: Long): Ruta? = rutaDao.getRutaById(id)
    
    suspend fun insertRuta(ruta: Ruta): Long = rutaDao.insertRuta(ruta)
    
    suspend fun updateRuta(ruta: Ruta) = rutaDao.updateRuta(ruta)
    
    suspend fun deleteRuta(ruta: Ruta) = rutaDao.deleteRuta(ruta)

    // Comentarios
    fun getComentariosPorRuta(rutaId: Long): Flow<List<Comentario>> = 
        comentarioDao.getComentariosPorRuta(rutaId)
    
    suspend fun insertComentario(comentario: Comentario) = 
        comentarioDao.insertComentario(comentario)
    
    suspend fun deleteComentario(comentario: Comentario) = 
        comentarioDao.deleteComentario(comentario)

    // Usuarios
    fun getUsuario(userId: String): Flow<Usuario?> = usuarioDao.getUsuario(userId)
    
    suspend fun insertUsuario(usuario: Usuario) = usuarioDao.insertUsuario(usuario)
    
    suspend fun updateUsuario(usuario: Usuario) = usuarioDao.updateUsuario(usuario)
    
    /**
     * Actualiza las estadísticas del usuario cuando completa una ruta
     */
    suspend fun actualizarEstadisticasUsuario(
        usuarioId: String,
        kmAdicionales: Double,
        caloriasAdicionales: Double,
        co2Adicional: Double
    ) {
        usuarioDao.actualizarEstadisticas(
            usuarioId = usuarioId,
            distancia = kmAdicionales,
            calorias = caloriasAdicionales,
            co2 = co2Adicional
        )
    }

    // Puntos GPS
    fun getPuntosPorRuta(rutaId: Long): Flow<List<PuntoGPSEntity>> = 
        puntoGPSDao.getPuntosPorRuta(rutaId)
    
    suspend fun insertPuntos(puntos: List<PuntoGPSEntity>) = 
        puntoGPSDao.insertPuntos(puntos)
}