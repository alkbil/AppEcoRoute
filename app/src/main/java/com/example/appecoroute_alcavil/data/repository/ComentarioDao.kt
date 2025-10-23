package com.example.appecoroute_alcavil.data.repository

import androidx.room.*
import com.example.appecoroute_alcavil.data.model.Comentario
import kotlinx.coroutines.flow.Flow

@Dao
interface ComentarioDao {
    @Query("SELECT * FROM comentarios WHERE rutaId = :rutaId")
    fun getComentariosPorRuta(rutaId: Long): Flow<List<Comentario>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComentario(comentario: Comentario)

    @Delete
    suspend fun deleteComentario(comentario: Comentario)
}