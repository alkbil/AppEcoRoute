package com.example.appecoroute_alcavil.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "comentarios",
    foreignKeys = [
        ForeignKey(
            entity = Ruta::class,
            parentColumns = ["id"],
            childColumns = ["rutaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index("rutaId")]
)
data class Comentario(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val rutaId: Long,
    val usuarioId: String,
    val texto: String,
    val fecha: Date = Date()
)