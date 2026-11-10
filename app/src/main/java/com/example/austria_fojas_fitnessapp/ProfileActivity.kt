package com.example.austria_fojas_fitnessapp

import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class ProfileActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etWeight: TextInputEditText
    private lateinit var etHeight: TextInputEditText
    private lateinit var tvBmiResult: TextView
    private lateinit var btnCalculateBmi: Button
    private lateinit var btnSaveProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        // Window insets matching your layout's root ID
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        etWeight = findViewById(R.id.etWeight)
        etHeight = findViewById(R.id.etHeight)
        tvBmiResult = findViewById(R.id.tvBmiResult)
        btnCalculateBmi = findViewById(R.id.btnCalculateBmi)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)

        // Load existing profile data if available (Read CRUD)
        loadExistingProfile()

        // Calculate BMI button logic
        btnCalculateBmi.setOnClickListener {
            val bmi = calculateBmi()
            if (bmi != null) {
                tvBmiResult.text = String.format("BMI: %.2f", bmi)
            }
        }

        // Save Profile button logic (Create/Update CRUD)
        btnSaveProfile.setOnClickListener {
            val weightStr = etWeight.text.toString().trim()
            val heightStr = etHeight.text.toString().trim()

            if (weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(this, "Please enter both weight and height.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weight = weightStr.toDoubleOrNull()
            val height = heightStr.toDoubleOrNull()

            if (weight == null || height == null || weight <= 0 || height <= 0) {
                Toast.makeText(this, "Please enter valid numeric values.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bmi = weight / ((height / 100) * (height / 100))
            tvBmiResult.text = String.format("BMI: %.2f", bmi)

            val success = dbHelper.saveProfile(weight, height, bmi)
            if (success) {
                Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error saving profile. Try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateBmi(): Double? {
        val weightStr = etWeight.text.toString().trim()
        val heightStr = etHeight.text.toString().trim()

        if (weightStr.isEmpty() || heightStr.isEmpty()) {
            Toast.makeText(this, "Enter weight and height first.", Toast.LENGTH_SHORT).show()
            return null
        }

        val weight = weightStr.toDoubleOrNull()
        val height = heightStr.toDoubleOrNull()

        if (weight == null || height == null || height == 0.0) {
            Toast.makeText(this, "Invalid input values.", Toast.LENGTH_SHORT).show()
            return null
        }

        // Height is entered in cm, convert to meters for BMI formula: weight / (height_m)^2
        val heightMeters = height / 100.0
        return weight / (heightMeters * heightMeters)
    }

    private fun loadExistingProfile() {
        val cursor: Cursor? = dbHelper.getProfile()
        if (cursor != null && cursor.moveToFirst()) {
            val weightIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_WEIGHT)
            val heightIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_HEIGHT)
            val bmiIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_BMI)

            if (weightIdx != -1 && heightIdx != -1 && bmiIdx != -1) {
                val weight = cursor.getDouble(weightIdx)
                val height = cursor.getDouble(heightIdx)
                val bmi = cursor.getDouble(bmiIdx)

                etWeight.setText(weight.toString())
                etHeight.setText(height.toString())
                tvBmiResult.text = String.format("BMI: %.2f", bmi)
            }
            cursor.close()
        }
    }
}