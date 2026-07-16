package com.example.lorategi_proiektua

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {
    lateinit var inputMail:EditText
    lateinit var inputPass:EditText
    lateinit var btnLogin:Button
    lateinit var goToReg:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash=installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screenSplash.setKeepOnScreenCondition{false}

        inputMail=findViewById(R.id.inputMail)
        inputPass=findViewById(R.id.inputPass)
        btnLogin=findViewById(R.id.btnLogin)
        goToReg=findViewById(R.id.linkRegister)

        //DATU BASEKO DATUAK EZABATU
        fun borrarDatos(){
            val admin = AdminSQLiteOpenHelper(
                this,
                "administracion", null, 5
            )
            val bd = admin.writableDatabase

            bd.delete("users", null, null)
            bd.close()
            System.out.println("Datuak ezabatu dira")
        }

        //ERREGISTRORA JOAN
        goToReg.setOnClickListener(){
            System.out.println("Go To Register")
            //Erregistrora joan
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            //logina itxi
            finish()
        }

        //LOGIN-AREN KONPROBAKETA EGIN
        btnLogin.setOnClickListener(){
            System.out.println("Log In")
            //comprobar si el usuario existe en la base de datos
            val admin = AdminSQLiteOpenHelper(
                this,
                "administracion", null, 5
            )
            val bd = admin.writableDatabase

            val email = inputMail.text.toString()
            val pass = inputPass.text.toString()
            val linea = bd.rawQuery(
                "select * from users", null
            )
            var userFound = false
            while (linea.moveToNext()) {
                val emailDB = linea.getString(1)
                if (emailDB == email) {
                    val passDB = linea.getString(2)
                    if (passDB == pass) {
                        System.out.println("Login Correcto")
                        Toast.makeText(this, "Logina zuzen", Toast.LENGTH_SHORT).show()
                        //productActivity-ra joan
                        val intent = Intent(this, ProductActivity::class.java)
                        startActivity(intent)
                        //Login itxi
                        finish()
                    } else {
                        System.out.println("Contraseña incorrecta")
                        Toast.makeText(this, "Pasahitz okerra", Toast.LENGTH_SHORT).show()
                    }
                    userFound = true

                    break
                }
            }
            if (!userFound) {
                Toast.makeText(this, "Erabiltzaile hori ez da existitzen", Toast.LENGTH_SHORT).show()
                System.out.println("User not found")
            }
        }
    }
}