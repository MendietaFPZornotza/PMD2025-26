package com.example.erronka.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.erronka.R
import com.example.erronka.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

        // View Binding: crea un objeto que da acceso directo a todas las vistas
        // del layout (sin findViewById)
        private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Inflamos (construimos) el layout usando View Binding
            binding = ActivityMainBinding.inflate(layoutInflater)
            // Asignamos la vista raíz al contenido de la actividad
            setContentView(binding.root)

            // Listener del botón de login
            binding.loginButton.setOnClickListener {
                // Obtenemos el texto de los campos de usuario y contraseña, eliminando espacios
                val username = binding.usernameEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString().trim()

                // Verificamos si los campos están vacíos
                if (username.isEmpty() || password.isEmpty()) {
                    // Mostramos un mensaje si falta algún campo
                    Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    // Simulamos una validación sencilla de usuario/contraseña
                    if (username == "admin" && password == "1234") {
                        Toast.makeText(this, "Login correcto ✅", Toast.LENGTH_SHORT).show()
                        // Aquí podrías abrir otra actividad si quisieras:
                         startActivity(Intent(this, SelectionActivity::class.java))
                    } else {
                        Toast.makeText(this, "Usuario o contraseña incorrectos ❌", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }