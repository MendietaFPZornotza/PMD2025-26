package com.example.jostailudendajosu

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


class ProductManagementActivity : AppCompatActivity() {
    lateinit var gorde: Button
    lateinit var irten: Button

    lateinit var izenaInput: EditText
    lateinit var adinInput: EditText
    lateinit var prezioInput: EditText

    lateinit var radioBotoia1: RadioButton
    lateinit var radioBotoia2: RadioButton
    lateinit var radioBotoia3: RadioButton
    lateinit var radioBotoia4: RadioButton

    lateinit var eskuragarri: CheckBox



    lateinit var espiner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_product_management)
        gorde = findViewById(R.id.gorde)
        irten = findViewById(R.id.irten)

        izenaInput = findViewById(R.id.editIZena)
        adinInput = findViewById(R.id.editAdin)
        prezioInput = findViewById(R.id.editPrezio)

        radioBotoia1 = findViewById(R.id.radioBotoia1)
        radioBotoia2 = findViewById(R.id.radioBotoia2)
        radioBotoia3 = findViewById(R.id.radioBotoia3)
        radioBotoia4 = findViewById(R.id.radioBotoia4)

        eskuragarri = findViewById(R.id.checkBox)

        espiner = findViewById(R.id.editEspiner)


        //Arraya listViewentzat
        val ListaAukera = arrayOf("Hezkuntzarako", "Umeentzat", "Zientzia")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ListaAukera)
        espiner.setAdapter(adapter)

        gorde.setOnClickListener { ProduktuakGehitu() }
        irten.setOnClickListener {
            val intent = Intent(this, PantailaPrintzipala::class.java)
            startActivity(intent)
        }
    }

    //Produktuak gehitzeko funtzioa (Insert)
    private fun ProduktuakGehitu() {
        val izena = izenaInput.text.toString()
        val adina = adinInput.text.toString().toIntOrNull()
        val prezioa = prezioInput.text.toString().toDoubleOrNull()
        val eskuragarriEgoera = eskuragarri.isChecked

        val jatorria = when {
            radioBotoia1.isChecked -> "Taiwan"
            radioBotoia2.isChecked -> "China"
            radioBotoia3.isChecked -> "Japon"
            radioBotoia4.isChecked -> "Euskadi"
            else -> "Ezezaguna"
        }

        val mota = espiner.selectedItem.toString()

        if (izena.isEmpty() || adina == null || prezioa == null) {
            Toast.makeText(this, "Mesedez, bete kanpo guztiak", Toast.LENGTH_SHORT).show()
            return
        }

        val sqlAdmin = SqlProduktuaGorde(this, "produktuakDB", null, 1)
        val db = sqlAdmin.writableDatabase

        val cursor = db.rawQuery("SELECT MAX(codigo) FROM produktuak", null)
        var nuevoCodigo = 1
        if (cursor.moveToFirst()) {
            nuevoCodigo = cursor.getInt(0) + 1
        }
        cursor.close()
        //Log.d("ProductDetailActivity", "mota ID: $mota")
        //Log.d("ProductDetailActivity", "jatorri ID: $jatorria")
        //Baloreak Sartzeko
        val values = ContentValues().apply {
            put("codigo", nuevoCodigo)
            put("izenburua", izena)
            put("mota", mota)
            put("adin", adina)
            put("jatorria", jatorria)
            put("prezioa", prezioa)
            put("eskuragarri", if (eskuragarriEgoera) 1 else 0)
        }

        val newRowId = db.insert("produktuak", null, values)
        //Log.d("SqlProduktuaGorde", "New product ID: $newRowId")

        db.close()
        //Toast bat erakuzteko
        if (newRowId != -1L) {
            Toast.makeText(this, "Produktu berria gorde da!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PantailaPrintzipala::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Errorea produktu berria gordetzean", Toast.LENGTH_SHORT).show()
        }
    }




}