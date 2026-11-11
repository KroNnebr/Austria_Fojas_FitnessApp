package com.example.austria_fojas_fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText etName, etAge, etWeight, etHeight;
    private Button btnSaveProfile, btnMealLog;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnMealLog = findViewById(R.id.btnMealLog);

        btnMealLog.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MealLogActivity.class);
            startActivity(intent);
        });
    }
}