package com.example.assignment2.network

import retrofit2.Response
import retrofit2.http.*

data class User(
    val id: String,
    val token: String
)

data class Todo(
    val id: String,
    val title: String,
    var completed: Boolean
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class TodoRequest(
    val title: String,
    val completed: Boolean
)

interface ApiService {

    @POST("/api/users/register")
    suspend fun register(@Body request: RegisterRequest): Response<User>

    @POST("/api/users/login")
    suspend fun login(@Body request: LoginRequest): Response<User>

    @GET("/api/users/{user_id}/todos")
    suspend fun getTodos(
        @Path("user_id") userId: String,
        @Query("apikey") apiKey: String
    ): Response<List<Todo>>

    @POST("/api/users/{user_id}/todos")
    suspend fun createTodo(
        @Path("user_id") userId: String,
        @Body todo: TodoRequest,
        @Query("apikey") apiKey: String
    ): Response<Todo>

    @PUT("/api/users/{user_id}/todos/{id}")
    suspend fun updateTodo(
        @Path("user_id") userId: String,
        @Path("id") todoId: String,
        @Body todo: TodoRequest,
        @Query("apikey") apiKey: String
    ): Response<Todo>
}
