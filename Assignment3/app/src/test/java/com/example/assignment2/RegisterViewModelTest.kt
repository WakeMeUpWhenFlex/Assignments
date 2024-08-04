package com.example.assignment2

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.assignment2.viewmodel.RegisterViewModel
import com.example.assignment2.network.ApiService
import com.example.assignment2.network.RegisterRequest
import com.example.assignment2.network.User
import com.example.assignment2.viewmodel.RegisterState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val application = mockk<Application>(relaxed = true)
    private val apiService = mockk<ApiService>()
    private val viewModel = RegisterViewModel(application)

    @Test
    fun `register should succeed with valid credentials`() = runTest {
        val user = User("validUser", "token")
        val request = RegisterRequest("validUser", "valid@example.com", "validPass")
        val response = Response.success(user)
        coEvery { apiService.register(request) } returns response

        viewModel.register(apiService, "validUser", "valid@example.com", "validPass")

        assertEquals(RegisterState.Success(user), viewModel.registerState.value)
    }

    @Test
    fun `register should fail with invalid credentials`() = runTest {
        val request = RegisterRequest("invalidUser", "invalid@example.com", "invalidPass")
        val response = Response.error<User>(401, mockk(relaxed = true))
        coEvery { apiService.register(request) } returns response

        viewModel.register(apiService, "invalidUser", "invalid@example.com", "invalidPass")

        assertEquals(RegisterState.Error("Registration failed"), viewModel.registerState.value)
    }

    @Test
    fun `register should fail with empty name`() {
        viewModel.register(apiService, "", "email@example.com", "password")
        assertEquals(RegisterState.Error("Name, email, and password must be provided"), viewModel.registerState.value)
    }

    @Test
    fun `register should fail with empty email`() {
        viewModel.register(apiService, "name", "", "password")
        assertEquals(RegisterState.Error("Name, email, and password must be provided"), viewModel.registerState.value)
    }

    @Test
    fun `register should fail with empty password`() {
        viewModel.register(apiService, "name", "email@example.com", "")
        assertEquals(RegisterState.Error("Name, email, and password must be provided"), viewModel.registerState.value)
    }
}
