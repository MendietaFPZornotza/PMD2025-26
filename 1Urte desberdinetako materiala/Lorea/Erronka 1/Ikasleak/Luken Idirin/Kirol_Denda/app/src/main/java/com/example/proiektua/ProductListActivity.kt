package com.example.proiektua

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proiektua.adapter.ProductAdapter
import com.example.proiektua.modelo.Product

class ProductListActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        // Inicializa el RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializa el dbHelper
        dbHelper = AdminSQLiteOpenHelper(this)

        // Obtención de productos desde la base de datos
        var products = dbHelper.getAllProducts()

        // Mapeo de los productos en un adapter para el RecyclerView
        productAdapter = ProductAdapter(this, products)  // Pasando el contexto y los productos
        recyclerView.adapter = productAdapter

        // Configurar el evento de edición
        productAdapter.setOnEditClickListener { productId ->
            val intent = Intent(this, EditProductActivity::class.java)
            intent.putExtra("product_id", productId)  // Pasamos el ID del producto a editar
            startActivityForResult(intent, EDIT_PRODUCT_REQUEST_CODE)
        }

        productAdapter.setOnDeleteClickListener { productId ->
            // Eliminar el producto de la base de datos
            val rowsDeleted = dbHelper.writableDatabase.delete(
                "productos",
                "id = ?",
                arrayOf(productId.toString())
            )

            if (rowsDeleted > 0) {
                Toast.makeText(this, "Produktua ezabatu da.", Toast.LENGTH_SHORT).show()
                // Actualizar la lista de productos
                val products = dbHelper.getAllProducts()
                productAdapter.updateProducts(products)
            } else {
                Toast.makeText(this, "Errorea produktua ezabatzean.", Toast.LENGTH_SHORT).show()
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

    // Aquí recibimos el resultado de la actividad EditProductActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PRODUCT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Si el producto fue editado correctamente, recargar la lista de productos
            val products = dbHelper.getAllProducts()  // Recargar los productos desde la base de datos
            productAdapter.updateProducts(products)  // Actualizar el adaptador con la lista nueva
        }
    }

    companion object {
        private const val EDIT_PRODUCT_REQUEST_CODE = 1
    }
}
