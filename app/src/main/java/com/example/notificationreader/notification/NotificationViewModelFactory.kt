package com.example.notificationreader.notification

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotificationViewModelFactory(private val application: Application, private val repository: NotificationRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NotificationViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(application = application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}