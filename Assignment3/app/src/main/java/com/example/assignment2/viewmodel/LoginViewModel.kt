package com.example.assignment2.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment2.network.ApiService
import com.example.assignment2.network.LoginRequest
import com.example.assignment2.network.User
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _loginState = mutableStateOf<LoginState>(LoginState.Idle)
    val loginState: LoginState
        get() = _loginState.value

    private fun setLoginState(newState: LoginState) {
        _loginState.value = newState
    }

    fun login(apiService: ApiService, email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            setLoginState(LoginState.Error("Email and password must be provided"))
            return
        }

        viewModelScope.launch {
            try {
                setLoginState(LoginState.Loading)
                val response: Response<User> = apiService.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    setLoginState(LoginState.Success(response.body()!!))
                } else {
                    setLoginState(LoginState.Error("Login failed"))
                }
            } catch (e: Exception) {
                setLoginState(LoginState.Error("An error occurred: ${e.message}"))
            }
        }
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}
