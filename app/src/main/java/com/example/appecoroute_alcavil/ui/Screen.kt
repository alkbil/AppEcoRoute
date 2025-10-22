package com.example.appecoroute_alcavil.ui

sealed class Screen(val route: String) {
    object RutasList : Screen("rutas_list")
    object RutaDetail : Screen("ruta_detail/{rutaId}") {
        fun createRoute(rutaId: Long) = "ruta_detail/$rutaId"
    }
    object AddRuta : Screen("add_ruta")
    object RegistroRuta : Screen("registro_ruta")
    object Profile : Screen("profile")
}