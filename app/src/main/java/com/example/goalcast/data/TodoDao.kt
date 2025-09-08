package com.example.goalcast.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * FROM todos_table WHERE dueDate >= :startOfDay AND dueDate < :endOfDay ORDER BY priority ASC, createdAt DESC")
    fun getTodosForDay(startOfDay: Long, endOfDay: Long): Flow<List<Todo>>

    @Query("SELECT * FROM todos_table WHERE id = :todoId")
    suspend fun getTodoById(todoId: Int): Todo?

    @Query("UPDATE todos_table SET isCompleted = 1 WHERE id = :todoId")
    suspend fun markTodoAsDone(todoId: Int)

    @Query("SELECT * FROM todos_table WHERE dueDate >= :startOfDay AND dueDate < :endOfDay")
    suspend fun getTodaysPendingTodosList(startOfDay: Long, endOfDay: Long): List<Todo>

}