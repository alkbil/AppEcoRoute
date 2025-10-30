package com.example.appecoroute_alcavil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appecoroute_alcavil.data.repository.AuthRepository
import com.example.appecoroute_alcavil.data.repository.EcoRouteDatabase
import com.example.appecoroute_alcavil.data.repository.EcoRouteRepository
import com.example.appecoroute_alcavil.ui.EcoRouteNavigation
import com.example.appecoroute_alcavil.ui.theme.AppEcoRouteTheme
import com.example.appecoroute_alcavil.ui.viewmodel.AuthViewModel
import com.example.appecoroute_alcavil.ui.viewmodels.RutasViewModel
import com.example.appecoroute_alcavil.utils.MapUtils
import com.example.appecoroute_alcavil.data.location.LocationManager
import com.example.appecoroute_alcavil.ui.viewmodels.LocationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar OSMDroid
        MapUtils.initializeOSMDroid(this)
        
        val database = EcoRouteDatabase.getDatabase(this)
        val repository = EcoRouteRepository(
            rutaDao = database.rutaDao(),
            comentarioDao = database.comentarioDao(),
            usuarioDao = database.usuarioDao(),
            puntoGPSDao = database.puntoGPSDao()
        )
        
        val authRepository = AuthRepository(
            usuarioDao = database.usuarioDao(),
            sesionDao = database.sesionDao()
        )
        
        val locationManager = LocationManager(this)
        
        // Inicializar datos de ejemplo
        EcoRouteDatabaseInitializer(repository, lifecycleScope).inicializarDatosEjemplo()
        
        setContent {
            AppEcoRouteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val rutasViewModel: RutasViewModel = viewModel(
                        factory = RutasViewModel.Factory(repository)
                    )
                    val locationViewModel: LocationViewModel = viewModel(
                        factory = LocationViewModel.Factory(locationManager)
                    )
                    val authViewModel: AuthViewModel = viewModel(
                        factory = AuthViewModel.Factory(authRepository)
                    )
                    
                    EcoRouteNavigation(
                        viewModel = rutasViewModel,
                        locationViewModel = locationViewModel,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}
