package com.example.mvc_passwordapp.view

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mvc_passwordapp.R

// Bigarren Activity-a, pasahitz zuzena egitearen ondoren bistaratuko dena.
class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Testu sinple bat erakutsi
        val textWelcome: TextView = findViewById(R.id.textWelcome)
        textWelcome.text = "Ongi etorri aplikaziora!"

        // (Opcional) Itzultzeko botoia gehitu nahi baduzu, hemen kudeatu dezakezu:
        // val buttonBack: Button = findViewById(R.id.buttonBack)
        // buttonBack.setOnClickListener { finish() }
    }
}