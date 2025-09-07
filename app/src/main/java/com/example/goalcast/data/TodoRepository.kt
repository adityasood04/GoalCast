package com.example.goalcast.data

import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TodoRepository @Inject constructor(private val todoDao: TodoDao) {

    fun getTodosForDay(startOfDay: Long, endOfDay: Long): Flow<List<Todo>> {
        return todoDao.getTodosForDay(startOfDay, endOfDay)
    }


    suspend fun insertTodo(todo: Todo) {
        todoDao.insert(todo)
    }


    suspend fun updateTodo(todo: Todo) {
        todoDao.update(todo)
    }

    suspend fun deleteTodo(todo: Todo) {
        todoDao.delete(todo)
    }

    suspend fun getTodaysPendingTodosList(): List<Todo> {
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

        return todoDao.getTodaysPendingTodosList(startOfDay, endOfDay)
    }
}
