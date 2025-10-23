package com.example.appecoroute_alcavil.data.repository

import com.example.appecoroute_alcavil.data.model.Ruta
import com.example.appecoroute_alcavil.data.model.TipoRuta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class EcoRouteDatabaseInitializer(
    private val repository: EcoRouteRepository,
    private val scope: CoroutineScope
) {
    fun inicializarDatosEjemplo() {
        scope.launch {
            // Ruta de ejemplo 1
            repository.insertRuta(
                Ruta(
                    nombre = "Parque Central - Circuito Verde",
                    tipo = TipoRuta.CAMINATA,
                    distancia = 2.5,
                    calificacion = 4.5f,
                    descripcion = "Ruta ideal para caminatas matutinas, con abundante vegetación y aire limpio."
                )
            )

            // Ruta de ejemplo 2
            repository.insertRuta(
                Ruta(
                    nombre = "Ciclovía Río Este",
                    tipo = TipoRuta.BICICLETA,
                    distancia = 8.0,
                    calificacion = 4.0f,
                    descripcion = "Recorrido en bicicleta junto al río, perfecto para ejercicio cardiovascular."
                )
            )

            // Ruta de ejemplo 3
            repository.insertRuta(
                Ruta(
                    nombre = "Sendero del Bosque",
                    tipo = TipoRuta.TROTE,
                    distancia = 5.0,
                    calificacion = 5.0f,
                    descripcion = "Ruta de trote en terreno natural con estaciones de ejercicio."
                )
            )

            // Ruta de ejemplo 4
            repository.insertRuta(
                Ruta(
                    nombre = "Ruta Panorámica",
                    tipo = TipoRuta.CAMINATA,
                    distancia = 3.0,
                    calificacion = 4.8f,
                    descripcion = "Camino escénico con vistas espectaculares y puntos de descanso."
                )
            )

            // Ruta de ejemplo 5
            repository.insertRuta(
                Ruta(
                    nombre = "Circuito Mountain Bike",
                    tipo = TipoRuta.BICICLETA,
                    distancia = 12.0,
                    calificacion = 4.2f,
                    descripcion = "Ruta desafiante para ciclismo de montaña con diversos niveles de dificultad."
                )
            )
        }
    }
}