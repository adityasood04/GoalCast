package com.example.goalcast.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos_table")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskDescription: String,
    var isCompleted: Boolean = false,
    val priority: Int = 2,
    val createdAt: Long = System.currentTimeMillis(),
    val dueDate: Long
)

