package com.example.appecoroute_alcavil.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.appecoroute_alcavil.MainActivity
import com.example.appecoroute_alcavil.R

class NotificationHelper(private val context: Context) {
    
    companion object {
        const val CHANNEL_ID_GENERAL = "ecoroute_general"
        const val CHANNEL_ID_ACHIEVEMENTS = "ecoroute_achievements"
        const val CHANNEL_ID_REMINDERS = "ecoroute_reminders"
        
        const val NOTIFICATION_ID_WELCOME = 1
        const val NOTIFICATION_ID_ROUTE_COMPLETED = 2
        const val NOTIFICATION_ID_ACHIEVEMENT = 3
        const val NOTIFICATION_ID_REMINDER = 4
    }
    
    init {
        createNotificationChannels()
    }
    
    /**
     * Crea los canales de notificación
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_ID_GENERAL,
                    "General",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notificaciones generales de la aplicación"
                },
                NotificationChannel(
                    CHANNEL_ID_ACHIEVEMENTS,
                    "Logros",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notificaciones de logros y metas alcanzadas"
                },
                NotificationChannel(
                    CHANNEL_ID_REMINDERS,
                    "Recordatorios",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Recordatorios de actividad física"
                }
            )
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            channels.forEach { notificationManager.createNotificationChannel(it) }
        }
    }
    
    /**
     * Muestra notificación de bienvenida
     */
    fun showWelcomeNotification(userName: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_GENERAL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("¡Bienvenido a EcoRoute, $userName!")
            .setContentText("Comienza a registrar tus rutas ecológicas")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_WELCOME, notification)
    }
    
    /**
     * Muestra notificación de ruta completada
     */
    fun showRouteCompletedNotification(
        distanciaKm: Double,
        caloriasQuemadas: Double,
        co2Evitado: Double
    ) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_GENERAL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("¡Ruta completada!")
            .setContentText("${String.format("%.2f", distanciaKm)} km • ${String.format("%.0f", caloriasQuemadas)} kcal")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        "Distancia: ${String.format("%.2f", distanciaKm)} km\n" +
                        "Calorías: ${String.format("%.0f", caloriasQuemadas)} kcal\n" +
                        "CO2 evitado: ${String.format("%.2f", co2Evitado)} kg"
                    )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_ROUTE_COMPLETED, notification)
    }
    
    /**
     * Muestra notificación de logro
     */
    fun showAchievementNotification(title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_ACHIEVEMENTS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("🏆 $title")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_ACHIEVEMENT, notification)
    }
    
    /**
     * Muestra recordatorio de actividad
     */
    fun showActivityReminderNotification() {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_REMINDERS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("¿Listo para una nueva ruta?")
            .setContentText("Es momento de hacer ejercicio y ayudar al medio ambiente")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_REMINDER, notification)
    }
    
    /**
     * Verifica si tiene permisos de notificación
     */
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } else {
            true
        }
    }
}
