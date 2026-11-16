package com.example.austria_fojas_fitnessapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MealLogActivity extends AppCompatActivity {

    private EditText etFoodName, etCalories;
    private Button btnSaveMeal;
    private DatabaseHelper dbHelper;
    private Spinner spinnerPreviousMeals;
    private int userId;
    private String mealDate;
    private ArrayList<String> foodList;
    private ArrayList<Integer> calorieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_log);
        userId = getIntent().getIntExtra("USER_ID", -1);
        mealDate = getIntent().getStringExtra("MEAL_DATE");

        dbHelper = new DatabaseHelper(this);

        etFoodName = findViewById(R.id.etFoodName);
        etCalories = findViewById(R.id.etCalories);
        btnSaveMeal = findViewById(R.id.btnSaveMeal);
        spinnerPreviousMeals = findViewById(R.id.pastMealDD);
        foodList = new ArrayList<>();
        calorieList = new ArrayList<>();

        loadPreviousMeals();
        spinnerPreviousMeals.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                        if (position > 0) {
                            etFoodName.setText(foodList.get(position));
                            etCalories.setText(String.valueOf(calorieList.get(position)));
                        }
                    }
                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {
                    }
                });

        btnSaveMeal.setOnClickListener(v -> {
            String food = etFoodName.getText().toString().trim();
            String caloriesStr = etCalories.getText().toString().trim();

            if (food.isEmpty() || caloriesStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_MEAL_USER_ID, userId);
            values.put(DatabaseHelper.COLUMN_FOOD_NAME, food);
            values.put(DatabaseHelper.COLUMN_CALORIES, Integer.parseInt(caloriesStr));
            values.put(DatabaseHelper.COLUMN_MEAL_DATE, mealDate);

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

    private void loadPreviousMeals() {
        foodList.clear();
        calorieList.clear();
        foodList.add("Select Previous Food");
        calorieList.add(0);
        Cursor cursor = dbHelper.getPrevMeal(userId);

        while (cursor.moveToNext()) {
            foodList.add(cursor.getString(0));
            calorieList.add(cursor.getInt(1));
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, foodList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPreviousMeals.setAdapter(adapter);
    }
}