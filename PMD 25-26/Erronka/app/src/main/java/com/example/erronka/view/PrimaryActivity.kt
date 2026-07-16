package com.example.erronka.view

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.erronka.R
import com.example.erronka.conexionBD.SocketClient
import com.example.erronka.databinding.ActivityPrimaryBinding
import com.example.erronka.model.Liburu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class PrimaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrimaryBinding
    private lateinit var client: SocketClient
    //private lateinit var adapter: LiburuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrimaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        //ActionBar-eko izenburuaren testua (title) erakutsi edo ezkutatzeko balio du
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.tableRecyclerView.layoutManager = LinearLayoutManager(this)

        client = SocketClient(this)

        binding.topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_liburuak -> {
                    binding.lblOpzioa.text = "LIBURUAK"
                    Toast.makeText(this, "Liburuak kargatzen... ", Toast.LENGTH_SHORT).show()
                    /**
                     * Coroutine bat abiarazten da Main thread-ean
                     * UI eguneratu behar dalako azken emaitzarekin
                     * Sareko eskaerak background thread-ean egingo dira
                     */
                    CoroutineScope(Dispatchers.Main).launch {
                        val sb = StringBuilder() // Testu guztia gordetzeko StringBuilder
                        // ----------------- LIBURUAK -----------------
                        var books: MutableList<Liburu>? = null
                        try {
                            // Liburuak zerbitzaritik lortu
                            /**
                             * Sareko eskaera: client.getBooks()
                             * withContext(Dispatchers.IO): background thread-ean exekutatzen da, UI ez blokeatzeko
                             * Emaitza books-ean gordetzen da
                             */
                            books = withContext(Dispatchers.IO) { client.getBooks() }
                        } catch (e: Exception) {
                            sb.append("Zerbitzaria deskonektatuta dago. Liburuak ez daude.\n")
                        }
                        if (books != null && books.isNotEmpty()) {
                            sb.append("\n=== Liburuak ===\n")
                            for (b in books) {
                                sb.append("- ${b.getIzenburua()} by ${b.getIdazlea()}\n")
                            }
                        }
                        binding.lblOpzioa.text =sb.toString()
                    }
                    true
                }

                R.id.menu_autoreak -> {
                    binding.lblOpzioa.text = "AUTOREAK"
                    Toast.makeText(this, "Autoreak aukeratu duzu ✍️", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.menu_switch -> {
                    Toast.makeText(this, "Aukera aldatu 🌀", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }
}