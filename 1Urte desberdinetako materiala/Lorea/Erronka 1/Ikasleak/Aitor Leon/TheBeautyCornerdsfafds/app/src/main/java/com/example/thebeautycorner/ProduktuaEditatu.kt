package com.example.thebeautycorner

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.ContentValues
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import com.example.thebeautycorner.model.Produktua

class ProduktuaEditatu : AppCompatActivity() {

    // EditText-ak deklaratu
    private lateinit var edtIzena: EditText
    private lateinit var edtMarka: EditText
    private lateinit var edtJatorria: EditText
    private lateinit var edtPrezioa: EditText
    private lateinit var spinnerMota : Spinner

    // CheckBox-ak deklaratu
    private lateinit var edtEskuragarritasuna: CheckBox
    private lateinit var checkboxMongolia: CheckBox
    private lateinit var checkboxNigeria: CheckBox
    private lateinit var checkboxAmorebieta: CheckBox

    // Button-a deklaratu
    private lateinit var btnActualizar: Button

    // Spinner-a konfiguratzeko funtzioa
    private fun spinnerKonfiguratu () {
        spinnerMota = findViewById(R.id.spinnerMota)
        val motaOptions = listOf(
            "Aukeratu Produktuaren Mota", "Kosmetikoak", "Makillajea", "Lurrinak", "Ile zaintza", "Larruazala"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, motaOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMota.adapter = adapter

        val productoJatorria = intent.getStringExtra("productoJatorria")
        val posicionJatorria = motaOptions.indexOf(productoJatorria)
        if (posicionJatorria != -1) {
            spinnerMota.setSelection(posicionJatorria)
        }
    }

    // CheckBox-ak konfiguratu
    private fun konfiguratuCheckBoxes() {

        checkboxMongolia = findViewById(R.id.checkboxMongolia)
        checkboxNigeria = findViewById(R.id.checkboxNigeria)
        checkboxAmorebieta = findViewById(R.id.checkboxAmorebieta)

        val checkboxes = listOf(checkboxMongolia, checkboxNigeria, checkboxAmorebieta)

        checkboxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // CheckBox bat aukeratzen bada, besteak ezingo dira aukeratu
                    checkboxes.filter { it != checkBox }.forEach { it.isChecked = false }
                }
            }
        }

        // Recuperar el valor de jatorria del Intent y seleccionar el CheckBox correspondiente
        val productoJatorria = intent.getStringExtra("productoJatorria")
        when (productoJatorria) {
            "Mongolia" -> checkboxMongolia.isChecked = true
            "Nigeria" -> checkboxNigeria.isChecked = true
            "Amorebieta" -> checkboxAmorebieta.isChecked = true
        }
    }

    // Aukeratutako produktuaren infromazioa kargatzeko
    private fun datuakKargatu () {

        // Textuak hasieratu
        edtIzena = findViewById(R.id.edtIzena)
        edtMarka = findViewById(R.id.edtMarka)
        edtPrezioa = findViewById(R.id.edtPrezioa)

        // CheckBox-ak hasieratu
        edtEskuragarritasuna = findViewById(R.id.checkboxEskuragarritasuna)
        checkboxNigeria = findViewById(R.id.checkboxNigeria)
        checkboxMongolia = findViewById(R.id.checkboxMongolia)
        checkboxAmorebieta = findViewById(R.id.checkboxAmorebieta)

        // Button-ak hasieratu
        btnActualizar = findViewById(R.id.btnEguneratu)

        // Intent bidez bidalitako  datuak hartu
        val productoId = intent.getIntExtra("productoId", -1)
        val prod : Produktua = prodAukeratu(productoId)

        // Produktuaren datuak kargatu EditText-etan
        edtIzena.setText(prod.izena)
        edtMarka.setText(prod.marka)
        edtPrezioa.setText(prod.prezioa.toString())
        edtEskuragarritasuna.isChecked = prod.eskuragarritasuna

        // Eguneratzeko botoia konfiguratu
        btnActualizar.setOnClickListener {
            // Produktuaren datu berriak deklaratu
            val updatedIzena = edtIzena.text.toString()
            val updatedMota = spinnerMota.selectedItem.toString()
            val updatedMarka = edtMarka.text.toString()
            val updatedJatorria = edtJatorria.text.toString()
            val updatedPrezioa = edtPrezioa.text.toString().toDouble()
            val updatedEskuragarritasuna = edtEskuragarritasuna.text.toString()

            // Produktua eguneratu
            prodEguneratu()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.produktuaeditatu)

        // Konfigurazioak hasieratu
        konfiguratuCheckBoxes()
        spinnerKonfiguratu()
        datuakKargatu()

        // Botoia hasieratu
        btnActualizar = findViewById(R.id.btnEguneratu)

        btnActualizar.setOnClickListener {
            prodEguneratu()
        }

    }

    // ID-aren bidez produktua aukeratzeko funtzioa
    private fun prodAukeratu(id: Int): Produktua {
        val admin = AdminSQLiteOpenHelper(this, "thebeautycorner", null, 2)
        val bd = admin.writableDatabase

        // Produktua ID bidez bilatu
        val query = bd.rawQuery(
            "SELECT * FROM produktuak WHERE kodea = ?",
            arrayOf(id.toString())
        )

        if (query.moveToFirst()) {
            // Produktua aurkitzen bada, objetua bueltatzen dugu
            val prodId = query.getString(query.getColumnIndex("kodea"))
            val izena = query.getString(query.getColumnIndex("izena"))
            val mota = query.getString(query.getColumnIndex("mota"))
            val marka = query.getString(query.getColumnIndex("marka"))
            val jatorria = query.getString(query.getColumnIndex("jatorria"))
            val prezioa = query.getDouble(query.getColumnIndex("prezioa"))
            val eskuragarritasuna = query.getInt(query.getColumnIndex("eskuragarritasuna")) == 1

            // Produktu objetua sortu eta bidali
            query.close()
            return Produktua(
                id = prodId,
                izena = izena,
                mota = mota,
                marka = marka,
                jatorria = jatorria,
                prezioa = prezioa,
                eskuragarritasuna = eskuragarritasuna
            )
        } else {
            // Ez bada aurkitzen, produktu hutsa bueltatuko da
            query.close()
            Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_LONG).show()
            return Produktua("", "", "", "", "", 0.0, false) // Producto vacío o por defecto
        }
    }

    // Produktua Eguneratzeko funtzioa
    private fun prodEguneratu () : Boolean {

        // Eguneratutako datuak deklaratu
        val izena = edtIzena.text.toString()
        val mota = spinnerMota.selectedItem.toString()
        val marka = edtMarka.text.toString()
        val prezioa = edtPrezioa.text.toString().toDoubleOrNull()
        val updatedEskuragarritasuna = if (edtEskuragarritasuna.isChecked) "Eskuragarri" else "Ez dago Eskuragarri"

        //Checkbox-en balioa hartu
        val updatedJatorria = when {
            checkboxMongolia.isChecked -> "Mongolia"
            checkboxNigeria.isChecked -> "Nigeria"
            checkboxAmorebieta.isChecked -> "Amorebieta"
            else -> "Motarik Ez"
        }


        // Kanpoen balidazioa
        if (izena.isEmpty() || mota.equals("Aukeratu Produktuaren Mota") || marka.isEmpty() || prezioa == null || updatedEskuragarritasuna.isEmpty()) {
            Toast.makeText(this, "Mesedez, bete datu guztiak", Toast.LENGTH_SHORT).show()
            return false
        }

        //DB-RAKO ADMINSITRATZAILEA DEKLARATU
        val admin = AdminSQLiteOpenHelper(this, "thebeautycorner", null, 2)
        val bd = admin.writableDatabase

        // DBB-an eguneratu
        val values = ContentValues().apply {
            put("izena", izena)
            put("mota", mota)
            put("marka", marka)
            put("jatorria", updatedJatorria)
            put("prezioa", prezioa)
            put("eskuragarritasuna", updatedEskuragarritasuna)
        }
        val productoId = intent.getIntExtra("productoId", -1)
        val filasActualizadas = bd.update("produktuak", values, "kodea=?", arrayOf(productoId.toString()))

        if (filasActualizadas > 0) {
            Toast.makeText(this, "Produktu eguneratu da", Toast.LENGTH_SHORT).show()
            bd.close()

            // Buelatu ProduktuenLista pantailara
            val intent : Intent = Intent(this, ProduktuenLista :: class.java)
            startActivity(intent)
            return true
        } else {
            Toast.makeText(this, "Errorea eguneratzean", Toast.LENGTH_SHORT).show()
            bd.close()
            return false
        }
    }

    // Menua beridazteko funtzioa
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true//super.onCreateOptionsMenu(menu)

    }

    // //Beridazten duen funtzioa gure menuaren botoiak funtzionatzeko
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