package com.example.expensetracker.core.profile

data class UserProfile(
    val uid: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val fcmToken: String? = null
)
