package com.example.austria_fojas_fitnessapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CalorieLimit extends AppCompatActivity {

    EditText inputCalorieLimit;
    Button btnSaveCalorieLimit;
    DatabaseHelper dbHelper;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_limit);

        dbHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);

        inputCalorieLimit = findViewById(R.id.inputCalorieLimit);
        btnSaveCalorieLimit = findViewById(R.id.btnSaveCalorieLimit);

        // Pre-fill current limit if it exists in DB
        int currentLimit = dbHelper.getCalorieLimit(userId);
        if (currentLimit > 0) {
            inputCalorieLimit.setText(String.valueOf(currentLimit));
        }

        btnSaveCalorieLimit.setOnClickListener(v -> {
            String limitStr = inputCalorieLimit.getText().toString().trim();
            if (limitStr.isEmpty()) {
                Toast.makeText(this, "Please enter a calorie limit", Toast.LENGTH_SHORT).show();
                return;
            }

            int newLimit = Integer.parseInt(limitStr);
            boolean success = dbHelper.updateCalorieLimit(userId, newLimit);

            if (success) {
                Toast.makeText(this, "Calorie limit updated!", Toast.LENGTH_SHORT).show();
                finish(); // Returns to Dashboard
            } else {
                Toast.makeText(this, "Failed to update limit", Toast.LENGTH_SHORT).show();
            }
        });
    }
}