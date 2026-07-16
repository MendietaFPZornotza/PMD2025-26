package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity


class updateproduktua : AppCompatActivity() {
    private lateinit var edtIzenburua: EditText
    private lateinit var edtKategoria: Spinner
    private lateinit var edtPrezioa: EditText
    private lateinit var edtMarca: EditText
    private lateinit var chkEskuragarritasuna: CheckBox
    private lateinit var btnUpdate: Button
    private lateinit var databaseHelper: SQL_User_Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updateproduktua)


        edtIzenburua = findViewById(R.id.tfizenprod)
        edtMarca = findViewById(R.id.tfmarka)
        edtPrezioa = findViewById(R.id.tfprice)
        chkEskuragarritasuna = findViewById(R.id.eskuragarritasuna)
        btnUpdate = findViewById(R.id.produktuaeguneratu)


        edtKategoria = findViewById(R.id.mota)
        val listakoAukerak = arrayOf("Telefonoa", "Ordenagailua", "Telebista", "Osagarria")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listakoAukerak)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edtKategoria.adapter = adapter


        databaseHelper = SQL_User_Database(this, "Elektronika_Objetuen_Denda.db", null, 1)


        val productId = intent.getIntExtra("productId", -1) // -1 es el valor por defecto si no se encuentra el extra
        Log.d("updateproduktua", "Product ID recibido: $productId")

        if (productId != -1) {
            Log.d("updateproduktua", "Llamando a loadProductData con ID: $productId")
            loadProductData(productId)
        } else {
            Log.e("updateproduktua", "ID no válido recibido: $productId")
        }


        btnUpdate.setOnClickListener {
            val newIzenburua = edtIzenburua.text.toString().trim()
            val newKategoria = edtKategoria.selectedItem.toString()
            val newPrezioa = edtPrezioa.text.toString().toDoubleOrNull()  // El valor de precio es un Double, pero lo convertimos a String antes de pasar
            val newMarca = edtMarca.text.toString().trim()
            val isAvailable = chkEskuragarritasuna.isChecked

            if (newIzenburua.isNotEmpty() && newKategoria.isNotEmpty() && newPrezioa != null && newMarca.isNotEmpty()) {

                val success = databaseHelper.updateProduct(productId, newIzenburua, newKategoria, newMarca, newPrezioa, isAvailable)

                if (success) {
                    Toast.makeText(this, "Produktua eguneratuta", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Menu2::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Errorea eguneratzerakoan", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Mesedez, bete eremu guztiak", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadProductData(productId: Int) {
        val product = databaseHelper.getProductById(productId)
        if (product != null) {

            Log.d("updateproduktua", "Product: $product")
            edtIzenburua.setText(product.izenburua)
            edtMarca.setText(product.marka)
            edtPrezioa.setText(product.prezioa.toString())
            chkEskuragarritasuna.isChecked = product.eskuragarritasuna

            val categories = arrayOf("Telefonoa", "Ordenagailua", "Telebista", "Osagarria")
            val position = categories.indexOf(product.kategoria)
            if (position >= 0) {
                edtKategoria.setSelection(position)
            } else {
                edtKategoria.setSelection(0)
            }
        } else {
            Toast.makeText(this, "Ez da produkturik aurkitu ID honekin: $productId", Toast.LENGTH_SHORT).show()
        }
    }
}