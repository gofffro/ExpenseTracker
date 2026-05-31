package com.example.expensetracker.core.profile

interface ProfileService {
    fun updateProfile(profile: UserProfile, onComplete: (Boolean) -> Unit)
    fun getProfile(uid: String, onComplete: (UserProfile?) -> Unit)
}
