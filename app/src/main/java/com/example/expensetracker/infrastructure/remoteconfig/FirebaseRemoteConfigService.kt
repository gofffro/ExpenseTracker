package com.example.expensetracker.infrastructure.remoteconfig

import com.example.expensetracker.core.remoteconfig.RemoteConfigService
import com.example.expensetracker.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRemoteConfigService @Inject constructor() : RemoteConfigService {

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance().apply {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // задержка 0, чтобы обновлялось сразу, по хорошему должно быть > 0
        }
        setConfigSettingsAsync(configSettings)
        setDefaultsAsync(R.xml.remote_config_defaults)
    }

    override fun getString(key: String): String = remoteConfig.getString(key)

    override fun getBoolean(key: String): Boolean = remoteConfig.getBoolean(key)

    override fun getLong(key: String): Long = remoteConfig.getLong(key)

    override fun fetchAndActivate(onComplete: (Boolean) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }
}
