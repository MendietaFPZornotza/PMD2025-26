package com.example.erronka_proba.data.tcp

import android.util.Log

/**
 * Fitxategia: PurchaseTcpService.kt
 *
 * Zertarako da?
 * - Erosketa komandoa TCP bidez bidaltzeko.
 *
 * Zer egiten du?
 * - purchase(): "PURCHASE;<userId>;<eventId>;<email>;<seatsCsv>" bidaltzen du.
 *
 * Protokoloa (zure komentarioa mantenduta):
 * Request:
 *  PURCHASE;userId;eventId;email;A1,A2,A3
 *
 * Response:
 *  PURCHASE_OK;CODE
 *  PURCHASE_FAIL;REASON
 *
 * Oharra:
 * - Hemen ez da response-a parseatzen; hori PurchaseRepository-n egiten da.
 *
 * Log-ak:
 * - Bidalitako eskaeraren oinarrizko datuak (seats kopurua, eventId, userId).
 * - Email-a ez dugu oso-osoan log-ean erakusten (luzera bakarrik).
 */
class PurchaseTcpService(
    private val base: TCPClient = TCPClient()
) {

    companion object {
        private const val TAG = "PurchaseTcpService"
    }

    suspend fun purchase(
        userId: Long,
        eventId: Int,
        email: String,
        seats: List<String>
    ): Result<String> {
        val seatsCsv = seats.joinToString(",") { it.trim() }
        val line = "PURCHASE;$userId;$eventId;${email.trim()};$seatsCsv"

        Log.d(TAG, "purchase(): userId=$userId eventId=$eventId seats=${seats.size} emailLen=${email.trim().length}")
        Log.v(TAG, "purchase(): seatsCsv='$seatsCsv'")

        return base.sendLine(line)
    }
}