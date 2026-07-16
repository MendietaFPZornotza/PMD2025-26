package com.example.jostailudendajosu

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Erregistratu : AppCompatActivity() {

    lateinit var ErregGorde: Button
    lateinit var irten: Button

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
        irten = findViewById(R.id.irten)
        ErregGorde = findViewById(R.id.ErregBotoi)
        izenaInput = findViewById(R.id.ErregIzen)
        passinput = findViewById(R.id.ErregPass)
        emailinput = findViewById(R.id.Erregemail)
        emaku = findViewById(R.id.radioBotoia1)
        gizon = findViewById(R.id.radioBotoia2)
        Spam = findViewById(R.id.chekeatu)
        espiner = findViewById(R.id.spinner)

        val ListaAukera = arrayOf("Busturia", "Gernika", "Bermeo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ListaAukera)
        espiner.setAdapter(adapter)

        ErregGorde.setOnClickListener { registrarUsuario() }
        irten.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }
    }

    //Erabiltzailea erregistratzeko funtizoa (Insert)
    private fun registrarUsuario() {
        val izena = izenaInput.text.toString()
        val email = emailinput.text.toString()
        val password = passinput.text.toString()
        val sexua = if (emaku.isChecked) "emakumea" else "gizona"
        val spamJaso = Spam.isChecked
        val hiria = espiner.selectedItem.toString()

        if (izena.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Mesedez, bete kanpo guztiak", Toast.LENGTH_SHORT).show()
            return
        }

        val sqlAdmin = SqlAdmin(this, "pertsonakDB", null, 1)
        val db = sqlAdmin.writableDatabase
        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(this, "Emailaren formatua ez da zuzena", Toast.LENGTH_SHORT).show()
            return
        }

        if (emailesiste(db, email)) {
            Toast.makeText(this, "Email hau dagoeneko erregistratuta dago", Toast.LENGTH_SHORT)
                .show()
            db.close()
            return
        }

        val insert =
            "INSERT INTO pertsonak (izena, email, password, sexua, spamJaso, hiria) VALUES ('$izena', '$email', '$password', '$sexua', '$spamJaso', '$hiria')"
        db.execSQL(insert)
        db.close()

        Toast.makeText(this, "Erregistroa eginda!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    //Emaila erregistratuta badago konprobatzeko funtzioa
    private fun emailesiste(db: SQLiteDatabase, email: String): Boolean {
        val query = "SELECT * FROM pertsonak WHERE email = ?"
        val cursor = db.rawQuery(query, arrayOf(email))

        val existe = cursor.count > 0
        cursor.close()

        return existe
    }
}