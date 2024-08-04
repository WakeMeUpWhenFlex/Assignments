package com.example.assignment2.network

import android.content.Context

fun saveUserSession(context: Context, userId: String, token: String) {
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("user_id", userId)
        putString("token", token)
        apply()
    }
}
