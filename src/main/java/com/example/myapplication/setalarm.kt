package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class setalarm : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var buttonSetAlarm: Button
    private lateinit var editTextDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setalarm)

        timePicker = findViewById(R.id.timePicker)
        buttonSetAlarm = findViewById(R.id.buttonSetAlarm)
        editTextDescription = findViewById(R.id.editTextDescription)

        buttonSetAlarm.setOnClickListener {
            setAlarm()
        }
    }

    private fun setAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
        calendar.set(Calendar.MINUTE, timePicker.minute)
        calendar.set(Calendar.SECOND, 0)

        // Get the description from the EditText
        val description = editTextDescription.text.toString()

        // Save the description in SharedPreferences
        val sharedPreferences = getSharedPreferences("ALARM_PREFS", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("ALARM_DESCRIPTION", description).apply()

        // Update the widget
        updateWidget(description)

        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("ALARM_DESCRIPTION", description)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        Toast.makeText(this, "Alarm set for ${timePicker.hour}:${timePicker.minute}", Toast.LENGTH_SHORT).show()
    }

    private fun updateWidget(description: String) {
        val intent = Intent(this, UpcomingEventsWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(application)
            .getAppWidgetIds(ComponentName(application, UpcomingEventsWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }
}
