package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Vibrator
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    private val CHANNEL_ID = "alarm_channel"

    override fun onReceive(context: Context?, intent: Intent?) {
        // Get the description from the intent
        val description = intent?.getStringExtra("ALARM_DESCRIPTION") ?: "No description provided"

        // Save the description to shared preferences (so the widget can access it)
        val sharedPreferences = context?.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putString("upcoming_event", description)?.apply()

        // Update the widget
        context?.let {
            updateWidget(it, description)
        }

        // Display a toast message
        Toast.makeText(context, "Alarm! Description: $description", Toast.LENGTH_LONG).show()

        // Send the notification
        sendNotification(context, description)

        // Vibrate the device
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(1000)
        }
    }

    private fun updateWidget(context: Context, description: String) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetComponent = ComponentName(context, UpcomingEventsWidget::class.java)
        val widgetIds = appWidgetManager.getAppWidgetIds(widgetComponent)

        // Loop through all widget instances and update them
        for (widgetId in widgetIds) {
            // Create a RemoteViews object to update the widget's layout
            val views = RemoteViews(context.packageName, R.layout.widget_upcoming_events)
            views.setTextViewText(R.id.textUpcomingEventDescription, description)

            // Update the widget
            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }

    private fun sendNotification(context: Context?, description: String) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Alarm Notification", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Alarm")
            .setContentText("TIME TO START YOUR WORKS\nDescription: $description")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
