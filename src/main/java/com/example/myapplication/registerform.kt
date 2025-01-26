package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class registerform : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var genderEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registerform)


        nameEditText = findViewById(R.id.editTextText)
        ageEditText = findViewById(R.id.editTextNumber)
        genderEditText = findViewById(R.id.editTextTextEmailAddress)
        addressEditText = findViewById(R.id.editTextText4)
        cityEditText = findViewById(R.id.editTextText5)
        countryEditText = findViewById(R.id.editTextText6)
        submitButton = findViewById(R.id.button4)

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)


        loadUserProfile()

        submitButton.setOnClickListener {
            saveUserProfile()


            val intent = Intent(this@registerform, homepage::class.java)
            startActivity(intent)
        }
    }

    private fun loadUserProfile() {

        nameEditText.setText(sharedPreferences.getString("name", ""))
        ageEditText.setText(sharedPreferences.getString("age", ""))
        genderEditText.setText(sharedPreferences.getString("gender", ""))
        addressEditText.setText(sharedPreferences.getString("address", ""))
        cityEditText.setText(sharedPreferences.getString("city", ""))
        countryEditText.setText(sharedPreferences.getString("country", ""))
    }

    private fun saveUserProfile() {

        val editor = sharedPreferences.edit()
        editor.putString("name", nameEditText.text.toString())
        editor.putString("age", ageEditText.text.toString())
        editor.putString("gender", genderEditText.text.toString())
        editor.putString("address", addressEditText.text.toString())
        editor.putString("city", cityEditText.text.toString())
        editor.putString("country", countryEditText.text.toString())
        editor.apply()
    }
}
