package com.example.erronka.conexionBD
import android.content.Context
import android.os.Looper
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.erronka.model.Genre
import com.example.erronka.model.IdazleData
import com.example.erronka.model.Liburu
import com.example.erronka.model.Writer
import java.io.BufferedReader
import java.io.InputStreamReader

import java.io.PrintWriter
import java.net.Socket//Zerbitzari batekin sareko konexioa egiteko erabiltzen da.
import android.os.Handler
/*Android-en ezin dugu UI thread blokeatu:
Sareko eskaera → ez duzu Thread.sleep() erabili nahi → app-a geldituko litzateke
Fitxategiak irakurtzea edo datu-basea → denbora luzea hartu dezake
Coroutine-ek aukera ematen du:
Lan luzea background thread batean
Kodea lineala dirudien moduan idatzi
UI thread ez blokeatu*/
import kotlinx.coroutines.*//sareko lana background thread batean egitea.
import java.net.InetSocketAddress //Zerbitzariaren IP helbidea + portua definitzeko.

/**
 * Dispatchers.IO: fitxategiak, sarea, datu-basea
 * Dispatchers.Main: UI (Toast, TextView…)
 */
/**
 * SocketClient: Zerbitzariaren komunikazioa kudeatzen du.
 * Coroutines erabiliko ditugu UI thread-a ez blokeatzeko.
 * JSON erabiltzen du datuak trukatzeko Gson-ekin.
 */
class SocketClient(private val context: Context) {

    // Zerbitzariaren host-a eta portua
    private val SERVER_HOST = "10.0.2.2" // localhost// Android emulator -> host makina
    private val SERVER_PORT = 8080
    private val gson = Gson() // JSON bihurketa egiteko Gson objektua
    //Zerbitzariak  JSON formatuan bidaltzen ditu datuak.
    /**
     * Zerbitzarira eskaera bidali eta erantzuna lortu.
     * Coroutine-ean exekutatu behar da (Dispatchers.IO)
     */
    /*suspend funtzioak background thread batean exekutatzen dira
    coroutine batekin, baina kodea lineala dirudien moduan idazten da.
    Denbora luzezko lana egiteko (sarea)
    UI thread-a ez blokeatzeko*/
    private suspend fun sendRequest(request: String): String? {
        return try {
            // Socketa konexio timeout batekin ireki
            val socket = Socket()
            try {
                // Zerbitzarira konektatzen da, 2 segundoko timeout-arekin
                socket.connect(InetSocketAddress(SERVER_HOST, SERVER_PORT), 2000)
                //Eskaera testu moduan bidaltzen da
                val out = PrintWriter(socket.getOutputStream(), true)
                out.println(request)
                out.flush()
                //withContext(Dispatchers.IO) → sareko lana background thread-eanZerbitzariak bidalitako erantzuna irakurtzen du
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                reader.readLine()
            } finally {
                socket.close()
            }
        } catch (e: Exception) {
            // Zerbitzaria deskonektatuta dagoenean, Toast Main thread-ean erakutsi
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Zerbitzaria deskonektatuta dago", Toast.LENGTH_LONG).show()
            }
            null
        }
    }
    // ----------------- AUTHORS -----------------
    //withContext(Dispatchers.IO): sareko lana background thread-ean
    suspend fun getAuthors(): MutableList<Writer> = withContext(Dispatchers.IO) {
        val authors = mutableListOf<Writer>()
        val response = sendRequest("GET_AUTHORS")
        if (!response.isNullOrEmpty()) {
            //val type = object TypeToken<> -- Anonymous object bat sortzen da informazio generikoa gordetzeko
            //.type -- TypeToken-ek barruan gordetzen duen Type objektua ateratzen dugu
            // Kasu hontan Writer-en lista bat
            val type = object : TypeToken<List<Writer>>() {}.type
            //Gson-ek:
            //JSON irakurtzen du
            //Objektu berriak sortzen ditu (Writer)
            //Zerrenda bat bueltatzen du
            authors.addAll(gson.fromJson(response, type))
        }
        authors
    }
    // ----------------- GENRES -----------------
    suspend fun getGenres(): MutableList<Genre> = withContext(Dispatchers.IO) {
        val genres = mutableListOf<Genre>()
        val response = sendRequest("GET_GENRES")
        if (!response.isNullOrEmpty()) {
            val type = object : TypeToken<List<Genre>>() {}.type
            genres.addAll(gson.fromJson(response, type))
        }
        genres
    }
    // ----------------- BOOKS -----------------
    suspend fun getBooks(): MutableList<Liburu> = withContext(Dispatchers.IO) {
        val books = mutableListOf<Liburu>()
        val response = sendRequest("GET_BOOKS")
        if (!response.isNullOrEmpty()) {
            val type = object : TypeToken<List<Liburu>>() {}.type
            books.addAll(gson.fromJson(response, type))
        }
        books
    }

    // ----------------- AUTHOR DATA -----------------
    suspend fun getAuthorData(): MutableList<IdazleData> = withContext(Dispatchers.IO) {
        val data = mutableListOf<IdazleData>()
        val response = sendRequest("GET_AUTHOR_DATA")
        if (!response.isNullOrEmpty()) {
            val type = object : TypeToken<List<IdazleData>>() {}.type
            data.addAll(gson.fromJson(response, type))
        }
        data
    }

    // ----------------- INSERT WRITER -----------------
    /**
     * insertWriter()
     *  1.-IO thread-ean exekutatu
     *  2.-Zerbitzarira mezua bidali
     *  3.-Erantzuna aztertu
     *      3.1.-SUCCESS → ID bueltatu
     *      3.2.-ERROR → Toast + -1
     */
    //newWriter: String -- Gehitu nahi dugun idazlearen izena
    //  Int = withContext(Dispatchers.IO)
    // Coroutine baten barruan thread-a aldatzeko erabiltzen da eta
    // bertan sortutako emaitza hemen bultatzeko
    //  Int bueltatzen du -- idazlearen ID-a baina

    suspend fun insertWriter(newWriter: String): Int = withContext(Dispatchers.IO) {
        /**
         * sendRequest(...)
         * Socket-a irekitzen du
         * Mezua zerbitzarira bidaltzen du
         * Erantzuna jasotzen du (String moduan)
         */
        val response = sendRequest("INSERT_WRITER:$newWriter")
        /**
         * erantzuna:
         * "SUCCESS:5": ondo (ID = 5)
         * null: errorea edo konexiorik ez
         */
        if (response != null && response.startsWith("SUCCESS:")) {
            /**
             * "SUCCESS:5" bada erantzuna
             * 5 itzuliko da hau da metodoak ID zenbakia bueltatzen du
             */
            response.split(":")[1].toInt()
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Idazlea txertatzea huts egin du", Toast.LENGTH_LONG).show()
            }
            -1
        }
    }

    // ----------------- INSERT BOOK -----------------
    suspend fun insertBook(izenburua: String, authorId: Int, generoak: List<String> = emptyList()) = withContext(Dispatchers.IO) {
        val genresString = generoak.joinToString(",")
        val response = sendRequest("INSERT_BOOK:$izenburua:$authorId:$genresString")
        withContext(Dispatchers.Main) {
            if (response == "SUCCESS") {
                Toast.makeText(context, "Liburua txertatu da generoekin: $genresString", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Liburua txertatzea huts egin du", Toast.LENGTH_LONG).show()
            }
        }
    }

    // ----------------- INSERT GENRE -----------------
    suspend fun insertGenre(newGenre: String): Int = withContext(Dispatchers.IO) {
        val response = sendRequest("INSERT_GENRE:$newGenre")
        if (response != null && response.startsWith("SUCCESS:")) {
            response.split(":")[1].toInt()
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Genero txertatzea huts egin du", Toast.LENGTH_LONG).show()
            }
            -1
        }
    }

    // ----------------- INSERT BOOK-GENRE -----------------
    suspend fun insertBookGen(newBookGen: String) = withContext(Dispatchers.IO) {
        val response = sendRequest("INSERT_BOOK_GENRE:$newBookGen")
        withContext(Dispatchers.Main) {
            if (response == "SUCCESS") {
                Toast.makeText(context, "Liburu-genero erlazioa txertatu da", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Liburu-genero erlazioa txertatzea huts egin du", Toast.LENGTH_LONG).show()
            }
        }
    }
    /**
     * Android Toast erakutsi
     */
    fun showToast(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Konexioa ixteko metodoa (socketa berez ixtea gertatzen da)
     */
    fun closeConnection() {
        showToast("Socket konexioa itxi da.")
    }
}
