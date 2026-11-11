package com.example.austria_fojas_fitnessapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MealLogActivity extends AppCompatActivity {

    private EditText etFoodName, etCalories;
    private Button btnSaveMeal;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_log);

        dbHelper = new DatabaseHelper(this);

        etFoodName = findViewById(R.id.etFoodName);
        etCalories = findViewById(R.id.etCalories);
        btnSaveMeal = findViewById(R.id.btnSaveMeal);

        btnSaveMeal.setOnClickListener(v -> {
            String food = etFoodName.getText().toString().trim();
            String caloriesStr = etCalories.getText().toString().trim();

            if (food.isEmpty() || caloriesStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_FOOD_NAME, food);
            values.put(DatabaseHelper.COLUMN_CALORIES, Integer.parseInt(caloriesStr));

            long newRowId = db.insert(DatabaseHelper.TABLE_MEALS, null, values);

            if (newRowId != -1) {
                Toast.makeText(this, "Meal Saved Successfully!", Toast.LENGTH_SHORT).show();
                etFoodName.setText("");
                etCalories.setText("");
            } else {
                Toast.makeText(this, "Error Saving Meal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}