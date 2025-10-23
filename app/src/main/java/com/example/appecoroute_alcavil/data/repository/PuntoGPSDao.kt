package com.example.appecoroute_alcavil.data.repository

import androidx.room.*
import com.example.appecoroute_alcavil.data.model.PuntoGPSEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PuntoGPSDao {
    @Query("SELECT * FROM puntos_gps WHERE rutaId = :rutaId ORDER BY timestamp ASC")
    fun getPuntosPorRuta(rutaId: Long): Flow<List<PuntoGPSEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPunto(punto: PuntoGPSEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPuntos(puntos: List<PuntoGPSEntity>)

    @Query("DELETE FROM puntos_gps WHERE rutaId = :rutaId")
    suspend fun deletePuntosPorRuta(rutaId: Long)
}