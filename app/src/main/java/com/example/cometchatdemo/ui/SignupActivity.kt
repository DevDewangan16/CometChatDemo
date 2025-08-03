package com.example.cometchatdemo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cometchatdemo.databinding.ActivitySignupBinding
import com.example.cometchatdemo.ui.viewmodel.AuthViewModel

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val authViewModel: AuthViewModel by viewModels()

    companion object {
        private const val TAG = "SignupActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPrefilledData()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupPrefilledData() {
        // Pre-fill UID if passed from LoginActivity
        val prefilledUid = intent.getStringExtra("prefill_uid")
        if (!prefilledUid.isNullOrEmpty()) {
            binding.etUserId.setText(prefilledUid)
        }
    }

    private fun setupClickListeners() {
        binding.btnSignup.setOnClickListener {
            val uid = binding.etUserId.text.toString().trim()
            val name = binding.etName.text.toString().trim()

            if (uid.isEmpty()) {
                binding.etUserId.error = "User ID is required"
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                binding.etName.error = "Name is required"
                return@setOnClickListener
            }

            authViewModel.signupUser(uid, name)
        }

        binding.tvLoginPrompt.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun observeViewModel() {
        authViewModel.authState.observe(this) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> {
                    binding.btnSignup.isEnabled = false
                    binding.btnSignup.text = "Creating Account..."
                }
                is AuthViewModel.AuthState.Success -> {
                    Log.d(TAG, "Signup successful for user: ${state.user.uid}")
                    Toast.makeText(this, "Account created successfully! Welcome ${state.user.name}", Toast.LENGTH_SHORT).show()

                    // Navigate to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                is AuthViewModel.AuthState.Error -> {
                    Log.e(TAG, "Signup failed: ${state.message}")

                    if (state.message.contains("already exists", ignoreCase = true) ||
                        state.message.contains("ERR_UID_ALREADY_EXISTS", ignoreCase = true)) {

                        Toast.makeText(this, "User already exists. Please login instead.", Toast.LENGTH_LONG).show()

                        // Navigate to login with pre-filled UID
                        val intent = Intent(this, LoginActivity::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Signup failed: ${state.message}", Toast.LENGTH_LONG).show()
                    }

                    binding.btnSignup.isEnabled = true
                    binding.btnSignup.text = "Sign Up"
                }
                else -> {
                    // Handle any other states
                    binding.btnSignup.isEnabled = true
                    binding.btnSignup.text = "Sign Up"
                }
            }
        }
    }
}