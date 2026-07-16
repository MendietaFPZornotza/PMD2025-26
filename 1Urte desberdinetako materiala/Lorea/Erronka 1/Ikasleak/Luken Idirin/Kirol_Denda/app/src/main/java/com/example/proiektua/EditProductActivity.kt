package com.example.proiektua

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class EditProductActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper
    private var productId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        // Inicializar la base de datos
        dbHelper = AdminSQLiteOpenHelper(this)

        // Obtener el ID del producto pasado desde ProductListActivity
        productId = intent.getIntExtra("product_id", -1)
        if (productId == -1) {
            Toast.makeText(this, "Errorea produktua kargatzean", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Referencias a las vistas
        val titleEditText: EditText = findViewById(R.id.edit_product_title)
        val typeSpinner: Spinner = findViewById(R.id.spinner_product_type)
        val sizeEditText: EditText = findViewById(R.id.edit_product_size)
        val priceEditText: EditText = findViewById(R.id.edit_product_price)
        val availabilityCheckBox: CheckBox = findViewById(R.id.checkbox_availability)
        val originSpainCheckBox: CheckBox = findViewById(R.id.checkbox_origin_spain)
        val originTaiwanCheckBox: CheckBox = findViewById(R.id.checkbox_origin_taiwan)
        val originEnglandCheckBox: CheckBox = findViewById(R.id.checkbox_origin_england)
        val saveButton: Button = findViewById(R.id.button_save_changes)
        val backButton: Button = findViewById(R.id.button_back_to_list)

        // Agregar listeners a los checkboxes
        originSpainCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                originTaiwanCheckBox.isChecked = false
                originEnglandCheckBox.isChecked = false
            }
        }

        originTaiwanCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                originSpainCheckBox.isChecked = false
                originEnglandCheckBox.isChecked = false
            }
        }

        originEnglandCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                originSpainCheckBox.isChecked = false
                originTaiwanCheckBox.isChecked = false
            }
        }

        // Ejecutar consulta para cargar los datos
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT produktua, tipo, talla, jatorria, precio, eskuragarritasuna FROM productos WHERE id = ?",
            arrayOf(productId.toString())
        )

        if (cursor.moveToFirst()) {
            titleEditText.setText(cursor.getString(0)) // izena
            sizeEditText.setText(cursor.getString(2)) // talla
            priceEditText.setText(cursor.getDouble(4).toString()) // prezioa
            availabilityCheckBox.isChecked = cursor.getInt(5) == 1 // eskuragarritasuna

            // Marcar los orígenes
            val origins = cursor.getString(3)?.split(",") ?: emptyList()
            originSpainCheckBox.isChecked = "Espainia" in origins
            originTaiwanCheckBox.isChecked = "Taiwan" in origins
            originEnglandCheckBox.isChecked = "Ingalaterra" in origins

            // Configuración del Spinner con diseño personalizado
            val motaAukerak = arrayOf("Zapatilak", "Baloiak", "Entrenamendu Ekipamendua")
            val adapter = ArrayAdapter(this, R.layout.spinner_item, motaAukerak)
            typeSpinner.adapter = adapter
            val spinnerPosition = motaAukerak.indexOf(cursor.getString(1)) // mota
            typeSpinner.setSelection(spinnerPosition)
        } else {
            Toast.makeText(this, "Errorea produktua kargatzean", Toast.LENGTH_SHORT).show()
            finish()
        }
        cursor.close()

        // Botón de guardar cambios
        saveButton.setOnClickListener {
            val updatedName = titleEditText.text.toString()
            val updatedSize = sizeEditText.text.toString()
            val updatedPrice = priceEditText.text.toString().toDoubleOrNull() ?: 0.0
            val updatedAvailability = if (availabilityCheckBox.isChecked) 1 else 0
            val updatedType = typeSpinner.selectedItem.toString()

            // Obtener orígenes seleccionados
            val updatedOrigins = mutableListOf<String>()
            if (originSpainCheckBox.isChecked) updatedOrigins.add("Espainia")
            if (originTaiwanCheckBox.isChecked) updatedOrigins.add("Taiwan")
            if (originEnglandCheckBox.isChecked) updatedOrigins.add("Ingalaterra")
            val updatedOrigin = updatedOrigins.joinToString(",")

            // Actualizar en la base de datos
            val registro = ContentValues().apply {
                put("produktua", updatedName)
                put("tipo", updatedType)
                put("talla", updatedSize)
                put("jatorria", updatedOrigin)
                put("precio", updatedPrice)
                put("eskuragarritasuna", updatedAvailability)
            }

            val rowsUpdated = db.update(
                "productos",
                registro,
                "id = ?",
                arrayOf(productId.toString())
            )

            if (rowsUpdated > 0) {
                Toast.makeText(this, "Produktua aldatu da", Toast.LENGTH_SHORT).show()
                // Agregar este bloque para pasar el resultado a ProductListActivity
                val resultIntent = Intent()
                setResult(RESULT_OK, resultIntent) // Indicar que la actualización fue exitosa
                finish() // Terminar la actividad y regresar
            } else {
                Toast.makeText(this, "Errorea produktua aldatzean", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón de volver atrás
        backButton.setOnClickListener {
            finish()
        }
    }
}
