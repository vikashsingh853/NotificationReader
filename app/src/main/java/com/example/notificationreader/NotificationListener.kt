package com.example.notificationreader

import android.app.Notification
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notificationreader.notification.NotificationModel
import com.example.notificationreader.notification.NotificationRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotificationListener : NotificationListenerService() {
    private lateinit var repository: NotificationRepo
    override fun onCreate() {
        super.onCreate()
        repository = NotificationRepo()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE,"")
        val text = extras.getCharSequence(Notification.EXTRA_TEXT,"").toString()

        //Find App Name from Package Name
        val pm = applicationContext.packageManager
        val ai: ApplicationInfo? = try {
            pm.getApplicationInfo(packageName, 0)
        } catch (e: NameNotFoundException) {
            null
        }
        val applicationName =
            (if (ai != null) pm.getApplicationLabel(ai) else "(unknown)") as String

        // Create a new notification
        val notificationData = NotificationModel(
            packageName = packageName,
            appName = applicationName,
            title = title,
            content = text,
        )

        if(notificationData.content.isNotEmpty() && notificationData.title.isNotEmpty()){
            // Insert the notification into the database using coroutines
            CoroutineScope(Dispatchers.Default
            ).launch {
                repository.insertNotification(notificationData)
            }

                Log.d("VIKASH",notificationData.packageName.toString().plus("kjsrbrge"))
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Handle removed notifications if necessary
    }
}

