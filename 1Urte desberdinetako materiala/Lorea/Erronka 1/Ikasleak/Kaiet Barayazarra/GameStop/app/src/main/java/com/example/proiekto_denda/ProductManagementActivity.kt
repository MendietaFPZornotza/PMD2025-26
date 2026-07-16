package com.example.proiekto_denda

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProductManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_management)

        // Toolbar-a konfiguratu
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Layout-eko eremuen erreferentziak
        val etGameTitle = findViewById<EditText>(R.id.etGameTitle)
        val spinnerGenre = findViewById<Spinner>(R.id.spinnerGenre)
        val checkPS5 = findViewById<CheckBox>(R.id.checkPS5)
        val checkXbox = findViewById<CheckBox>(R.id.checkXbox)
        val checkPC = findViewById<CheckBox>(R.id.checkPC)
        val checkSwitch = findViewById<CheckBox>(R.id.checkSwitch)
        val etPrice = findViewById<EditText>(R.id.etPrice)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val checkAvailability = findViewById<CheckBox>(R.id.checkAvailability)
        val btnAddGame = findViewById<Button>(R.id.btnAddGame)

        // Generoen Spinner-a konfiguratu
        val genres = arrayOf("Ekintza", "Abentura", "Kirolak", "RPG", "Estrategia")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genres)
        spinnerGenre.adapter = spinnerAdapter

        // "Bideojoko Bat Gehitu" botoian klik egitean egingo den ekintza
        btnAddGame.setOnClickListener {
            val title = etGameTitle.text.toString()
            val genre = spinnerGenre.selectedItem.toString()
            val platforms = mutableListOf<String>()
            if (checkPS5.isChecked) platforms.add("PS5")
            if (checkXbox.isChecked) platforms.add("Xbox")
            if (checkPC.isChecked) platforms.add("PC")
            if (checkSwitch.isChecked) platforms.add("Nintendo Switch")
            val price = etPrice.text.toString().toDoubleOrNull()
            val description = etDescription.text.toString()
            val availability = checkAvailability.isChecked

            if (title.isNotEmpty() && price != null) {
                // DBHelper erabiliz bideojokoa gorde
                val dbHelper = DBHelper(this)
                val success = dbHelper.insertGame(
                    title = title,
                    genre = genre,
                    platforms = platforms.joinToString(", "),
                    price = price,
                    description = description,
                    available = availability
                )

                if (success) {
                    Toast.makeText(this, "Bideojokoa ongi gehitu da!", Toast.LENGTH_LONG).show()
                    clearFields()
                } else {
                    Toast.makeText(this, "Errorea bideojokoa gehitzerakoan.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Bete eremu guztiak mesedez.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Eremuak garbitzeko metodoa
    private fun clearFields() {
        findViewById<EditText>(R.id.etGameTitle).text.clear()
        findViewById<Spinner>(R.id.spinnerGenre).setSelection(0)
        findViewById<CheckBox>(R.id.checkPS5).isChecked = false
        findViewById<CheckBox>(R.id.checkXbox).isChecked = false
        findViewById<CheckBox>(R.id.checkPC).isChecked = false
        findViewById<CheckBox>(R.id.checkSwitch).isChecked = false
        findViewById<EditText>(R.id.etPrice).text.clear()
        findViewById<EditText>(R.id.etDescription).text.clear()
        findViewById<CheckBox>(R.id.checkAvailability).isChecked = false
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
                // Jada bideojokoak gehitzeko jardueran zaude, mezua erakutsi
                Toast.makeText(this, "Hemen zaude: Gehitu bideojoko bat", Toast.LENGTH_SHORT).show()
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
