package com.example.proiekto_denda

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var spinnerGenre: Spinner
    private lateinit var checkPS5: CheckBox
    private lateinit var checkXbox: CheckBox
    private lateinit var checkPC: CheckBox
    private lateinit var checkSwitch: CheckBox
    private lateinit var etPrice: EditText
    private lateinit var etDescription: EditText
    private lateinit var checkAvailability: CheckBox
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    private var gameId: Int? = null // Jokoaren IDa editatzeko moduan
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Toolbar-a konfiguratu
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Ikusgarriak hasieratu
        etTitle = findViewById(R.id.etGameTitle)
        spinnerGenre = findViewById(R.id.spinnerGenre)
        checkPS5 = findViewById(R.id.checkPS5)
        checkXbox = findViewById(R.id.checkXbox)
        checkPC = findViewById(R.id.checkPC)
        checkSwitch = findViewById(R.id.checkSwitch)
        etPrice = findViewById(R.id.etPrice)
        etDescription = findViewById(R.id.etDescription)
        checkAvailability = findViewById(R.id.checkAvailability)
        btnUpdate = findViewById(R.id.btnUpdateGame)
        btnDelete = findViewById(R.id.btnDeleteGame)

        dbHelper = DBHelper(this)

        // Editatzeko moduan gauden egiaztatzea
        gameId = intent.getIntExtra("GAME_ID", -1).takeIf { it != -1 }

        if (gameId != null) {
            loadGameData() // Jokoaren datuak kargatu editatzeko
            btnDelete.visibility = Button.VISIBLE // Ezabatzeko botoia erakutsi editatzeko moduan
        }

        // Aldaketak gorde edo joko berri bat sortu
        btnUpdate.setOnClickListener {
            saveOrUpdateGame()
        }

        // Jokoaren ezabaketa
        btnDelete.setOnClickListener {
            deleteGame()
        }
    }

    // Menua inflatzea
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu) // Jada badagoen menua erabiltzen dugu
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

    private fun loadGameData() {
        // Jokoaren datuak lortu datu-basean
        val game = dbHelper.getAllGames().find { it.id == gameId }
        game?.let {
            etTitle.setText(it.title)

            // Spinnerrean generoa hautatzea
            val genreIndex = resources.getStringArray(R.array.genres).indexOf(it.genre)
            if (genreIndex >= 0) spinnerGenre.setSelection(genreIndex)

            // Markatutako plataformak hautatzea
            checkPS5.isChecked = it.platforms.contains("PS5")
            checkXbox.isChecked = it.platforms.contains("Xbox")
            checkPC.isChecked = it.platforms.contains("PC")
            checkSwitch.isChecked = it.platforms.contains("Nintendo Switch")

            etPrice.setText(it.price.toString())
            etDescription.setText(it.description)
            checkAvailability.isChecked = it.available
        } ?: run {
            Toast.makeText(this, "Errore bat egon da jokoaren datuak kargatzean", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun saveOrUpdateGame() {
        val title = etTitle.text.toString()
        val genre = spinnerGenre.selectedItem.toString()
        val platforms = mutableListOf<String>().apply {
            if (checkPS5.isChecked) add("PS5")
            if (checkXbox.isChecked) add("Xbox")
            if (checkPC.isChecked) add("PC")
            if (checkSwitch.isChecked) add("Nintendo Switch")
        }.joinToString(", ")
        val price = etPrice.text.toString().toDoubleOrNull()
        val description = etDescription.text.toString()
        val available = checkAvailability.isChecked

        // Datuak egiaztatzea
        if (title.isEmpty() || genre.isEmpty() || platforms.isEmpty() || price == null) {
            Toast.makeText(this, "Mesedez, bete guztiak datuak zuzen", Toast.LENGTH_SHORT).show()
            return
        }

        if (gameId == null) {
            // Joko berri bat sortu
            val success = dbHelper.insertGame(title, genre, platforms, price, description, available)
            if (success) {
                Toast.makeText(this, "Joko berria arrakastaz gehitu da", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, GameListActivity::class.java)
                intent.putExtra("REFRESH_LIST", true)  // Parametro gehigarria zerrenda eguneratu behar dela adierazteko
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Errore bat egon da jokoaren gehitzean", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Joko existitua eguneratu
            val success = dbHelper.updateGame(gameId!!, title, genre, platforms, price, description, available)
            if (success) {
                Toast.makeText(this, "Joko arrakastaz eguneratu da", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, GameListActivity::class.java)
                intent.putExtra("REFRESH_LIST", true)  // Parametro gehigarria zerrenda eguneratu behar dela adierazteko
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Errore bat egon da jokoaren eguneratzean", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteGame() {
        gameId?.let {
            // Confirmatzeko AlertDialog bat sortu
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Ezabatzea baieztatu")
            builder.setMessage("Ziur zaude joko hau ezabatu nahi duzula?")
            builder.setPositiveButton("Bai") { dialog, which ->
                val success = dbHelper.deleteGameById(it)
                if (success) {
                    Toast.makeText(this, "Joko arrakastaz ezabatu da", Toast.LENGTH_SHORT).show()
                    // Zerrendara itzuli
                    val intent = Intent(this, GameListActivity::class.java)
                    intent.putExtra("REFRESH_LIST", true)  // Parametro gehigarria zerrenda eguneratu behar dela adierazteko
                    startActivity(intent)
                    finish() // Gaur egungo jarduera amaitu
                } else {
                    Toast.makeText(this, "Errore bat egon da jokoaren ezabatzean", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Ez") { dialog, which ->
                dialog.dismiss() // Baieztapena ezeztatzea
            }

            // Dialogoa erakutsi
            builder.show()
        }
    }
}
