package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.util.Patterns

class MainActivity : AppCompatActivity() {

    lateinit var user: TextView
    lateinit var pass: TextView
    lateinit var btnlog: Button
    lateinit var btnerreg: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        screenSplash.setKeepOnScreenCondition { false }

        user = findViewById(R.id.user)
        pass = findViewById(R.id.password)
        btnlog = findViewById(R.id.btnlog)
        btnerreg = findViewById(R.id.erreg)

        btnlog.setOnClickListener {
            SaioHasi()
        }
        btnerreg.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }
    }

    private fun SaioHasi() {
        val gmail = user.text.toString().trim()
        val contraseña = pass.text.toString().trim()

        if (gmail.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Mesedez datu guztiak sartu", Toast.LENGTH_SHORT).show()
            return
        }


        if (!isValidEmail(gmail)) {
            Toast.makeText(this, "Sartu baliozko Gmail-a", Toast.LENGTH_SHORT).show()
            return
        }

        val admin = SQL_User_Database(this, "Elektronika_Objetuen_Denda.db", null, 1)
        val bd = admin.readableDatabase

        val cursor = bd.rawQuery(
            "SELECT * FROM erabiltzaileak WHERE gmail = ? AND pasahitza = ?",
            arrayOf(gmail, contraseña)
        )

        if (cursor.moveToFirst()) {
            Toast.makeText(this, "Saioa ongi hasi da!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Menu2::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Gmail-a edo pasahitza txarto dago", Toast.LENGTH_SHORT).show()
        }

        cursor.close()
        bd.close()
    }


    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}