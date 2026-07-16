package com.example.erronka.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.erronka.R
import com.example.erronka.conexionBD.SocketClient
import com.example.erronka.model.Genre
import com.example.erronka.model.IdazleData
import com.example.erronka.model.Liburu
import com.example.erronka.model.Writer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SocketClientTest_Activity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var client: SocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_socketclienttest)

            // TextView aurkitu layout-etik
            textView = findViewById(R.id.textView)

            // SocketClient sortu Android context-ekin
            client = SocketClient(this)

            // Coroutine bat martxan jarri Main thread-ean, UI blokeatu gabe
            CoroutineScope(Dispatchers.Main).launch {
                val sb = StringBuilder() // Testu guztia gordetzeko StringBuilder

                // ----------------- IDAZLEAK -----------------
                var authors: MutableList<Writer>? = null
                try {
                    // IDAZLEAK zerbitzaritik lortu IO thread-ean
                    authors = withContext(Dispatchers.IO) { client.getAuthors() }
                } catch (e: Exception) {
                    // Zerbitzaria deskonektatuta badago, mezua gehitu
                    sb.append("Zerbitzaria deskonektatuta dago. Idazleak ez daude.\n")
                }
                // Idazleak badaude, TextView-ra gehitu
                if (authors != null && authors.isNotEmpty()) {
                    sb.append("=== Idazleak ===\n")
                    for (w in authors) {
                        sb.append("- ${w.getIzena()}\n")
                    }
                }

                // ----------------- GENEOAK -----------------
                var genres: MutableList<Genre>? = null
                try {
                    // Generoak zerbitzaritik lortu
                    genres = withContext(Dispatchers.IO) { client.getGenres() }
                } catch (e: Exception) {
                    sb.append("Zerbitzaria deskonektatuta dago. Generoak ez daude.\n")
                }
                if (genres != null && genres.isNotEmpty()) {
                    sb.append("\n=== Generoak ===\n")
                    for (g in genres) {
                        sb.append("- ${g.getGeneroa()}\n")
                    }
                }

                // ----------------- LIBURUAK -----------------
                var books: MutableList<Liburu>? = null
                try {
                    // Liburuak zerbitzaritik lortu
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

                // ----------------- IDAZLE DATUAK -----------------
                var authorData: MutableList<IdazleData>? = null
                try {
                    // Idazle bakoitzaren datuak lortu
                    authorData = withContext(Dispatchers.IO) { client.getAuthorData() }
                } catch (e: Exception) {
                    sb.append("Zerbitzaria deskonektatuta dago. Idazle datuak ez daude.\n")
                }
                if (authorData != null && authorData.isNotEmpty()) {
                    sb.append("\n=== Idazle Datuak ===\n")
                    for (d in authorData) {
                        sb.append("- ${d.getIzena()} liburu kopurua: ${d.getLiburuak().size}\n")
                    }
                }

                // ----------------- TEST TXERKETAK -----------------
                var newWriterId = -1
                try {
                    // Idazle berria txertatu
                    newWriterId =
                        withContext(Dispatchers.IO) { client.insertWriter("Mario Vargas Llosa") }
                } catch (e: Exception) {
                    sb.append("Zerbitzaria deskonektatuta dago. Ez da txertatu Mario Vargas Llosa.\n")
                }
                if (newWriterId != -1) {
                    sb.append("\nTxertatu berria: Mario Vargas Llosa, ID=$newWriterId\n")
                    try {
                        // Liburu berria txertatu idazlearekin
                        withContext(Dispatchers.IO) {
                            client.insertBook(
                                "La ciudad y los perros",
                                newWriterId,
                                listOf("Ficción", "Novela")
                            )
                        }
                        sb.append("Txertatu berria: La ciudad y los perros\n")
                    } catch (_: Exception) {}
                }

                var newGenreId = -1
                try {
                    // Genero berria txertatu
                    newGenreId =
                        withContext(Dispatchers.IO) { client.insertGenre("Realismo mágico") }
                } catch (e: Exception) {
                    sb.append("Zerbitzaria deskonektatuta dago. Ez da txertatu Realismo mágico.\n")
                }
                if (newGenreId != -1) sb.append("Txertatu berria: Realismo mágico, ID=$newGenreId\n")

                // ----------------- UI eguneratu -----------------
                // StringBuilder-eko guztia TextView-ra idatzi
                textView.text = sb.toString()
            }
        }
    }