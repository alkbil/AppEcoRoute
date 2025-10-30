package com.example.appecoroute_alcavil.notifications

import android.content.Context
import androidx.work.*
import com.example.appecoroute_alcavil.data.repository.EcoRouteDatabase
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val database = EcoRouteDatabase.getDatabase(applicationContext)
            val sesionDao = database.sesionDao()
            
            // Obtener usuario actual
            val usuario = sesionDao.getUsuarioActivo().first()
            
            if (usuario != null && usuario.notificacionesActivas && usuario.recordatoriosActivos) {
                // Mostrar notificación de recordatorio
                val notificationHelper = NotificationHelper(applicationContext)
                notificationHelper.showActivityReminderNotification()
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
    
    companion object {
        private const val WORK_NAME = "ecoroute_reminder_work"
        
        /**
         * Programa los recordatorios diarios
         */
        fun scheduleReminder(context: Context, hour: Int, minute: Int) {
            val currentTime = java.util.Calendar.getInstance()
            val targetTime = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, hour)
                set(java.util.Calendar.MINUTE, minute)
                set(java.util.Calendar.SECOND, 0)
            }
            
            // Si la hora ya pasó hoy, programar para mañana
            if (targetTime.before(currentTime)) {
                targetTime.add(java.util.Calendar.DAY_OF_YEAR, 1)
            }
            
            val delay = targetTime.timeInMillis - currentTime.timeInMillis
            
            val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
                1, TimeUnit.DAYS
            )
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(WORK_NAME)
                .build()
            
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
        }
        
        /**
         * Cancela los recordatorios programados
         */
        fun cancelReminder(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
