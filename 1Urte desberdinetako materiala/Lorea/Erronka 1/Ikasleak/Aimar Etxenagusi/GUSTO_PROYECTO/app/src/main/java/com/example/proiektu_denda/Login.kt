package com.example.proiektu_denda

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


class Login : AppCompatActivity() {
    private lateinit var email:EditText
    private lateinit var pasahitza:EditText
    private lateinit var botoiaSahioaHasi:Button
    private lateinit var botoiaErregistratu:Button

    override fun onCreate(savedInstanceState: Bundle?) {

        val screenSplash=installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        screenSplash.setKeepOnScreenCondition{false}

        email = findViewById(R.id.emailEditText)
        pasahitza = findViewById(R.id.passwordEditText)
        botoiaSahioaHasi = findViewById(R.id.saioaHasiBotoia)
        botoiaErregistratu = findViewById(R.id.erregistratuBotoia)

        //Erregistratu orrialdera joateko
        botoiaErregistratu.setOnClickListener{
            val i = Intent(this, Erregistratu::class.java)
            startActivity(i)
        }

        //Sahioa hasteko eta dendara sartzeko
        botoiaSahioaHasi.setOnClickListener {
            val admin = AdminSQLiteOpenHelper(
                this,
                "administracion.db", null, 1
            )
            val bd = admin.writableDatabase
            val korreo: String = email.text.toString()
            val contra: String = pasahitza.text.toString()

            //Select honekin komprobatzen dut korreoa existitzen den
            val query = """
                SELECT erabiltzailea, pasahitza 
                FROM clientes 
                WHERE postaElektronikoa = ?
            """
            val cursor = bd.rawQuery(query, arrayOf(korreo))

            if (cursor.moveToFirst()) {
                val dbPassword = cursor.getString(cursor.getColumnIndexOrThrow("pasahitza"))
                val username = cursor.getString(cursor.getColumnIndexOrThrow("erabiltzailea"))
                //Korreoa existitzen bada hemen, pasahitza dagoen ikusten dut.
                //Hau egin dut gero ezberdintzeko korreoa den okerra edo pasahitza ( agian biak )
                if (dbPassword == contra) {
                    Toast.makeText(this, "Saioa hasita", Toast.LENGTH_LONG).show()
                    val i = Intent(this, Menu::class.java)
                    //Menu orrialdera bidali izena ongi etorria egiteko
                    i.putExtra("erabiltzailea", username)
                    startActivity(i)
                } else {
                    Toast.makeText(this, "Pasahitz okerra", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Ez dago korreo hori duen bezerorik", Toast.LENGTH_LONG).show()
            }

            cursor.close()
            bd.close()

        }
    }


}