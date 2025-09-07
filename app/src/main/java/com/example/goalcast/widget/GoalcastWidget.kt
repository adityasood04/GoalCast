package com.example.goalcast.widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.goalcast.R

class GoalcastWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @SuppressLint("RestrictedApi")
    @Composable
    private fun Content() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(R.color.widget_background)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Goalcast Goals",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(R.color.widget_text_primary),
                    fontSize = 18.sp
                )
            )
            Text(
                text = "Todo Text..",
                style = TextStyle(
                    color = ColorProvider(R.color.widget_text_secondary),
                    fontSize = 14.sp
                )
            )
        }
    }
}
