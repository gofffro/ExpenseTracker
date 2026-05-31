package com.example.expensetracker

import android.app.Application
import android.app.ActivityManager
import android.content.Context
import android.os.Process
import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import com.example.expensetracker.BuildConfig
import com.yandex.mapkit.MapKitFactory

@HiltAndroidApp
class ExpenseTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initAppMetrica()
        if (isMainProcess()) {
            initMapKit()
        }
    }

    private fun isMainProcess(): Boolean {
        val pid = Process.myPid()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfo = activityManager.runningAppProcesses?.find { it.pid == pid }
        return processInfo?.processName == packageName
    }

    private fun initAppMetrica() {
        val config = AppMetricaConfig
            .newConfigBuilder(BuildConfig.APPMETRICA_API_KEY)
            .withLogs()
            .withSessionTimeout(60)
            .withCrashReporting(true)
            .withNativeCrashReporting(true)
            .build()
        AppMetrica.activate(this, config)
        AppMetrica.enableActivityAutoTracking(this)
    }

    private fun initMapKit() {
        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPS_API_KEY)
        MapKitFactory.initialize(this)
    }
}
