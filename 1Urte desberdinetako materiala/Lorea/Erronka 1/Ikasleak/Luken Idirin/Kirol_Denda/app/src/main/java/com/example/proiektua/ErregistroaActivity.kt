package com.example.proiektua

import android.content.ContentValues
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern

class ErregistroaActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_erregistroa)

        // Inicializar el helper de la base de datos
        dbHelper = AdminSQLiteOpenHelper(this)

        // Referencias a los elementos de la interfaz de usuario
        val usernameEditText = findViewById<EditText>(R.id.username)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)
        val notificationCheckBox = findViewById<CheckBox>(R.id.notificationPref)
        val citySpinner = findViewById<Spinner>(R.id.citySpinner)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // Configuración del Spinner para la selección de ciudad
        val hiriak = arrayOf("Madril", "Bartzelona", "Valentzia")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, hiriak)
        citySpinner.adapter = adapter

        // Acción del botón de registro
        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val selectedGenderId = genderGroup.checkedRadioButtonId
            val selectedGender = findViewById<RadioButton>(selectedGenderId)?.text.toString()
            val notificationsEnabled = if (notificationCheckBox.isChecked) 1 else 0
            val selectedCity = citySpinner.selectedItem.toString()

            // Validación de los datos
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && selectedGender.isNotEmpty() && selectedCity.isNotEmpty()) {
                val emailValid = isValidEmail(email)
                val passwordValid = isValidPassword(password)

                when {
                    !emailValid -> Toast.makeText(this, "Email-a ez da zuzena", Toast.LENGTH_SHORT).show()
                    !passwordValid -> Toast.makeText(this, "Pasahitza gutxienez 6 karaktere izan behar ditu", Toast.LENGTH_SHORT).show()
                    else -> {
                        // Insertar en la base de datos
                        val db = dbHelper.writableDatabase
                        val values = ContentValues().apply {
                            put("username", username)
                            put("email", email)
                            put("password", password)
                            put("gender", selectedGender)
                            put("city", selectedCity)
                            put("notification_pref", notificationsEnabled)
                        }
                        val newRowId = db.insert("users", null, values)
                        if (newRowId != -1L) {
                            Toast.makeText(this, "Erregistroa arrakastaz burutu da!", Toast.LENGTH_SHORT).show()
                            finish() // Cierra la actividad y vuelve a la de login
                        } else {
                            Toast.makeText(this, "Errorea gertatu da erregistroan", Toast.LENGTH_SHORT).show()
                        }
                        db.close()
                    }
                }
            } else {
                Toast.makeText(this, "Bete datu guztiak", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Validación de correo electrónico
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")
        return emailPattern.matcher(email).matches()
    }

    // Validación de contraseña (mínimo 6 caracteres)
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6 // Solo requiere que la longitud sea mayor o igual a 6
    }
}

