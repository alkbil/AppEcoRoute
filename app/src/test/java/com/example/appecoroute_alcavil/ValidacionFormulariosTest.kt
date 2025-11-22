package com.example.appecoroute_alcavil

import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para validación de formularios
 * 
 * Valida:
 * - Validación de emails
 * - Validación de contraseñas
 * - Validación de nombres
 */
class ValidacionFormulariosTest {

    @Test
    fun `email válido debe pasar validación`() {
        // Given: Emails válidos
        val emailsValidos = listOf(
            "usuario@example.com",
            "test.user@domain.co",
            "name+tag@mail.org",
            "user123@test-domain.com"
        )
        
        // When/Then: Todos deben ser válidos
        emailsValidos.forEach { email ->
            assertTrue(
                "Email '$email' debe ser válido",
                esEmailValido(email)
            )
        }
    }

    @Test
    fun `email inválido debe fallar validación`() {
        // Given: Emails inválidos
        val emailsInvalidos = listOf(
            "",                    // Vacío
            "usuario",             // Sin @
            "@domain.com",         // Sin nombre
            "usuario@",            // Sin dominio
            "usuario @domain.com", // Con espacio
            "usuario@@domain.com", // Doble @
            "usuario@domain"       // Sin TLD
        )
        
        // When/Then: Ninguno debe ser válido
        emailsInvalidos.forEach { email ->
            assertFalse(
                "Email '$email' debe ser inválido",
                esEmailValido(email)
            )
        }
    }

    @Test
    fun `contraseña válida debe cumplir requisitos mínimos`() {
        // Given: Contraseñas válidas (mínimo 8 caracteres)
        val contraseñasValidas = listOf(
            "12345678",
            "Password123",
            "MiContraseña!",
            "Abc12345"
        )
        
        // When/Then: Todas deben ser válidas
        contraseñasValidas.forEach { password ->
            assertTrue(
                "Contraseña '$password' debe ser válida",
                esContraseñaValida(password)
            )
        }
    }

    @Test
    fun `contraseña inválida debe fallar validación`() {
        // Given: Contraseñas inválidas
        val contraseñasInvalidas = listOf(
            "",          // Vacía
            "1234567",   // Menos de 8 caracteres
            "pass",      // Muy corta
            "       "    // Solo espacios
        )
        
        // When/Then: Ninguna debe ser válida
        contraseñasInvalidas.forEach { password ->
            assertFalse(
                "Contraseña '$password' debe ser inválida",
                esContraseñaValida(password)
            )
        }
    }

    @Test
    fun `nombre válido debe tener al menos 2 caracteres`() {
        // Given: Nombres válidos
        val nombresValidos = listOf(
            "Juan",
            "María José",
            "Pedro Pablo",
            "Li" // 2 caracteres mínimo
        )
        
        // When/Then: Todos deben ser válidos
        nombresValidos.forEach { nombre ->
            assertTrue(
                "Nombre '$nombre' debe ser válido",
                esNombreValido(nombre)
            )
        }
    }

    @Test
    fun `nombre inválido debe fallar validación`() {
        // Given: Nombres inválidos
        val nombresInvalidos = listOf(
            "",       // Vacío
            " ",      // Solo espacio
            "A",      // Un solo carácter
            "   "     // Espacios
        )
        
        // When/Then: Ninguno debe ser válido
        nombresInvalidos.forEach { nombre ->
            assertFalse(
                "Nombre '$nombre' debe ser inválido",
                esNombreValido(nombre)
            )
        }
    }

    @Test
    fun `validar distancia debe aceptar solo números positivos`() {
        // Given: Distancias de prueba
        assertTrue("5.5 es válido", esDistanciaValida("5.5"))
        assertTrue("10 es válido", esDistanciaValida("10"))
        assertTrue("0.5 es válido", esDistanciaValida("0.5"))
        
        assertFalse("-5 no es válido", esDistanciaValida("-5"))
        assertFalse("abc no es válido", esDistanciaValida("abc"))
        assertFalse("vacío no es válido", esDistanciaValida(""))
    }

    @Test
    fun `formato de email debe tener estructura correcta`() {
        // Given: Email de prueba
        val email = "test@example.com"
        
        // When: Se valida estructura
        val partes = email.split("@")
        
        // Then: Debe tener 2 partes
        assertEquals("Email debe tener 2 partes separadas por @", 2, partes.size)
        assertTrue("Primera parte no debe estar vacía", partes[0].isNotEmpty())
        assertTrue("Segunda parte debe contener punto", partes[1].contains("."))
    }

    // ============================================
    // Funciones de validación (simulan lógica de la app)
    // ============================================
    
    private fun esEmailValido(email: String): Boolean {
        if (email.isBlank()) return false
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return regex.matches(email)
    }

    private fun esContraseñaValida(password: String): Boolean {
        return password.trim().length >= 8
    }

    private fun esNombreValido(nombre: String): Boolean {
        return nombre.trim().length >= 2
    }

    private fun esDistanciaValida(distancia: String): Boolean {
        val numero = distancia.toDoubleOrNull() ?: return false
        return numero > 0
    }
}
