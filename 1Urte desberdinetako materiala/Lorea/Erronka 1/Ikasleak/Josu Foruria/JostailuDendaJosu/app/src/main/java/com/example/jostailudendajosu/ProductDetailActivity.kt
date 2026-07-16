package com.example.jostailudendajosu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var izenburuaEditText: EditText
    private lateinit var adinEditText: EditText

    private lateinit var prezioaEditText: EditText
    private lateinit var eskuragarriCheckBox: CheckBox

    private lateinit var jatorriaEditSpinner: Spinner
    private lateinit var Espinerr: Spinner

    lateinit var eguneratu: Button
    lateinit var ezaba: Button
    lateinit var irten: Button

    private lateinit var dbHelper: SqlProduktuaGorde

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        izenburuaEditText = findViewById(R.id.izenburuaEditText)
        Espinerr= findViewById(R.id.editEspiner2)
        adinEditText = findViewById(R.id.adinEditText)
        jatorriaEditSpinner = findViewById(R.id.editEspinerJatorria)
        prezioaEditText = findViewById(R.id.prezioaEditText)
        eskuragarriCheckBox = findViewById(R.id.eskuragarriCheckBox)

        eguneratu = findViewById(R.id.eguneratuButton)
        ezaba = findViewById(R.id.ezabatuButton)
        irten = findViewById(R.id.irtenButton)

        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        //Log.d("ProductDetailActivity", "Product ID: $productId")

        dbHelper = SqlProduktuaGorde(this, "produktuakDB", null, 1)
        val produk = dbHelper.getProduktuaById(productId)

        //Produktuak erakuzteko bere baloreekin
        if (produk != null) {
            izenburuaEditText.setText(produk.izenburua)
            val ListaAukeraMota = arrayOf("Hezkuntzarako", "Umeentzat", "Zientzia")
            val adapterMota = ArrayAdapter(this, android.R.layout.simple_spinner_item, ListaAukeraMota)
            adapterMota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            Espinerr.adapter = adapterMota

            val ListaAukeraJatorria = arrayOf("China", "Taiwan", "Japon", "Euskadi")
            val adapterJatorria = ArrayAdapter(this, android.R.layout.simple_spinner_item, ListaAukeraJatorria)
            adapterJatorria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            jatorriaEditSpinner.adapter = adapterJatorria

            val indexmota = ListaAukeraMota.indexOf(produk.mota)
            if (indexmota >= 0) {
                Espinerr.setSelection(indexmota)
            }
            val indexJatorri = ListaAukeraJatorria.indexOf(produk.mota)
            if (indexJatorri >= 0) {
                jatorriaEditSpinner.setSelection(indexJatorri)
            }
            eskuragarriCheckBox.isChecked = produk.eskuragarri
            adinEditText.setText(produk.adin.toString())
            prezioaEditText.setText(produk.prezioa.toString())
        } else {

            Toast.makeText(this, "Produktua ez aurkitua", Toast.LENGTH_SHORT).show()

            finish()
        }

        //Irten botoia, Vueltatzeko Pantaila printzipalera
        irten.setOnClickListener {
            Toast.makeText(this, "Pantaila printzipala!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PantailaPrintzipala::class.java)
            startActivity(intent)

        }

        //Ezabatu botoia, produktu bat borratzeko
        ezaba.setOnClickListener {
            if (productId != -1) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Konfirmazioa")
                builder.setMessage("Zihur zaude produktua ezabatu nahi duzula?")
                builder.setPositiveButton("Bai") { _, _ ->
                    val isDeleted = dbHelper.deleteProduktuaById(productId)
                    if (isDeleted) {
                        Toast.makeText(this, "Produktua borratuta", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, PantailaPrintzipala::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Errorea Produktua Eliminatzen", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.setNegativeButton("Ez") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.show()
            } else {
                Toast.makeText(this, "Id-a ez da egokia", Toast.LENGTH_SHORT).show()
            }
        }


        //Eguneratu botoia, Produktu bat eguneratzeko
        eguneratu.setOnClickListener {
            val izenburua = izenburuaEditText.text.toString()
            val mota = Espinerr.selectedItem.toString()
            val adin = adinEditText.text.toString().toIntOrNull() ?: 0
            val jatorria = jatorriaEditSpinner.selectedItem.toString()
            val prezioa = prezioaEditText.text.toString().toDoubleOrNull() ?: 0.0
            val eskuragarri = eskuragarriCheckBox.isChecked

            if (izenburua.isEmpty() || adin == null || prezioa == null) {
                Toast.makeText(this, "Mesedez, ez utzi balorerik gabe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (productId != -1) {
                val produktua = Produktuak(
                    codigo = productId,
                    izenburua = izenburua,
                    mota = mota,
                    adin = adin,
                    jatorria = jatorria,
                    prezioa = prezioa,
                    eskuragarri = eskuragarri
                )

                val isUpdated = dbHelper.updateProduktua(produktua)
                if (isUpdated) {
                    Toast.makeText(this, "Produktua eguneratua!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, PantailaPrintzipala::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Errorea produktua eguneratzen", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Produktua ez da aurkitua", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
