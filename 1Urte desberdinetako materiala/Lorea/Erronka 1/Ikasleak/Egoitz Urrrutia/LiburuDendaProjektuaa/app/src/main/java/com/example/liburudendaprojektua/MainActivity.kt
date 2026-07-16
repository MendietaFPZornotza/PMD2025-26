package com.example.liburudendaprojektua

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {
    lateinit var posta: EditText
    lateinit var pass: EditText
    lateinit var sartu: Button
    lateinit var erregistratu: Button
    lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        val screemSplashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = AdminSQLiteOpenHelper(this, "usuariosDB", null, 2)

        posta = findViewById(R.id.posta)
        pass = findViewById(R.id.pass)
        sartu = findViewById(R.id.sartu)
        erregistratu = findViewById(R.id.erregistratu)

        screemSplashScreen.setKeepOnScreenCondition { false }

        sartu.setOnClickListener {
            val email = posta.text.toString().trim()
            val password = pass.text.toString().trim()

            if (validateInputs(email, password)) {
                if (checkLogin(email, password)) {
                    posta.text.clear()
                    pass.text.clear()
                    Toast.makeText(this, "ONGI ETORRI!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, PantailaNagusiaActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Posta edo Pasahitza okerrak", Toast.LENGTH_SHORT).show()
                }
            }
        }

        erregistratu.setOnClickListener {
            val intent = Intent(this, ErregistratuActivity::class.java)
            startActivity(intent)
        }
    }
//Inputak balidatu
    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            posta.error = "Mesedez, sartu korreoa"
            posta.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            pass.error = "Mesedez,sartu pasahitza"
            pass.requestFocus()
            return false
        }

        return true
    }

    // Konprobatu posta eta pasahitza existitzen diren
    private fun checkLogin(email: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE correo = ? AND password = ?",
            arrayOf(email, password)
        )

        return cursor.moveToFirst()
    }

}
