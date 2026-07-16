package com.example.proiektufinala_asieraldekoag

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProduktuaEditatu : AppCompatActivity() {
    private lateinit var dbHelper: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produktua_editatu)

        dbHelper = openOrCreateDatabase("Altzairuen_Denda.db", MODE_PRIVATE, null)

        val txtIzena: EditText = findViewById(R.id.txtIzena)
        val spinnerMota: Spinner = findViewById(R.id.spinner)
        val checkBoxes = listOf(
            findViewById<CheckBox>(R.id.checkBox),
            findViewById<CheckBox>(R.id.checkBox2),
            findViewById<CheckBox>(R.id.checkBox3),
            findViewById<CheckBox>(R.id.checkBox4),
            findViewById<CheckBox>(R.id.checkBox5),
            findViewById<CheckBox>(R.id.checkBox6)
        )
        val txtDimentsioak: EditText = findViewById(R.id.txtDimentsioak)
        val txtPrezioa: EditText = findViewById(R.id.txtPrezioa)
        val btnEguneratu: Button = findViewById(R.id.btnEguneratu)
        val btnBueltatu: Button = findViewById(R.id.btnBueltatu) // Obtener btnBueltatu

        val motaOptions = arrayOf(
            "Sofa", "Mahaia", "Aulkia", "Ohea", "Armairua", "Bankua", "Ispilua", "Besaulkia"
        )
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, motaOptions)
        spinnerMota.adapter = spinnerAdapter

        val productoId = intent.getIntExtra("productoId", -1)

        val cursor = dbHelper.rawQuery(
            "SELECT * FROM produktuak WHERE id = ?",
            arrayOf(productoId.toString())
        )
        if (cursor.moveToFirst()) {
            txtIzena.setText(cursor.getString(cursor.getColumnIndexOrThrow("altzariIzena")))
            val mota = cursor.getString(cursor.getColumnIndexOrThrow("altzariMota"))
            spinnerMota.setSelection(motaOptions.indexOf(mota))

            val materiales = cursor.getString(cursor.getColumnIndexOrThrow("materiales")).split(",")
            checkBoxes.forEach { it.isChecked = materiales.contains(it.text.toString()) }

            txtDimentsioak.setText(cursor.getString(cursor.getColumnIndexOrThrow("dimentsioak")))
            txtPrezioa.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("prezioa")).toString())
        }
        cursor.close()

        // Acción para el botón Eguneratu
        btnEguneratu.setOnClickListener {
            val nuevoNombre = txtIzena.text.toString()
            val nuevaMota = spinnerMota.selectedItem.toString()
            val nuevosMateriales =
                checkBoxes.filter { it.isChecked }.joinToString(",") { it.text.toString() }
            val nuevasDimentsioak = txtDimentsioak.text.toString()
            val nuevoPrezioa = txtPrezioa.text.toString().toDoubleOrNull() ?: 0.0

            val contentValues = ContentValues().apply {
                put("altzariIzena", nuevoNombre)
                put("altzariMota", nuevaMota)
                put("materiales", nuevosMateriales)
                put("dimentsioak", nuevasDimentsioak)
                put("prezioa", nuevoPrezioa)
            }

            dbHelper.update("produktuak", contentValues, "id = ?", arrayOf(productoId.toString()))
            Toast.makeText(this, "Produktua eguneratu da!", Toast.LENGTH_SHORT).show()

            setResult(RESULT_OK)
            finish()

            val intent1 = Intent(this, MainPage::class.java)
            startActivity(intent1)
        }

        // Acción para el botón Bueltatu
        btnBueltatu.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)  // Navegar a la actividad MainPage
        }
    }
}