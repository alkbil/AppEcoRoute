package com.example.appecoroute_alcavil.data.repository

import com.example.appecoroute_alcavil.data.model.Sesion
import com.example.appecoroute_alcavil.data.model.Usuario
import com.example.appecoroute_alcavil.utils.PasswordHasher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AuthRepository(
    private val usuarioDao: UsuarioDao,
    private val sesionDao: SesionDao
) {
    
    /**
     * Registra un nuevo usuario
     */
    suspend fun registrarUsuario(
        nombre: String,
        email: String,
        password: String
    ): RegistroResult {
        // Validar contraseña
        when (val validation = PasswordHasher.validatePasswordStrength(password)) {
            is PasswordHasher.ValidationResult.Error -> {
                return RegistroResult.PasswordInvalida(validation.messages)
            }
            PasswordHasher.ValidationResult.Success -> { /* Continuar */ }
        }
        
        // Verificar si el email ya existe
        if (usuarioDao.existeEmail(email)) {
            return RegistroResult.EmailYaExiste
        }
        
        // Crear hash de la contraseña
        val passwordHash = PasswordHasher.hashPassword(password)
        
        // Crear usuario
        val usuario = Usuario(
            nombre = nombre,
            email = email,
            passwordHash = passwordHash,
            fechaRegistro = System.currentTimeMillis()
        )
        
        try {
            usuarioDao.insertUsuario(usuario)
            return RegistroResult.Exitoso(usuario)
        } catch (e: Exception) {
            return RegistroResult.Error(e.message ?: "Error desconocido")
        }
    }
    
    /**
     * Inicia sesión de usuario
     */
    suspend fun iniciarSesion(
        email: String,
        password: String,
        recordarme: Boolean = false
    ): LoginResult {
        // Buscar usuario por email
        val usuario = usuarioDao.getUsuarioByEmail(email)
            ?: return LoginResult.CredencialesInvalidas
        
        // Verificar contraseña
        if (!PasswordHasher.verifyPassword(password, usuario.passwordHash)) {
            return LoginResult.CredencialesInvalidas
        }
        
        // Crear sesión
        val sesion = Sesion(
            usuarioId = usuario.id,
            recordarme = recordarme,
            ultimoAcceso = System.currentTimeMillis()
        )
        
        try {
            // Eliminar sesión anterior si existe
            sesionDao.eliminarSesion()
            
            // Guardar nueva sesión
            sesionDao.insertSesion(sesion)
            
            return LoginResult.Exitoso(usuario)
        } catch (e: Exception) {
            return LoginResult.Error(e.message ?: "Error al crear sesión")
        }
    }
    
    /**
     * Obtiene el usuario actualmente en sesión
     */
    fun getUsuarioActual(): Flow<Usuario?> = sesionDao.getUsuarioActivo()
    
    /**
     * Verifica si hay una sesión activa válida
     */
    suspend fun haySesionActiva(): Boolean {
        val sesion = sesionDao.getSesionActiva()
        return sesion?.esValida() == true
    }
    
    /**
     * Actualiza el último acceso de la sesión
     */
    suspend fun actualizarUltimoAcceso() {
        if (sesionDao.existeSesion()) {
            sesionDao.actualizarUltimoAcceso(System.currentTimeMillis())
        }
    }
    
    /**
     * Cierra la sesión actual
     */
    suspend fun cerrarSesion(): Boolean {
        return try {
            sesionDao.eliminarSesion()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Actualiza la foto de perfil del usuario
     */
    suspend fun actualizarFotoPerfil(usuarioId: String, fotoUri: String): Boolean {
        return try {
            usuarioDao.updateFotoPerfil(usuarioId, fotoUri)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // Resultados sellados para type-safe handling
    sealed class RegistroResult {
        data class Exitoso(val usuario: Usuario) : RegistroResult()
        object EmailYaExiste : RegistroResult()
        data class PasswordInvalida(val errores: List<String>) : RegistroResult()
        data class Error(val mensaje: String) : RegistroResult()
    }
    
    sealed class LoginResult {
        data class Exitoso(val usuario: Usuario) : LoginResult()
        object CredencialesInvalidas : LoginResult()
        data class Error(val mensaje: String) : LoginResult()
    }
}
