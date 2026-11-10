package com.example.austria_fojas_fitnessapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find your 'Get Started' button from activity_main.xml
        val btnStart = findViewById<Button>(R.id.btnStart)

        // Use an explicit Intent to navigate to ProfileActivity
        btnStart.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            // Optional: Pass data between screens if needed
            // intent.putExtra("USERNAME", "Franz")
            startActivity(intent)
        }
    }
}