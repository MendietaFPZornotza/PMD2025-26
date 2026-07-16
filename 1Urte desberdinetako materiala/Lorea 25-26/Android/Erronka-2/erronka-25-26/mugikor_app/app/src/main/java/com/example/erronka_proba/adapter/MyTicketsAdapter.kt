package com.example.erronka_proba.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.erronka_proba.R
import com.example.erronka_proba.model.domain.TicketGroup
import com.google.android.material.button.MaterialButton

/**
 * Fitxategia: MyTicketsAdapter.kt
 *
 * Zertarako da?
 * - "Nire sarrerak" pantailako RecyclerView-a betetzeko (item_my_ticket).
 *
 * Zer egiten du?
 * - TicketGroup zerrenda bat erakusten du:
 *   - izenburua, meta (data·ordua·aretoa), kopurua
 *   - deskarga botoia (QR/barcode edo kodea deskargatzeko)
 *
 * Nola erabiltzen da?
 * - submit(newItems): zerrenda berria sartu eta UI freskatu.
 * - onDownloadClick: item bakoitzeko deskarga ekintza.
 *
 * Log-ak:
 * - submit, bind eta deskarga klik-ak jarraitzeko.
 */
class MyTicketsAdapter(
    private var items: List<TicketGroup>,
    private val onDownloadClick: (TicketGroup) -> Unit
) : RecyclerView.Adapter<MyTicketsAdapter.VH>() {

    companion object {
        private const val TAG = "MyTicketsAdapter"
    }

    /**
     * Zerrenda berria sartzen du eta RecyclerView eguneratzen du.
     * Portaera: notifyDataSetChanged() (zure originala bezala).
     */
    fun submit(newItems: List<TicketGroup>) {
        Log.i(TAG, "submit(): item kopurua=${newItems.size}")
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        Log.d(TAG, "onCreateViewHolder(): viewType=$viewType")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_my_ticket, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        Log.v(TAG, "onBindViewHolder(): pos=$position, title=${item.title}")
        holder.bind(item, onDownloadClick)
    }

    /**
     * ViewHolder: ticket talde bakoitzeko lerroa.
     */
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tvEventTitle)
        private val tvMeta: TextView = itemView.findViewById(R.id.tvEventMeta)
        private val tvCount: TextView = itemView.findViewById(R.id.tvCount)
        private val btnDownload: MaterialButton = itemView.findViewById(R.id.btnDownload)

        /**
         * UI-a betetzen du eta deskarga botoiaren listener-a ezartzen du.
         */
        fun bind(item: TicketGroup, onDownloadClick: (TicketGroup) -> Unit) {
            tvTitle.text = item.title
            tvMeta.text = "${item.date} · ${item.time} · ${item.room}"
            tvCount.text = "x${item.count}"

            btnDownload.setOnClickListener {
                Log.d("MyTicketsAdapter", "Deskarga klik -> title=${item.title}, count=${item.count}")
                onDownloadClick(item)
            }
        }
    }
}