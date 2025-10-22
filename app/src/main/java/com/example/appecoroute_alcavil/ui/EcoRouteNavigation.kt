package com.example.appecoroute_alcavil.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import android.content.Context
import com.example.appecoroute_alcavil.data.location.LocationManager
import com.example.appecoroute_alcavil.ui.screens.AddRutaScreen
import com.example.appecoroute_alcavil.ui.screens.RegistroRutaScreen
import com.example.appecoroute_alcavil.ui.screens.RutaDetailScreen
import com.example.appecoroute_alcavil.ui.screens.RutasListScreen
import com.example.appecoroute_alcavil.ui.viewmodels.LocationViewModel
import com.example.appecoroute_alcavil.ui.viewmodels.RutasViewModel

@Composable
fun EcoRouteNavigation(
    viewModel: RutasViewModel,
    locationViewModel: LocationViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.RutasList.route
    ) {
        composable(Screen.RutasList.route) {
            RutasListScreen(
                viewModel = viewModel,
                onNavigateToDetail = { rutaId ->
                    navController.navigate(Screen.RutaDetail.createRoute(rutaId))
                },
                onNavigateToAdd = {
                    navController.navigate(Screen.AddRuta.route)
                },
                onNavigateToRegistro = {
                    navController.navigate(Screen.RegistroRuta.route)
                }
            )
        }
        
        composable(Screen.AddRuta.route) {
            AddRutaScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.RegistroRuta.route) {
            RegistroRutaScreen(
                locationViewModel = locationViewModel,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.RutaDetail.route,
            arguments = listOf(
                navArgument("rutaId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val rutaId = backStackEntry.arguments?.getLong("rutaId") ?: 0L
            RutaDetailScreen(
                rutaId = rutaId,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}