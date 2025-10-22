package com.example.appecoroute_alcavil.data.repository

import androidx.room.TypeConverter
import com.example.appecoroute_alcavil.data.model.TipoRuta
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTipoRuta(tipo: TipoRuta): String {
        return tipo.name
    }

    @TypeConverter
    fun toTipoRuta(tipo: String): TipoRuta {
        return TipoRuta.valueOf(tipo)
    }
}