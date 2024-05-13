package com.example.notificationreader.notification

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationRepo {
    private val _notification = MutableStateFlow<NotificationModel?>(null)
    val notification: StateFlow<NotificationModel?> =_notification.asStateFlow()

    private val _currentTime = MutableStateFlow(0L)
    val currentTime: StateFlow<Long> = _currentTime

    private val _playSound = MutableStateFlow(false)
    val playSound: StateFlow<Boolean> = _playSound

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private var job: Job? = null

    fun startTimer() {
        job?.cancel() // Cancel the previous job if it's running
        job = CoroutineScope(Dispatchers.Default).launch {
            var time = 0L
            while (true) {
                delay(1000) // Update the timer every second
                time++
                _currentTime.value = time
                _title.value="vikash$time"
            }
        }
    }

    private fun setData(){
        _playSound.value=true
        Log.d("VIKASH","inside fun")

    }

     suspend fun insertNotification(notif: NotificationModel){
         CoroutineScope(Dispatchers.Default).launch {
             _notification.emit(notif)
             Log.d("VIKASH",notif.content)
             if(notif.content.contains("SMALL")){
                 Log.d("VIKASH",notif.content.plus("efsefse"))
                setData()
             }
//             delay(5000)
//             if(_playSound.value){
//                 _playSound.value=false
//             }
         }

    }
}