package com.example.goalcast.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.goalcast.MainActivity
import com.example.goalcast.R
import com.example.goalcast.data.AppDatabase
import com.example.goalcast.data.TodoRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TodoWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var repository: TodoRepository

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            ACTION_TODO_COMPLETED -> {
                val todoId = intent.getIntExtra(EXTRA_TODO_ID, -1)
                if (todoId != -1) {
                    handleTodoCompleted(context, todoId)
                }
            }

            ACTION_ADD_TODO -> {
                openAddTodoSheet(context)
            }

            ACTION_REFRESH_WIDGET -> {
                refreshWidget(context)
            }
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.todo_widget)

        val intent = Intent(context, TodoWidgetService::class.java)
        views.setRemoteAdapter(R.id.widget_list_view, intent)

        val addIntent = Intent(context, TodoWidgetProvider::class.java).apply {
            action = ACTION_ADD_TODO
        }
        val addPendingIntent = PendingIntent.getBroadcast(
            context, 0, addIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_add_button, addPendingIntent)

        val refreshIntent = Intent(context, TodoWidgetProvider::class.java).apply {
            action = ACTION_REFRESH_WIDGET
        }
        val refreshPendingIntent = PendingIntent.getBroadcast(
            context, 1, refreshIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_refresh_button, refreshPendingIntent)

        val clickIntent = Intent(context, TodoWidgetProvider::class.java).apply {
            action = ACTION_TODO_COMPLETED
        }
        val clickPendingIntent = PendingIntent.getBroadcast(
            context, 2, clickIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setPendingIntentTemplate(R.id.widget_list_view, clickPendingIntent)

        val headerIntent = Intent(context, MainActivity::class.java)
        val headerPendingIntent = PendingIntent.getActivity(
            context, 3, headerIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_header, headerPendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view)
    }


    // todo : fix it
    private fun handleTodoCompleted(context: Context, todoId: Int) {
        Log.i("Adi", "handleTodoCompleted: $todoId")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val database = AppDatabase.getDatabase(context)
                database.todoDao().markTodoAsDone(todoId)
                refreshWidget(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun openAddTodoSheet(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(MainActivity.OPEN_ADD_TODO_SHEET, true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(intent)
    }

    private fun refreshWidget(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, TodoWidgetProvider::class.java)
        )
        for (appWidgetId in appWidgetIds) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view)
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        const val ACTION_TODO_COMPLETED = "TODO_COMPLETED"
        const val ACTION_ADD_TODO = "ADD_TODO"
        const val ACTION_REFRESH_WIDGET = "REFRESH_WIDGET"
        const val EXTRA_TODO_ID = "todo_id"

        fun updateWidget(context: Context) {
            val intent = Intent(context, TodoWidgetProvider::class.java).apply {
                action = ACTION_REFRESH_WIDGET
            }
            context.sendBroadcast(intent)
        }
    }
}