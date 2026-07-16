package com.example.erronka_proba.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.erronka_proba.R
import com.example.erronka_proba.model.domain.SeatCell

/**
 * Fitxategia: SeatAdapter.kt
 *
 * Zertarako da?
 * - Eserlekuen mapa (seat map) RecyclerView bidez marrazteko.
 *
 * Zer egiten du?
 * - "Cells" zerrendan datozen elementuak erakusten ditu:
 *   - Seat (eserlekua) -> klikagarria
 *   - Aisle (pasabidea) -> ez klikagarria
 *   - Empty (hutsunea) -> ez klikagarria
 *
 * Nola erabiltzen da?
 * - cells: eserlekuen egitura (grid) deskribatzen duen zerrenda.
 * - onChanged: erabiltzaileak eserleku bat aukeratu/askatu duenean deitzen da.
 *
 * Log-ak:
 * - ViewHolder mota sortzea, bind-a eta seat toggle ekintzak jarraitzeko.
 */
class SeatAdapter(
    private val cells: List<SeatCell>,
    private val onChanged: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private companion object {
        private const val TAG = "SeatAdapter"

        // ViewType konstanteak
        private const val VT_SEAT = 1
        private const val VT_AISLE = 2
        private const val VT_EMPTY = 3
    }

    /**
     * Posizio bakoitzean zein item mota den esaten dio RecyclerView-ari.
     */
    override fun getItemViewType(position: Int): Int = when (cells[position]) {
        is SeatCell.SeatItem -> VT_SEAT
        SeatCell.Aisle -> VT_AISLE
        SeatCell.Empty -> VT_EMPTY
    }

    /**
     * ViewHolder-a sortzen du viewType-ren arabera.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "onCreateViewHolder(): viewType=$viewType")

        val inf = LayoutInflater.from(parent.context)
        return when (viewType) {
            VT_SEAT -> SeatVH(inf.inflate(R.layout.item_seat, parent, false))
            VT_AISLE -> SimpleVH(inf.inflate(R.layout.item_aisle, parent, false))
            else -> SimpleVH(inf.inflate(R.layout.item_empty, parent, false))
        }
    }

    override fun getItemCount(): Int = cells.size

    /**
     * Posizio bakoitzeko cell-a lotu (bind) eta, seat-a bada, klik logika aplikatu.
     *
     * Ohar garrantzitsua:
     * - Aisle/Empty elementuak ez dira klikagarriak (portaera originala).
     * - Seat-a OCCUPIED bada, ez da aldatzen.
     * - Seat-a FREE <-> SELECTED txandakatzen da.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val cell = cells[position]) {

            is SeatCell.SeatItem -> {
                Log.v(TAG, "onBindViewHolder(): SEAT pos=$position state=${cell.state}")
                (holder as SeatVH).bind(cell)

                // Klik listener-a bind bakoitzean jartzen da, item-ak birziklatzen direlako.
                holder.itemView.setOnClickListener {
                    if (cell.state == SeatCell.State.OCCUPIED) {
                        Log.d(TAG, "Seat klik (OCCUPIED) -> ez da ezer egiten (pos=$position)")
                        return@setOnClickListener
                    }

                    // FREE <-> SELECTED toggle (portaera originala)
                    val oldState = cell.state
                    cell.state = if (cell.state == SeatCell.State.SELECTED) {
                        SeatCell.State.FREE
                    } else {
                        SeatCell.State.SELECTED
                    }

                    Log.i(TAG, "Seat toggle pos=$position: $oldState -> ${cell.state}")

                    notifyItemChanged(position)
                    onChanged()
                }
            }

            else -> {
                // Aisle/Empty: ez klikagarriak (portaera originala)
                Log.v(TAG, "onBindViewHolder(): NON-CLICKABLE pos=$position type=${cell::class.java.simpleName}")
                Unit
            }
        }
    }

    /**
     * ViewHolder sinplea (Aisle/Empty).
     * - Ez du bind logikarik behar: layout hutsa da.
     */
    class SimpleVH(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * ViewHolder: eserlekua (SeatItem).
     * - State-ren arabera atzeko kolorea/irudia eta check ikonoa eguneratzen ditu.
     */
    class SeatVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val seatView: View = itemView.findViewById(R.id.vSeat)
        private val tvCheck: TextView = itemView.findViewById(R.id.tvCheck)

        /**
         * Eserlekuaren egoera bistaratzen du:
         * - FREE: libre (check ezkutuan)
         * - SELECTED: aukeratua (check ikusgai)
         * - OCCUPIED: okupatua (check ezkutuan)
         */
        fun bind(seat: SeatCell.SeatItem) {
            when (seat.state) {
                SeatCell.State.FREE -> {
                    seatView.setBackgroundResource(R.drawable.bg_seat_free)
                    tvCheck.visibility = View.GONE
                }
                SeatCell.State.SELECTED -> {
                    seatView.setBackgroundResource(R.drawable.bg_seat_selected)
                    tvCheck.visibility = View.VISIBLE
                }
                SeatCell.State.OCCUPIED -> {
                    seatView.setBackgroundResource(R.drawable.bg_seat_occupied)
                    tvCheck.visibility = View.GONE
                }
            }
        }
    }
}