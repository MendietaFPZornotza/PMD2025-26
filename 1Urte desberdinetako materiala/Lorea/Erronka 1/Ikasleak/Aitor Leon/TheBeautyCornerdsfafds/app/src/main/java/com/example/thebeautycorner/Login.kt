package com.example.thebeautycorner

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    // EditText-ak deklaratu
    private lateinit var loginInputEmail : TextView
    private lateinit var loginInputPass : TextView

    // Button-ak deklaratu
    private lateinit var loginBotoia : Button
    private lateinit var erregBotoia : Button

    // Erabiltzailea exisitzen den konprobatzeko funtzioa
    private fun erabiltzaileaKonprobatu (erabEmail : String, erabPass : String) : Boolean{

        val admin = AdminSQLiteOpenHelper(this, "thebeautycorner", null, 2)
        val bd = admin.writableDatabase

        val query = bd.rawQuery(
            "SELECT * FROM erabiltzaileak WHERE postaelektronikoa = ? AND pasahitza = ?",
            arrayOf(erabEmail, erabPass)
        )
        val erabExiste = query.moveToFirst()
        query.close()

        if (erabExiste) {
            return true
        } else {
            Toast.makeText(this, "Ez dira erabiltzailerik aurkitu. Erregistratu", Toast.LENGTH_LONG).show()
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        // ToolBar-eko aplikazioaren tituloa kendu
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Email-a eta pasahitza idazteko inputa hasieratu
        loginInputEmail = findViewById(R.id.loginInputEmail)
        loginInputPass = findViewById(R.id.loginInputPass)

        // Button-ak hasieratu
        loginBotoia = findViewById(R.id.loginBotoia)
        erregBotoia = findViewById(R.id.erregBotoia)


        loginBotoia.setOnClickListener {
            // Baloreak hartu
            val email = loginInputEmail.text.toString()
            val pass = loginInputPass.text.toString()

            // Korreoaren kontrola
            if (!email.contains("@")) {
                Toast.makeText(this, "Zure korreoa @ bat izan behar du.", Toast.LENGTH_LONG).show()
            } else {
                if (erabiltzaileaKonprobatu(email, pass)) {
                    Toast.makeText(this, "Saioa Hazi duzu, Ongi Etorri.", Toast.LENGTH_LONG).show()

                    val sharedPreferences: SharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("email", email)
                    editor.apply()

                    val intent : Intent = Intent(this, Printzipala :: class.java)
                    startActivity(intent)

                } else {

                    val intent : Intent = Intent(this, Erregistroa :: class.java)
                    startActivity(intent)
                }
            }
        }

        // Erregistratzeko pantailara joateko botoia
        erregBotoia.setOnClickListener {
            val intent : Intent = Intent(this, Erregistroa :: class.java)
            startActivity(intent)
        }
    }
}