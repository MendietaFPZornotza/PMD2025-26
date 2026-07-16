package com.example.proiekto_denda

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GameListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_list)

        // Toolbar-a konfiguratu
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // RecyclerView-a konfiguratu
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewGames)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Jokoak kargatu datu-baseetik
        val dbHelper = DBHelper(this)
        val games = dbHelper.getAllGames() // DBHelper-en dagoen funtzioa

        // Adaptadorea konfiguratu klik ekintza batekin
        recyclerView.adapter = GameAdapter(this, games) { selectedGame ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("GAME_ID", selectedGame.id)
            startActivity(intent)
        }
    }

    // Toolbar-ean menua inflatu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu) // Jada daukagun menua erabiltzen dugu
        return true
    }

    // Menuaren aukeren ekintzak kudeatu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_game -> {
                // Joko bat gehitzeko jarduera-ra joan
                startActivity(Intent(this, ProductManagementActivity::class.java))
                return true
            }
            R.id.menu_list_games -> {
                // Jokoen zerrenda erakusten duen jarduera-ra joan
                startActivity(Intent(this, GameListActivity::class.java))
                return true
            }
            R.id.menu_logout -> {
                // Saioan amaitzeko login pantailara itzuli
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return true
            }
            R.id.menu_exit -> {
                // Aplikazioa itxi
                finishAffinity()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
