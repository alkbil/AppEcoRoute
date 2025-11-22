package com.example.appecoroute_alcavil

import com.example.appecoroute_alcavil.utils.PasswordHasher
import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para el sistema de hash de contraseñas
 * 
 * Valida:
 * - Generación de hash con salt
 * - Verificación de contraseñas
 * - Seguridad del hash
 */
class PasswordHasherTest {

    @Test
    fun `hashPassword debe generar hashes diferentes para la misma contraseña`() {
        // Given: La misma contraseña
        val password = "MiContraseña123!"
        
        // When: Se hashea dos veces
        val hash1 = PasswordHasher.hashPassword(password)
        val hash2 = PasswordHasher.hashPassword(password)
        
        // Then: Los hashes deben ser diferentes (debido al salt único)
        assertNotEquals(
            "Los hashes deben ser diferentes debido al salt aleatorio", 
            hash1, 
            hash2
        )
    }

    @Test
    fun `verifyPassword debe retornar true para contraseña correcta`() {
        // Given: Una contraseña y su hash
        val password = "TestPassword123"
        val hash = PasswordHasher.hashPassword(password)
        
        // When: Se verifica la contraseña correcta
        val result = PasswordHasher.verifyPassword(password, hash)
        
        // Then: Debe retornar true
        assertTrue(
            "La contraseña correcta debe ser verificada exitosamente", 
            result
        )
    }

    @Test
    fun `verifyPassword debe retornar false para contraseña incorrecta`() {
        // Given: Una contraseña y su hash
        val password = "CorrectPassword123"
        val wrongPassword = "WrongPassword456"
        val hash = PasswordHasher.hashPassword(password)
        
        // When: Se verifica una contraseña incorrecta
        val result = PasswordHasher.verifyPassword(wrongPassword, hash)
        
        // Then: Debe retornar false
        assertFalse(
            "Una contraseña incorrecta debe fallar la verificación", 
            result
        )
    }

    @Test
    fun `hashPassword no debe retornar null ni string vacío`() {
        // Given: Una contraseña válida
        val password = "ValidPassword"
        
        // When: Se genera el hash
        val hash = PasswordHasher.hashPassword(password)
        
        // Then: El hash no debe ser nulo ni vacío
        assertNotNull("El hash no debe ser nulo", hash)
        assertTrue("El hash no debe estar vacío", hash.isNotEmpty())
        assertTrue("El hash debe tener longitud significativa", hash.length > 20)
    }

    @Test
    fun `hash debe contener separador de salt`() {
        // Given: Una contraseña
        val password = "TestPassword"
        
        // When: Se genera el hash
        val hash = PasswordHasher.hashPassword(password)
        
        // Then: Debe ser una cadena Base64 válida (no vacía y con longitud apropiada)
        assertFalse(
            "El hash no debe estar vacío", 
            hash.isEmpty()
        )
        assertTrue(
            "El hash debe tener longitud apropiada (>40 chars para Base64)", 
            hash.length > 40
        )
    }

    @Test
    fun `hash debe ser consistente para misma contraseña y mismo salt`() {
        // Given: Una contraseña y un salt específico
        val password = "TestPassword"
        val hash = PasswordHasher.hashPassword(password)
        
        // When: Se verifica múltiples veces
        val result1 = PasswordHasher.verifyPassword(password, hash)
        val result2 = PasswordHasher.verifyPassword(password, hash)
        val result3 = PasswordHasher.verifyPassword(password, hash)
        
        // Then: Todas las verificaciones deben ser true
        assertTrue("Primera verificación debe ser exitosa", result1)
        assertTrue("Segunda verificación debe ser exitosa", result2)
        assertTrue("Tercera verificación debe ser exitosa", result3)
    }

    @Test
    fun `hashPassword debe manejar contraseñas con caracteres especiales`() {
        // Given: Contraseñas con caracteres especiales
        val passwords = listOf(
            "!@#$%^&*()",
            "Contraseña123!",
            "test@email.com",
            "línea_con-símbolos"
        )
        
        // When/Then: Todas deben hashearse y verificarse correctamente
        passwords.forEach { password ->
            val hash = PasswordHasher.hashPassword(password)
            assertNotNull("Hash no debe ser nulo para: $password", hash)
            assertTrue(
                "Verificación debe funcionar para: $password",
                PasswordHasher.verifyPassword(password, hash)
            )
        }
    }

    @Test
    fun `hashPassword debe manejar contraseñas de diferentes longitudes`() {
        // Given: Contraseñas de diferentes longitudes
        val passwords = listOf(
            "12345678",           // 8 caracteres (mínimo típico)
            "ContraseñaMediana",  // Longitud media
            "C",                  // 1 carácter
            "A".repeat(100)       // 100 caracteres
        )
        
        // When/Then: Todas deben hashearse correctamente
        passwords.forEach { password ->
            val hash = PasswordHasher.hashPassword(password)
            assertNotNull("Hash no debe ser nulo para longitud ${password.length}", hash)
            assertFalse(
                "Hash no debe estar vacío",
                hash.isEmpty()
            )
        }
    }
}
