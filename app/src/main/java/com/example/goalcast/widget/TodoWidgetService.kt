package com.example.goalcast.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViewsService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return TodoWidgetViewsFactory(this.applicationContext)
    }
}