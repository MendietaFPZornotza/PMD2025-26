package com.example.jostailudendajosu

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Erregistratu : AppCompatActivity() {

    lateinit var ErregGorde: Button

    lateinit var izenaInput: EditText
    lateinit var passinput: EditText
    lateinit var emailinput: EditText

    lateinit var emaku: RadioButton
    lateinit var gizon: RadioButton

    lateinit var Spam: CheckBox

    lateinit var espiner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_erregistratu)

        ErregGorde = findViewById(R.id.ErregBotoi)
        izenaInput = findViewById(R.id.ErregIzen)
        passinput = findViewById(R.id.ErregPass)
        emailinput = findViewById(R.id.Erregemail)
        emaku = findViewById(R.id.radioBotoia2)
        gizon = findViewById(R.id.radioBotoia)
        Spam = findViewById(R.id.chekeatu)
        espiner = findViewById(R.id.spinner)

        val ListaAukera = arrayOf("Valentzia", "Madrid", "Bartzelona")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ListaAukera)
        espiner.setAdapter(adapter)

        ErregGorde.setOnClickListener { registrarUsuario() }
    }
    private fun registrarUsuario() {
        val izena = izenaInput.text.toString()
        val email = emailinput.text.toString()
        val password = passinput.text.toString()
        val sexua = if (emaku.isChecked) "Femenino" else "Masculino"
        val spamJaso = Spam.isChecked
        val hiria = espiner.selectedItem.toString()

        if (izena.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Mesedez, bete kanpo guztiak", Toast.LENGTH_SHORT).show()
            return
        }

        val sqlAdmin = SqlAdmin(this, "pertsonakDB", null, 1)
        val db = sqlAdmin.writableDatabase

        val insert = "INSERT INTO pertsonak (izena, email, password, sexua, spamJaso, hiria) VALUES ('$izena', '$email', '$password', '$sexua', '$spamJaso', '$hiria')"
        db.execSQL(insert)
        db.close()

        Toast.makeText(this, "Erregistroa eginda!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}