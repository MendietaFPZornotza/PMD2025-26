package com.example.thebeautycorner

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Erregistroa : AppCompatActivity() {

    // EditText-ak dklaratu
    private lateinit var txtIzena : TextView
    private lateinit var txtEmail : TextView
    private lateinit var txtPass : TextView

    // RadioButton-ak deklaratu
    private lateinit var genEmakumea : RadioButton
    private lateinit var genGizona : RadioButton
    private lateinit var genBeteBat : RadioButton

    // Spinnerra Deklaratu
    private lateinit var aukeraHiria : Spinner

    // Botoiak Deklaratu
    private lateinit var erregBotoia : Button
    private lateinit var btnLogin : Button


    // Generoaren balorea aukertzeko funtzioa
    private fun generoaAukeratu (genEmak: RadioButton, genGiz: RadioButton, genBest: RadioButton): String {

        return when {
            genEmakumea.isChecked -> "Emakumea"
            genGizona.isChecked -> "Gizona"
            genBeteBat.isChecked -> "Beste Bat"
            else -> "Ez da hautatu"
        }
    }

    // Erregistratzeko funtzioa
    private fun erregistratu () {

        val izena = txtIzena.text.toString()
        val email = txtEmail.text.toString()
        val pass = txtPass.text.toString()
        val hiria = aukeraHiria.selectedItem.toString()
        val generoa = generoaAukeratu(genEmakumea, genGizona, genBeteBat)

        // Konprobatu erabiltzailea benetan exisitzen den
        if (erabExiste(email)) {
            Toast.makeText(this, "Erabiltzailea exisittzen da, saiatu berriro logeatzen.", Toast.LENGTH_LONG).show()

            // Erabiltzailea existitzen bada, login pantailara bideratuko zaio berriro
            val intent : Intent = Intent(this, Login :: class.java)
            startActivity(intent)
        } else {
            // Erabiltzailea erregistratu
            val admin = AdminSQLiteOpenHelper(this, "thebeautycorner", null, 2)
            val bd = admin.writableDatabase
            val registroa = ContentValues().apply {
                put("erabiltzaileizena", izena)
                put("postaelektronikoa", email)
                put("pasahitza", pass)
                put("generoa", generoa)
                put("egoitzahiria", hiria.toString() )
            }
            bd.insert("erabiltzaileak", null, registroa)
            bd.close()

            Toast.makeText(this, "Erregsitratu zara. Ongi etorri, $izena", Toast.LENGTH_LONG).show()

            val sharedPreferences: SharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("email", email)
            editor.apply()

            // Pantaila printzipalera bideratu
            val intent : Intent = Intent(this, Printzipala :: class.java)
            startActivity(intent)
        }
    }

    // Erabiltzailea benetan exisitzen den konprobatu
    private fun erabExiste (mail : String) : Boolean {

        val admin = AdminSQLiteOpenHelper(this, "thebeautycorner", null, 2)
        val bd = admin.writableDatabase

        val query = bd.rawQuery(
            "SELECT * FROM erabiltzaileak WHERE postaelektronikoa = ?",
            arrayOf(mail)
        )
        val erabExiste = query.moveToFirst()
        query.close()

        if (erabExiste) {
            return true
        } else {
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.erregistroa)

        // EditText-ak hasieratu
        txtIzena = findViewById(R.id.erregIzenaText)
        txtEmail = findViewById(R.id.erregEmailText)
        txtPass = findViewById(R.id.erregPassText)

        // RadioButton-ak deklaratu
        genEmakumea = findViewById(R.id.radioEmakumea)
        genGizona = findViewById(R.id.radioGizona)
        genBeteBat = findViewById(R.id.radioBesteBat)

        // Spinner-a deklaratu
        aukeraHiria = findViewById(R.id.spinnerHiria)
        erregBotoia = findViewById(R.id.erregBotoia)

        btnLogin = findViewById(R.id.btnLogin)

        // Spinner-aren baloreak hasieratu
        val hiriak = listOf("Nongoa Zara?","Elorrio", "Amorebieta", "Durango", "Iurreta", "Abadiño", "Otxandio")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hiriak)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        aukeraHiria.adapter = adapter

        erregBotoia.setOnClickListener {
            if (!txtEmail.text.contains("@")) {
                Toast.makeText(this, "Mesedez, aukeratu hiria.", Toast.LENGTH_SHORT).show()
            } else if (aukeraHiria.selectedItem.toString() == "Aukeratu") {
                Toast.makeText(this, "Zure email-a @ bat izan behar du.", Toast.LENGTH_SHORT).show()
            } else {
                val generoa = generoaAukeratu(genEmakumea, genGizona, genBeteBat)

                if (generoa == "Ez da hautatu") {
                    Toast.makeText(this, "Ez da generorik aukeratu.", Toast.LENGTH_SHORT).show()
                } else {
                    erregistratu()
                }
            }
        }
    }
}