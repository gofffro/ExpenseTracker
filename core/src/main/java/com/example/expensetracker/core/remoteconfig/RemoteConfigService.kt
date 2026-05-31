package com.example.expensetracker.core.remoteconfig

interface RemoteConfigService {
    fun getString(key: String): String
    fun getBoolean(key: String): Boolean
    fun getLong(key: String): Long
    fun fetchAndActivate(onComplete: (Boolean) -> Unit)
}
