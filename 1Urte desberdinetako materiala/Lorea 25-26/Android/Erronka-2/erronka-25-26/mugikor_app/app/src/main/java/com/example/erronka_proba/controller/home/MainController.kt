package com.example.erronka_proba.controller.home

import android.content.Intent
import android.util.Log
import com.example.erronka_proba.R
import com.example.erronka_proba.data.ConnectivityState
import com.example.erronka_proba.data.repo.EventRepository
import com.example.erronka_proba.model.domain.EventCategory
import com.example.erronka_proba.model.mapper.toDomainEvent
import com.example.erronka_proba.model.ui.HomeItem
import com.example.erronka_proba.view.auth.LoginActivity
import com.example.erronka_proba.view.menu.AntzerkiakActivity
import com.example.erronka_proba.view.menu.GuActivity
import com.example.erronka_proba.view.menu.KontzertuakActivity
import com.example.erronka_proba.view.menu.PelikulakActivity
import com.example.erronka_proba.view.profile.NireProfilaActivity
import com.example.erronka_proba.view.purchase.EventDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Fitxategia: MainController.kt
 *
 * Zertarako da?
 * - Home (MainActivity) pantailako logika kudeatzeko: datuak kargatu, drawer-a, nabigazioa eta klikak.
 *
 * Zer egiten du?
 * - onCreate(): home edukia kargatzen du (3 atal: pelikulak/antzerkiak/kontzertuak).
 * - onDrawerItemSelected(): drawer menutik pantaila egokira joaten da.
 * - onSeeAll(): atalaren arabera "guztiak ikusi" pantailara joaten da.
 * - onEventClicked()/onCartClicked(): event detail-era irekitzen du.
 * - onProfileEdit()/onLogout(): profilera edo login-era nabigazioa.
 *
 * Nola erabiltzen da?
 * - View interfazeak Activity/Fragment-ari UI ekintzak eskatzen dizkio (loading, mezua, nabigazioa).
 * - scope.launch erabiliz datuak kargatzen dira (coroutines).
 *
 * Log-ak:
 * - Karga prozesua, result-ak, online/offline egoera eta nabigazioak jarraitzeko.
 */
class MainController(
    private val view: View,
    private val scope: CoroutineScope,
    private val repo: EventRepository = EventRepository()
) {

    companion object {
        private const val TAG = "MainController"
    }

    interface View {
        fun context(): android.content.Context
        fun goTo(intent: Intent, finish: Boolean = false)

        fun setLoading(loading: Boolean)
        fun showHome(items: List<HomeItem>)
        fun showMessage(msg: String)
    }

    /**
     * View-a prest dagoenean deitzen da (Activity.onCreate-etik).
     */
    fun onCreate() {
        Log.i(TAG, "onCreate(): Home karga abiarazi")
        loadHome()
    }

    /**
     * Home-ko atalak kargatzen ditu:
     * - MOVIES, THEATRE, CONCERTS
     *
     * Portaera originala:
     * - Edozein atalak huts egiten badu, mezua erakusten da eta besteek jarraitzen dute.
     * - ConnectivityState.online true/false ezartzen da atal bakoitzean (bere logika berdin mantenduta).
     */
    private fun loadHome() {
        Log.d(TAG, "loadHome(): loading=true")
        view.setLoading(true)

        scope.launch {
            Log.d(TAG, "loadHome(): repo.getHomeSection(...) hasiera")

            val moviesRes = repo.getHomeSection(EventCategory.MOVIES)
            val theatreRes = repo.getHomeSection(EventCategory.THEATRE)
            val concertsRes = repo.getHomeSection(EventCategory.CONCERTS)

            Log.d(TAG, "loadHome(): loading=false")
            view.setLoading(false)

            // Edozein huts eginda ere, erakusteko zerrenda eraikitzen da.
            val items = mutableListOf<HomeItem>()

            moviesRes.fold(
                onSuccess = { list ->
                    Log.i(TAG, "MOVIES success: ${list.size} item")
                    items += HomeItem.Section("Pelikulak")
                    items += list.take(3)
                    ConnectivityState.online = true
                },
                onFailure = {
                    Log.e(TAG, "MOVIES failure: ${it.message}", it)
                    view.showMessage("Ezin izan da pelikulak kargatu: ${it.message}")
                    ConnectivityState.online = false
                    view.showMessage("Konexiorik ez: ezin da eguneratu edo erosi")
                }
            )

            theatreRes.fold(
                onSuccess = { list ->
                    Log.i(TAG, "THEATRE success: ${list.size} item")
                    items += HomeItem.Section("Antzerkiak")
                    items += list.take(3)
                    ConnectivityState.online = true
                },
                onFailure = {
                    Log.e(TAG, "THEATRE failure: ${it.message}", it)
                    view.showMessage("Ezin izan da antzerkiak kargatu: ${it.message}")
                    ConnectivityState.online = false
                    view.showMessage("Konexiorik ez: ezin da eguneratu edo erosi")
                }
            )

            concertsRes.fold(
                onSuccess = { list ->
                    Log.i(TAG, "CONCERTS success: ${list.size} item")
                    items += HomeItem.Section("Kontzertuak")
                    items += list.take(3)
                    ConnectivityState.online = true
                },
                onFailure = {
                    Log.e(TAG, "CONCERTS failure: ${it.message}", it)
                    view.showMessage("Ezin izan da kontzertuak kargatu: ${it.message}")
                    ConnectivityState.online = false
                    view.showMessage("Konexiorik ez: ezin da eguneratu edo erosi")
                }
            )

            Log.i(TAG, "loadHome(): showHome() -> item kopurua=${items.size}")
            view.showHome(items)
        }
    }

    /**
     * Drawer-eko item bat aukeratzean: pantaila egokira.
     */
    fun onDrawerItemSelected(itemId: Int) {
        val ctx = view.context()
        Log.d(TAG, "onDrawerItemSelected(): itemId=$itemId")

        val intent = when (itemId) {
            R.id.nav_movies -> Intent(ctx, PelikulakActivity::class.java)
            R.id.nav_theatre -> Intent(ctx, AntzerkiakActivity::class.java)
            R.id.nav_concerts -> Intent(ctx, KontzertuakActivity::class.java)
            R.id.nav_about -> Intent(ctx, GuActivity::class.java)
            else -> null
        }

        intent?.let {
            Log.i(TAG, "Drawer nabigazioa -> ${it.component?.className}")
            view.goTo(it)
        }
    }

    /**
     * Home-ko ataletan agertzen den "Guztiak ikusi" botoia.
     */
    fun onSeeAll(sectionTitle: String) {
        val ctx = view.context()
        Log.d(TAG, "onSeeAll(): sectionTitle=$sectionTitle")

        val intent = when {
            sectionTitle.contains("Pelikulak", true) -> Intent(ctx, PelikulakActivity::class.java)
            sectionTitle.contains("Antzerkiak", true) -> Intent(ctx, AntzerkiakActivity::class.java)
            sectionTitle.contains("Kontzertuak", true) -> Intent(ctx, KontzertuakActivity::class.java)
            else -> null
        }

        intent?.let {
            Log.i(TAG, "SeeAll nabigazioa -> ${it.component?.className}")
            view.goTo(it)
        }
    }

    /**
     * Event card klik-a: detail-era.
     */
    fun onEventClicked(card: HomeItem.EventCard) {
        Log.d(TAG, "onEventClicked(): id=${card.id}, title=${card.title}")
        openDetail(card)
    }

    /**
     * Cart klik-a: detail-era (portaera berdina).
     */
    fun onCartClicked(card: HomeItem.EventCard) {
        Log.d(TAG, "onCartClicked(): id=${card.id}, title=${card.title}")
        openDetail(card)
    }

    /**
     * EventDetailActivity irekitzen du.
     * - card.toDomainEvent() mapper-a erabiltzen du (portaera originala).
     */
    private fun openDetail(card: HomeItem.EventCard) {
        val ctx = view.context()
        Log.i(TAG, "openDetail(): EventDetailActivity -> id=${card.id}")

        val event = card.toDomainEvent()

        view.goTo(
            Intent(ctx, EventDetailActivity::class.java).apply {
                putExtra(EventDetailActivity.EXTRA_EVENT, event)
            }
        )
    }

    /**
     * Profila editatzeko pantailara.
     */
    fun onProfileEdit() {
        Log.i(TAG, "onProfileEdit(): NireProfilaActivity-ra")
        view.goTo(Intent(view.context(), NireProfilaActivity::class.java))
    }

    /**
     * Logout: LoginActivity-ra eta uneko activity-a ixteko (finish=true).
     */
    fun onLogout() {
        Log.i(TAG, "onLogout(): LoginActivity-ra (finish=true)")
        view.goTo(Intent(view.context(), LoginActivity::class.java), finish = true)
    }
}