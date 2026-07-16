package com.example.jostailudendajosu

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    lateinit var Login: Button
    lateinit var erregistratu: Button

    lateinit var emailinput: EditText
    lateinit var passinput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


      Login = findViewById(R.id.hasibtn)
       erregistratu = findViewById(R.id.erregbtn)

        emailinput = findViewById(R.id.emainInput)
        passinput = findViewById(R.id.pasahitzaInput)

        //Splash-a
        screenSplash.setKeepOnScreenCondition{false}


        //Login botoia email eta pass konprobatzeko badaude datu basean
        Login.setOnClickListener {
            val admin = SqlAdmin(this, "pertsonakDB", null, 1)
            val bd = admin.readableDatabase

            val email = emailinput.text.toString()
            val pass = passinput.text.toString()

            val cursor: Cursor = bd.rawQuery(
                "SELECT * FROM pertsonak WHERE email = ? AND password = ?",
                arrayOf(email, pass)
            )

            if (cursor.moveToFirst()) {

                Toast.makeText(this, "Login zuzena", Toast.LENGTH_LONG).show()
               val intent = Intent(this, PantailaPrintzipala::class.java)
               startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email-a edo pasahitza okerra", Toast.LENGTH_LONG).show()
            }

            cursor.close()
            bd.close()
        }
        //Erregistratu botoia erregistratu pantailara eramaten duena
        erregistratu.setOnClickListener {
            val intent = Intent(this, Erregistratu::class.java)
            startActivity(intent)
        }


    }
}