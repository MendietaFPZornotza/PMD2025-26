package com.example.erronka_proba.data.tcp

import android.util.Log
import com.example.erronka_proba.model.domain.TicketGroup

/**
 * Fitxategia: TicketsTcpService.kt
 *
 * Zertarako da?
 * - Tiketen informazioa TCP bidez eskuratzeko:
 *   - Nire sarrerak (taldeka)
 *   - Deskarga kodearen arabera PDF ruta lortu
 *
 * Zer egiten du?
 * - myTickets(userId):
 *   - "MY_TICKETS;<userId>" bidali
 *   - Response parse:
 *     - MY_TICKETS_EMPTY -> emptyList()
 *     - MY_TICKETS_OK;num;row;row;...
 * - getTicketsByCode(code):
 *   - "GET_TICKETS_BY_CODE;<code>" bidali
 *   - Response parse:
 *     - TICKETS_CODE_EMPTY -> error
 *     - TICKETS_CODE_OK;...;firstRow -> last field (pdf ruta)
 *
 * Log-ak:
 * - userId, items kopurua, code luzera eta erroreak jarraitzeko.
 */
class TicketsTcpService(
    private val base: TCPClient = TCPClient()
) {

    companion object {
        private const val TAG = "TicketsTcpService"
    }

    /**
     * Erabiltzailearen ticket taldeak lortu.
     */
    suspend fun myTickets(userId: Long): Result<List<TicketGroup>> {
        Log.d(TAG, "myTickets(): userId=$userId")

        return base.sendLine("MY_TICKETS;$userId").map { line ->
            Log.d(TAG, "myTickets() response='$line'")

            val p = line.split(";")
            when (p[0]) {
                "MY_TICKETS_EMPTY" -> {
                    Log.i(TAG, "MY_TICKETS_EMPTY")
                    emptyList()
                }

                "MY_TICKETS_OK" -> {
                    // Formatoa: MY_TICKETS_OK;num;code|eventId|title|date|time|room|count|buyDate;...
                    val items = p.drop(2) // p[1] -> num (ez dugu erabiltzen; portaera originala)
                    Log.i(TAG, "MY_TICKETS_OK: rawItems=${items.size}")

                    val parsed = items
                        .filter { it.contains("|") }
                        .map { row ->
                            val f = row.split("|")

                            // Portaera originala mantentzen da (indexak zuzenean)
                            TicketGroup(
                                downloadCode = f[0],
                                eventId = f[1].toInt(),
                                title = f[2],
                                date = f[3],
                                time = f[4],
                                room = f[5],
                                count = f[6].toInt(),
                                boughtAt = f.getOrNull(7).orEmpty()
                            )
                        }

                    Log.i(TAG, "MY_TICKETS_OK: parsedGroups=${parsed.size}")
                    parsed
                }

                else -> {
                    Log.w(TAG, "Erantzun ezezaguna: '$line'")
                    throw RuntimeException("Erantzun ezezaguna: $line")
                }
            }
        }
    }

    /**
     * Deskarga kodearen arabera PDF ruta lortu.
     */
    suspend fun getTicketsByCode(code: String): Result<String> {
        val c = code.trim()
        Log.d(TAG, "getTicketsByCode(): codeLen=${c.length}")

        return base.sendLine("GET_TICKETS_BY_CODE;$c").map { line ->
            Log.d(TAG, "getTicketsByCode() response='$line'")

            val p = line.split(";")
            when (p[0]) {
                "TICKETS_CODE_EMPTY" -> {
                    Log.w(TAG, "TICKETS_CODE_EMPTY")
                    throw RuntimeException("Ez dago sarrerarik")
                }

                "TICKETS_CODE_OK" -> {
                    val first = p.getOrNull(2) ?: throw RuntimeException("Format okerra")
                    val fields = first.split("|")
                    val path = fields.last() // ruta PDF (/tickets/XXXX.pdf)

                    Log.i(TAG, "TICKETS_CODE_OK: pathLen=${path.length}")
                    path
                }

                else -> {
                    Log.w(TAG, "Erantzun ezezaguna: '$line'")
                    throw RuntimeException("Erantzun ezezaguna: $line")
                }
            }
        }
    }
}