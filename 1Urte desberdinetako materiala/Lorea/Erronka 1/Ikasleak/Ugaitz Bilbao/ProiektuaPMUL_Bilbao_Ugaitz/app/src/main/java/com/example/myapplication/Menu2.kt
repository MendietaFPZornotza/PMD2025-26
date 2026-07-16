package com.example.myapplication

import Produktuak
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog

class Menu2 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NombreAdapter
    private lateinit var databaseHelper: SQL_User_Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu2)


        databaseHelper = SQL_User_Database(this, "Elektronika_Objetuen_Denda.db", null, 1)


        val produktuakList = databaseHelper.getProduktuak()


        recyclerView = findViewById(R.id.produktuak)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NombreAdapter(produktuakList)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_create_product -> {
                val intent = Intent(this, Productua_gehitu::class.java)
                startActivity(intent)
                true
            }
            R.id.action_delete_product -> {
                val selectedItems = adapter.getSelectedItems()
                if (selectedItems.isNotEmpty()) {
                    // Confirmar la eliminación de los productos seleccionados
                    AlertDialog.Builder(this)
                        .setTitle("Ezabatzeko baieztapena")
                        .setMessage("Ziur zaude hautatutako produktuak ezabatu nahi dituzula?")
                        .setPositiveButton("Sí") { _, _ ->

                            selectedItems.forEach { product: Produktuak ->
                                databaseHelper.deleteProduct(product.id)
                            }


                            val updatedList = databaseHelper.getProduktuak()
                            adapter.updateData(updatedList)

                            // Limpiar la selección
                            adapter.clearSelection()

                            Toast.makeText(this, "Produktua ezabatu da", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("No", null)
                        .show()
                } else {

                    Toast.makeText(this, "Mesedez produktu bat aikeratu gutxienez", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.action_update_product -> {
                val selectedItems = adapter.getSelectedItems()
                if (selectedItems.size == 1) {

                    val product = selectedItems[0]
                    val intent = Intent(this, updateproduktua::class.java)
                    intent.putExtra("productId", product.id)
                    intent.putExtra("product_name", product.izenburua)
                    intent.putExtra("product_category", product.kategoria)
                    intent.putExtra("product_price", product.prezioa)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Mesedez, hautatu produktu bat soilik eguneratzeko", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.action_log_out -> {
                val intent = Intent(this, MainActivity::class.java) // Actividad de inicio de sesión
                startActivity(intent)
                finish()
                true
            }
            R.id.action_go_out -> {
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}