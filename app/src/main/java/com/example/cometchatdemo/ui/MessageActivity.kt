package com.example.cometchatdemo.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cometchatdemo.databinding.ActivityMessageBinding
import com.example.cometchatdemo.ui.viewmodel.MessageViewModel

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    private val viewModel: MessageViewModel by viewModels()

    private var uid: String? = null
    private var guid: String? = null

    companion object {
        private const val TAG = "MessageActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        observeViewModel()
        setupHeaderBackButton()
        loadChatEntity()
    }

    private fun getIntentData() {
        uid = intent.getStringExtra("uid")
        guid = intent.getStringExtra("guid")
    }

    private fun observeViewModel() {
        viewModel.chatEntity.observe(this) { entity ->
            when (entity) {
                is MessageViewModel.ChatEntity.UserEntity -> {
                    Log.d(TAG, "Successfully loaded user: ${entity.user.uid}")
                    binding.messageHeader.setUser(entity.user)
                    binding.messageList.setUser(entity.user)
                    binding.messageComposer.setUser(entity.user)
                }
                is MessageViewModel.ChatEntity.GroupEntity -> {
                    Log.d(TAG, "Successfully loaded group: ${entity.group.guid}")
                    binding.messageHeader.setGroup(entity.group)
                    binding.messageList.setGroup(entity.group)
                    binding.messageComposer.setGroup(entity.group)
                }
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            Log.e(TAG, errorMessage)
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // Handle loading state if needed
        }
    }

    private fun loadChatEntity() {
        when {
            uid != null -> viewModel.loadUser(uid!!)
            guid != null -> viewModel.loadGroup(guid!!)
            else -> {
                Log.e(TAG, "No user ID or group ID provided")
                Toast.makeText(this, "Missing user ID or group ID", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupHeaderBackButton() {
        binding.messageHeader.setOnBackButtonPressed {
            finish()
        }
    }
}