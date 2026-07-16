package com.example.erronka_proba.model.ui

import android.util.Log
import com.example.erronka_proba.model.domain.Event

/**
 * Fitxategia: HomeItem.kt
 *
 * Zertarako da?
 * - Home pantailan RecyclerView-ak erakusten dituen item motak definitzeko (UI eredua).
 *
 * Zer egiten du?
 * - Sealed class:
 *   - Section(title): atalaren burua
 *   - EventCard(...): ekitaldi txartelaren datuak
 *
 * Nola erabiltzen da?
 * - HomeAdapter-ek HomeItem zerrenda jasotzen du eta viewType-a aukeratzen du.
 * - EventCard.toEvent(): UI eredutik domain Event-era bihurtzeko laguntzailea.
 */
sealed class HomeItem {

    data class Section(val title: String) : HomeItem()

    data class EventCard(
        val id: Int,
        val iconRes: Int,
        val title: String,
        val dateTime: String,
        val price: String,
        val genre: String,
        val room: String,
        val synopsis: String,
        val imageRes: Int,
        val aforo: Int,
        val imagePath: String? = null // 🔥 berria (FTPS-rako)
    ) : HomeItem()

    companion object {
        private const val TAG = "HomeItem"
    }

    /**
     * EventCard -> Event bihurketa.
     *
     * Portaera originala:
     * - dateTime: "YYYY-MM-DD · HH:MM" modukoa, "·" bidez zatituta.
     * - price: "8.5€" -> Double
     *
     * Ohar berria:
     * - imageRes placeholder moduan mantentzen da
     * - imagePath: serverreko argazkiaren bidea (FTPS bidez deskargatzeko)
     */
    fun EventCard.toEvent(): Event {
        Log.v(TAG, "toEvent(): id=$id titleLen=${title.length}")

        val parts = dateTime.split("·").map { it.trim() }
        val date = parts.getOrNull(0) ?: ""
        val time = parts.getOrNull(1) ?: ""

        val priceValue = price.replace("€", "").trim().toDoubleOrNull() ?: 0.0

        Log.v(TAG, "toEvent(): date='$date' time='$time' priceValue=$priceValue")

        return Event(
            id = id,
            title = title,
            imageRes = imageRes,
            synopsis = synopsis,
            genre = genre,
            room = room,
            date = date,
            time = time,
            pricePerTicket = priceValue,
            aforo = aforo,
            imagePath = imagePath
        )
    }
}