package com.example.erronka_proba.data.repo

import android.util.Log
import com.example.erronka_proba.R
import com.example.erronka_proba.data.tcp.EventNet
import com.example.erronka_proba.data.tcp.EventTcpService
import com.example.erronka_proba.model.domain.EventCategory
import com.example.erronka_proba.model.ui.HomeItem

/**
 * Fitxategia: EventRepository.kt
 *
 * Zertarako da?
 * - Ekitaldien zerrendak lortzeko datu-geruza (Home-eko section-ak, eta abar).
 *
 * Zer egiten du?
 * - TCP bidez kategoria bateko event zerrenda eskatzen du (EventTcpService).
 * - Jasotako EventNet objektuak HomeItem.EventCard bihurtzen ditu (UI-rako).
 *
 * Nola erabiltzen da?
 * - Controller-ek deitzen du:
 *   - getHomeSection(category)
 *
 * Log-ak:
 * - Kategoria, jasotako elementu kopurua eta map prozesua jarraitzeko.
 */
class EventRepository(
    private val tcp: EventTcpService = EventTcpService()
) {

    companion object {
        private const val TAG = "EventRepository"
    }

    /**
     * Home pantailarako section bat eskuratzen du.
     *
     * Itzulera:
     * - Result<List<HomeItem.EventCard>>
     *
     * Portaera originala:
     * - tcp.list(category) -> Result<List<EventNet>>
     * - map { EventNet -> HomeItem.EventCard }
     */
    suspend fun getHomeSection(category: EventCategory): Result<List<HomeItem.EventCard>> {
        Log.d(TAG, "getHomeSection(): category=$category")

        return tcp.list(category).map { list ->
            Log.i(TAG, "getHomeSection() success: category=$category size=${list.size}")
            list.map { it.toHomeCard(category) }
        }
    }

    /**
     * TCP-tik datorren EventNet -> HomeItem.EventCard bihurketa.
     *
     * Portaera originala:
     * - iconRes kategoriaz aukeratzen da
     * - dateTime: "$date · $time"
     * - price: "${price}€"
     * - imageRes: icon bera (originalean bezala)
     */
    private fun EventNet.toHomeCard(category: EventCategory): HomeItem.EventCard {
        val icon = when (category) {
            EventCategory.MOVIES -> R.drawable.ic_movie
            EventCategory.THEATRE -> R.drawable.ic_theatre
            EventCategory.CONCERTS -> R.drawable.ic_mic
        }

        Log.v(TAG, "toHomeCard(): id=$id titleLen=${title.length} category=$category")

        return HomeItem.EventCard(
            id = id,
            iconRes = icon,
            title = title,
            dateTime = "$date · $time",
            price = "${price}€",
            genre = genre,
            room = room,
            synopsis = synopsis,
            imageRes = icon,
            aforo = aforo,
            imagePath = imagePath
        )
    }
}