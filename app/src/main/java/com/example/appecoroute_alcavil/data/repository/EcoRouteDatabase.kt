package com.example.appecoroute_alcavil.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appecoroute_alcavil.data.model.Comentario
import com.example.appecoroute_alcavil.data.model.PuntoGPSEntity
import com.example.appecoroute_alcavil.data.model.Ruta
import com.example.appecoroute_alcavil.data.model.Usuario

@Database(
    entities = [Ruta::class, Comentario::class, Usuario::class, PuntoGPSEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EcoRouteDatabase : RoomDatabase() {
    abstract fun rutaDao(): RutaDao
    abstract fun comentarioDao(): ComentarioDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun puntoGPSDao(): PuntoGPSDao

    companion object {
        @Volatile
        private var INSTANCE: EcoRouteDatabase? = null

        fun getDatabase(context: Context): EcoRouteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EcoRouteDatabase::class.java,
                    "ecoroute_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}