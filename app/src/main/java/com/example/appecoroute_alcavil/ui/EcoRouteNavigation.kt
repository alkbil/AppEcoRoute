package com.example.appecoroute_alcavil.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import android.content.Context
import com.example.appecoroute_alcavil.data.location.LocationManager
import com.example.appecoroute_alcavil.ui.screens.*
import com.example.appecoroute_alcavil.ui.viewmodel.AuthViewModel
import com.example.appecoroute_alcavil.ui.viewmodels.LocationViewModel
import com.example.appecoroute_alcavil.ui.viewmodels.RutasViewModel

@Composable
fun EcoRouteNavigation(
    viewModel: RutasViewModel,
    locationViewModel: LocationViewModel,
    authViewModel: AuthViewModel,
    navController: NavHostController = rememberNavController()
) {
    val authState by authViewModel.authState.collectAsState()
    
    // Determinar la ruta inicial basándose en el estado de autenticación
    val startDestination = when (authState) {
        is AuthViewModel.AuthState.Autenticado -> Screen.RutasList.route
        else -> Screen.Login.route
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Pantallas de autenticación
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.RutasList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.RutasList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Pantalla de perfil
        composable(Screen.Profile.route) {
            PerfilScreen(
                authViewModel = authViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
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
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
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
                authViewModel = authViewModel,
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