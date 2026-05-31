package com.example.expensetracker.infrastructure.profile

import com.example.expensetracker.core.profile.ProfileService
import com.example.expensetracker.core.profile.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseProfileService @Inject constructor() : ProfileService {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    override fun updateProfile(profile: UserProfile, onComplete: (Boolean) -> Unit) {
        if (profile.uid.isEmpty()) {
            onComplete(false)
            return
        }
        usersCollection.document(profile.uid).set(profile)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    override fun getProfile(uid: String, onComplete: (UserProfile?) -> Unit) {
        if (uid.isEmpty()) {
            onComplete(null)
            return
        }
        usersCollection.document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    onComplete(document.toObject(UserProfile::class.java))
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }
}
