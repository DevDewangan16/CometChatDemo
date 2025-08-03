package com.example.cometchatdemo.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cometchat.chat.models.Group
import com.cometchat.chat.models.User
import com.example.cometchatdemo.data.repository.ChatRepository
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

    private val repository = ChatRepository()

    private val _chatEntity = MutableLiveData<ChatEntity>()
    val chatEntity: LiveData<ChatEntity> = _chatEntity

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadUser(uid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = repository.getUser(uid)
                _chatEntity.value = ChatEntity.UserEntity(user)
            } catch (e: Exception) {
                _error.value = "Could not find user: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadGroup(guid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val group = repository.getGroup(guid)
                _chatEntity.value = ChatEntity.GroupEntity(group)
            } catch (e: Exception) {
                _error.value = "Could not find group: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    sealed class ChatEntity {
        data class UserEntity(val user: User) : ChatEntity()
        data class GroupEntity(val group: Group) : ChatEntity()
    }
}