package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Productua_gehitu : AppCompatActivity() {

    lateinit var izenaprod: EditText
    lateinit var mota: Spinner
    lateinit var marka: EditText
    lateinit var prezioa: EditText
    lateinit var btngehitu: Button
    lateinit var database: SQL_User_Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productua_gehitu)

        izenaprod = findViewById(R.id.tfizenprod)
        mota = findViewById(R.id.mota)
        marka = findViewById(R.id.tfmarka)
        prezioa = findViewById(R.id.tfprice)
        btngehitu = findViewById(R.id.button)

        database = SQL_User_Database(this, "Elektronika_Objetuen_Denda.db", null, 1) // Incrementa la versión a 2 o superior

        val ListakoAukerak = arrayOf("Telefonoa", "Ordenagailua", "Telebista", "Osagarria")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ListakoAukerak)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mota.adapter = adapter

        btngehitu.setOnClickListener {
            val izenburua = izenaprod.text.toString()
            val kategoria = mota.selectedItem.toString()
            val markaValue = marka.text.toString()
            val prezioaValue = prezioa.text.toString()

            if (izenburua.isNotEmpty() && markaValue.isNotEmpty() && prezioaValue.isNotEmpty()) {
                try {
                    val prezioaDouble = prezioaValue.toDouble()
                    val result = database.insertProduct(izenburua, kategoria, markaValue, prezioaDouble, true)

                    if (result != -1L) {
                        Toast.makeText(this, "Produktua sortu da", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Menu2::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Errorea produktua sortzerakoan", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Sartu prezio ondo", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Mesedez datu guztiak sartu", Toast.LENGTH_SHORT).show()
            }
        }
    }
}