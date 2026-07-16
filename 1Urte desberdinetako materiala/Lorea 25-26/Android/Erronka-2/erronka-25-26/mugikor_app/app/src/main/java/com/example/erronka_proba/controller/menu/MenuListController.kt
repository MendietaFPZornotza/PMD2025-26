package com.example.erronka_proba.controller.menu

import android.content.Intent
import android.util.Log
import com.example.erronka_proba.data.ConnectivityState
import com.example.erronka_proba.data.tcp.EventTcpService
import com.example.erronka_proba.model.domain.EventCategory
import com.example.erronka_proba.model.mapper.toListItem
import com.example.erronka_proba.model.ui.ListItem
import com.example.erronka_proba.view.purchase.EventDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Fitxategia: MenuListController.kt
 *
 * Zertarako da?
 * - "Pelikulak / Antzerkiak / Kontzertuak" zerrenda pantailaren logika kudeatzeko.
 *
 * Zer egiten du?
 * - Kategoria baten araberako zerrenda kargatzen du (TCP bidez).
 * - Bilaketa (search) eta genero iragazkia aplikatzen du.
 * - Item baten "cart" sakatzean, EventDetail-era nabigatzen du.
 *
 * Nola erabiltzen da?
 * - category: zein menuri dagokion (MOVIES/THEATRE/CONCERTS).
 * - scope.launch: sare/TCP deia egiteko.
 * - View interfazeak UI ekintzak egiten ditu (title, loading, list, menu...).
 *
 * Log-ak:
 * - Karga, filtro, bilaketa eta nabigazio pausuak jarraitzeko.
 */
class MenuListController(
    private val view: View,
    private val scope: CoroutineScope,
    private val category: EventCategory,
    private val service: EventTcpService = EventTcpService()
) {

    companion object {
        private const val TAG = "MenuListController"
    }

    interface View {
        fun showTitle(title: String)
        fun setFilterButtonText(text: String)
        fun submitList(list: List<ListItem>)
        fun applyFilter(query: String, genre: String?)
        fun openGenreMenu(options: List<String>)
        fun goTo(intent: Intent)
        fun context(): android.content.Context
        fun showMessage(msg: String)
        fun setLoading(loading: Boolean)
    }

    // Uneko genero iragazkia (null -> ez dago aukeratuta)
    private var selectedGenre: String? = null

    // Kargatutako datu guztiak (filtroak egiteko oinarria)
    private var all: List<ListItem> = emptyList()

    /**
     * View-a sortzean: titulua eta filtro botoiaren testua, eta karga.
     */
    fun onCreate() {
        val title = when (category) {
            EventCategory.MOVIES -> "Pelikulak"
            EventCategory.THEATRE -> "Antzerkiak"
            EventCategory.CONCERTS -> "Kontzertuak"
        }

        Log.i(TAG, "onCreate(): category=$category title=$title")

        view.showTitle(title)
        view.setFilterButtonText("Filtroak")
        load()
    }

    /**
     * TCP bidez kategoria honetako zerrenda kargatzen du.
     * Portaera originala:
     * - onSuccess: map -> ListItem eta submitList
     * - onFailure: mezua + list hutsa + online=false + offline mezua
     */
    private fun load() {
        Log.d(TAG, "load(): loading=true")
        view.setLoading(true)

        scope.launch {
            Log.d(TAG, "load(): service.list(category=$category)")
            val res = service.list(category)

            Log.d(TAG, "load(): loading=false")
            view.setLoading(false)

            res.fold(
                onSuccess = { netEvents ->
                    Log.i(TAG, "load() success: netEvents=${netEvents.size}")
                    all = netEvents.map { it.toListItem(category) }
                    view.submitList(all)
                    ConnectivityState.online = true
                },
                onFailure = { e ->
                    Log.e(TAG, "load() failure: ${e.message}", e)
                    view.showMessage("Ezin kargatu: ${e.message ?: "ezezaguna"}")

                    all = emptyList()
                    view.submitList(emptyList())

                    ConnectivityState.online = false
                    view.showMessage("Konexiorik ez: ezin da eguneratu edo erosi")
                }
            )
        }
    }

    /**
     * Search testua aldatzen denean: view-ari iragazkia aplikatzeko eskatzen dio.
     */
    fun onSearchChanged(text: String) {
        Log.d(TAG, "onSearchChanged(): text='$text' selectedGenre=${selectedGenre ?: "null"}")
        view.applyFilter(text, selectedGenre)
    }

    /**
     * Filtro botoia sakatzean: genero aukerak sortu eta view-ari menu moduan erakusteko eskatzen dio.
     */
    fun onFilterClicked() {
        // Dagoeneko kargatutako "all" zerrendatik genero bereiziak ateratzen dira
        val genres = mutableListOf("Guztiak")
        val distinct = all.map { it.genre }.distinct().sorted()
        genres.addAll(distinct)

        Log.d(TAG, "onFilterClicked(): genero aukerak=${genres.size} (all=${all.size})")

        view.openGenreMenu(genres)
    }

    /**
     * Genero bat aukeratzean:
     * - selectedGenre eguneratu
     * - botoiaren testua aldatu
     * - iragazkia aplikatu (search testuarekin)
     */
    fun onGenrePicked(genre: String, currentQuery: String) {
        Log.i(TAG, "onGenrePicked(): genre='$genre' query='$currentQuery'")
        selectedGenre = genre

        view.setFilterButtonText("Filtroak: $genre")
        view.applyFilter(currentQuery, selectedGenre)
    }

    /**
     * Item bateko "cart" sakatzean: Event sortu eta detail-era.
     * Portaera originala:
     * - price "€" kendu eta Double-ra
     * - synopsis hutsik badago: testu lehenetsia
     */
    fun onCartClicked(item: ListItem) {
        Log.i(TAG, "onCartClicked(): id=${item.id}, title=${item.title}")

        val priceValue = item.price.replace("€", "").trim().toDoubleOrNull() ?: 0.0
        Log.d(TAG, "onCartClicked(): price='${item.price}' -> $priceValue")

        val event = com.example.erronka_proba.model.domain.Event(
            id = item.id,
            title = item.title,
            imageRes = item.iconRes,
            synopsis = if (item.synopsis.isBlank()) "Sinopsia laster..." else item.synopsis,
            genre = item.genre,
            room = item.room,
            date = item.date,
            time = item.time,
            pricePerTicket = priceValue,
            aforo = item.aforo,
            imagePath = item.imagePath
        )

        val i = Intent(view.context(), EventDetailActivity::class.java).apply {
            putExtra(EventDetailActivity.EXTRA_EVENT, event)
        }

        Log.d(TAG, "onCartClicked(): EventDetailActivity-ra nabigatzen")
        view.goTo(i)
    }
}