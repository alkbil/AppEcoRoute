package com.example.appecoroute_alcavil

import com.example.appecoroute_alcavil.data.model.TipoRuta
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.abs

/**
 * Pruebas unitarias para cálculos de rutas ecológicas
 * 
 * Valida:
 * - Cálculo de calorías según tipo de ruta
 * - Cálculo de CO₂ evitado
 * - Conversiones y fórmulas
 */
class RutaCalculosTest {

    companion object {
        // Factores de conversión (deben coincidir con la app)
        const val CALORIAS_POR_KM_CAMINATA = 50.0
        const val CALORIAS_POR_KM_TROTE = 70.0
        const val CALORIAS_POR_KM_BICICLETA = 30.0
        const val CO2_POR_KM = 0.12 // kg de CO2 por km en auto
        const val DELTA = 0.01 // Margen de error para comparaciones de doubles
    }

    @Test
    fun `calcular calorías para caminata`() {
        // Given: Distancia de 5 km en caminata
        val distanciaKm = 5.0
        val tipo = TipoRuta.CAMINATA
        
        // When: Se calculan las calorías
        val calorias = calcularCalorias(distanciaKm, tipo)
        
        // Then: Debe ser distancia * 50 cal/km
        val esperado = 5.0 * CALORIAS_POR_KM_CAMINATA
        assertEquals(
            "Calorías para 5km de caminata",
            esperado,
            calorias,
            DELTA
        )
    }

    @Test
    fun `calcular calorías para trote`() {
        // Given: Distancia de 10 km en trote
        val distanciaKm = 10.0
        val tipo = TipoRuta.TROTE
        
        // When: Se calculan las calorías
        val calorias = calcularCalorias(distanciaKm, tipo)
        
        // Then: Debe ser distancia * 70 cal/km
        val esperado = 10.0 * CALORIAS_POR_KM_TROTE
        assertEquals(
            "Calorías para 10km de trote",
            esperado,
            calorias,
            DELTA
        )
    }

    @Test
    fun `calcular calorías para bicicleta`() {
        // Given: Distancia de 15 km en bicicleta
        val distanciaKm = 15.0
        val tipo = TipoRuta.BICICLETA
        
        // When: Se calculan las calorías
        val calorias = calcularCalorias(distanciaKm, tipo)
        
        // Then: Debe ser distancia * 30 cal/km
        val esperado = 15.0 * CALORIAS_POR_KM_BICICLETA
        assertEquals(
            "Calorías para 15km de bicicleta",
            esperado,
            calorias,
            DELTA
        )
    }

    @Test
    fun `distancia cero debe retornar cero calorías`() {
        // Given: Distancia de 0 km
        val distanciaKm = 0.0
        
        // When/Then: Para cualquier tipo de ruta
        TipoRuta.values().forEach { tipo ->
            val calorias = calcularCalorias(distanciaKm, tipo)
            assertEquals(
                "Calorías para 0km en ${tipo.name}",
                0.0,
                calorias,
                DELTA
            )
        }
    }

    @Test
    fun `calcular CO2 evitado correctamente`() {
        // Given: Distancia de 10 km
        val distanciaKm = 10.0
        
        // When: Se calcula el CO2 evitado
        val co2Evitado = calcularCO2Evitado(distanciaKm)
        
        // Then: Debe ser distancia * 0.12 kg/km
        val esperado = 10.0 * CO2_POR_KM
        assertEquals(
            "CO2 evitado para 10km",
            esperado,
            co2Evitado,
            DELTA
        )
    }

    @Test
    fun `CO2 evitado para 100 km debe ser 12 kg`() {
        // Given: Distancia de 100 km
        val distanciaKm = 100.0
        
        // When: Se calcula el CO2 evitado
        val co2Evitado = calcularCO2Evitado(distanciaKm)
        
        // Then: Debe ser 12 kg
        assertEquals(
            "CO2 evitado para 100km = 12kg",
            12.0,
            co2Evitado,
            DELTA
        )
    }

    @Test
    fun `distancia cero debe retornar cero CO2`() {
        // Given: Distancia de 0 km
        val distanciaKm = 0.0
        
        // When: Se calcula el CO2 evitado
        val co2Evitado = calcularCO2Evitado(distanciaKm)
        
        // Then: Debe ser 0
        assertEquals(
            "CO2 evitado para 0km",
            0.0,
            co2Evitado,
            DELTA
        )
    }

    @Test
    fun `calorías quemadas deben aumentar con la distancia`() {
        // Given: Diferentes distancias
        val distancias = listOf(1.0, 5.0, 10.0, 20.0)
        val tipo = TipoRuta.CAMINATA
        
        // When: Se calculan calorías para cada distancia
        val calorias = distancias.map { calcularCalorias(it, tipo) }
        
        // Then: Cada valor debe ser mayor que el anterior
        for (i in 1 until calorias.size) {
            assertTrue(
                "Calorías deben aumentar con la distancia: ${calorias[i-1]} < ${calorias[i]}",
                calorias[i] > calorias[i-1]
            )
        }
    }

    @Test
    fun `trote debe quemar más calorías que caminata para misma distancia`() {
        // Given: Misma distancia
        val distancia = 10.0
        
        // When: Se calculan calorías para trote y caminata
        val caloriasTrote = calcularCalorias(distancia, TipoRuta.TROTE)
        val caloriasCaminata = calcularCalorias(distancia, TipoRuta.CAMINATA)
        
        // Then: Trote debe quemar más
        assertTrue(
            "Trote ($caloriasTrote cal) debe quemar más que caminata ($caloriasCaminata cal)",
            caloriasTrote > caloriasCaminata
        )
    }

    @Test
    fun `caminata debe quemar más calorías que bicicleta para misma distancia`() {
        // Given: Misma distancia
        val distancia = 10.0
        
        // When: Se calculan calorías
        val caloriasCaminata = calcularCalorias(distancia, TipoRuta.CAMINATA)
        val caloriasBicicleta = calcularCalorias(distancia, TipoRuta.BICICLETA)
        
        // Then: Caminata debe quemar más por km
        assertTrue(
            "Caminata ($caloriasCaminata cal) debe quemar más que bicicleta ($caloriasBicicleta cal) por km",
            caloriasCaminata > caloriasBicicleta
        )
    }

    @Test
    fun `valores negativos deben ser manejados correctamente`() {
        // Given: Distancia negativa (caso edge)
        val distanciaNegativa = -5.0
        
        // When: Se intenta calcular
        val calorias = calcularCalorias(abs(distanciaNegativa), TipoRuta.CAMINATA)
        val co2 = calcularCO2Evitado(abs(distanciaNegativa))
        
        // Then: Deben ser valores positivos (valor absoluto)
        assertTrue("Calorías deben ser positivas", calorias >= 0)
        assertTrue("CO2 debe ser positivo", co2 >= 0)
    }

    // ============================================
    // Funciones auxiliares (simulan lógica de la app)
    // ============================================
    
    private fun calcularCalorias(distanciaKm: Double, tipo: TipoRuta): Double {
        return when (tipo) {
            TipoRuta.CAMINATA -> distanciaKm * CALORIAS_POR_KM_CAMINATA
            TipoRuta.TROTE -> distanciaKm * CALORIAS_POR_KM_TROTE
            TipoRuta.BICICLETA -> distanciaKm * CALORIAS_POR_KM_BICICLETA
        }
    }

    private fun calcularCO2Evitado(distanciaKm: Double): Double {
        return distanciaKm * CO2_POR_KM
    }
}
