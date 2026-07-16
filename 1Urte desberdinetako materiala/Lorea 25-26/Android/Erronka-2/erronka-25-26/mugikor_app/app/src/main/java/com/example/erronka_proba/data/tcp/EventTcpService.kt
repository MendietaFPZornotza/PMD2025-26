package com.example.erronka_proba.data.tcp

import android.util.Log
import com.example.erronka_proba.model.domain.EventCategory

/**
 * Fitxategia: EventTcpService.kt
 *
 * Zertarako da?
 * - Ekitaldien zerrenda TCP bidez eskuratzeko (kategoriaz).
 *
 * Zer egiten du?
 * - list(category): "GET_EVENTS_B;<CATEGORY>" bidaltzen du.
 * - Zerbitzariaren erantzuna parseatzen du eta EventNet zerrenda itzultzen du.
 *
 * Protokoloa (zure komentarioa mantenduta):
 * Request:
 *  GET_EVENTS_B;MOVIES|THEATRE|CONCERTS
 *
 * Response:
 *  EVENTS_OK;<count>;<id|title|genre|date|time|room|price|synopsis|aforo>;<id|...>
 *  EVENTS_EMPTY
 *  EVENTS_FAIL;<reason>  (hemen ez dago bereizita parse-an; bestela "Bad response")
 *  MEZU_EZ_EZAGUNA       (handler gabe)
 *
 * Log-ak:
 * - Bidalitako komandoa, jasotako line-a eta parse prozesuko elementu kopuruak.
 */
class EventTcpService(
    private val base: TCPClient = TCPClient()
) {

    companion object {
        private const val TAG = "EventTcpService"
    }

    /**
     * Kategoria bateko event-ak eskatzen ditu.
     */
    suspend fun list(category: EventCategory): Result<List<EventNet>> {
        Log.d(TAG, "list(): category=$category -> sendLine(GET_EVENTS_B)")
        return base.sendLine("GET_EVENTS_B;$category").mapCatching { line ->
            Log.d(TAG, "list() response='$line'")
            parseEvents(line)
        }
    }

    /**
     * Zerbitzariaren erantzuna parseatzen du.
     *
     * Portaera originala:
     * - "EVENTS_OK" -> items subList(2..) hartu eta chunk bakoitza '|' bidez zatitu.
     * - "EVENTS_EMPTY" -> emptyList()
     * - bestela -> error("Bad response: $line")
     */
    private fun parseEvents(line: String): List<EventNet> {
        val parts = line.split(";")
        if (parts.isEmpty()) error("Empty response")

        val code = parts[0]
        Log.v(TAG, "parseEvents(): code='$code' parts=${parts.size}")

        return when (code) {
            "EVENTS_OK" -> {
                val items = if (parts.size > 2) parts.subList(2, parts.size) else emptyList()
                Log.i(TAG, "EVENTS_OK: items=${items.size}")

                items.mapNotNull { chunk ->
                    if (chunk.isBlank()) return@mapNotNull null

                    val f = chunk.split("|")

                    // Gutxieneko egitura:
                    // 0:id 1:title 2:genre 3:date 4:time 5:room 6:price 7:...sinopsia... 8:aforo 9:argazkia
                    if (f.size < 10) {
                        Log.w(TAG, "Chunk ez-osoa (skip): fields=${f.size} chunk='${chunk.take(120)}'")
                        return@mapNotNull null
                    }

                    val id = f[0].toIntOrNull() ?: 0
                    val title = f[1]
                    val genre = f[2]
                    val date = f[3]
                    val time = f[4]
                    val room = f[5]
                    val price = f[6].toDoubleOrNull() ?: 0.0

                    // 🔥 Azken biak finkoak:
                    val aforo = f[f.size - 2].toIntOrNull() ?: 0
                    val imagePath = f[f.size - 1].takeIf { it.isNotBlank() }

                    // 🔥 Sinopsia: 7tik (barne) azken-2ra (kanpo) arte elkartu
                    val synopsis = if (f.size > 9) {
                        f.subList(7, f.size - 2).joinToString("|")
                    } else {
                        f[7]
                    }

                    val event = EventNet(
                        id = id,
                        title = title,
                        genre = genre,
                        date = date,
                        time = time,
                        room = room,
                        price = price,
                        synopsis = synopsis,
                        aforo = aforo,
                        imagePath = imagePath
                    )

                    Log.v(TAG, "Parsed EventNet: id=${event.id} titleLen=${event.title.length} img=${event.imagePath != null}")
                    event
                }
            }

            "EVENTS_EMPTY" -> {
                Log.i(TAG, "EVENTS_EMPTY: 0 item")
                emptyList()
            }

            else -> {
                Log.w(TAG, "Bad response: '${line.take(200)}'")
                error("Bad response: ${line.take(200)}")
            }
        }
    }
}

/**
 * Fitxategia: EventNet (data class)
 *
 * Zertarako da?
 * - TCP geruzatik datorren event informazioa irudikatzeko (network DTO).
 *
 * Zer egiten du?
 * - EventTcpService.parseEvents() funtzioak osatzen du.
 * - Gero mapper-ek (toListItem/toHomeCard...) beste UI/Domain eredura bihurtzen dute.
 */
data class EventNet(
    val id: Int,
    val title: String,
    val genre: String,
    val date: String,
    val time: String,
    val room: String,
    val price: Double,
    val synopsis: String,
    val aforo: Int,
    val imagePath: String? // 🔥 "Dokumentuak/Argazkiak/aaaa.jpg"
)