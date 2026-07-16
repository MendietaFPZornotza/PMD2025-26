package com.example.proiektufinala_asieraldekoag

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

class ErregistroOrria : AppCompatActivity() {
    lateinit var Erabiltzailea: EditText
    lateinit var PostaElektronikoa: EditText
    lateinit var Pasahitza: EditText
    lateinit var Gizona: RadioButton
    lateinit var Emakumea: RadioButton
    lateinit var Bestea: RadioButton
    lateinit var Baldintzak: CheckBox
    lateinit var EgoitzaSpinner: Spinner
    lateinit var Erregistratu: Button
    lateinit var dbHelper: SQL_User_Database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_erregistro_orria)

        //HASIERAKETAK
        Erabiltzailea = findViewById(R.id.txtErab)
        PostaElektronikoa = findViewById(R.id.txtPostaE)
        Pasahitza = findViewById(R.id.txtPass)
        Gizona = findViewById(R.id.btnGizona)
        Emakumea = findViewById(R.id.btnEmakumea)
        Bestea = findViewById(R.id.btnBestea)
        Baldintzak = findViewById(R.id.chkAdina)
        EgoitzaSpinner = findViewById(R.id.spnEgoitza)
        Erregistratu = findViewById(R.id.btnErregistroa)

        // Configuración de la base de datos
        dbHelper = SQL_User_Database(this, "Altzairuen_Denda.db", null, 1)

        // Valores del SPINNER
        val ListakoAukerak = arrayOf("Madril", "Bartzelona", "Valentzia", "Sevilla", "Zaragoza", "Malaga", "Bilbo", "Alacant")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ListakoAukerak)
        EgoitzaSpinner.setAdapter(adapter)

        // Acción del botón "Erregistroa"
        Erregistratu.setOnClickListener {
            Erregistro()
        }

        // Acción del botón "Bueltatu"
        val Bueltatu: Button = findViewById(R.id.btnBueltatu)
        Bueltatu.setOnClickListener {
            // Inicia MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun Erregistro() {
        if (eremuakEgiaztatu()) {
            erabiltzaileaSartuDatubasean()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    //Eremu guztiak beteta daudela egiaztatu
    private fun eremuakEgiaztatu(): Boolean {
        val erab = Erabiltzailea.text.toString().trim()
        val posta = PostaElektronikoa.text.toString().trim()
        val pass = Pasahitza.text.toString().trim()
        val generoa = when {
            Gizona.isChecked -> "Gizona"
            Emakumea.isChecked -> "Emakumea"
            Bestea.isChecked -> "Bestea"
            else -> ""
        }

        // Comprobar si el correo electrónico tiene los dominios @gmail.com o @hotmail.com
        val correoValido = posta.endsWith("@gmail.com") || posta.endsWith("@hotmail.com")

        return if (erab.isEmpty() || posta.isEmpty() || pass.isEmpty() || generoa.isEmpty() || !Baldintzak.isChecked) {
            Toast.makeText(this, "Mesedez, bete eremu guztiak eta onartu baldintzak", Toast.LENGTH_SHORT).show()
            false
        } else if (!correoValido) {
            // Mostrar un mensaje si el correo no es válido
            Toast.makeText(this, "Posta elektronikoa ezin da izan besterik gabe: @gmail.com edo @hotmail.com", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }
    //Erabiltzailea sartu DATU BASEAN
    private fun erabiltzaileaSartuDatubasean() {
        val erab = Erabiltzailea.text.toString().trim()
        val posta = PostaElektronikoa.text.toString().trim()
        val pass = Pasahitza.text.toString().trim()
        val generoa = when {
            Gizona.isChecked -> "Gizona"
            Emakumea.isChecked -> "Emakumea"
            Bestea.isChecked -> "Bestea"
            else -> ""
        }

        val result = dbHelper.erabiltzaileaSartu(erab, posta, pass, generoa)
        if (result != -1L) {
            Toast.makeText(this, "Erabiltzaile berria erregistratu egin da :)", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Errorea erabiltzailea erregistratzean", Toast.LENGTH_SHORT).show()
        }
    }
    //Erregistroa egitean eremu guztiak garbitu
    private fun limpiarCampos() {
        Erabiltzailea.setText("")
        PostaElektronikoa.setText("")
        Pasahitza.setText("")
        Gizona.isChecked = false
        Emakumea.isChecked = false
        Bestea.isChecked = false
        EgoitzaSpinner.setSelection(0)
        Baldintzak.isChecked = false
    }
}