package com.example.proiektua

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddProductActivity : AppCompatActivity() {
    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Inicializar el helper de la base de datos
        dbHelper = AdminSQLiteOpenHelper(this)

        // Referencias a los elementos de la interfaz de usuario
        val editProductName = findViewById<EditText>(R.id.edit_product_title)
        val spinnerProductType = findViewById<Spinner>(R.id.spinner_product_type)
        val editProductSize = findViewById<EditText>(R.id.edit_product_size)
        val checkboxOriginSpain = findViewById<CheckBox>(R.id.checkbox_origin_spain)
        val checkboxOriginTaiwan = findViewById<CheckBox>(R.id.checkbox_origin_taiwan)
        val checkboxOriginEngland = findViewById<CheckBox>(R.id.checkbox_origin_england)
        val editProductPrice = findViewById<EditText>(R.id.edit_product_price)
        val checkboxAvailability = findViewById<CheckBox>(R.id.checkbox_availability)
        val buttonAddProduct = findViewById<Button>(R.id.button_add_product)

        // Configuración del Spinner con diseño personalizado
        val motaAukerak = arrayOf("Zapatilak", "Baloiak", "Entrenamendu Ekipamendua")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, motaAukerak)
        spinnerProductType.adapter = adapter

        // Acción para limitar la selección de los CheckBox
        checkboxOriginSpain.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkboxOriginTaiwan.isChecked = false
                checkboxOriginEngland.isChecked = false
            }
        }

        checkboxOriginTaiwan.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkboxOriginSpain.isChecked = false
                checkboxOriginEngland.isChecked = false
            }
        }

        checkboxOriginEngland.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkboxOriginSpain.isChecked = false
                checkboxOriginTaiwan.isChecked = false
            }
        }

        // Acción al hacer click en el botón "Añadir Producto"
        buttonAddProduct.setOnClickListener {
            val productName = editProductName.text.toString()
            val productType = spinnerProductType.selectedItem.toString()
            val productSize = editProductSize.text.toString()
            val productOrigin = when {
                checkboxOriginSpain.isChecked -> "Espainia"
                checkboxOriginTaiwan.isChecked -> "Taiwan"
                checkboxOriginEngland.isChecked -> "Ingalaterra"
                else -> null
            }
            val productPrice = editProductPrice.text.toString().toDoubleOrNull()
            val productAvailability = if (checkboxAvailability.isChecked) 1 else 0

            // Validación de los datos
            if (productName.isNotEmpty() && productType.isNotEmpty() && productSize.isNotEmpty() && productOrigin != null && productPrice != null) {
                val db = dbHelper.writableDatabase

                // Crear ContentValues para insertar el producto
                val values = ContentValues().apply {
                    put("produktua", productName)
                    put("tipo", productType)
                    put("talla", productSize)
                    put("jatorria", productOrigin)
                    put("precio", productPrice)
                    put("eskuragarritasuna", productAvailability)
                }

                // Insertar el producto en la base de datos
                val newRowId = db.insert("productos", null, values)
                if (newRowId != -1L) {
                    Toast.makeText(this, "Produktua ondo gehitu da!", Toast.LENGTH_SHORT).show()
                    finish() // Cerrar la actividad y volver atrás
                } else {
                    Toast.makeText(this, "Errorea produktua gehitzerakoan", Toast.LENGTH_SHORT).show()
                }
                db.close()
            } else {
                Toast.makeText(this, "Mesedez, bete derrigorrezko eremuak", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    // Gestión de eventos de los ítems del menú de opciones
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_add_product -> {
                // Acción para "Kirol produktu Berria Gehitu"
                val intent = Intent(this, AddProductActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_product_list -> {
                // Acción para "Kirol produktuen Zerrenda"
                val intent = Intent(this, ProductListActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_logout -> {
                // "Saioa itxi" - Redirigir al login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()  // Cierra la actividad actual
                true
            }
            R.id.nav_exit -> {
                // "Irten" - Cerrar la aplicación
                finishAffinity()  // Cierra la aplicación por completo
                true
            }
            else -> false
        }
    }

}
