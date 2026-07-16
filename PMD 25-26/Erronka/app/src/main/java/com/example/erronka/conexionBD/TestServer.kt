package com.example.erronka.conexionBD

import com.example.erronka.model.Genre
import com.example.erronka.model.IdazleData
import com.example.erronka.model.Liburu
import com.example.erronka.model.Writer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.Arrays
import java.util.concurrent.atomic.AtomicInteger
import com.google.gson.Gson
/**
 * Test server sinple bat soketekin komunikatzeko.
 * Server honek datu-base baten moduko portaera simulatzen du,
 * eskariak kudeatzen ditu eta JSON erantzunak bidaltzen ditu.
 */
object TestServer {

    private const val PORT = 8080 // Sarrera portua
    private val gson = Gson() // JSON bihurketa

    // Datu-base simulatuaren zerrendak
    private val writers = mutableListOf(
        Writer(1, "Gabriel García Márquez"),
        Writer(2, "Jorge Luis Borges"),
        Writer(3, "Pablo Neruda")
    )

    private val genres = mutableListOf(
        Genre(1, "Ficción"),
        Genre(2, "Poesía"),
        Genre(3, "Ensayo"),
        Genre(4, "Novela"),
        Genre(5, "Cuento")
    )

    private val books = mutableListOf(
        Liburu(1, "Cien años de soledad", "Gabriel García Márquez", mutableListOf("Ficción", "Novela")),
        Liburu(2, "El amor en los tiempos del cólera", "Gabriel García Márquez", mutableListOf("Ficción", "Novela")),
        Liburu(3, "Ficciones", "Jorge Luis Borges", mutableListOf("Ficción", "Cuento")),
        Liburu(4, "El Aleph", "Jorge Luis Borges", mutableListOf("Ficción", "Cuento")),
        Liburu(5, "Veinte poemas de amor", "Pablo Neruda", mutableListOf("Poesía")),
        Liburu(6, "Canto general", "Pablo Neruda", mutableListOf("Poesía"))
    )

    private val authorData = mutableListOf(
        IdazleData(1, "Gabriel García Márquez", mutableListOf(books[0], books[1])),
        IdazleData(2, "Jorge Luis Borges", mutableListOf(books[2], books[3])),
        IdazleData(3, "Pablo Neruda", mutableListOf(books[4], books[5]))
    )

    // ID kontagailuak idazle eta liburu berrientzat
    private val writerIdCounter = AtomicInteger(4)
    private val bookIdCounter = AtomicInteger(7)

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            // Server socket sortu eta portuan entzuteko
            val serverSocket = ServerSocket(PORT)
            println("Test server started on port $PORT")
            println("Waiting for client connections...")

            while (true) {
                // Bezero berria konektatzen denean
                val clientSocket = serverSocket.accept()
                println("Client connected: ${clientSocket.inetAddress}")

                // Bezeroa thread berri batean kudeatu
                Thread { handleClient(clientSocket) }.start()
            }

        } catch (e: Exception) {
            // Serverra hastean erroreak kudeatu
            println("Error starting server: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Bezero baten eskariak kudeatzen ditu
     */
    private fun handleClient(clientSocket: Socket) {
        clientSocket.use { socket ->
            try {
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                val writer = PrintWriter(socket.getOutputStream(), true)

                val request = reader.readLine() // Bezeroaren eskaera irakurri
                println("Received request: $request")

                val response = processRequest(request) // Eskaria prozesatu
                println("Sending response: $response")

                writer.println(response) // Erantzuna bidali

            } catch (e: Exception) {
                println("Error handling client: ${e.message}")
                e.printStackTrace()
            } finally {
                try {
                    socket.close() // Bezeroaren soketa itxi
                } catch (e: Exception) {
                    println("Error closing client socket: ${e.message}")
                }
            }
        }
    }

    /**
     * Eskaria prozesatzen du eta erantzun egokia sortzen du
     */
    private fun processRequest(request: String?): String {
        if (request.isNullOrEmpty()) return "ERROR: Empty request" // Hutsa den eskaera

        val parts = request.split(":") // Komandoa eta parametroak bereizi
        val command = parts[0]

        return when (command) {

            // IDAZLEAK lortu
            "GET_AUTHORS" -> gson.toJson(writers)

            // GENEROAK lortu
            "GET_GENRES" -> gson.toJson(genres)

            // LIBURUAK lortu
            "GET_BOOKS" -> gson.toJson(books)

            // IDAZLEAREN DATUAK bere liburuekin lortu
            "GET_AUTHOR_DATA" -> gson.toJson(authorData)

            // IDAZLE BERRIA INSERT
            "INSERT_WRITER" -> {
                if (parts.size > 1) {
                    val writerName = parts[1]
                    val newId = writerIdCounter.getAndIncrement()
                    val newWriter = Writer(newId, writerName)
                    writers.add(newWriter)

                    // Idazle berria authorData-n gehitu liburu zerrenda hutsarekin
                    val newAuthorData = IdazleData(newId, writerName, mutableListOf())
                    authorData.add(newAuthorData)

                    println("Writer inserted: $writerName with ID: $newId")
                    "SUCCESS:$newId"
                } else "ERROR: Missing writer name" // Izena falta da
            }

            // LIBURU BERRIA INSERT
            "INSERT_BOOK" -> {
                if (parts.size > 2) {
                    val bookTitle = parts[1]
                    val authorId = parts[2].toIntOrNull() ?: return "ERROR: Invalid author ID"

                    // Generoak jaso, komaz bereizi
                    val bookGenres = if (parts.size > 3 && parts[3].isNotEmpty()) {
                        parts[3].split(",").map { it.trim() }.filter { it.isNotEmpty() }.toMutableList()
                    } else {
                        mutableListOf()
                    }

                    // Idazlea aurkitu
                    val author = writers.find { it.getId() == authorId }

                    return if (author != null) {
                        val newBookId = bookIdCounter.getAndIncrement()
                        val newBook = Liburu(newBookId, bookTitle, author.getIzena(), bookGenres)
                        books.add(newBook)

                        // Liburua idazlearen datuetara gehitu
                        authorData.find { it.getId() == authorId }?.getLiburuak()?.add(newBook)

                        println("Book inserted: $bookTitle by ${author.getIzena()} (ID: $newBookId) with genres: $bookGenres")
                        "SUCCESS"
                    } else "ERROR: Author not found with ID: $authorId"
                } else "ERROR: Missing book information" // Liburu informazioa falta da
            }

            // LIBURU-GENERO LOTURA INSERT
            "INSERT_BOOK_GENRE" -> {
                if (parts.size > 1) {
                    val bookGenreInfo = parts[1]
                    println("Book-genre relationship inserted: $bookGenreInfo")
                    "SUCCESS"
                } else "ERROR: Missing book-genre information"
            }

            // GENERO BERRIA INSERT
            "INSERT_GENRE" -> {
                if (parts.size > 1) {
                    val genreName = parts[1]
                    val newGenreId = genres.size + 1 // ID sinple bat sortu
                    val newGenre = Genre(newGenreId, genreName)
                    genres.add(newGenre)

                    println("Genre inserted: $genreName with ID: $newGenreId")
                    "SUCCESS:$newGenreId"
                } else "ERROR: Missing genre name"
            }

            // Komando ezezaguna
            else -> "ERROR: Unknown command: $command"
        }
    }
}
/**
* Test server sinple bat soketekin komunikatzeko.
* Server honek datu-base baten moduko portaera simulatzen du,
* eskariak kudeatzen ditu eta JSON erantzunak bidaltzen ditu.**/
/**
object TestServer {

    private const val PORT = 8080
    private val gson = Gson()

    // ID kontagailuak
    private val writerIdCounter = AtomicInteger(4)
    private val bookIdCounter = AtomicInteger(7)

    // Datu simulatuak
    private val writers = mutableListOf(
        Writer(1, "Gabriel García Márquez"),
        Writer(2, "Jorge Luis Borges"),
        Writer(3, "Pablo Neruda")
    )

    private val genres = mutableListOf(
        Genre(1, "Ficción"),
        Genre(2, "Poesía"),
        Genre(3, "Ensayo"),
        Genre(4, "Novela"),
        Genre(5, "Cuento")
    )

    private val books = mutableListOf(
        Liburu(1, "Cien años de soledad", "Gabriel García Márquez", listOf("Ficción", "Novela")),
        Liburu(2, "El amor en los tiempos del cólera", "Gabriel García Márquez", listOf("Ficción", "Novela")),
        Liburu(3, "Ficciones", "Jorge Luis Borges", listOf("Ficción", "Cuento")),
        Liburu(4, "El Aleph", "Jorge Luis Borges", listOf("Ficción", "Cuento")),
        Liburu(5, "Veinte poemas de amor", "Pablo Neruda", listOf("Poesía")),
        Liburu(6, "Canto general", "Pablo Neruda", listOf("Poesía"))
    )

    private val authorData = mutableListOf(
        IdazleData(1, "Gabriel García Márquez", mutableListOf(books[0], books[1])),
        IdazleData(2, "Jorge Luis Borges", mutableListOf(books[2], books[3])),
        IdazleData(3, "Pablo Neruda", mutableListOf(books[4], books[5]))
    )

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val serverSocket = ServerSocket(PORT)
            println("Test server started on port $PORT")
            println("Waiting for client connections...")

            while (true) {
                val clientSocket = serverSocket.accept()
                println("Client connected: ${clientSocket.inetAddress}")

                // Bezero bakoitza thread batean kudeatu
                Thread { handleClient(clientSocket) }.start()
            }
        } catch (e: Exception) {
            println("Error starting server: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun handleClient(socket: Socket) = socket.use { s ->
        try {
            val reader = BufferedReader(InputStreamReader(s.getInputStream()))
            val writer = PrintWriter(s.getOutputStream(), true)

            val request = reader.readLine()
            println("Received request: $request")

            val response = processRequest(request)
            println("Sending response: $response")
            writer.println(response)

        } catch (e: Exception) {
            println("Error handling client: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun processRequest(request: String?): String {
        if (request.isNullOrEmpty()) return "ERROR: Empty request"

        val parts = request.split(":")
        val command = parts[0]

        return when (command) {

            // IDAZLEAK JSON formatuan
            "GET_AUTHORS" -> gson.toJson(writers)

            // GENEROAK JSON formatuan
            "GET_GENRES" -> gson.toJson(genres)

            // LIBURUAK JSON formatuan
            "GET_BOOKS" -> gson.toJson(books)

            // IDAZLEAREN DATUAK bere liburuekin
            "GET_AUTHOR_DATA" -> gson.toJson(authorData)

            // IDAZLE BERRIA INSERT
            "INSERT_WRITER" -> parts.getOrNull(1)?.let { name ->
                val id = writerIdCounter.getAndIncrement()
                val newWriter = Writer(id, name)
                writers.add(newWriter)

                // AuthorData gehitu liburu zerrenda hutsarekin
                authorData.add(IdazleData(id, name, mutableListOf()))

                println("Writer inserted: $name with ID: $id")
                "SUCCESS:$id"
            } ?: "ERROR: Missing writer name"

            // LIBURU BERRIA INSERT
            "INSERT_BOOK" -> {
                val title = parts.getOrNull(1)
                val authorId = parts.getOrNull(2)?.toIntOrNull()
                if (title == null || authorId == null) return "ERROR: Missing book info"

                val bookGenres = parts.getOrNull(3)?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
                    ?: emptyList()

                val author = writers.find { it.getId() == authorId }
                    ?: return "ERROR: Author not found with ID: $authorId"

                val newBookId = bookIdCounter.getAndIncrement()
                val newBook = Liburu(newBookId, title, author.getIzena(), bookGenres.toList())
                books.add(newBook)

                authorData.find { it.getId() == authorId }?.liburuak?.add(newBook)

                println("Book inserted: $title by ${author.getIzena()} (ID: $newBookId) with genres: $bookGenres")
                "SUCCESS"
            }

            // LIBURU-GENERO LOTURA INSERT (sinple simulazioa)
            "INSERT_BOOK_GENRE" -> parts.getOrNull(1)?.let { info ->
                println("Book-genre relationship inserted: $info")
                "SUCCESS"
            } ?: "ERROR: Missing book-genre information"

            // GENERO BERRIA INSERT
            "INSERT_GENRE" -> parts.getOrNull(1)?.let { name ->
                val id = genres.size + 1
                val genre = Genre(id, name)
                genres.add(genre)
                println("Genre inserted: $name with ID: $id")
                "SUCCESS:$id"
            } ?: "ERROR: Missing genre name"

            else -> "ERROR: Unknown command: $command" // Komando ezezaguna
        }
    }
}**/