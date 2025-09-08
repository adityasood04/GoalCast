package com.example.goalcast.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.goalcast.R
import com.example.goalcast.data.AppDatabase
import com.example.goalcast.data.Todo
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TodoWidgetViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private var todoList = listOf<Todo>()
    private val database by lazy { AppDatabase.getDatabase(context) }

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        todoList = runBlocking {
            try {
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

                val todos = database.todoDao().getTodaysPendingTodosList(startOfDay, endOfDay)
                todos.filter { !it.isCompleted }
                    .sortedWith(compareBy({ -it.priority }, { it.createdAt }))
                    .take(10)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    override fun onDestroy() {
        todoList = emptyList()
    }

    override fun getCount(): Int = if (todoList.isEmpty()) 1 else todoList.size

    override fun getViewAt(position: Int): RemoteViews {
        return if (todoList.isEmpty()) {
            createEmptyView()
        } else {
            createTodoView(todoList[position])
        }
    }

    private fun createTodoView(todo: Todo): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_todo_item)
        views.setTextViewText(R.id.widget_todo_text, todo.taskDescription)

        val drawableRes = when (todo.priority) {
            3 -> R.drawable.bg_strip_high
            2 -> R.drawable.bg_strip_medium
            else -> R.drawable.bg_strip_low
        }

        views.setInt(R.id.widget_priority_indicator, "setBackgroundResource", drawableRes)

        // todo : fix the intent action
        val fillInIntent = Intent().apply {
            action = TodoWidgetProvider.ACTION_TODO_COMPLETED
            putExtra(TodoWidgetProvider.EXTRA_TODO_ID, todo.id)
        }
        views.setOnClickFillInIntent(R.id.widget_check_icon, fillInIntent)
        return views
    }

    private fun createEmptyView(): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_empty_item)
        views.setTextViewText(R.id.widget_loading_text, "No goals for today!\nTap + to add one.")
        return views
    }

    override fun getLoadingView(): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_loading_item)
        views.setTextViewText(R.id.widget_loading_text, "Loading goals...")
        return views
    }

    override fun getViewTypeCount(): Int = 3

    override fun getItemId(position: Int): Long =
        if (todoList.isEmpty()) 0 else todoList[position].id.toLong()

    override fun hasStableIds(): Boolean = true
}