package com.example.notificationreader.notification

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotificationViewModel(private val application: Application, private val repository: NotificationRepo) : AndroidViewModel(application) {

    val notification = repository.notification

    val playSound:StateFlow<Boolean> = repository.playSound

    val currentTime: StateFlow<Long> = repository.currentTime
    val title: StateFlow<String> = repository.title

    private val _isNotificationPermissionGranted = MutableStateFlow(false)
    val isNotificationPermissionGranted = _isNotificationPermissionGranted.asStateFlow()

    fun refreshNotificationPermission() {
        val enabledListeners = NotificationManagerCompat.getEnabledListenerPackages(application)
        _isNotificationPermissionGranted.update {
            enabledListeners.contains(application.packageName)
        }
    }

    init {
        refreshNotificationPermission()
//        Log.d("VIKASH", repository.notification.value.toString().plus("model"))
         repository.startTimer()
    }
}
