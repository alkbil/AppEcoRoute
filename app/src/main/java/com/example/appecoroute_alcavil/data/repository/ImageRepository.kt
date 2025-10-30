package com.example.appecoroute_alcavil.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageRepository(private val context: Context) {
    
    private val imageDirectory: File by lazy {
        File(context.filesDir, "images").apply {
            if (!exists()) mkdirs()
        }
    }
    
    /**
     * Crea un archivo temporal para capturar foto con la cámara
     */
    fun createTempImageFile(): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFile = File(imageDirectory, "TEMP_${timestamp}.jpg")
        
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
    }
    
    /**
     * Guarda una imagen de forma permanente
     */
    fun saveImage(sourceUri: Uri, prefix: String = "IMG"): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val destinationFile = File(imageDirectory, "${prefix}_${timestamp}.jpg")
        
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            destinationFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        
        return destinationFile.absolutePath
    }
    
    /**
     * Elimina una imagen
     */
    fun deleteImage(imagePath: String): Boolean {
        return try {
            File(imagePath).delete()
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Obtiene el URI de una imagen guardada
     */
    fun getImageUri(imagePath: String): Uri {
        return Uri.fromFile(File(imagePath))
    }
    
    /**
     * Limpia archivos temporales antiguos (más de 1 día)
     */
    fun cleanTempFiles() {
        val oneDayAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000)
        imageDirectory.listFiles()?.forEach { file ->
            if (file.name.startsWith("TEMP_") && file.lastModified() < oneDayAgo) {
                file.delete()
            }
        }
    }
}
