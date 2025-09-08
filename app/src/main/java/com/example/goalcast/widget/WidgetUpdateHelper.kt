package com.example.goalcast.widget

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetUpdateHelper @Inject constructor() {

    fun updateWidgets(context: Context) {
        TodoWidgetProvider.updateWidget(context)
    }
}