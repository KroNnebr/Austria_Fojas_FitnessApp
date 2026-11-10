package com.example.austria_fojas_fitnessapp

import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class MealLogActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etFoodName: TextInputEditText
    private lateinit var etCalories: TextInputEditText
    private lateinit var spinnerMealType: Spinner
    private lateinit var btnSaveMeal: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_meal_log)

        // Window insets matching your layout's root scrollview ID
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        etFoodName = findViewById(R.id.etFoodName)
        etCalories = findViewById(R.id.etCalories)
        spinnerMealType = findViewById(R.id.spinnerMealType)
        btnSaveMeal = findViewById(R.id.btnSaveMeal)

        // Populate spinner with meal categories
        val mealCategories = arrayOf("Breakfast", "Lunch", "Dinner", "Snack")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mealCategories)
        spinnerMealType.adapter = adapter

        // Create Meal Entry (Create CRUD)
        btnSaveMeal.setOnClickListener {
            val foodName = etFoodName.text.toString().trim()
            val caloriesStr = etCalories.text.toString().trim()
            val mealType = spinnerMealType.selectedItem.toString()

            if (foodName.isEmpty() || caloriesStr.isEmpty()) {
                Toast.makeText(this, "Please enter food name and calories.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val calories = caloriesStr.toIntOrNull()
            if (calories == null || calories < 0) {
                Toast.makeText(this, "Please enter a valid calorie amount.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = dbHelper.addMeal(foodName, calories, mealType)
            if (success) {
                Toast.makeText(this, "Meal logged successfully!", Toast.LENGTH_SHORT).show()
                etFoodName.text?.clear()
                etCalories.text?.clear()
            } else {
                Toast.makeText(this, "Error saving meal. Try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}