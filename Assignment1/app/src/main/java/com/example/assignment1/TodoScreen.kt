package com.example.assignment1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

@Composable
fun TodoScreen(todoItems: List<TodoItem>, onAddClick: () -> Unit, onToggleComplete: (TodoItem) -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Filled.Add, contentDescription = "Add Todo")
            }
        },
        content = { paddingValues ->
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                modifier = Modifier.padding(paddingValues)
            ) {
                items(todoItems) { item ->
                    TodoRow(item = item, onToggleComplete = onToggleComplete)
                }
            }
        }
    )
}

@Composable
fun TodoRow(item: TodoItem, onToggleComplete: (TodoItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = { onToggleComplete(item) }
        )
    }
}
