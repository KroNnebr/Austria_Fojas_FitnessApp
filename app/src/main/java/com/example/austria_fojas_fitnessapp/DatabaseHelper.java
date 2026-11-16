
package com.example.austria_fojas_fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AustriaFojasFitness.db";

    // User table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Meal table
    public static final String TABLE_MEALS = "meals";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_MEAL_USER_ID = "user_id";
    public static final String COLUMN_FOOD_NAME = "food_name";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_MEAL_DATE = "meal_date";

    // Profile table
    public static final String TABLE_PROFILES = "profiles";
    public static final String COLUMN_PROFILE_USER_ID = "user_id";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_BMI = "bmi";
    public static final String COLUMN_CALORIE_LIMIT = "calorie_limit";

    // SQL statements
    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USERNAME + " TEXT UNIQUE," +
                    COLUMN_PASSWORD + " TEXT)";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_MEALS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MEAL_USER_ID + " INTEGER NOT NULL, " +
                    COLUMN_FOOD_NAME + " TEXT, " +
                    COLUMN_CALORIES + " INTEGER, " +
                    COLUMN_MEAL_DATE + " TEXT NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_MEAL_USER_ID + ") REFERENCES " +
                    TABLE_USERS + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE)";

    private static final String SQL_CREATE_PROFILES =
            "CREATE TABLE " + TABLE_PROFILES + " (" +
                    COLUMN_PROFILE_USER_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_WEIGHT + " REAL," +
                    COLUMN_HEIGHT + " REAL," +
                    COLUMN_BMI + " REAL," +
                    COLUMN_CALORIE_LIMIT + " INTEGER," +
                    "FOREIGN KEY (" + COLUMN_PROFILE_USER_ID + ") REFERENCES " +
                    TABLE_USERS + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_MEALS;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_PROFILES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);

        return result != -1;
    }

    public int loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT user_id FROM users WHERE username = ? AND password = ?",
                new String[]{username, password}
        );

        int userId = -1;

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }

        cursor.close();
        return userId;
    }

    // Save profile and calorie limit
    public boolean saveProfile(int userId, double weight, double height, double bmi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILE_USER_ID, userId);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_HEIGHT, height);
        values.put(COLUMN_BMI, bmi);

        long result = db.insertWithOnConflict(
                TABLE_PROFILES,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        return result != -1;
    }

    // Get the user's calorie limit
    public int getCalorieLimit(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT calorie_limit FROM profiles WHERE user_id = ?",
                new String[]{String.valueOf(userId)}
        );

        int calorieLimit = 0;
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            calorieLimit = cursor.getInt(0);
        }
        cursor.close();
        return calorieLimit;
    }

    // Get daily calories consumed
    public int getDailyCalories(int userId, String mealDate) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(calories) FROM meals " +
                        "WHERE user_id = ? AND meal_date = ?",
                new String[]{
                        String.valueOf(userId),
                        mealDate
                }
        );
        int totalCalories = 0;
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            totalCalories = cursor.getInt(0);
        }
        cursor.close();
        return totalCalories;
    }

    public double getWeight(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT weight FROM profiles WHERE user_id = ?",
                new String[]{String.valueOf(userId)}
        );

        double weight = 0;

        if (cursor.moveToFirst()) {
            weight = cursor.getDouble(0);
        }

        cursor.close();
        return weight;
    }

    public double getHeight(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT height FROM profiles WHERE user_id = ?",
                new String[]{String.valueOf(userId)}
        );

        double height = 0;

        if (cursor.moveToFirst()) {
            height = cursor.getDouble(0);
        }

        cursor.close();
        return height;
    }

    public double getBMI(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT bmi FROM profiles WHERE user_id = ?",
                new String[]{String.valueOf(userId)}
        );
        double bmi = 0;

        if (cursor.moveToFirst()) {
            bmi = cursor.getDouble(0);
        }
        cursor.close();
        return bmi;
    }

    public Cursor getPrevMeal(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT DISTINCT food_name, calories FROM meals WHERE user_id = ?",
                new String[]{String.valueOf(userId)}
        );
    }

}
