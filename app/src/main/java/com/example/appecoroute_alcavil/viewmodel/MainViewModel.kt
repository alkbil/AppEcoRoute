package com.example.appecoroute_alcavil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appecoroute_alcavil.model.Route
import com.example.appecoroute_alcavil.repository.RouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repo: RouteRepository = RouteRepository()
) : ViewModel() {

    private val _routes = MutableStateFlow<List<Route>>(emptyList())
    val routes: StateFlow<List<Route>> = _routes

    init {
        loadRoutes()
    }

    private fun loadRoutes() {
        viewModelScope.launch {
            val data = repo.getRoutes()
            _routes.value = data
        }
    }
}
