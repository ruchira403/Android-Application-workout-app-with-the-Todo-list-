package com.example.myapplication

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class homepage : AppCompatActivity() {
    private val CHANNEL_ID = "ReminderChannel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_homepage)


        createNotificationChannel()


        showReminderNotifications()

        val button2 = findViewById<Button>(R.id.button7)
        button2.setOnClickListener {
            showConfirmationMessage(
                "Time to start your workout!",
               "Don't forget to check the muscles page.",
                muclepage1::class.java
            )
        }

        val button3 = findViewById<Button>(R.id.button9)
        button3.setOnClickListener {
            showConfirmationMessage(
                "Don't forget to set the alarm!",
                "Set your workout alarm to stay on track.",
                setalarm::class.java
            )
        }

        val button4 = findViewById<Button>(R.id.button6)
        button4.setOnClickListener {

            val intent = Intent(this, userprofile::class.java)
            startActivity(intent)
        }
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Reminder Notifications"
            val descriptionText = "Channel for reminder notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showReminderNotifications() {
        val notificationManager = NotificationManagerCompat.from(this)


        val reminder1Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your own icon
            .setContentTitle("Workout Reminder")
            .setContentText("Time to start your workout! Don't forget to check the muscles page.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)


        val reminder2Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your own icon
            .setContentTitle("Alarm Reminder")
            .setContentText("Don't forget to set the alarm! Set your workout alarm to stay on track.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(1, reminder1Builder.build())
        notificationManager.notify(2, reminder2Builder.build())
    }

    private fun showConfirmationMessage(title: String, message: String, nextActivity: Class<*>) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                // Navigate to the selected activity after confirmation
                val intent = Intent(this, nextActivity)
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }
}
