package com.example.cometchatdemo.data.repository
import android.util.Log
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.exceptions.CometChatException
import com.cometchat.chat.models.Group
import com.cometchat.chat.models.User
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ChatRepository {

    companion object {
        private const val TAG = "ChatRepository"
    }

    suspend fun createTestUser(uid: String, name: String): User = suspendCancellableCoroutine { continuation ->
        val user = User().apply {
            this.uid = uid
            this.name = name
            this.avatar = "https://avatar.iran.liara.run/public/boy?username=$uid"
        }

        val authKey = "88740ea58bf60513c1b4d633dc1c00e7363dcafa" // Replace with actual CometChat auth key

        CometChat.createUser(user, authKey, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(createdUser: User) {
                Log.d("CometChat", "Test user created: ${createdUser.uid}")
                continuation.resume(createdUser)
            }

            override fun onError(e: CometChatException) {
                Log.e("CometChat", "Failed to create user: ${e.message}")
                if (e.code == "ERR_UID_ALREADY_EXISTS") {
                    // Try to fetch the existing user instead
                    CometChat.getUser(uid, object : CometChat.CallbackListener<User>() {
                        override fun onSuccess(existingUser: User) {
                            continuation.resume(existingUser)
                        }

                        override fun onError(innerEx: CometChatException) {
                            continuation.resumeWithException(innerEx)
                        }
                    })
                } else {
                    continuation.resumeWithException(e)
                }
            }
        })
    }


    suspend fun loginUser(uid: String): User = suspendCancellableCoroutine { continuation ->
        // Check if CometChat is initialized
        if (!isCometChatInitialized()) {
            continuation.resumeWithException(
                Exception("CometChat not initialized. Please wait and try again.")
            )
            return@suspendCancellableCoroutine
        }

        CometChatUIKit.login(uid, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User) {
                Log.d(TAG, "Login successful for user: ${user.uid}")
                continuation.resume(user)
            }

            override fun onError(e: CometChatException) {
                Log.e(TAG, "Login failed: ${e.message}")
                continuation.resumeWithException(e)
            }
        })
    }

    suspend fun getUser(uid: String): User = suspendCancellableCoroutine { continuation ->
        CometChat.getUser(uid, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User) {
                continuation.resume(user)
            }

            override fun onError(e: CometChatException?) {
                continuation.resumeWithException(e ?: Exception("Unknown error"))
            }
        })
    }

    suspend fun getGroup(guid: String): Group = suspendCancellableCoroutine { continuation ->
        CometChat.getGroup(guid, object : CometChat.CallbackListener<Group>() {
            override fun onSuccess(group: Group) {
                continuation.resume(group)
            }

            override fun onError(e: CometChatException?) {
                continuation.resumeWithException(e ?: Exception("Unknown error"))
            }
        })
    }

    fun logout() {
        CometChat.logout(object : CometChat.CallbackListener<String>() {
            override fun onSuccess(message: String) {
                Log.d(TAG, "Logout successful")
            }

            override fun onError(e: CometChatException) {
                Log.e(TAG, "Logout error: ${e.message}")
            }
        })
    }

    private fun isCometChatInitialized(): Boolean {
        return try {
            CometChat.getLoggedInUser() != null || true // This will throw if not initialized
            true
        } catch (e: Exception) {
            false
        }
    }

}