package com.example.appecoroute_alcavil.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val email: String,
    val kilometrosTotales: Double = 0.0,
    val caloriasQuemadas: Double = 0.0,
    val co2Ahorrado: Double = 0.0
)