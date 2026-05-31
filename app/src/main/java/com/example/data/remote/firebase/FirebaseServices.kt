package com.example.data.remote.firebase

/**
 * Foundation for Firebase Authentication.
 * To be implemented with actual Firebase SDK in future phases.
 */
class FirebaseAuthService {
    
    fun sendOtp(phoneNumber: String, onCodeSent: (String) -> Unit, onError: (Exception) -> Unit) {
        // TODO: Implement actual phone auth
        // Demo implementation
        onCodeSent("demo-verification-id")
    }

    fun verifyOtp(verificationId: String, code: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        // TODO: Implement actual code verification
        // Demo implementation
        if (code == "1234") {
            onSuccess()
        } else {
            onError(Exception("Invalid code"))
        }
    }
}

/**
 * Foundation for Firestore Database Service.
 * Collections to prepare: users, profiles, roles, schools, subscriptions, settings
 */
class FirestoreService {
    
    fun createProfile(userId: String, profileData: Map<String, Any>) {
        // TODO: Write to 'profiles' collection
    }

    fun getProfile(userId: String) {
        // TODO: Read from 'profiles' collection
    }
}

/**
 * Foundation for Firebase Cloud Storage.
 */
class FirebaseStorageService {
    
    fun uploadAvatar(userId: String, localUri: String) {
        // TODO: Upload image to Storage -> users/{userId}/avatar.jpg
    }
}
