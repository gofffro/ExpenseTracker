package com.example.expensetracker.infrastructure.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.expensetracker.core.auth.AuthResult
import com.example.expensetracker.core.auth.AuthService
import com.example.expensetracker.core.auth.User
import com.example.expensetracker.core.auth.AuthProvider
import com.yandex.authsdk.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CompletableDeferred

@Singleton
class AuthServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthService {

    private val sdk = YandexAuthSdk.create(YandexAuthOptions(context))
    private var pendingLogin: CompletableDeferred<AuthResult>? = null

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override suspend fun loginWithYandex(activity: Activity): AuthResult {
        val deferred = CompletableDeferred<AuthResult>()
        pendingLogin = deferred

        try {
            // Используем контракт из SDK для создания Intent
            val intent = sdk.contract.createIntent(context, YandexAuthLoginOptions())
            activity.startActivityForResult(intent, REQUEST_CODE_YANDEX)
        } catch (e: Exception) {
            deferred.complete(AuthResult.Error("Failed to start Yandex login: ${e.message}"))
        }

        return deferred.await()
    }

    override suspend fun loginWithVk(activity: Activity): AuthResult {
        return AuthResult.Error("VK Login not implemented")
    }

    override suspend fun loginAsGuest(): AuthResult {
        val user = User(
            id = "guest_${System.currentTimeMillis()}",
            name = "Guest User",
            email = null,
            avatarUrl = null,
            provider = AuthProvider.GUEST
        )
        saveUser(user, "guest_token")
        return AuthResult.Success(user)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == REQUEST_CODE_YANDEX) {
            val result = sdk.contract.parseResult(resultCode, data)
            when (result) {
                is YandexAuthResult.Success -> {
                    val token = result.token
                    val user = User(
                        id = "yandex_${token.value.hashCode()}",
                        name = "Yandex User",
                        email = null,
                        avatarUrl = null,
                        provider = AuthProvider.YANDEX
                    )
                    saveUser(user, token.value)
                    pendingLogin?.complete(AuthResult.Success(user))
                }
                is YandexAuthResult.Failure -> {
                    pendingLogin?.complete(AuthResult.Error(result.exception.message ?: "Auth failed"))
                }
                is YandexAuthResult.Cancelled -> {
                    pendingLogin?.complete(AuthResult.Error("Login cancelled"))
                }
            }
            return true
        }
        return false
    }

    override fun logout() {
        prefs.edit().clear().apply()
    }

    override fun getCurrentUser(): User? {
        val id = prefs.getString("user_id", null) ?: return null
        val name = prefs.getString("user_name", "") ?: ""
        val email = prefs.getString("user_email", null)
        val providerStr = prefs.getString("user_provider", null)
        val provider = when (providerStr) {
            "VK" -> AuthProvider.VK
            "GUEST" -> AuthProvider.GUEST
            else -> AuthProvider.YANDEX
        }
        
        return User(id, name, email, null, provider)
    }

    private fun saveUser(user: User, token: String) {
        prefs.edit().apply {
            putString("user_id", user.id)
            putString("user_name", user.name)
            putString("user_email", user.email)
            putString("user_provider", user.provider.name)
            putString("access_token", token)
            apply()
        }
    }

    companion object {
        const val REQUEST_CODE_YANDEX = 1001
    }
}
