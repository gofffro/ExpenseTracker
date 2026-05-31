package com.example.gigaitest.data.repository

import com.example.gigaitest.BuildConfig
import com.example.gigaitest.data.api.GigaOAuthApi
import com.example.gigaitest.data.preferences.GigaChatPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GigaChatTokenManager @Inject constructor(
    private val oauthApi: GigaOAuthApi,
    private val prefs: GigaChatPreferences
) {
    private val mutex = Mutex()
    private var cachedToken: String? = null
    private var expiresAt: Long = 0L

    suspend fun getValidToken(): String = mutex.withLock {
        val now = System.currentTimeMillis()
        
        if (cachedToken != null && expiresAt > now + 60_000L) {
            return cachedToken!!
        }

        val stored = prefs.getToken().firstOrNull()
        if (stored != null && stored.expiresAt > now + 60_000L) {
            cachedToken = stored.accessToken
            expiresAt = stored.expiresAt
            return stored.accessToken
        }

        val response = oauthApi.getAccessToken(
            rqUid = UUID.randomUUID().toString(),
            authorizationKey = "Basic ${BuildConfig.GIGACHAT_AUTH_KEY}"
        )
        
        cachedToken = response.accessToken
        expiresAt = response.expiresAt
        prefs.setToken(response.accessToken, response.expiresAt)
        
        return response.accessToken
    }
}
