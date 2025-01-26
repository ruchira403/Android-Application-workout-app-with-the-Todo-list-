package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class userprofile : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_userprofile)


        nameTextView = findViewById(R.id.name)
        ageTextView = findViewById(R.id.age)
        genderTextView = findViewById(R.id.tele)
        addressTextView = findViewById(R.id.address)
        cityTextView = findViewById(R.id.textView14)
        countryTextView = findViewById(R.id.textView11)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.homeButton)
        backButton = findViewById(R.id.backButton)


        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        loadUserProfile(sharedPreferences)


        editButton.setOnClickListener {

            val intent = Intent(this@userprofile, registerform::class.java)
            startActivity(intent)
        }


        deleteButton.setOnClickListener {
            confirmDelete(sharedPreferences)
        }


        backButton.setOnClickListener {
            val intent = Intent(this@userprofile, homepage::class.java)
            startActivity(intent)
        }
    }

    private fun loadUserProfile(sharedPreferences: SharedPreferences) {
        nameTextView.text = sharedPreferences.getString("name", "")
        ageTextView.text = sharedPreferences.getString("age", "")
        genderTextView.text = sharedPreferences.getString("gender", "")
        addressTextView.text = sharedPreferences.getString("address", "")
        cityTextView.text = sharedPreferences.getString("city", "")
        countryTextView.text = sharedPreferences.getString("country", "")
    }

    private fun confirmDelete(sharedPreferences: SharedPreferences) {
        // Show confirmation dialog before deleting
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Are you sure you want to delete your profile?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                deleteUserProfile(sharedPreferences)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Confirm Delete")
        alert.show()
    }

    private fun deleteUserProfile(sharedPreferences: SharedPreferences) {

        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()


        val intent = Intent(this@userprofile, userprofile::class.java)
        startActivity(intent)


        finish()
    }
}
