package com.example.jostailudendajosu

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PantailaPrintzipala : AppCompatActivity() {
    private lateinit var produktuakAdapter: ProduktuaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantaila_printzipala)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Menua agertzeko
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

        //Menu Item aukeratzeko
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            //Produktuak gehitzeko zerrendara eramaten du
            R.id.JosGorde-> {
                val intent = Intent(this, ProductManagementActivity::class.java)
                startActivity(intent)
                true
            }
            //Jostailu zerrenda erakuzten du
            R.id.JosZer -> {
                val toolbar: Toolbar = findViewById(R.id.toolbar)
                setSupportActionBar(toolbar)

                val dbHelper = SqlProduktuaGorde(this, "produktuakDB", null, 1)
                val produktuakList = dbHelper.getProduktuak()

                produktuakAdapter = ProduktuaAdapter(produktuakList)


                val recyclerView = findViewById<RecyclerView>(R.id.productosRecyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = produktuakAdapter
                true
            }
            //Saioa sarratzeko botoia
            R.id.Saitxi-> {
                Toast.makeText(this, "Saioa Itxi da", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            //Aplikazioa sarratzeko botoia
            R.id.irten-> {
                Toast.makeText(this, "irten  Zerrenda Sakatu da", Toast.LENGTH_LONG).show()
                finishAffinity()
                true
            }else-> return super.onOptionsItemSelected(item)
        }
    }

}