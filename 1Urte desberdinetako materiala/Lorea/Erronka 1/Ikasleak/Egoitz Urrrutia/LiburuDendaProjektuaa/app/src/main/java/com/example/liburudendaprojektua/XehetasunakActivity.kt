package com.example.liburudendaprojektua

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class XehetasunakActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper
    private lateinit var editTextIzenburua: EditText
    private lateinit var editTextEgilea: EditText
    private lateinit var spinnerGeneroa: Spinner
    private lateinit var spinnerArgitaletxea: Spinner
    private lateinit var editTextPrezioa: EditText
    private lateinit var checkEskuragarritasuna: CheckBox
    private lateinit var botonEguneratu: Button
    private lateinit var botonEzabatu: Button
    private lateinit var botonIrten: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xehetasunak)


        editTextIzenburua = findViewById(R.id.editTextIzenburua)
        editTextEgilea = findViewById(R.id.editTextEgilea)
        spinnerGeneroa = findViewById(R.id.spinnerGeneroa)
        spinnerArgitaletxea = findViewById(R.id.spinnerArgitaletxea)
        editTextPrezioa = findViewById(R.id.editTextPrezioa)
        checkEskuragarritasuna = findViewById(R.id.editTextEskuragarritasuna)
        botonEguneratu = findViewById(R.id.botonEguneratu)
        botonEzabatu = findViewById(R.id.botonEzabatu)
        botonIrten = findViewById(R.id.botonIrten)


        val generoaOptions = resources.getStringArray(R.array.generoa_array)
        val argitaletxeaOptions = resources.getStringArray(R.array.argitaletxea_array)


        val generoaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generoaOptions)
        generoaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGeneroa.adapter = generoaAdapter


        val argitaletxeaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, argitaletxeaOptions)
        argitaletxeaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerArgitaletxea.adapter = argitaletxeaAdapter


        val libroId = intent.getIntExtra("id", -1)
        dbHelper = AdminSQLiteOpenHelper(this, "liburuDB", null, 2)

        if (libroId != -1) {
            // Datu basetik erakutsi
            val libro = obtenerDetallesLibro(libroId)
            if (libro != null) {
                cargarDatosLibro(libro)
            }
        }

        // Konfiguratu "Eguneratu" botoia
        botonEguneratu.setOnClickListener {
            if (libroId != -1) {
                actualizarLibro(libroId)
            }
        }

        // Konfiguratu "Ezabatu" botoia
        botonEzabatu.setOnClickListener {
            if (libroId != -1) {
                showDeleteConfirmationDialog(libroId)
            }
        }

        // Konfigurazioa "Irten"
        botonIrten.setOnClickListener {
            finish() // Activity-a itxi egiten dau
        }
    }

    private fun showDeleteConfirmationDialog(libroId: Int) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Seguru ahal zaude?")
            .setMessage("Ziur al zaude liburu hau ezabatu nahi duzula? Ekintza hau ez da atzera bueltarik izango.")
            .setPositiveButton("Bai") { _, _ ->
                eliminarLibro(libroId)
            }
            .setNegativeButton("Ez") { dialog, _ ->
                dialog.dismiss() // Itxi egiten dau dialog-a
            }
            .create()

        dialog.show()
    }

    private fun obtenerDetallesLibro(id: Int): LiburuakListatu? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM liburuak WHERE id = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            val libro = LiburuakListatu(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                izenburua = cursor.getString(cursor.getColumnIndexOrThrow("izenburua")),
                egilea = cursor.getString(cursor.getColumnIndexOrThrow("egilea")),
                generoa = cursor.getString(cursor.getColumnIndexOrThrow("generoa")),
                argitaletxea = cursor.getString(cursor.getColumnIndexOrThrow("argitaletxea")),
                prezioa = cursor.getDouble(cursor.getColumnIndexOrThrow("prezioa")),
                eskuragarritasuna = cursor.getInt(cursor.getColumnIndexOrThrow("eskuragarritasuna")) == 1
            )
            cursor.close()
            libro
        } else {
            cursor.close()
            null
        }
    }

    private fun cargarDatosLibro(libro: LiburuakListatu) {
        editTextIzenburua.setText(libro.izenburua)
        editTextEgilea.setText(libro.egilea)
        // Spinnerak kargatu
        val generoaIndex = (spinnerGeneroa.adapter as ArrayAdapter<String>).getPosition(libro.generoa)
        spinnerGeneroa.setSelection(generoaIndex)
        val argitaletxeaIndex = (spinnerArgitaletxea.adapter as ArrayAdapter<String>).getPosition(libro.argitaletxea)
        spinnerArgitaletxea.setSelection(argitaletxeaIndex)
        editTextPrezioa.setText(libro.prezioa.toString())
        checkEskuragarritasuna.isChecked = libro.eskuragarritasuna
    }

    private fun actualizarLibro(id: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("izenburua", editTextIzenburua.text.toString())
            put("egilea", editTextEgilea.text.toString())
            put("generoa", spinnerGeneroa.selectedItem.toString())
            put("argitaletxea", spinnerArgitaletxea.selectedItem.toString())
            put("prezioa", editTextPrezioa.text.toString().toDoubleOrNull() ?: 0.0)
            put("eskuragarritasuna", if (checkEskuragarritasuna.isChecked) 1 else 0)
        }
        val rowsUpdated = db.update("liburuak", values, "id = ?", arrayOf(id.toString()))
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Liburua eguneratua", Toast.LENGTH_SHORT).show()
            finish()
            val intent = Intent(this, LiburuakIkusi::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Errorea eguneratzean", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarLibro(id: Int) {
        val db = dbHelper.writableDatabase
        val rowsDeleted = db.delete("liburuak", "id = ?", arrayOf(id.toString()))
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Liburua ezabatuta", Toast.LENGTH_SHORT).show()
            finish() // Bueltatzen da.
            val intent = Intent(this, LiburuakIkusi::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Errorea ezabatzean", Toast.LENGTH_SHORT).show()
        }
    }
}
