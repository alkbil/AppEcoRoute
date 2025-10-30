package com.example.appecoroute_alcavil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appecoroute_alcavil.data.repository.AuthRepository
import com.example.appecoroute_alcavil.data.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    // Estado de autenticación
    private val _authState = MutableStateFlow<AuthState>(AuthState.NoAutenticado)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    // Usuario actual
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()
    
    init {
        verificarSesionActiva()
    }
    
    /**
     * Verifica si hay una sesión activa al iniciar
     */
    private fun verificarSesionActiva() {
        viewModelScope.launch {
            authRepository.getUsuarioActual().collect { usuario ->
                _usuarioActual.value = usuario
                _authState.value = if (usuario != null) {
                    AuthState.Autenticado(usuario)
                } else {
                    AuthState.NoAutenticado
                }
            }
        }
    }
    
    /**
     * Inicia sesión
     */
    fun login(email: String, password: String, recordarme: Boolean = false) {
        // Validaciones básicas
        if (email.isBlank()) {
            _authState.value = AuthState.Error("El email no puede estar vacío")
            return
        }
        
        if (password.isBlank()) {
            _authState.value = AuthState.Error("La contraseña no puede estar vacía")
            return
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _authState.value = AuthState.Error("Email inválido")
            return
        }
        
        _authState.value = AuthState.Cargando
        
        viewModelScope.launch {
            when (val resultado = authRepository.iniciarSesion(email, password, recordarme)) {
                is AuthRepository.LoginResult.Exitoso -> {
                    _authState.value = AuthState.Autenticado(resultado.usuario)
                }
                AuthRepository.LoginResult.CredencialesInvalidas -> {
                    _authState.value = AuthState.Error("Email o contraseña incorrectos")
                }
                is AuthRepository.LoginResult.Error -> {
                    _authState.value = AuthState.Error(resultado.mensaje)
                }
            }
        }
    }
    
    /**
     * Registra un nuevo usuario
     */
    fun registrar(nombre: String, email: String, password: String, confirmarPassword: String) {
        // Validaciones
        if (nombre.isBlank()) {
            _authState.value = AuthState.Error("El nombre no puede estar vacío")
            return
        }
        
        if (email.isBlank()) {
            _authState.value = AuthState.Error("El email no puede estar vacío")
            return
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _authState.value = AuthState.Error("Email inválido")
            return
        }
        
        if (password.isBlank()) {
            _authState.value = AuthState.Error("La contraseña no puede estar vacía")
            return
        }
        
        if (password != confirmarPassword) {
            _authState.value = AuthState.Error("Las contraseñas no coinciden")
            return
        }
        
        _authState.value = AuthState.Cargando
        
        viewModelScope.launch {
            when (val resultado = authRepository.registrarUsuario(nombre, email, password)) {
                is AuthRepository.RegistroResult.Exitoso -> {
                    // Iniciar sesión automáticamente después del registro
                    login(email, password, recordarme = true)
                }
                AuthRepository.RegistroResult.EmailYaExiste -> {
                    _authState.value = AuthState.Error("Este email ya está registrado")
                }
                is AuthRepository.RegistroResult.PasswordInvalida -> {
                    _authState.value = AuthState.Error(resultado.errores.joinToString("\n"))
                }
                is AuthRepository.RegistroResult.Error -> {
                    _authState.value = AuthState.Error(resultado.mensaje)
                }
            }
        }
    }
    
    /**
     * Cierra la sesión actual
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.cerrarSesion()
            _authState.value = AuthState.NoAutenticado
            _usuarioActual.value = null
        }
    }
    
    /**
     * Limpia el estado de error
     */
    fun limpiarError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.NoAutenticado
        }
    }
    
    /**
     * Actualiza foto de perfil
     */
    fun actualizarFotoPerfil(fotoUri: String) {
        viewModelScope.launch {
            _usuarioActual.value?.let { usuario ->
                val exitoso = authRepository.actualizarFotoPerfil(usuario.id, fotoUri)
                if (exitoso) {
                    // El usuario se actualizará automáticamente por el Flow
                }
            }
        }
    }
    
    // Estados de autenticación
    sealed class AuthState {
        object NoAutenticado : AuthState()
        object Cargando : AuthState()
        data class Autenticado(val usuario: Usuario) : AuthState()
        data class Error(val mensaje: String) : AuthState()
    }
    
    // Factory para crear el ViewModel
    companion object {
        fun Factory(authRepository: AuthRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AuthViewModel(authRepository) as T
                }
            }
        }
    }
}
