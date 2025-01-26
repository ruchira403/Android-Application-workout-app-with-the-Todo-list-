package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class armworkout : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var skipButton: Button
    private lateinit var stopButton: Button
    private var isWorkout = true
    private var workoutIndex = 0
    private var timer: CountDownTimer? = null
    private val workoutTime = 2 * 60 * 1000L
    private val restTime = 1 * 10 * 1000L
    private lateinit var vibrator: Vibrator

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_armworkout)

        timerTextView = findViewById(R.id.timerTextView)
        startButton = findViewById(R.id.startButton)
        skipButton = findViewById(R.id.skipButton)
        stopButton = findViewById(R.id.stopButton)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        startButton.setOnClickListener {
            startNextTimer()
        }

        skipButton.setOnClickListener {
            skipCurrentTimer()
        }

        stopButton.setOnClickListener {
            stopTimer()
        }

        createNotificationChannel()
    }

    private fun startNextTimer() {
        val time = if (isWorkout) workoutTime else restTime

        timer?.cancel()
        timer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)

                // Display toast message when the workout timer is at 2 minutes
                if (isWorkout && millisUntilFinished in (2 * 60 * 1000L - 1000)..(2 * 60 * 1000L)) {
                    Toast.makeText(this@armworkout, "Start your workout!", Toast.LENGTH_SHORT).show()
                }

                // Display toast message when the rest timer reaches 10 seconds
                if (!isWorkout && millisUntilFinished in 10 * 1000L downTo 9 * 1000L) {
                    Toast.makeText(this@armworkout, "This is rest time!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFinish() {
                if (isWorkout) {
                    // Vibrate to indicate the end of the workout session
                    vibrator.vibrate(500)
                    isWorkout = false
                    timerTextView.text = "Rest"
                    startNextTimer()  // Automatically start the rest timer
                } else {
                    isWorkout = true
                    workoutIndex++
                    if (workoutIndex < 5) {
                        startNextTimer()  // Start the next workout session after rest
                    } else {
                        workoutComplete()  // Workout session complete
                    }
                }
            }
        }.start()
    }

    private fun skipCurrentTimer() {
        timer?.cancel()
        if (isWorkout) {
            timerTextView.text = "Skipped to Rest"
            isWorkout = false
        } else {
            workoutIndex++
            if (workoutIndex < 5) {
                timerTextView.text = "Skipped to Next Workout"
                isWorkout = true
            } else {
                workoutComplete()
            }
        }
        startNextTimer()
    }

    private fun stopTimer() {
        timer?.cancel()
        timerTextView.text = "Timer Stopped"
        Toast.makeText(this, "You stopped the time", Toast.LENGTH_SHORT).show()
    }

    private fun workoutComplete() {
        timerTextView.text = "Workout Complete!"
        sendNotification()
    }

    private fun sendNotification() {
        val intent = Intent(this, armworkout::class.java)
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(this, "armWorkoutChannel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Workout Complete")
            .setContentText("Keep going!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.notify(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Workout Notifications"
        val descriptionText = "Notification after workout is complete"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("armWorkoutChannel", name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
