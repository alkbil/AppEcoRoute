package com.example.appecoroute_alcavil.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "puntos_gps",
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
data class PuntoGPSEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val rutaId: Long,
    val latitud: Double,
    val longitud: Double,
    val timestamp: Long,
    val altitud: Double? = null,
    val precision: Float? = null
) {
    fun toPuntoGPS(): PuntoGPS = PuntoGPS(
        latitud = latitud,
        longitud = longitud,
        timestamp = timestamp,
        altitud = altitud,
        precision = precision
    )
}