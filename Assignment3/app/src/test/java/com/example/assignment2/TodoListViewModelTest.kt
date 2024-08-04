package com.example.assignment2

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.assignment2.network.ApiService
import com.example.assignment2.network.Todo
import com.example.assignment2.viewmodel.TodoListState
import com.example.assignment2.viewmodel.TodoListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class TodoListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val application = mockk<Application>(relaxed = true)
    private val apiService = mockk<ApiService>()
    private val viewModel = TodoListViewModel(application)

    @Test
    fun `getTodos should succeed`() = runTest {
        val todos = listOf(Todo("1", "Test Todo", false))
        val response = Response.success(todos)
        coEvery { apiService.getTodos("userId", any()) } returns response

        viewModel.getTodos(apiService, "userId")

        assertEquals(TodoListState.Success(todos), viewModel.todoListState)
    }

    @Test
    fun `getTodos should fail`() = runTest {
        val response = Response.error<List<Todo>>(500, mockk(relaxed = true))
        coEvery { apiService.getTodos("userId", any()) } returns response

        viewModel.getTodos(apiService, "userId")

        assertEquals(TodoListState.Error("Fetching todos failed"), viewModel.todoListState)
    }

    @Test
    fun `createTodo should succeed`() = runTest {
        val todos = listOf(Todo("1", "New Todo", false))
        coEvery { apiService.createTodo("userId", any(), any()) } returns Response.success(Todo(
            id = "1",
            title = "New Todo",
            completed = false
        ))
        coEvery { apiService.getTodos("userId", any()) } returns Response.success(todos)

        viewModel.createTodo(apiService, "userId", "New Todo")
        
        viewModel.getTodos(apiService, "userId")

        assertEquals(TodoListState.Success(todos), viewModel.todoListState)
    }

    @Test
    fun `createTodo should fail`() = runTest {
        coEvery { apiService.createTodo("userId", any(), any()) } throws Exception("An error occurred")
        coEvery { apiService.getTodos("userId", any()) } returns Response.error(500, mockk(relaxed = true))

        viewModel.createTodo(apiService, "userId", "Failed Todo")

        assertEquals(TodoListState.Error("An error occurred: An error occurred"), viewModel.todoListState)
    }
}
