package com.example.proiekto_denda

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proiekto_denda.GameListActivity
import com.example.proiekto_denda.LoginActivity
import com.example.proiekto_denda.ProductManagementActivity
import com.example.proiekto_denda.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar-a konfiguratu
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    // Menua inflatzea
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Menuaren aukeren ekintzak kudeatzea
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_game -> {
                // Joko bat gehitzeko jarduera batean nabigatu
                startActivity(Intent(this, ProductManagementActivity::class.java))
                return true
            }
            R.id.menu_list_games -> {
                // Jokoen zerrenda erakusten duen jarduera batean nabigatu
                startActivity(Intent(this, GameListActivity::class.java))
                return true
            }
            R.id.menu_logout -> {
                // Saioaren hasierako pantailara itzuli
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return true
            }
            R.id.menu_exit -> {
                // Aplikaziotik irten
                finishAffinity()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
