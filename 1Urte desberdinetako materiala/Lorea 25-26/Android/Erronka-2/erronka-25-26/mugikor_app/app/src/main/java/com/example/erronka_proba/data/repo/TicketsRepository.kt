package com.example.erronka_proba.data.repo

import android.util.Log
import com.example.erronka_proba.data.tcp.TicketsTcpService
import com.example.erronka_proba.model.domain.TicketGroup

/**
 * Fitxategia: TicketsRepository.kt
 *
 * Zertarako da?
 * - "Nire sarrerak" datuak lortzeko eta tiketen PDF path-a/URL-a eskuratzeko.
 *
 * Zer egiten du?
 * - myTickets(userId): erabiltzailearen ticket taldeak eskatzen ditu.
 * - downloadPdfPath(code): deskarga kodearen arabera PDF-aren path-a eskatzen du.
 *
 * Nola erabiltzen da?
 * - Profile/MyTickets controller-ek repo honi deitzen dio.
 *
 * Log-ak:
 * - userId eta code luzera, eta request/response flow-a jarraitzeko.
 */
class TicketsRepository(
    private val service: TicketsTcpService
) {

    companion object {
        private const val TAG = "TicketsRepository"
    }

    /**
     * Erabiltzailearen sarrerak (taldeka) eskuratzea.
     */
    suspend fun myTickets(userId: Long): Result<List<TicketGroup>> {
        Log.d(TAG, "myTickets(): userId=$userId")
        return service.myTickets(userId).also { res ->
            res.onSuccess { list -> Log.i(TAG, "myTickets() success: groups=${list.size}") }
            res.onFailure { e -> Log.e(TAG, "myTickets() failure: ${e.message}", e) }
        }
    }

    /**
     * Deskarga kodearekin PDF path-a/identifikatzailea lortzea.
     */
    suspend fun downloadPdfPath(code: String): Result<String> {
        Log.d(TAG, "downloadPdfPath(): codeLen=${code.trim().length}")
        return service.getTicketsByCode(code).also { res ->
            res.onSuccess { path -> Log.i(TAG, "downloadPdfPath() success: pathLen=${path.length}") }
            res.onFailure { e -> Log.e(TAG, "downloadPdfPath() failure: ${e.message}", e) }
        }
    }
}