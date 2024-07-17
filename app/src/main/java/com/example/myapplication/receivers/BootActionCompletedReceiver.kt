package com.example.myapplication.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.myapplication.notifications.showNotification
import com.example.myapplication.utils.toDeltaTime
import com.example.myapplication.utils.toFormattedDateTime

class BootActionCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val sharedPreferences = context?.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
            val firstBoot = sharedPreferences?.getLong(FIRST_BOOT, -1)
            val secondBoot = sharedPreferences?.getLong(SECOND_BOOT, -1)

            when {
                firstBoot?.toInt() == -1 -> {
                    sharedPreferences.edit()
                        ?.putLong(FIRST_BOOT, System.currentTimeMillis())?.commit()
                    showNotification(
                        context,
                        "No boots detected"
                    )
                }

                firstBoot?.toInt() != -1 && secondBoot?.toInt() == -1 -> {
                    sharedPreferences.edit()
                        ?.putLong(SECOND_BOOT, System.currentTimeMillis())?.commit()
                    showNotification(
                        context,
                        "The boot was detected = ${firstBoot?.toFormattedDateTime()}"
                    )
                }

                else -> {
                    sharedPreferences?.edit()
                        ?.putLong(FIRST_BOOT, secondBoot ?: -1)
                        ?.putLong(SECOND_BOOT, System.currentTimeMillis())?.commit()
                    if (context != null) {
                        val delta = secondBoot!! - firstBoot!!
                        showNotification(
                            context,
                            "Last boots time delta = ${delta.toDeltaTime()}"
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val FIRST_BOOT = "first_boot"
        const val SECOND_BOOT = "second_boot"
        const val MY_PREF = "myPref"
    }
}