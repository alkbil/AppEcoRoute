package com.example.appecoroute_alcavil.data.repository

import androidx.room.*
import com.example.appecoroute_alcavil.data.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE id = :userId")
    fun getUsuario(userId: String): Flow<Usuario?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: Usuario)

    @Update
    suspend fun updateUsuario(usuario: Usuario)
}