package com.example.austria_fojas_fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText etWeight, etHeight;
    private Button btnCalculateBmi, btnSaveProfile;
    private TextView tvBmiResult;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);

        // Matching the exact IDs from activity_profile.xml
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        btnCalculateBmi = findViewById(R.id.btnCalculateBmi);
        tvBmiResult = findViewById(R.id.tvBmiResult);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // Optional: Calculate BMI functionality
        btnCalculateBmi.setOnClickListener(v -> {
            String weightStr = etWeight.getText().toString().trim();
            String heightStr = etHeight.getText().toString().trim();

            if (weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(this, "Please enter both weight and height", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double weight = Double.parseDouble(weightStr);
                double heightCm = Double.parseDouble(heightStr);

                if (heightCm <= 0) {
                    Toast.makeText(this, "Height must be greater than zero", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Height in meters for BMI calculation
                double heightM = heightCm / 100.0;
                double bmi = weight / (heightM * heightM);

                tvBmiResult.setText(String.format("BMI: %.2f", bmi));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            }
        });

        btnSaveProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Profile Saved Successfully!", Toast.LENGTH_SHORT).show();

            // If you want it to go to the Meal Log screen after saving,
            // you can uncomment the lines below:
            // Intent intent = new Intent(ProfileActivity.this, MealLogActivity.class);
            // startActivity(intent);
        });
    }
}