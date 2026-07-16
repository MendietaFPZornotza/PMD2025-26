package com.example.proiektua

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        // Instalar pantalla de splash
        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Mantener la pantalla de splash hasta una condición (actualizar según sea necesario)
        screenSplash.setKeepOnScreenCondition { false }

        // Llamada para obtener todos los usuarios y mostrarlos en Logcat
        val dbHelper = AdminSQLiteOpenHelper(this)
        val users = dbHelper.getAllUsers()
        users.forEach { user ->
            Log.d("UserList", user)  // Muestra cada usuario en la consola de Logcat
        }


        // Referencias a los elementos de la interfaz de usuario
        val emailEditText = findViewById<EditText>(R.id.loginEmail)
        val passwordEditText = findViewById<EditText>(R.id.loginPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // Configura el botón de registro para abrir la actividad de registro
        registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, ErregistroaActivity::class.java)
            startActivity(intent)
        }

        // Acción del botón de login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validación simple
            if (email.isNotEmpty() && password.isNotEmpty()) {
                val db = dbHelper.readableDatabase
                val cursor = db.rawQuery(
                    "SELECT username FROM users WHERE email = ? AND password = ?",
                    arrayOf(email, password)
                )

                if (cursor.moveToFirst()) {
                    // Get the username from the cursor
                    val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))

                    // Login successful, redirect to the main activity
                    Toast.makeText(this, "Ongi etorri, $username!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    // para que el nombre del usuario aparezca en el home
                    intent.putExtra("USERNAME", username)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Email edo pasahitza okerra", Toast.LENGTH_SHORT).show()
                }
                cursor.close()
                db.close()
            } else {
                Toast.makeText(this, "Bete datu guztiak", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
