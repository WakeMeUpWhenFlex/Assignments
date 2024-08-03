package com.example.assignment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignment2.network.RetrofitInstance
import com.example.assignment2.network.saveUserSession
import com.example.assignment2.ui.*
import com.example.assignment2.ui.theme.Assignment2Theme
import com.example.assignment2.viewmodel.TodoListViewModel
import kotlinx.coroutines.launch

data class TodoItem(val name: String, var isCompleted: MutableState<Boolean>)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoListScreen(todoItems: List<TodoItem>, onAddItem: (String) -> Unit) {
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var newItemText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = newItemText,
                    onValueChange = {
                        newItemText = it
                        showError = false
                    },
                    label = { Text(stringResource(id = R.string.new_todo)) },
                    trailingIcon = {
                        IconButton(onClick = { newItemText = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = stringResource(id = R.string.clear_text))
                        }
                    }
                )
                if (showError) {
                    Text(
                        text = stringResource(id = R.string.error_empty_todo),
                        color = MaterialTheme.colors.error
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(
                        onClick = {
                            if (newItemText.isNotEmpty()) {
                                onAddItem(newItemText)
                                newItemText = ""
                                coroutineScope.launch {
                                    bottomSheetState.hide()
                                }
                            } else {
                                showError = true
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(id = R.string.save))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = {
                            newItemText = ""
                            coroutineScope.launch {
                                bottomSheetState.hide()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        bottomSheetState.show()
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_todo))
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(contentPadding = PaddingValues(horizontal = 12.dp)) {
                    items(todoItems.size) { index ->
                        val item = todoItems[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        ) {
                            Text(item.name, fontSize = 18.sp, modifier = Modifier.weight(1f))
                            Checkbox(
                                checked = item.isCompleted.value,
                                onCheckedChange = { isChecked ->
                                    item.isCompleted.value = isChecked
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment2Theme {
                val todoListViewModel: TodoListViewModel = viewModel()
                var userId by remember { mutableStateOf<String?>(null) }
                var token by remember { mutableStateOf<String?>(null) }

                var showLogin by remember { mutableStateOf(true) }

                if (showLogin) {
                    LoginScreen(
                        apiService = RetrofitInstance.api,
                        onLoginSuccess = { id, tok ->
                            userId = id
                            token = tok
                            saveUserSession(applicationContext, id, tok)
                            todoListViewModel.getTodos(RetrofitInstance.api, id)
                            showLogin = false
                        },
                        onNavigateToRegister = { showLogin = false }
                    )
                } else {
                    RegisterScreen(
                        apiService = RetrofitInstance.api,
                        onRegisterSuccess = { id, tok ->
                            userId = id
                            token = tok
                            saveUserSession(applicationContext, id, tok)
                            todoListViewModel.getTodos(RetrofitInstance.api, id)
                            showLogin = true
                        },
                        onNavigateToLogin = { showLogin = true }
                    )
                }

                if (userId != null && token != null) {
                    val todoItems = remember { mutableStateListOf<TodoItem>() }
                    LaunchedEffect(Unit) {
                        userId?.let { todoListViewModel.getTodos(RetrofitInstance.api, it) }
                    }
                    TodoListScreen(
                        todoItems = todoItems,
                        onAddItem = { name ->
                            todoItems.add(TodoItem(name, mutableStateOf(false)))
                        }
                    )
                }
            }
        }
    }
}
