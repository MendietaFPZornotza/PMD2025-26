package com.example.proiektu_denda

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern

class Erregistratu : AppCompatActivity() {
    private lateinit var izena: EditText
    private lateinit var mail: EditText
    private lateinit var pasahitza: EditText
    private lateinit var gizona: RadioButton
    private lateinit var emakumea: RadioButton
    private lateinit var bestea: RadioButton
    private lateinit var diabetikoa: CheckBox
    private lateinit var laktosa: CheckBox
    private lateinit var spinner: Spinner
    private lateinit var erregistratuBtn: Button
    private lateinit var itzuliBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_erregistratu)

        izena = findViewById(R.id.erabiltzaileIzena)
        mail = findViewById(R.id.postaElektronikoa)
        pasahitza = findViewById(R.id.password)
        gizona = findViewById(R.id.gizonButton)
        emakumea = findViewById(R.id.emakumeaButton)
        bestea = findViewById(R.id.besteaButton)
        diabetikoa = findViewById(R.id.diabetikoCheckBox)
        laktosa = findViewById(R.id.laktosaCheckBox)
        spinner = findViewById(R.id.spinner)
        erregistratuBtn = findViewById(R.id.ERREGISTRATU)
        itzuliBtn = findViewById(R.id.itzuliBtn)

        // Hiriak Spinerreko aukerak
        val hiriak = arrayOf("Madril", "Bartzelona", "Valentzia", "Bilbao", "Malaga", "Sevilla", "Granada", "Zaragoza")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hiriak)
        spinner.adapter = adapter

        // Login-era itzultzeko
        itzuliBtn.setOnClickListener {
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }

        // Behin dena bete dela erregistratzeko, Login-era eramaten dizu
        erregistratuBtn.setOnClickListener {
            val admin = AdminSQLiteOpenHelper(this, "administracion.db", null, 1) // Usa la misma versión
            val bd = admin.writableDatabase

            val erabiltzailea: String = izena.text.toString()
            val postaelektronikoa: String = mail.text.toString()
            val contraseña: String = pasahitza.text.toString()
            val egoitzaHiria: String = spinner.selectedItem.toString()
            val generoa = when {
                gizona.isChecked -> "Gizonezkoa"
                emakumea.isChecked -> "Emakumezkoa"
                else -> "Bestea"
            }
            val diabetikoaValue = if (diabetikoa.isChecked) 1 else 0
            val laktosaValue = if (laktosa.isChecked) 1 else 0

            val registro = ContentValues()

            // Komprobatzeko hutsune guztiak hutsik ez daudela (bestela abisua)
            if (erabiltzailea.isNotEmpty() && postaelektronikoa.isNotEmpty() && contraseña.isNotEmpty()) {
                val emailValid = isValidEmail(postaelektronikoa)
                val passwordValid = isValidPassword(contraseña)

                when {
                    !emailValid -> Toast.makeText(this, "Email okerra. Adb: 'izena@gmail-com'", Toast.LENGTH_SHORT).show()
                    !passwordValid -> Toast.makeText(this, "Pasahitzak gutxienez 8 karaktere izan behar ditu", Toast.LENGTH_SHORT).show()
                    else -> {
                        if (gizona.isChecked || emakumea.isChecked || bestea.isChecked) {
                            registro.put("erabiltzailea", erabiltzailea)
                            registro.put("postaElektronikoa", postaelektronikoa)
                            registro.put("pasahitza", contraseña)
                            registro.put("generoa", generoa)
                            registro.put("egoitzaHiria", egoitzaHiria)
                            registro.put("diabetikoa", diabetikoaValue)
                            registro.put("laktosa", laktosaValue)

                            bd.insert("clientes", null, registro)

                            bd.close()
                            izena.setText("")
                            mail.setText("")
                            pasahitza.setText("")
                            gizona.isChecked = false
                            emakumea.isChecked = false
                            bestea.isChecked = false
                            diabetikoa.isChecked = false
                            laktosa.isChecked = false

                            Toast.makeText(this, "Erregistratu zara", Toast.LENGTH_LONG).show()

                            val i = Intent(this, Login::class.java)
                            startActivity(i)
                        } else {
                            Toast.makeText(this, "Aukeratu genero bat", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Bete hutsune guztiak", Toast.LENGTH_LONG).show()
            }
        }
    }


    //email-a balidatzeko da, hau "--@--.com" izan behar da email-aren egitura
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")
        return emailPattern.matcher(email).matches()
    }

    //pasahitza balidatzeko, hau da, gutxienez 8 karaktereko pasahitza izan behar da
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
}
