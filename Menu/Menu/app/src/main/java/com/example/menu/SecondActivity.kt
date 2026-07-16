package com.example.menu

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    private lateinit var buttonBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Inicializar el botón
        buttonBack = findViewById(R.id.buttonBack)

        // Configurar el listener del botón
        buttonBack.setOnClickListener {
            // Cerrar la SecondActivity y volver a la MainActivity
            finish()
        }
    }
}

