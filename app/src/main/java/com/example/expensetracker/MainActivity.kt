package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.expensetracker.core.theme.ExpenseTrackerTheme
import com.example.expensetracker.presentation.ui.ExpenseTrackerApp
import dagger.hilt.android.AndroidEntryPoint

import android.content.Intent
import com.example.expensetracker.core.analytics.AnalyticsService
import com.example.expensetracker.core.auth.AuthService
import com.example.expensetracker.core.remoteconfig.RemoteConfigService
import com.example.expensetracker.infrastructure.auth.AuthServiceImpl
import javax.inject.Inject
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var analyticsService: AnalyticsService

    @Inject
    lateinit var remoteConfigService: RemoteConfigService

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        askNotificationPermission()
        remoteConfigService.fetchAndActivate { }

        setContent {
            ExpenseTrackerTheme {
                ExpenseTrackerApp(
                    analyticsService = analyticsService,
                    remoteConfigService = remoteConfigService
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Пробрасываем результат в сервис авторизации
        (authService as? AuthServiceImpl)?.handleActivityResult(requestCode, resultCode, data)
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
