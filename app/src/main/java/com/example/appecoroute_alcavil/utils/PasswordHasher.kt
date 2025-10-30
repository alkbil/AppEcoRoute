package com.example.appecoroute_alcavil.utils

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

object PasswordHasher {
    
    /**
     * Genera un hash seguro de la contraseña usando SHA-256 con salt
     */
    fun hashPassword(password: String): String {
        // Generar un salt aleatorio de 16 bytes
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        
        // Combinar salt + password
        val saltedPassword = salt + password.toByteArray()
        
        // Aplicar SHA-256
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(saltedPassword)
        
        // Combinar salt + hash y codificar en Base64
        val combined = salt + hash
        return Base64.getEncoder().encodeToString(combined)
    }
    
    /**
     * Verifica si una contraseña coincide con el hash almacenado
     */
    fun verifyPassword(password: String, storedHash: String): Boolean {
        return try {
            // Decodificar el hash almacenado
            val combined = Base64.getDecoder().decode(storedHash)
            
            // Extraer el salt (primeros 16 bytes)
            val salt = combined.sliceArray(0 until 16)
            val originalHash = combined.sliceArray(16 until combined.size)
            
            // Aplicar el mismo proceso con el salt original
            val saltedPassword = salt + password.toByteArray()
            val digest = MessageDigest.getInstance("SHA-256")
            val newHash = digest.digest(saltedPassword)
            
            // Comparar los hashes
            MessageDigest.isEqual(originalHash, newHash)
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Valida los requisitos de seguridad de una contraseña
     */
    fun validatePasswordStrength(password: String): ValidationResult {
        val errors = mutableListOf<String>()
        
        if (password.length < 6) {
            errors.add("La contraseña debe tener al menos 6 caracteres")
        }
        
        if (!password.any { it.isDigit() }) {
            errors.add("Debe contener al menos un número")
        }
        
        if (!password.any { it.isLetter() }) {
            errors.add("Debe contener al menos una letra")
        }
        
        return if (errors.isEmpty()) {
            ValidationResult.Success
        } else {
            ValidationResult.Error(errors)
        }
    }
    
    sealed class ValidationResult {
        object Success : ValidationResult()
        data class Error(val messages: List<String>) : ValidationResult()
    }
}
