package com.example.erronka_proba.controller.purchase

import android.util.Log
import com.example.erronka_proba.model.domain.Event

/**
 * Fitxategia: TicketQrController.kt
 *
 * Zertarako da?
 * - QR / payload testua sortzeko logika zentralizatzeko.
 *
 * Zer egiten du?
 * - buildPayload(): QR kodean sartu beharreko string trinkoa sortzen du.
 * - buildDetails(): pantailan erakusteko testu “ederragoa” sortzen du.
 *
 * Nola erabiltzen da?
 * - TicketQrActivity-k controller hau erabil dezake payload-a eta detail testua sortzeko.
 *
 * Log-ak:
 * - Sortutako string-en luzera eta oinarrizko datuak jarraitzeko (kontsolan ez dugu datu gehiegi iragazi nahi).
 */
class TicketQrController {

    companion object {
        private const val TAG = "TicketQrController"
    }

    /**
     * QR-rako payload trinkoa sortzen du.
     * Formatua (portaera originala):
     * EVENT|title|date|time|room|seat1,seat2|total
     */
    fun buildPayload(event: Event, seats: List<String>, total: Double): String {
        val payload =
            "EVENT|${event.title}|${event.date}|${event.time}|${event.room}|${seats.joinToString(",")}|$total"

        Log.d(TAG, "buildPayload(): event='${event.title}', seats=${seats.size}, len=${payload.length}")
        return payload
    }

    /**
     * Pantailarako testu xehea sortzen du (line break-ekin).
     */
    fun buildDetails(event: Event, seats: List<String>, total: Double): String {
        val details =
            "Ekitaldia: ${event.title}\n" +
                    "Data: ${event.date}\n" +
                    "Ordua: ${event.time}\n" +
                    "Lekua: ${event.room}\n" +
                    "Eserlekua: ${seats.joinToString(", ")}\n\n" +
                    "Ordainduta: ${total}€"

        Log.d(TAG, "buildDetails(): event='${event.title}', seats=${seats.size}, len=${details.length}")
        return details
    }
}