package com.example.appecoroute_alcavil.data.repository

import androidx.room.*
import com.example.appecoroute_alcavil.data.model.Ruta
import kotlinx.coroutines.flow.Flow

@Dao
interface RutaDao {
    @Query("SELECT * FROM rutas")
    fun getAllRutas(): Flow<List<Ruta>>

    @Query("SELECT * FROM rutas WHERE id = :rutaId")
    suspend fun getRutaById(rutaId: Long): Ruta?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRuta(ruta: Ruta): Long

    @Update
    suspend fun updateRuta(ruta: Ruta)

    @Delete
    suspend fun deleteRuta(ruta: Ruta)

    @Query("SELECT * FROM rutas ORDER BY calificacion DESC LIMIT 5")
    fun getTopRutas(): Flow<List<Ruta>>
}