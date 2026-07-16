package com.example.liburudendaprojektua

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LiburuaGehitu : AppCompatActivity() {
    private lateinit var izenburua: EditText
    private lateinit var egilea: EditText
    private lateinit var generoa: Spinner
    private lateinit var argitaletxea: Spinner
    private lateinit var prezioa: EditText
    private lateinit var eskuragarritasuna: CheckBox
    private lateinit var gehituBotoia: Button
    private lateinit var dbHelper: AdminSQLiteOpenHelper
    private lateinit var atzera: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liburua_gehitu)

        dbHelper = AdminSQLiteOpenHelper(this, "liburuDB", null, 2)


        izenburua = findViewById(R.id.izenburua)
        egilea = findViewById(R.id.egilea)
        generoa = findViewById(R.id.generoa)
        argitaletxea = findViewById(R.id.argitaletxea)
        prezioa = findViewById(R.id.prezioa)
        eskuragarritasuna = findViewById(R.id.eskuragarritasuna)
        gehituBotoia = findViewById(R.id.gehituBotoia)
        atzera = findViewById(R.id.atzera)
        //Spinnerrak konfiratu
        ArrayAdapter.createFromResource(
            this,
            R.array.generoa_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            generoa.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.argitaletxea_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            argitaletxea.adapter = adapter
        }

        // Gehitu botoia konfiguratu
        gehituBotoia.setOnClickListener {
            addBook()
        }
        // Atzera botoia konfiguratu
        atzera.setOnClickListener {
            //  PantailaNagusiaActivityera bueltatzeko
            val intent = Intent(this, PantailaNagusiaActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun addBook() {
        val izenburuaText = izenburua.text.toString().trim()
        val egileaText = egilea.text.toString().trim()
        val generoaText = generoa.selectedItem.toString()
        val argitaletxeaText = argitaletxea.selectedItem.toString()
        val prezioaText = prezioa.text.toString().trim()
        val eskuragarri = if (eskuragarritasuna.isChecked) 1 else 0 // 1 eskuragarri, 0 ez eskuragarri

        if (izenburuaText.isEmpty() || egileaText.isEmpty() || prezioaText.isEmpty()) {
            Toast.makeText(this, "Bete eremu guztiak mesedez", Toast.LENGTH_SHORT).show()
            return
        }

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("izenburua", izenburuaText)
            put("egilea", egileaText)
            put("generoa", generoaText)
            put("argitaletxea", argitaletxeaText)
            put("prezioa", prezioaText.toDoubleOrNull() ?: 0.0)
            put("eskuragarritasuna", eskuragarri)
        }

        val newRowId = db.insert("liburuak", null, values)
        db.close()

        if (newRowId != -1L) {
            Toast.makeText(this, "Liburua arrakastaz gehitu da", Toast.LENGTH_SHORT).show()
            clearFields()
            val intent = Intent(this, PantailaNagusiaActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Errorea liburua gehitzerakoan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        izenburua.text.clear()
        egilea.text.clear()
        prezioa.text.clear()
        eskuragarritasuna.isChecked = false
        generoa.setSelection(0)
        argitaletxea.setSelection(0)
    }
}
