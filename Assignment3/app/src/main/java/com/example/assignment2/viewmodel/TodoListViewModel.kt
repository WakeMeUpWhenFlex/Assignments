package com.example.assignment2.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment2.network.ApiService
import com.example.assignment2.network.Todo
import com.example.assignment2.network.TodoRequest
import kotlinx.coroutines.launch
import retrofit2.Response

class TodoListViewModel(application: Application) : AndroidViewModel(application) {
    var todoListState by mutableStateOf<TodoListState>(TodoListState.Idle)
        private set

    private val apiKey = "acc687ba-e922-4371-b4cf-ddfae1d92bc4"

    fun getTodos(apiService: ApiService, userId: String) {
        viewModelScope.launch {
            try {
                todoListState = TodoListState.Loading
                Log.d("TodoListViewModel", "Fetching todos for user: $userId")
                val response: Response<List<Todo>> = apiService.getTodos(userId, apiKey)
                if (response.isSuccessful) {
                    Log.d("TodoListViewModel", "Todos fetched successfully")
                    todoListState = TodoListState.Success(response.body()!!)
                } else {
                    Log.e("TodoListViewModel", "Failed to fetch todos: ${response.errorBody()?.string()}")
                    todoListState = TodoListState.Error("Fetching todos failed")
                }
            } catch (e: Exception) {
                Log.e("TodoListViewModel", "An error occurred: ${e.message}")
                todoListState = TodoListState.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun createTodo(apiService: ApiService, userId: String, title: String) {
        viewModelScope.launch {
            try {
                val todoRequest = TodoRequest(title, false)
                Log.d("TodoListViewModel", "Creating todo for user: $userId with title: $title")
                apiService.createTodo(userId, todoRequest, apiKey)
                getTodos(apiService, userId) // refresh todo list
            } catch (e: Exception) {
                Log.e("TodoListViewModel", "An error occurred: ${e.message}")
                todoListState = TodoListState.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun updateTodo(apiService: ApiService, userId: String, todo: Todo) {
        viewModelScope.launch {
            try {
                val todoRequest = TodoRequest(todo.title, todo.completed)
                Log.d("TodoListViewModel", "Updating todo for user: $userId with id: ${todo.id}")
                apiService.updateTodo(userId, todo.id, todoRequest, apiKey)
                getTodos(apiService, userId) // refresh todo list
            } catch (e: Exception) {
                Log.e("TodoListViewModel", "An error occurred: ${e.message}")
                todoListState = TodoListState.Error("An error occurred: ${e.message}")
            }
        }
    }
}

sealed class TodoListState {
    object Idle : TodoListState()
    object Loading : TodoListState()
    data class Success(val todos: List<Todo>) : TodoListState()
    data class Error(val message: String) : TodoListState()
}
