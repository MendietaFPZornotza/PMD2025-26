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

class PantailaPrintzipala : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantaila_printzipala)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.JosGorde-> {
                val intent = Intent(this, ProductManagementActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.JosZer-> {
                Toast.makeText(this, "Jostailu  Zerrenda Sakatu da", Toast.LENGTH_LONG).show()
                true
            }
            R.id.Saitxi-> {
                Toast.makeText(this, "Saioa Itxi da", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.irten-> {
                Toast.makeText(this, "irten  Zerrenda Sakatu da", Toast.LENGTH_LONG).show()
                finishAffinity()
                true
            }else-> return super.onOptionsItemSelected(item)
        }
    }
}