package com.example.notificationreader

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.notificationreader.notification.NotificationRepo
import com.example.notificationreader.notification.NotificationViewModel
import com.example.notificationreader.notification.NotificationViewModelFactory
import com.example.notificationreader.ui.theme.NotificationReaderTheme

class MainActivity : ComponentActivity() {

    private val repository: NotificationRepo = NotificationRepo()

    private val mainViewModel: NotificationViewModel by viewModels<NotificationViewModel> {
        NotificationViewModelFactory(
            this.application,
            repository = repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotificationReaderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotificationApps(mainViewModel)
                }

            }
        }
    }

    @Composable
    fun RequestAccessScreen(mainViewModel: NotificationViewModel) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "We need access to your notifications to manage and search them effectively.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {

                val activityForResultLauncher =
                    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        mainViewModel.refreshNotificationPermission()
                    }
                activityForResultLauncher.launch(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }) {
                Text("Grant Access")
            }
        }
    }

    @Composable
    fun NotificationApps(mainViewModel: NotificationViewModel) {
        val isPermissionGranted by mainViewModel.isNotificationPermissionGranted.collectAsState()
        if (isPermissionGranted) {
            MainContent(mainViewModel)
        } else {
            RequestAccessScreen(mainViewModel)
        }
    }

    @Composable
    fun MainContent(mainViewModel: NotificationViewModel) {

        val playSound by mainViewModel.playSound.collectAsState()


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = if (playSound) "Sound is playing" else "Sound is not Playing")
        }

    }

}
