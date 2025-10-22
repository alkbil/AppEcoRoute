package com.example.appecoroute_alcavil

import androidx.lifecycle.LifecycleCoroutineScope
import com.example.appecoroute_alcavil.data.repository.EcoRouteRepository
import kotlinx.coroutines.launch

class EcoRouteDatabaseInitializer(
    private val repository: EcoRouteRepository,
    private val scope: LifecycleCoroutineScope
) {
    /**
     * Esta función se encarga de poblar la base de datos con datos de ejemplo
     * si es necesario.
     */
    fun inicializarDatosEjemplo() {
        // Usamos un coroutine scope para no bloquear el hilo principal
        scope.launch {
            // Aquí puedes añadir la lógica para insertar datos.
            // Es una buena práctica comprobar primero si la base de datos ya tiene datos
            // para no insertarlos cada vez que se abre la app.

            // Ejemplo (tendrás que crear estas funciones en tu repositorio):
            // if (repository.estaVacia()) {
            //     repository.insertarDatosDeEjemplo()
            // }
        }
    }
}

