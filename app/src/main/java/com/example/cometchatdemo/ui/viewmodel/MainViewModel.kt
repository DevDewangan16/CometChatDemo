package com.example.cometchatdemo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cometchat.chat.models.User
import com.example.cometchatdemo.data.repository.ChatRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = ChatRepository()

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginUser(uid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = repository.loginUser(uid)
                _loginState.value = LoginState.Success(user)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Login failed")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createTestUser(uid: String, name: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                repository.createTestUser(uid, name)
                callback(true)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failed to create test user: ${e.message}")
                callback(false)
            }
        }
    }

    fun logout() {
        repository.logout()
    }

    sealed class LoginState {
        data class Success(val user: User) : LoginState()
        data class Error(val message: String) : LoginState()
    }
}