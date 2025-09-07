package com.example.goalcast.data

import kotlinx.coroutines.flow.Flow
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
}
