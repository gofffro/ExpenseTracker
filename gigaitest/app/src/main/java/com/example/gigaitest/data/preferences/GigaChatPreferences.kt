package com.example.gigaitest.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gigaitest.data.model.GigaTokenEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "giga_chat_prefs")

@Singleton
class GigaChatPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val GIGA_TOKEN_KEY = stringPreferencesKey("access_token")
    private val GIGA_EXPIRES_AT_KEY = longPreferencesKey("expires_at")

    fun getToken(): Flow<GigaTokenEntity?> {
        return context.dataStore.data.map { prefs ->
            val token = prefs[GIGA_TOKEN_KEY]
            val expires = prefs[GIGA_EXPIRES_AT_KEY] ?: 0L
            if (token != null && expires != 0L) {
                GigaTokenEntity(token, expires)
            } else null
        }
    }

    suspend fun setToken(token: String, expiresAt: Long) {
        context.dataStore.edit { prefs ->
            prefs[GIGA_TOKEN_KEY] = token
            prefs[GIGA_EXPIRES_AT_KEY] = expiresAt
        }
    }
}
