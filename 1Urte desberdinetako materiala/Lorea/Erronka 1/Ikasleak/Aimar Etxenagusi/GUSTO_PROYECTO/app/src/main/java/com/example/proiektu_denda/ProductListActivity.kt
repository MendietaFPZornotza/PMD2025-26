package com.example.proiektu_denda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proiektu_denda.modelo.Producto

class ProductListActivity : AppCompatActivity() {
    private lateinit var lista:RecyclerView
    private lateinit var productDatabaseHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        lista = findViewById(R.id.lista)


        productDatabaseHelper = AdminSQLiteOpenHelper(this, "administracion.db", null, 1)
        //produktu guztiak lista baten sartzen du gero listan erakukusteko
        val productList: List<Producto> = productDatabaseHelper.getAllProducts()


        lista.layoutManager = LinearLayoutManager(this)

        //listan sartu produktuak
        lista.adapter = ProductAdapter(productList)


    }

    //menua ikusteko
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.aukerenmenua, menu)
        return true
    }

    //menuko aukeren ekintzak
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.produktuBerria->{
                val i = Intent(this, com.example.proiektu_denda.ProductManagementActivity::class.java)
                startActivity(i)
                true
            }

            R.id.zerrendaIkusi->{
                val i = Intent(this, com.example.proiektu_denda.ProductListActivity::class.java)
                startActivity(i)
                true
            }

            R.id.SaioaItxi->{
                Toast.makeText(this, "Saioa itxita", Toast.LENGTH_LONG).show()
                val i = Intent(this, com.example.proiektu_denda.Login::class.java)
                startActivity(i)
                true
            }

            R.id.salir->{
                finishAffinity()
                true
            }
            else->return super.onOptionsItemSelected(item)
        }
    }
}