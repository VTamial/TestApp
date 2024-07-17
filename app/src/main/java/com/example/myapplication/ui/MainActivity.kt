package com.example.myapplication.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myapplication.R
import com.example.myapplication.notifications.showNotification
import com.example.myapplication.receivers.BootActionCompletedReceiver.Companion.FIRST_BOOT
import com.example.myapplication.receivers.BootActionCompletedReceiver.Companion.MY_PREF
import com.example.myapplication.receivers.BootActionCompletedReceiver.Companion.SECOND_BOOT
import com.example.myapplication.utils.toDeltaTime
import com.example.myapplication.utils.toFormattedDateTime
import com.example.myapplication.workmanagers.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        showInfo()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = java.util.concurrent.TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }

    private fun showInfo() {
        val sharedPref = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        val bootTime = sharedPref.getLong(FIRST_BOOT, -1)
        val bootTime2 = sharedPref.getLong(SECOND_BOOT, -1)

        when {
            bootTime.toInt() == -1 -> {
                findViewById<TextView>(R.id.bootInfo).text = bootTime.toFormattedDateTime()
                findViewById<TextView>(R.id.bootInfo2).visibility = TextView.GONE
            }

            bootTime.toInt() != -1 && bootTime2.toInt() == -1 -> {
                findViewById<TextView>(R.id.bootInfo).text = bootTime.toFormattedDateTime()
                findViewById<TextView>(R.id.bootInfo2).text = bootTime2.toFormattedDateTime()
            }

            else -> {
                findViewById<TextView>(R.id.bootInfo2).visibility = TextView.GONE
                findViewById<TextView>(R.id.bootInfo).text =
                    "Delta = ${(bootTime2 - bootTime).toDeltaTime()}"
            }
        }
    }

}