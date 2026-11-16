
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

    private int userId;
    private double weight;
    private double height;
    private double bmi;
    private boolean isBmiCalculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userId = getIntent().getIntExtra("USER_ID", -1);

        dbHelper = new DatabaseHelper(this);

        // Matching the IDs from activity_profile.xml
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        btnCalculateBmi = findViewById(R.id.btnCalculateBmi);
        tvBmiResult = findViewById(R.id.tvBmiResult);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // Calculate BMI button
        btnCalculateBmi.setOnClickListener(v -> {

            String weightStr = etWeight.getText().toString().trim();
            String heightStr = etHeight.getText().toString().trim();

            if (weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(this, "Please enter both weight and height", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                weight = Double.parseDouble(weightStr);
                height = Double.parseDouble(heightStr);

                if (weight <= 0 || height <= 0) {
                    Toast.makeText(this, "Weight and height must be greater than zero", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Height in meters for BMI calculation
                double heightM = height / 100.0;
                bmi = weight / (heightM * heightM);

                tvBmiResult.setText(String.format("BMI: %.2f", bmi));

                isBmiCalculated = true;

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            }
        });

        // Save Profile button
        btnSaveProfile.setOnClickListener(v -> {

            if (!isBmiCalculated) {
                Toast.makeText(this, "Please calculate BMI first", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.saveProfile(userId, weight, height, bmi);

            if (success) {
                Toast.makeText(this, "Profile Saved Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ProfileActivity.this, Dashboard.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Error Saving Profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}