package com.example.myapplication

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class UpcomingEventsWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            // Get the alarm description from SharedPreferences
            val sharedPreferences = context.getSharedPreferences("ALARM_PREFS", Context.MODE_PRIVATE)
            val description = sharedPreferences.getString("ALARM_DESCRIPTION", "No upcoming events")

            // Update the widget's UI
            val views = RemoteViews(context.packageName, R.layout.widget_upcoming_events)
            views.setTextViewText(R.id.textUpcomingEventDescription, description)

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
