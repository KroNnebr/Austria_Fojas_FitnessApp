package com.example.austria_fojas_fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextInputEditText user = findViewById(R.id.getUser);
        TextInputEditText password = findViewById(R.id.getPassword);
        TextInputEditText confirmPassword = findViewById(R.id.getConfirmPassword);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Button RegisterConf = findViewById(R.id.btnSignUp);
        Button LoginSwitch = findViewById(R.id.switchLogin);

        RegisterConf.setOnClickListener(v -> {
            String userName = user.getText().toString().trim();
            String userPassword = password.getText().toString();
            String userConfirmPassword = confirmPassword.getText().toString();

                if (userName.isEmpty() || userPassword.isEmpty() || userConfirmPassword.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!userPassword.equals(userConfirmPassword)){
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(userPassword.length() < 8){
                    Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!userPassword.matches(".*[A-Z].*")){
                    Toast.makeText(this, "Password must contain at least one uppercase letter", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!userPassword.matches(".*[a-z].*")){
                    Toast.makeText(this, "Password must contain at least one lowercase letter", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!userPassword.matches(".*\\d.*")){
                    Toast.makeText(this, "Password must contain at least one number", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean success = dbHelper.registerUser(userName, userPassword);

                if (success) {
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                } else {
                    Toast.makeText(this, "Registration Failed. Username may already exist.", Toast.LENGTH_SHORT).show();
                }
            });


        LoginSwitch.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });


    }

}