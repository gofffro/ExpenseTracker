package com.example.expensetracker.feature.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.analytics.AnalyticsService
import com.example.expensetracker.core.auth.AuthResult
import com.example.expensetracker.core.auth.AuthService
import com.example.expensetracker.core.error.CrashReporter
import com.example.expensetracker.core.profile.ProfileService
import com.example.expensetracker.core.profile.UserProfile
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val analyticsService: AnalyticsService,
    private val profileService: ProfileService,
    private val crashReporter: CrashReporter
) : ViewModel() {

    private val _loginResult = MutableSharedFlow<AuthResult>()
    val loginResult: SharedFlow<AuthResult> = _loginResult

    val currentUser = authService.getCurrentUser()

    init {
        crashReporter.log("LoginViewModel initialized")
        currentUser?.let { user ->
            crashReporter.setUserId(user.id)
            updateUserProfile(AuthResult.Success(user))
        }
    }

    fun loginWithVk(activity: Activity) {
        crashReporter.log("Login with VK initiated")
        viewModelScope.launch {
            val result = authService.loginWithVk(activity)
            handleResult(result, "vk")
        }
    }

    fun loginWithYandex(activity: Activity) {
        crashReporter.log("Login with Yandex initiated")
        viewModelScope.launch {
            val result = authService.loginWithYandex(activity)
            handleResult(result, "yandex")
        }
    }

    fun loginAsGuest() {
        crashReporter.log("Login as Guest initiated")
        viewModelScope.launch {
            val result = authService.loginAsGuest()
            handleResult(result, "guest")
        }
    }

    fun triggerTestCrash() {
        crashReporter.log("Manual test crash triggered")
        throw RuntimeException("Manual crash for Laboratory Work #8")
    }

    private suspend fun handleResult(result: AuthResult, provider: String) {
        if (result is AuthResult.Success) {
            crashReporter.setUserId(result.user.id)
            crashReporter.setKey("auth_provider", provider)
            analyticsService.trackEvent("user_logged_in", mapOf("provider" to provider))
            updateUserProfile(result)
        }
        _loginResult.emit(result)
    }

    private fun updateUserProfile(result: AuthResult.Success) {
        viewModelScope.launch {
            val token = try {
                FirebaseMessaging.getInstance().token.await()
            } catch (e: Exception) {
                null
            }

            val profile = UserProfile(
                uid = result.user.id,
                email = result.user.email ?: "",
                displayName = result.user.name ?: "",
                fcmToken = token
            )

            profileService.updateProfile(profile) { success ->
                if (success) {
                    analyticsService.trackEvent("profile_updated")
                }
            }
        }
    }
}
