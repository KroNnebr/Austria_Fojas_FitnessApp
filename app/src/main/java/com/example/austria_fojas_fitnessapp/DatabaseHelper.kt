package com.example.austria_fojas_fitnessapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "FitnessApp.db"
        private const val DATABASE_VERSION = 1

        // Table 1: User Profile
        const val TABLE_PROFILE = "profile"
        const val COLUMN_PROFILE_ID = "id"
        const val COLUMN_WEIGHT = "weight"
        const val COLUMN_HEIGHT = "height"
        const val COLUMN_BMI = "bmi"

        // Table 2: Meal Logs
        const val TABLE_MEALS = "meals"
        const val COLUMN_MEAL_ID = "id"
        const val COLUMN_FOOD_NAME = "food_name"
        const val COLUMN_CALORIES = "calories"
        const val COLUMN_MEAL_TYPE = "meal_type"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createProfileTable = ("CREATE TABLE $TABLE_PROFILE (" +
                "$COLUMN_PROFILE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_WEIGHT REAL, " +
                "$COLUMN_HEIGHT REAL, " +
                "$COLUMN_BMI REAL)")

        val createMealsTable = ("CREATE TABLE $TABLE_MEALS (" +
                "$COLUMN_MEAL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_FOOD_NAME TEXT, " +
                "$COLUMN_CALORIES INTEGER, " +
                "$COLUMN_MEAL_TYPE TEXT)")

        db?.execSQL(createProfileTable)
        db?.execSQL(createMealsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PROFILE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MEALS")
        onCreate(db)
    }

    // --- PROFILE CRUD ---

    // Insert or Update Profile (Create/Update)
    fun saveProfile(weight: Double, height: Double, bmi: Double): Boolean {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_PROFILE") // Keep only latest profile record

        val values = ContentValues().apply {
            put(COLUMN_WEIGHT, weight)
            put(COLUMN_HEIGHT, height)
            put(COLUMN_BMI, bmi)
        }
        val result = db.insert(TABLE_PROFILE, null, values)
        db.close()
        return result != -1L
    }

    // Read Profile
    fun getProfile(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_PROFILE LIMIT 1", null)
    }

    // --- MEAL LOG CRUD ---

    // Create Meal Entry
    fun addMeal(foodName: String, calories: Int, mealType: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FOOD_NAME, foodName)
            put(COLUMN_CALORIES, calories)
            put(COLUMN_MEAL_TYPE, mealType)
        }
        val result = db.insert(TABLE_MEALS, null, values)
        db.close()
        return result != -1L
    }

    // Read All Meals
    fun getAllMeals(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_MEALS ORDER BY $COLUMN_MEAL_ID DESC", null)
    }

    // Update Meal Entry
    fun updateMeal(id: Int, foodName: String, calories: Int, mealType: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FOOD_NAME, foodName)
            put(COLUMN_CALORIES, calories)
            put(COLUMN_MEAL_TYPE, mealType)
        }
        val rowsAffected = db.update(TABLE_MEALS, values, "$COLUMN_MEAL_ID = ?", arrayOf(id.toString()))
        db.close()
        return rowsAffected > 0
    }

    // Delete Meal Entry
    fun deleteMeal(id: Int): Boolean {
        val db = this.writableDatabase
        val rowsDeleted = db.delete(TABLE_MEALS, "$COLUMN_MEAL_ID = ?", arrayOf(id.toString()))
        db.close()
        return rowsDeleted > 0
    }
}