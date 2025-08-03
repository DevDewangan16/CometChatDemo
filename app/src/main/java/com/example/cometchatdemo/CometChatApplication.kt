package com.example.cometchatdemo

import android.app.Application
import android.util.Log
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.exceptions.CometChatException
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit
import com.cometchat.chatuikit.shared.cometchatuikit.UIKitSettings

class CometChatApplication : Application() {

    companion object {
        private const val TAG = "CometChatApplication"

        // TODO: Replace with your actual CometChat credentials
        private const val APP_ID = "279743c078fab24b"
        private const val REGION = "in"
        private const val AUTH_KEY = "88740ea58bf60513c1b4d633dc1c00e7363dcafa"
    }

    override fun onCreate() {
        super.onCreate()
        initializeCometChat()
    }

    private fun initializeCometChat() {
        // Step 1: Initialize CometChat Core first
        val appSettings = com.cometchat.chat.core.AppSettings.AppSettingsBuilder()
            .subscribePresenceForAllUsers()
            .setRegion(REGION)
            .build()

        CometChat.init(this, APP_ID, appSettings, object : CometChat.CallbackListener<String>() {
            override fun onSuccess(successString: String) {
                Log.d(TAG, "CometChat initialization completed successfully")
                // Step 2: Initialize UIKit after core initialization
                initializeUIKit()
            }

            override fun onError(e: CometChatException) {
                Log.e(TAG, "CometChat initialization failed: ${e.message}")
            }
        })
    }

    private fun initializeUIKit() {
        val uiKitSettings = UIKitSettings.UIKitSettingsBuilder()
            .setRegion(REGION)
            .setAppId(APP_ID)
            .setAuthKey(AUTH_KEY)
            .subscribePresenceForAllUsers()
            .build()

        CometChatUIKit.init(this, uiKitSettings, object : CometChat.CallbackListener<String?>() {
            override fun onSuccess(successString: String?) {
                Log.d(TAG, "CometChat UIKit initialization completed successfully")
            }

            override fun onError(e: CometChatException?) {
                Log.e(TAG, "CometChat UIKit initialization failed: ${e?.message}")
            }
        })
    }
}