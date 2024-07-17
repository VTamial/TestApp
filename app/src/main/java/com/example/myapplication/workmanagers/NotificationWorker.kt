package com.example.myapplication.workmanagers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.notifications.showNotification

class NotificationWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        showNotification(context, "BOOT STUFF")
        return Result.success()
    }
}