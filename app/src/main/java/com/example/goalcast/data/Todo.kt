package com.example.goalcast.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos_table")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "taskDescription")
    var taskDescription: String,

    @ColumnInfo(name = "isCompleted")
    var isCompleted: Boolean = false,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "dueDate")
    var dueDate: Long,

    @ColumnInfo(name = "priority")
    var priority: Int
)

