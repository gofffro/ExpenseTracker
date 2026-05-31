package com.example.expensetracker.core.auth

import android.app.Activity

interface AuthService {
    suspend fun loginWithYandex(activity: Activity): AuthResult
    suspend fun loginAsGuest(): AuthResult
    fun logout()
    fun getCurrentUser(): User?
}

data class User(
    val id: String,
    val name: String,
    val email: String?,
    val avatarUrl: String?,
    val provider: AuthProvider
)

enum class AuthProvider { YANDEX, GUEST }

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Cancelled : AuthResult()
}
