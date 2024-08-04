package com.example.assignment2

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.assignment2.viewmodel.LoginViewModel
import com.example.assignment2.viewmodel.LoginState
import com.example.assignment2.network.ApiService
import com.example.assignment2.network.LoginRequest
import com.example.assignment2.network.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val application = mockk<Application>(relaxed = true)
    private val apiService = mockk<ApiService>()
    private val viewModel = LoginViewModel(application)

    @Test
    fun `login should succeed with valid credentials`() = runTest {
        val user = User("validUser", "token")
        val request = LoginRequest("validUser", "validPass")
        val response = Response.success(user)
        coEvery { apiService.login(request) } returns response

        viewModel.login(apiService, "validUser", "validPass")

        assertEquals(LoginState.Success(user), viewModel.loginState)
    }

    @Test
    fun `login should fail with invalid credentials`() = runTest {
        val request = LoginRequest("invalidUser", "invalidPass")
        val response = Response.error<User>(401, mockk(relaxed = true))
        coEvery { apiService.login(request) } returns response

        viewModel.login(apiService, "invalidUser", "invalidPass")

        assertEquals(LoginState.Error("Login failed"), viewModel.loginState)
    }

    @Test
    fun `login should fail with empty email`() {
        viewModel.login(apiService, "", "password")
        assertEquals(LoginState.Error("Email and password must be provided"), viewModel.loginState)
    }

    @Test
    fun `login should fail with empty password`() {
        viewModel.login(apiService, "email", "")
        assertEquals(LoginState.Error("Email and password must be provided"), viewModel.loginState)
    }
}
