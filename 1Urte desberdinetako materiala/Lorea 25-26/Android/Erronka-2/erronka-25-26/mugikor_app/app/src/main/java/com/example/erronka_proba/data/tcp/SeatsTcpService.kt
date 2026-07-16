package com.example.erronka_proba.data.tcp

import android.util.Log

/**
 * Fitxategia: SeatsTcpService.kt
 *
 * Zertarako da?
 * - Ekitaldi baten okupatutako eserlekuak TCP bidez eskuratzeko.
 *
 * Zer egiten du?
 * - getTakenSeats(eventId): "GET_TAKEN_SEATS;<eventId>" bidaltzen du.
 * - Response-a parseatzen du eta eserleku kodeen zerrenda itzultzen du.
 *
 * Protokoloa (inplizitua kodean):
 * - TAKEN_OK;<csv>  (csv hutsa izan daiteke)
 * - Bestela -> RuntimeException("Erantzun ezezaguna: ...")
 *
 * Log-ak:
 * - eventId, response line, eta seats kopurua jarraitzeko.
 */
class SeatsTcpService(
    private val base: TCPClient = TCPClient()
) {

    companion object {
        private const val TAG = "SeatsTcpService"
    }

    suspend fun getTakenSeats(eventId: Int): Result<List<String>> {
        Log.d(TAG, "getTakenSeats(): eventId=$eventId")

        return base.sendLine("GET_TAKEN_SEATS;$eventId").map { line ->
            Log.d(TAG, "getTakenSeats() response='$line'")

            val p = line.split(";")
            when (p[0]) {
                "TAKEN_OK" -> {
                    val csv = p.getOrNull(1).orEmpty().trim()

                    val seats = if (csv.isBlank()) {
                        emptyList()
                    } else {
                        csv.split(",")
                            .map { it.trim() }
                            .filter { it.isNotBlank() }
                    }

                    Log.i(TAG, "TAKEN_OK: seats=${seats.size}")
                    seats
                }

                else -> {
                    Log.w(TAG, "Erantzun ezezaguna: '$line'")
                    throw RuntimeException("Erantzun ezezaguna: $line")
                }
            }
        }
    }
}