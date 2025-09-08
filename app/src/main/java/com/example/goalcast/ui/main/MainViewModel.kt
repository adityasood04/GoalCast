package com.example.goalcast.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goalcast.data.Todo
import com.example.goalcast.data.TodoRepository
import com.example.goalcast.widget.WidgetUpdateHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val widgetUpdateHelper: WidgetUpdateHelper,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    init {
        fetchTodosForToday()
    }

    private fun fetchTodosForToday() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val startOfDay = calendar.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val endOfDay = calendar.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }.timeInMillis

            repository.getTodosForDay(startOfDay, endOfDay).collectLatest { todoList ->
                _todos.value = todoList.sortedWith(compareBy({ it.isCompleted }, { -it.priority }))
            }
        }
    }

    fun addTodo(description: String, priority: Int, dueDate: Long) {
        viewModelScope.launch {
            val newTodo = Todo(
                taskDescription = description,
                isCompleted = false,
                createdAt = System.currentTimeMillis(),
                dueDate = dueDate,
                priority = priority
            )
            repository.insertTodo(newTodo)
            widgetUpdateHelper.updateWidgets(context)
        }
    }

    fun updateTodoStatus(todo: Todo, isCompleted: Boolean) {
        viewModelScope.launch {
            val updatedTodo = todo.copy(isCompleted = isCompleted)
            repository.updateTodo(updatedTodo)
            widgetUpdateHelper.updateWidgets(context)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
            widgetUpdateHelper.updateWidgets(context)
        }
    }
}