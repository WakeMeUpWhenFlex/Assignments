package com.example.assignment1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen() {
    val (todoItems, setTodoItems) = remember { mutableStateOf(listOf<TodoItem>()) }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = { Text("Todo") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { coroutineScope.launch { scaffoldState.bottomSheetState.expand() } }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Todo")
            }
        },
        sheetContent = {
            Column {
                // 添加下拉手柄
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(Color.Gray, shape = MaterialTheme.shapes.medium)
                    )
                }
                AddTodoBottomSheet(
                    onSave = { newItem ->
                        setTodoItems(todoItems + TodoItem(name = newItem))
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    },
                    onCancel = {
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                )
            }
        },
        sheetPeekHeight = 0.dp, // 初始隐藏 BottomSheet
        content = {
            TodoScreen(
                todoItems = todoItems,
                onAddClick = { coroutineScope.launch { scaffoldState.bottomSheetState.expand() } },
                onToggleComplete = { item ->
                    setTodoItems(
                        todoItems.map {
                            if (it == item) it.copy(isCompleted = !it.isCompleted) else it
                        }
                    )
                }
            )
        }
    )
}
