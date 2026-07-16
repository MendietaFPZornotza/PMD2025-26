package com.example.proiektufinala_asieraldekoag

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {
    lateinit var PostaElektronikoa: EditText
    lateinit var Pasahitza: EditText
    lateinit var Login: Button
    lateinit var Erregistratu: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        val screemSplash=installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //HASIERAKETAK
        PostaElektronikoa = findViewById(R.id.txtPosta)
        Pasahitza = findViewById(R.id.txtPass)
        Login = findViewById(R.id.btnLogin)
        Erregistratu = findViewById(R.id.btnErregistratu)

        Login.setOnClickListener(){
            Logeatu()
        }
        Erregistratu.setOnClickListener(){
            Erregistroa()
        }
        screemSplash.setKeepOnScreenCondition{ false }
    }
    fun Logeatu() {
        val postaE = PostaElektronikoa.text.toString().trim()
        val pasahitza = Pasahitza.text.toString().trim()

        if (postaE.isEmpty() || pasahitza.isEmpty()) {
            Toast.makeText(this, "Mesedez datu guztiak sartu", Toast.LENGTH_SHORT).show()
            return
        }

        if (!postaE.contains("@gmail.com") && !postaE.contains("@hotmail.com")) {
            Toast.makeText(this, "Mesedez, sartu Gmail edo Hotmail posta bat", Toast.LENGTH_SHORT).show()
            return
        }

        val admin = SQL_User_Database(this, "Altzairuen_Denda.db", null, 1)
        val bd = admin.readableDatabase

        val cursor = bd.rawQuery(
            "SELECT * FROM erabiltzaileak WHERE posta = ? AND pass = ?",
            arrayOf(postaE, pasahitza)
        )

        if (cursor.moveToFirst()) {
            Toast.makeText(this, "Saioa ongi hasi da!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Gmail-a edo pasahitza txarto dago", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        bd.close()
    }
    fun Erregistroa() {
        val intent1 = Intent(this, ErregistroOrria::class.java)
        startActivity(intent1)
    }
}