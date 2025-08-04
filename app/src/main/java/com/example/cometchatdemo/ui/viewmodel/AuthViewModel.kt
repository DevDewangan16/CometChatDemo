package com.example.cometchatdemo.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cometchat.chat.models.User
import com.example.cometchatdemo.data.repository.ChatRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val chatRepository = ChatRepository()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val user: User) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    fun loginUser(uid: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                // First check if user exists
                val user = chatRepository.getUser(uid)

                // If user exists, try to login
                val loggedInUser = chatRepository.loginUser(uid)
                _authState.value = AuthState.Success(loggedInUser)

            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun signupUser(uid: String, name: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                // Create new user
                val createdUser = chatRepository.createTestUser(uid, name)

                // After successful creation, login the user
                val loggedInUser = chatRepository.loginUser(uid)
                _authState.value = AuthState.Success(loggedInUser)

            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Signup failed")
            }
        }
    }
}