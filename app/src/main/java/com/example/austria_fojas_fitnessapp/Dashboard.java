package com.example.austria_fojas_fitnessapp;
/*
HOLY FUCK ANDAMI CHECKLIST (Y = Check)
- Logout = Y
- Weight, Height, BMI textview change = Y
- Budget textview change = Y
- Date textview change = Y
- Date picker = Y
- Set calories button = Y
- Set meals button = Y
- Update profile button = Y
- Progress Bar = Y
 */


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Dashboard extends AppCompatActivity {

    TextView weight, height, bmi, remainingBudget, date, budget;
    Button btnPreviousDate, btnNextDate, btnLogout, btnSetCalories, btnSetMeals, btnUpdateProfile;
    Calendar selectedDate = Calendar.getInstance();
    DatabaseHelper dbHelper;
    ProgressBar radialProgress;

    private void openCalendar(int userId) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Dashboard.this,
                (datePicker, year, month, day) -> {
                    selectedDate.set(year, month, day);
                    updateCal(userId);
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void updateCal(int userId) {
        SimpleDateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        date.setText(displayFormat.format(selectedDate.getTime()));
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String selectedDateString = dbFormat.format(selectedDate.getTime());
        int totalCalories = dbHelper.getDailyCalories(userId, selectedDateString);

        budget.setText("Budget: " + totalCalories + " kcal");
        int calorieLimit = dbHelper.getCalorieLimit(userId);
        int remainingCalories = calorieLimit - totalCalories;
        remainingBudget.setText(remainingCalories + " kcal");

        // Calculate progress percentage
        int progress = 0;
        if (calorieLimit > 0) {
            progress = (int) ((totalCalories * 100.0) / calorieLimit);
        }

        // Keep progress between 0 and 100
        progress = Math.max(0, Math.min(progress, 100));
        radialProgress.setMax(100);
        radialProgress.setProgress(progress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        int userId = getIntent().getIntExtra("USER_ID", -1);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // TextViews
        weight = findViewById(R.id.txtWeight);
        height = findViewById(R.id.txtHeight);
        bmi = findViewById(R.id.txtBMI);
        remainingBudget = findViewById(R.id.txtRemainingBudget);
        date = findViewById(R.id.txtDate);
        budget = findViewById(R.id.txtBudget);

        // Buttons
        btnPreviousDate = findViewById(R.id.btnPreviousDate);
        btnNextDate = findViewById(R.id.btnNextDate);
        btnLogout = findViewById(R.id.btnLogout);
        btnSetCalories = findViewById(R.id.btnSetCalories);
        btnSetMeals = findViewById(R.id.btnSetMeals);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        // Progress Bar
        radialProgress = findViewById(R.id.radialProgress);

        // Logout
        btnLogout.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Date Picker
        updateCal(userId);

        date.setOnClickListener(view -> openCalendar(userId));

        btnPreviousDate.setOnClickListener(view -> {
            selectedDate.add(Calendar.DAY_OF_MONTH, -1);
            updateCal(userId);
        });

        btnNextDate.setOnClickListener(view -> {
            selectedDate.add(Calendar.DAY_OF_MONTH, 1);
            updateCal(userId);
        });

        // Set textview values
        weight.setText("● " + dbHelper.getWeight(userId) + " kg");
        height.setText("● " + dbHelper.getHeight(userId) + " cm");
        bmi.setText("● " + dbHelper.getBMI(userId) + " kg/m^2");

        // Set calorie limit
        btnSetCalories.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, CalorieLimit.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        btnSetMeals.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, MealLogActivity.class);
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String selectedDateString = dbFormat.format(selectedDate.getTime());
            intent.putExtra("USER_ID", userId);
            intent.putExtra("MEAL_DATE", selectedDateString);
            startActivity(intent);
        });

        btnUpdateProfile.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, ProfileActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

    }
}
