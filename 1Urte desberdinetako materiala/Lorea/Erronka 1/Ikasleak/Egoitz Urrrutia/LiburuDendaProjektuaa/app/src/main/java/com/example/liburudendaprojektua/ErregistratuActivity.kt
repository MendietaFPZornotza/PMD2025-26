package com.example.liburudendaprojektua

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ErregistratuActivity : AppCompatActivity() {
    private lateinit var izena: EditText
    private lateinit var korreo: EditText
    private lateinit var pasahitza: EditText
    private lateinit var generoa: RadioGroup
    private lateinit var irizpideak: CheckBox
    private lateinit var hiriak: Spinner
    private lateinit var registro: Button
    private lateinit var itzuli: Button
    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.erregistratu)

        dbHelper = AdminSQLiteOpenHelper(this, "usuariosDB", null, 2)

        izena = findViewById(R.id.izena)
        korreo = findViewById(R.id.korreo)
        pasahitza = findViewById(R.id.pasahitza)
        generoa = findViewById(R.id.radioGroup)
        irizpideak = findViewById(R.id.irizpideak)
        hiriak = findViewById(R.id.hiriak)
        registro = findViewById(R.id.registro)
        itzuli = findViewById(R.id.itzuli)

        itzuli.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Spinnerra konfiguratu
        ArrayAdapter.createFromResource(
            this,
            R.array.hiriak_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            hiriak.adapter = adapter
        }

        registro.setOnClickListener {
            if (validateInputs()) {
                registerUser()
            }
        }
    }
//Inputak Balidatu
    private fun validateInputs(): Boolean {
        if (izena.text.toString().isEmpty()) {
            izena.error = "Izena sartu mesedez"
            return false
        }

        if (korreo.text.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(korreo.text.toString()).matches()) {
            korreo.error = "Posta zuzen bat sartu"
            return false
        }

        if (pasahitza.text.toString().isEmpty()) {
            pasahitza.error = "Pasahitza sartu mesedez"
            return false
        }

        if (generoa.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Genero bat aukeratu", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!irizpideak.isChecked) {
            Toast.makeText(this, "Jakinarazpen irizpideak onartu", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registerUser() {
        val nombre = izena.text.toString()
        val correo = korreo.text.toString()
        val password = pasahitza.text.toString()
        val generoId = generoa.checkedRadioButtonId
        val genero = findViewById<RadioButton>(generoId).text.toString()
        val ciudad = hiriak.selectedItem.toString()

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("correo", correo)
            put("password", password)
            put("genero", genero)
            put("ciudad", ciudad)
        }

        val newRowId = db.insert("usuarios", null, values)
        db.close()
//Erregistroa ondo dagoen edo ez irtentzen da
        if (newRowId != -1L) {
            Toast.makeText(this, "Erregistro zuzena", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Errorea erregistroan", Toast.LENGTH_SHORT).show()
        }
    }
}
