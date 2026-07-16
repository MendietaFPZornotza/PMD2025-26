package com.example.proiektufinala_asieraldekoag

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.app.AlertDialog
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NombreAdapter
    private lateinit var databaseHelper: SQL_User_Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        databaseHelper = SQL_User_Database(this, "Altzairuen_Denda.db", null, 1)

        val produktuakList = databaseHelper.getProduktuak()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = NombreAdapter(produktuakList) { productId ->
            mostrarDialogoDeBorrado(productId)
        }
        recyclerView.adapter = adapter
    }

    private fun mostrarDialogoDeBorrado(productId: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Seguru zaude produktu hau ezabatu nahi duzula?")

        builder.setPositiveButton("Bai") { dialog, _ ->
            val rowsDeleted = databaseHelper.deleteProduct(productId)
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Produktua ezabatu egin da", Toast.LENGTH_SHORT).show()
                actualizarRecyclerView()
            } else {
                Toast.makeText(this, "Ez da posible produktua ezabatzea", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Ez") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun actualizarRecyclerView() {
        val updatedList = databaseHelper.getProduktuak()
        adapter = NombreAdapter(updatedList) { productId ->
            mostrarDialogoDeBorrado(productId)
        }
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.altzariberria -> {
                val intent = Intent(this, ProductManagementActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            R.id.saioaitxi -> {
                Toast.makeText(this, "Saioa ITXI egin da", Toast.LENGTH_LONG).show()
                val intent1 = Intent(this, MainActivity::class.java)
                startActivity(intent1)
                finish()
                return true
            }
            R.id.irten -> {
                finishAffinity()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}