package com.example.cometchatdemo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cometchatdemo.databinding.ActivityLoginBinding
import com.example.cometchatdemo.ui.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val uid = binding.etUserId.text.toString().trim()

            if (uid.isEmpty()) {
                binding.etUserId.error = "User ID is required"
                return@setOnClickListener
            }

            authViewModel.loginUser(uid)
        }

        binding.tvSignupPrompt.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun observeViewModel() {
        authViewModel.authState.observe(this) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> {
                    binding.btnLogin.isEnabled = false
                    binding.btnLogin.text = "Logging in..."
                }
                is AuthViewModel.AuthState.Success -> {
                    Log.d(TAG, "Login successful for user: ${state.user.uid}")
                    Toast.makeText(this, "Welcome ${state.user.name}", Toast.LENGTH_SHORT).show()

                    // Navigate to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                is AuthViewModel.AuthState.Error -> {
                    Log.e(TAG, "Login failed: ${state.message}")

                    if (state.message.contains("does not exist", ignoreCase = true) ||
                        state.message.contains("ERR_UID_NOT_FOUND", ignoreCase = true)) {

                        Toast.makeText(this, "User not found. Please sign up first.", Toast.LENGTH_LONG).show()

                        // Navigate to signup with pre-filled UID
                        val intent = Intent(this, SignupActivity::class.java)
                        intent.putExtra("prefill_uid", binding.etUserId.text.toString().trim())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Login failed: ${state.message}", Toast.LENGTH_LONG).show()
                    }

                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Login"
                }
                else -> {
                    // Handle any other states
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Login"
                }
            }
        }
    }
}