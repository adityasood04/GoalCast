package com.example.goalcast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.goalcast.ui.main.MainScreen
import com.example.goalcast.ui.theme.GoalCastTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val openAddTodoSheet = intent.getBooleanExtra(OPEN_ADD_TODO_SHEET, false)
        setContent {
            GoalCastTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(openAddTodoSheet = openAddTodoSheet)
                }
            }
        }
    }
    companion object {
        const val OPEN_ADD_TODO_SHEET = "open_add_todo_sheet"
    }
}