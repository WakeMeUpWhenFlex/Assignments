package com.example.assignment2.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment2.network.ApiService
import com.example.assignment2.network.RegisterRequest
import com.example.assignment2.network.User
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val _registerState = mutableStateOf<RegisterState>(RegisterState.Idle)
    val registerState: State<RegisterState> = _registerState

    fun register(apiService: ApiService, name: String, email: String, password: String) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _registerState.value = RegisterState.Error("Name, email, and password must be provided")
            return
        }

        viewModelScope.launch {
            try {
                _registerState.value = RegisterState.Loading
                val response: Response<User> = apiService.register(RegisterRequest(name, email, password))
                if (response.isSuccessful) {
                    _registerState.value = RegisterState.Success(response.body()!!)
                } else {
                    _registerState.value = RegisterState.Error("Registration failed")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("An error occurred: ${e.message}")
            }
        }
    }
}

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data class Success(val user: User) : RegisterState()
    data class Error(val message: String) : RegisterState()
}
