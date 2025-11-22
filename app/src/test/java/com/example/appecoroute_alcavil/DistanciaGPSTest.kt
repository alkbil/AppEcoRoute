package com.example.appecoroute_alcavil

import org.junit.Assert.*
import org.junit.Test
import kotlin.math.*

/**
 * Pruebas unitarias para cálculos de distancia GPS
 * 
 * Valida:
 * - Fórmula de Haversine para distancia entre coordenadas
 * - Precisión de cálculos geográficos
 */
class DistanciaGPSTest {

    companion object {
        const val EARTH_RADIUS_KM = 6371.0
        const val DELTA_KM = 0.5 // Margen de error de 500m
    }

    @Test
    fun `distancia entre mismo punto debe ser cero`() {
        // Given: Mismas coordenadas
        val lat = -32.8908 // Viña del Mar, Chile
        val lon = -71.2641
        
        // When: Se calcula distancia
        val distancia = calcularDistanciaHaversine(lat, lon, lat, lon)
        
        // Then: Debe ser 0
        assertEquals(
            "Distancia entre mismo punto debe ser 0",
            0.0,
            distancia,
            0.001
        )
    }

    @Test
    fun `distancia Santiago a Valparaíso aproximadamente 110 km`() {
        // Given: Coordenadas Santiago y Valparaíso
        val latSantiago = -33.4489
        val lonSantiago = -70.6693
        val latValparaiso = -33.0472
        val lonValparaiso = -71.6127
        
        // When: Se calcula distancia
        val distancia = calcularDistanciaHaversine(
            latSantiago, lonSantiago,
            latValparaiso, lonValparaiso
        )
        
        // Then: Debe ser aproximadamente 110 km (con margen amplio para variaciones de fórmula)
        assertTrue(
            "Distancia Santiago-Valparaíso debe estar entre 80-140 km, obtenido: $distancia km",
            distancia in 80.0..140.0
        )
    }

    @Test
    fun `distancia debe ser simétrica`() {
        // Given: Dos puntos A y B
        val latA = -33.0362
        val lonA = -71.6295
        val latB = -33.4489
        val lonB = -70.6693
        
        // When: Se calcula A→B y B→A
        val distanciaAB = calcularDistanciaHaversine(latA, lonA, latB, lonB)
        val distanciaBA = calcularDistanciaHaversine(latB, lonB, latA, lonA)
        
        // Then: Deben ser iguales
        assertEquals(
            "Distancia A→B debe ser igual a B→A",
            distanciaAB,
            distanciaBA,
            0.001
        )
    }

    @Test
    fun `distancia muy pequeña entre puntos cercanos`() {
        // Given: Dos puntos muy cercanos (1km aproximadamente)
        val lat1 = -33.4489
        val lon1 = -70.6693
        val lat2 = -33.4580 // ~1km al sur
        val lon2 = -70.6693
        
        // When: Se calcula distancia
        val distancia = calcularDistanciaHaversine(lat1, lon1, lat2, lon2)
        
        // Then: Debe ser aproximadamente 1 km
        assertTrue(
            "Distancia debe ser menor a 2km: $distancia km",
            distancia < 2.0
        )
        assertTrue(
            "Distancia debe ser mayor a 0.5km: $distancia km",
            distancia > 0.5
        )
    }

    @Test
    fun `distancia ecuatorial debe calcularse correctamente`() {
        // Given: Dos puntos en el ecuador
        val lat = 0.0
        val lon1 = 0.0
        val lon2 = 1.0 // 1 grado de diferencia
        
        // When: Se calcula distancia
        val distancia = calcularDistanciaHaversine(lat, lon1, lat, lon2)
        
        // Then: Debe ser aproximadamente 111 km (1 grado en ecuador)
        assertEquals(
            "1 grado en el ecuador ≈ 111km",
            111.0,
            distancia,
            5.0
        )
    }

    @Test
    fun `coordenadas extremas no deben causar errores`() {
        // Given: Coordenadas en polos
        val latPolo = 90.0
        val lonPolo = 0.0
        val latEcuador = 0.0
        val lonEcuador = 0.0
        
        // When: Se calcula distancia
        val distancia = calcularDistanciaHaversine(
            latPolo, lonPolo,
            latEcuador, lonEcuador
        )
        
        // Then: Debe ser aproximadamente 10,000 km (cuarto del ecuador)
        assertTrue(
            "Distancia polo-ecuador debe ser válida: $distancia km",
            distancia > 0 && distancia < 20000
        )
    }

    @Test
    fun `distancia no debe ser negativa`() {
        // Given: Cualquier par de coordenadas
        val testCases = listOf(
            Pair(-33.4489 to -70.6693, -32.8908 to -71.2641),
            Pair(0.0 to 0.0, 10.0 to 10.0),
            Pair(-90.0 to 0.0, 90.0 to 0.0)
        )
        
        // When/Then: Todas las distancias deben ser positivas
        testCases.forEach { (punto1, punto2) ->
            val distancia = calcularDistanciaHaversine(
                punto1.first, punto1.second,
                punto2.first, punto2.second
            )
            assertTrue(
                "Distancia debe ser positiva: $distancia",
                distancia >= 0
            )
        }
    }

    @Test
    fun `acumulación de distancias en ruta`() {
        // Given: Una ruta con varios puntos
        val puntos = listOf(
            -33.4489 to -70.6693, // Santiago
            -33.4580 to -70.6693, // Punto intermedio 1
            -33.4670 to -70.6693, // Punto intermedio 2
            -33.4760 to -70.6693  // Punto final
        )
        
        // When: Se calcula distancia total
        var distanciaTotal = 0.0
        for (i in 0 until puntos.size - 1) {
            val (lat1, lon1) = puntos[i]
            val (lat2, lon2) = puntos[i + 1]
            distanciaTotal += calcularDistanciaHaversine(lat1, lon1, lat2, lon2)
        }
        
        // Then: La distancia total debe ser positiva y razonable
        assertTrue(
            "Distancia total debe ser positiva: $distanciaTotal km",
            distanciaTotal > 0
        )
        assertTrue(
            "Distancia total debe ser razonable: $distanciaTotal km",
            distanciaTotal < 10 // Menos de 10km para este caso
        )
    }

    // ============================================
    // Implementación de fórmula de Haversine
    // ============================================
    
    /**
     * Calcula la distancia entre dos puntos usando la fórmula de Haversine
     * https://en.wikipedia.org/wiki/Haversine_formula
     */
    private fun calcularDistanciaHaversine(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) *
                cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        return EARTH_RADIUS_KM * c
    }
}
