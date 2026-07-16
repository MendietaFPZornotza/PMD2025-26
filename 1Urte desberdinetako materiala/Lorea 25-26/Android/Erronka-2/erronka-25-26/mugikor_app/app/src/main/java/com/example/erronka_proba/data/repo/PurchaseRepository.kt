package com.example.erronka_proba.data.repo

import android.util.Log
import com.example.erronka_proba.data.tcp.PurchaseTcpService

/**
 * Fitxategia: PurchaseRepository.kt
 *
 * Zertarako da?
 * - Erosketa (purchase) egiteko datu-geruza: TCP zerbitzura deitu eta erantzuna interpretatu.
 *
 * Zer egiten du?
 * - service.purchase(...) deitzen du.
 * - Zerbitzariaren erantzuna parseatzen du:
 *   - PURCHASE_OK;CODE -> Result.success(CODE)
 *   - PURCHASE_FAIL;MSG -> Result.failure(Exception(MSG))
 *   - Bestela -> Result.failure(Exception("Bad response: ..."))
 *
 * Nola erabiltzen da?
 * - Controller-ek buy(...) deitzen du eta Result<String> jasotzen du:
 *   - success: downloadCode
 *   - failure: errore mezua
 *
 * Log-ak:
 * - Erosketa deia, seats kopurua, eta erantzun kodeak jarraitzeko.
 * - Kontuz: email-a log-ean ez dugu oso-osoan erakutsiko; luzera bakarrik.
 */
class PurchaseRepository(
    private val service: PurchaseTcpService = PurchaseTcpService()
) {

    companion object {
        private const val TAG = "PurchaseRepository"
    }

    /**
     * Erosketa egin.
     */
    suspend fun buy(
        userId: Long,
        eventId: Int,
        email: String,
        seats: List<String>
    ): Result<String> {
        Log.i(TAG, "buy(): userId=$userId eventId=$eventId seats=${seats.size} emailLen=${email.trim().length}")

        return service.purchase(userId, eventId, email, seats).mapCatching { line ->
            Log.d(TAG, "buy() response='$line'")

            val p = line.split(";")
            val code = p.getOrNull(0).orEmpty()

            when (code) {
                "PURCHASE_OK" -> {
                    val downloadCode = p.getOrNull(1)?.trim().takeIf { !it.isNullOrBlank() }
                        ?: error("NO_CODE")
                    Log.i(TAG, "PURCHASE_OK: codeLen=${downloadCode.length}")
                    downloadCode
                }

                "PURCHASE_FAIL" -> {
                    val msg = p.getOrNull(1) ?: "PURCHASE_FAIL"
                    Log.w(TAG, "PURCHASE_FAIL: $msg")
                    error(msg)
                }

                else -> {
                    Log.w(TAG, "Bad response code='$code'")
                    error("Bad response: $line")
                }
            }
        }
    }
}