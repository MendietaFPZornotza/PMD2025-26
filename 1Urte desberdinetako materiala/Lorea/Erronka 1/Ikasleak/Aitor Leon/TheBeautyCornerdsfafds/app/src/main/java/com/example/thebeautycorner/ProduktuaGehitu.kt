package com.example.thebeautycorner

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProduktuaGehitu : AppCompatActivity() {

    private lateinit var txtProdIzena: EditText
    private lateinit var spinnerProdMota: Spinner
    private lateinit var txtProdMarka: EditText
    private lateinit var txtProdPrezioa: EditText
    private lateinit var chProdEskuragarritasuna: CheckBox
    private lateinit var checkMongolia: CheckBox
    private lateinit var checkNigeria: CheckBox
    private lateinit var checkAmorebieta: CheckBox
    private lateinit var btnGorde: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.produktuagehitu)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Button-a hasieratu
        btnGorde = findViewById(R.id.buttonGorde)

        // EditText-ak hasieratu
        txtProdIzena = findViewById(R.id.txtProdIzena)
        txtProdMarka = findViewById(R.id.txtProdMarka)
        txtProdPrezioa = findViewById(R.id.txtProdPrezioa)

        // Spinnerra hasieratu
        spinnerProdMota = findViewById(R.id.spinnerProdMota)

        // CheckBox-ak hasieratu
        checkMongolia = findViewById(R.id.checkMongolia)
        checkNigeria = findViewById(R.id.checkNigeria)
        checkAmorebieta = findViewById(R.id.checkAmorebieta)
        chProdEskuragarritasuna = findViewById(R.id.checkboxProdEskuragarritasuna)

        // CheckBox bakarra aukeratu ahal izateko funtzioa
        checkBoxBakarraAukeratu()

        btnGorde.setOnClickListener {
            if (produktuaGehitu()) {
                Toast.makeText(
                    this,
                    "Produktuaren datuak kargatu dira eta DBB-an txertatu dira.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(this, "Errorea produktua gehitzerakoan.", Toast.LENGTH_LONG).show()
            }
        }

        // Spinner-aren balioak hasieratu
        val motaOptions = listOf(
            "Aukeratu Produktuaren Mota", "Makillajea", "Lurrinak", "Ile zaintza", "Larruazala"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, motaOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProdMota.adapter = adapter
    }

    // CheckBox bakarra aukeratzeko funtzioa
    private fun checkBoxBakarraAukeratu() {
        val checkboxes = listOf(checkMongolia, checkNigeria, checkAmorebieta)

        for (checkbox in checkboxes) {
            checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    checkboxes.forEach { otherCheckbox ->
                        if (otherCheckbox != buttonView) {
                            otherCheckbox.isChecked = false
                        }
                    }
                }
            }
        }
    }

    private fun produktuaGehitu(): Boolean {
        // CheckBox-en balorea hartu
        val jatorria = when {
            checkMongolia.isChecked -> "Mongolia"
            checkNigeria.isChecked -> "Nigeria"
            checkAmorebieta.isChecked -> "Amorebieta"
            else -> null
        }

        // CheckBox-en kontrola
        if (jatorria == null) {
            Toast.makeText(this, "Aukeratu jatorri bat gutxienez", Toast.LENGTH_SHORT).show()
            return false
        }

        val izena = txtProdIzena.text.toString()
        val mota = spinnerProdMota.selectedItem.toString()
        val marka = txtProdMarka.text.toString()
        val prezioa = txtProdPrezioa.text.toString().toDoubleOrNull()
        val eskuragarritasuna = chProdEskuragarritasuna.isChecked

        if (izena.isEmpty() || mota.equals("Produktu Mota") || marka.isEmpty() || prezioa == null) {
            Toast.makeText(this, "Mesedez, bete datu guztiak", Toast.LENGTH_SHORT).show()
            return false
        }

        val admin = AdminSQLiteOpenHelper(this, "thebeautycorner", null, 2)
        val bd = admin.writableDatabase

        val registro = ContentValues().apply {
            put("izena", izena)
            put("mota", mota)
            put("marka", marka)
            put("jatorria", jatorria)
            put("prezioa", prezioa)
            put("eskuragarritasuna", if (eskuragarritasuna) 1 else 0)
        }

        val resultado = bd.insert("produktuak", null, registro)
        bd.close()

        return if (resultado != -1L) {
            txtProdIzena.setText("")
            spinnerProdMota.setSelection(0)
            txtProdMarka.setText("")
            checkMongolia.isChecked = false
            checkNigeria.isChecked = false
            checkAmorebieta.isChecked = false
            txtProdPrezioa.setText("")
            chProdEskuragarritasuna.isChecked = false

            val intent = Intent(this, ProduktuenLista::class.java)
            startActivity(intent)

            true
        } else {
            false
        }
    }

    // Menua beridazteko funtzioa
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true//super.onCreateOptionsMenu(menu)

    }

    // Beridazten duen funtzioa gure menuaren botoiak funtzionatzeko
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuKosmetikoBerria   -> {
                val intent: Intent = Intent(this, ProduktuaGehitu::class.java)

                startActivity(intent)
                true
            }

            R.id.menuKosmetikoZerrenda -> {
                val intent: Intent = Intent(this, ProduktuenLista::class.java)
                startActivity(intent)
                true
            }

            R.id.menuSaioaItxi         -> {
                val intent: Intent = Intent(this, Login::class.java)
                startActivity(intent)
                true
            }

            R.id.menuIrten             -> {
                finishAffinity()
                true
            }

            else                       -> return super.onOptionsItemSelected(item)
        }
    }
}
