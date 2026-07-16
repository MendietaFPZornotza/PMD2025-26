package com.example.erronka_proba.controller.purchase

import android.content.Intent
import android.util.Log
import com.example.erronka_proba.data.ConnectivityState
import com.example.erronka_proba.data.tcp.SeatsTcpService
import com.example.erronka_proba.model.domain.Event
import com.example.erronka_proba.model.domain.SeatCell
import com.example.erronka_proba.view.purchase.EventDetailActivity
import com.example.erronka_proba.view.purchase.PurchaseConfirmActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Fitxategia: EventDetailController.kt
 *
 * Zertarako da?
 * - Ekitaldi baten detail pantailako logika kudeatzeko: eserlekuak kargatu, aukeratu, laburpena kalkulatu eta erosketara joan.
 *
 * Zer egiten du?
 * - onCreate(event):
 *   - Offline bada, erosketa desgaitu.
 *   - Ekitaldia marraztu.
 *   - TCP bidez okupatutako eserlekuak hartu.
 *   - Eserleku-grid-a eraiki (Seat/Aisle/Empty).
 *   - Laburpena kalkulatu eta eguneratu.
 * - onSeatsChanged():
 *   - Aukeraketa aldatzean laburpena berriz kalkulatu.
 * - onContinue():
 *   - Aukeratutako eserlekuak balidatu eta PurchaseConfirmActivity-ra bidali.
 *
 * Nola erabiltzen da?
 * - scope.launch: TCP deia egiteko.
 * - View interfazeak UI funtzioak ematen ditu (render, showSeats, updateSummary, nabigazioa...).
 *
 * Log-ak:
 * - Online/offline, eserlekuen karga, aukeratutakoak eta total kalkuluak jarraitzeko.
 */
class EventDetailController(
    private val view: View,
    private val scope: CoroutineScope,
    private val seatsService: SeatsTcpService
) {

    companion object {
        private const val TAG = "EventDetailController"

        // Eserlekuen diseinua:
        // 5 eserleku ezkerrean + pasabidea + 5 eserleku eskuinean
        private const val COLS_PER_SIDE = 5
        private const val TOTAL_SEATS_PER_ROW = 10
        private const val TOTAL_CELLS_PER_ROW = 11 // 5 + pasillo + 5 (konstante moduan uzten da, nahiz eta ez erabili)
    }

    interface View {
        fun renderEvent(event: Event)
        fun showSeats(cells: List<SeatCell>)
        fun updateSummary(seatsText: String, totalText: String)
        fun goTo(intent: Intent)
        fun showSeatError()
        fun context(): android.content.Context
        fun setBuyEnabled(enabled: Boolean)
        fun showMessage(msg: String)
        fun disableBuy(bool: Boolean)
    }

    private lateinit var event: Event
    private var cells: MutableList<SeatCell> = mutableListOf()

    /**
     * Pantaila sortzean:
     * - Ekitaldia erakutsi
     * - TCP bidez okupatutako eserlekuak kargatu
     * - Grid-a eraiki eta UI-ra bidali
     */
    fun onCreate(event: Event) {
        Log.i(TAG, "onCreate(): eventId=${event.id}, title=${event.title}, online=${ConnectivityState.online}")

        // Portaera originala: offline bada, erosketa desgaitu
        if (!ConnectivityState.online) {
            Log.w(TAG, "Offline: buy desgaitzen (disableBuy=true)")
            view.disableBuy(true)
        }

        this.event = event
        view.renderEvent(event)

        scope.launch {
            Log.d(TAG, "getTakenSeats(): eventId=${event.id}")
            val takenRes = seatsService.getTakenSeats(event.id)

            val taken = takenRes.getOrElse { ex ->
                Log.e(TAG, "getTakenSeats() failure: ${ex.message}", ex)
                emptyList()
            }.toSet()

            Log.i(TAG, "Taken seats kopurua=${taken.size}")

            cells = buildSeats(event.aforo, taken).toMutableList()
            Log.i(TAG, "Cells sortuta=${cells.size} (aforo=${event.aforo})")

            view.showSeats(cells)
            refreshSummary()
        }
    }

    /**
     * Eserleku aukeraketa aldatzen denean (SeatAdapter-etik deitua normalean).
     */
    fun onSeatsChanged() {
        Log.d(TAG, "onSeatsChanged(): laburpena eguneratzen")
        refreshSummary()
    }

    /**
     * "Jarraitu" / "Erosi" aurreko pausua:
     * - Offline bada, blokeatu.
     * - Aukeratutako eserlekuak badaude, PurchaseConfirmActivity-ra.
     */
    fun onContinue() {
        Log.i(TAG, "onContinue(): online=${ConnectivityState.online}")

        if (!ConnectivityState.online) {
            Log.w(TAG, "Erosketa blokeatuta: offline")
            view.showMessage("Konexiorik ez: ezin da erosi")
            return
        }

        val selected = cells
            .filterIsInstance<SeatCell.SeatItem>()
            .filter { it.state == SeatCell.State.SELECTED }
            .map { it.code }

        Log.d(TAG, "onContinue(): selectedCount=${selected.size}")

        if (selected.isEmpty()) {
            Log.w(TAG, "onContinue(): eserlekurik ez -> showSeatError()")
            view.showSeatError()
            return
        }

        val total = selected.size * event.pricePerTicket
        val totalRounded = (total * 100.0).roundToInt() / 100.0

        Log.i(TAG, "onContinue(): total=$totalRounded (${selected.size} x ${event.pricePerTicket})")

        val i = Intent(view.context(), PurchaseConfirmActivity::class.java).apply {
            putExtra(EventDetailActivity.EXTRA_EVENT, event)
            putStringArrayListExtra(EventDetailActivity.EXTRA_SEATS, ArrayList(selected))
            putExtra(EventDetailActivity.EXTRA_TOTAL, totalRounded)
        }

        view.goTo(i)
    }

    /**
     * Laburpena eguneratzen du:
     * - Aukeratutako eserlekuak eta prezio totala kalkulatu.
     */
    fun refreshSummary() {
        val selected = cells
            .filterIsInstance<SeatCell.SeatItem>()
            .filter { it.state == SeatCell.State.SELECTED }
            .map { it.code }

        val count = selected.size
        val total = count * event.pricePerTicket
        val totalRounded = (total * 100.0).roundToInt() / 100.0

        Log.v(TAG, "refreshSummary(): count=$count total=$totalRounded")

        val seatsText = "Eserlekua: " + (if (selected.isEmpty()) "-" else selected.joinToString(", "))
        val totalText = "Prezioa: $count x ${event.pricePerTicket}€ = ${totalRounded}€"

        view.updateSummary(seatsText, totalText)
    }

    /**
     * Eserlekuen grid-a eraikitzen du (Seat/Aisle/Empty).
     *
     * Portaera originala:
     * - Fila bakoitzean 10 eserleku posizio sortzen dira (nahiz eta azkena ez osatu).
     * - 6. eserlekuaren aurretik pasabidea sartzen da.
     * - aforoa amaitzean, gainerakoa Empty-rekin betetzen da (rejilla perfektua mantentzeko).
     */
    fun buildSeats(aforo: Int, taken: Set<String>): List<SeatCell> {
        Log.d(TAG, "buildSeats(): aforo=$aforo taken=${taken.size}")

        val list = mutableListOf<SeatCell>()
        val rows = (aforo + TOTAL_SEATS_PER_ROW - 1) / TOTAL_SEATS_PER_ROW

        var created = 0

        for (r in 1..rows) {
            for (c in 1..TOTAL_SEATS_PER_ROW) {

                // Pasabidea 6. eserlekuaren aurretik
                if (c == COLS_PER_SIDE + 1) list.add(SeatCell.Aisle)

                if (created >= aforo) {
                    list.add(SeatCell.Empty)
                    continue
                }

                created++
                val code = "${rowLabel(r)}$c" // A1..A10, B1..B10...
                val state = if (taken.contains(code)) {
                    SeatCell.State.OCCUPIED
                } else {
                    SeatCell.State.FREE
                }

                list.add(SeatCell.SeatItem(code, state))
            }
        }

        Log.d(TAG, "buildSeats(): created=$created listSize=${list.size}")
        return list
    }

    /**
     * Fila zenbakitik etiketa sortzen du:
     * - 1 -> A
     * - 2 -> B
     * - ...
     * - 26 -> Z
     * - 27 -> AA
     */
    fun rowLabel(r: Int): String {
        var n = r
        val sb = StringBuilder()

        while (n > 0) {
            n--
            sb.append(('A'.code + (n % 26)).toChar())
            n /= 26
        }

        val label = sb.reverse().toString()
        Log.v(TAG, "rowLabel(): r=$r -> $label")
        return label
    }
}