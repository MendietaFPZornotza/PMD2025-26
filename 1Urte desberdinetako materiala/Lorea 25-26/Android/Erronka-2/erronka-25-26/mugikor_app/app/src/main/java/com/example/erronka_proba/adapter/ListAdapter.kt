package com.example.erronka_proba.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.erronka_proba.R
import com.example.erronka_proba.data.ftps.FtpsImageClient
import com.example.erronka_proba.model.ui.ListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.LinkedHashMap

/**
 * Fitxategia: ListAdapter.kt
 *
 * Zertarako da?
 * - "List" pantailako RecyclerView-a betetzeko (item_list_card layout-a).
 *
 * Zer egiten du?
 * - Barnean 2 zerrenda mantentzen ditu:
 *   - all: item guztiak (originalak)
 *   - shown: momentuan erakusten diren itemak (filter aplikatuta)
 *
 * Nola erabiltzen da?
 * - submit(list): datu guztiak sartzen ditu eta UI eguneratzen du.
 * - filter(query, genre): testuaren eta generoaren arabera iragazten du.
 *
 * Log-ak:
 * - submit/filter prozesua jarraitzeko (tamainak, query, genero...).
 */
class ListAdapter(
    private val scope: CoroutineScope,
    private val ftps: FtpsImageClient,
    private val onBuy: (ListItem) -> Unit
) : RecyclerView.Adapter<ListAdapter.VH>() {

    companion object {
        private const val TAG = "ListAdapter"

        // 🔥 Miniatura cache sinplea (LRU modukoa).
        private const val CACHE_MAX = 80
        private val cache = object : LinkedHashMap<String, ByteArray>(CACHE_MAX, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, ByteArray>?): Boolean =
                size > CACHE_MAX
        }

        private fun cacheGet(key: String): ByteArray? = synchronized(cache) { cache[key] }
        private fun cachePut(key: String, value: ByteArray) = synchronized(cache) { cache[key] = value }
    }

    // Zerrenda originala (datu guztiak)
    private val all = mutableListOf<ListItem>()

    // Pantailan erakusten den zerrenda (filtratua)
    private val shown = mutableListOf<ListItem>()

    /**
     * Zerrenda berria sartzen du (all + shown) eta RecyclerView eguneratzen du.
     * Portaera: notifyDataSetChanged() (zure originala bezala).
     */
    fun submit(list: List<ListItem>) {
        Log.i(TAG, "submit(): item kopurua=${list.size}")

        all.clear()
        all.addAll(list)

        shown.clear()
        shown.addAll(list)

        notifyDataSetChanged()
    }

    /**
     * Iragazkia aplikatzen du:
     * - query: testuaren arabera (title)
     * - genre: genero zehatza edo null/"Guztiak" bada, ez du murrizten
     *
     * Oharra:
     * - Lowercase erabiliz egiten da bilaketa (zure originala bezala).
     */
    fun filter(query: String, genre: String?) {
        val q = query.trim().lowercase()

        Log.d(TAG, "filter(): query='$q', genre='${genre ?: "null"}', all=${all.size}")

        val filtered = all.filter { item ->
            val matchesText = q.isEmpty() || item.title.lowercase().contains(q)
            val matchesGenre = genre == null || genre == "Guztiak" || item.genre == genre
            matchesText && matchesGenre
        }

        shown.clear()
        shown.addAll(filtered)

        Log.i(TAG, "filter(): shown=${shown.size}")

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        Log.d(TAG, "onCreateViewHolder(): viewType=$viewType")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list_card, parent, false)
        return VH(v, scope, ftps, onBuy)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = shown[position]
        Log.v(TAG, "onBindViewHolder(): pos=$position, title=${item.title}")
        holder.bind(item)
    }

    override fun getItemCount(): Int = shown.size

    /**
     * ViewHolder: item_list_card layout-eko osagaiak lotzen ditu.
     */
    class VH(
        itemView: View,
        private val scope: CoroutineScope,
        private val ftps: FtpsImageClient,
        private val onBuy: (ListItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvRoom: TextView = itemView.findViewById(R.id.tvRoom)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val btnCart: View = itemView.findViewById(R.id.btnCart)

        /**
         * Item-a UI-n margotu eta "buy/cart" listener-a ezarri.
         */
        fun bind(item: ListItem) {
            // Default icon-a
            ivIcon.setImageResource(item.iconRes)
            ImageViewCompat.setImageTintList(ivIcon, itemView.context.getColorStateList(R.color.white))

            tvTitle.text = item.title
            tvRoom.text = item.room
            tvDate.text = item.date
            tvTime.text = item.time
            tvPrice.text = item.price

            // 🔥 Miniatura FTPS
            val raw = item.imagePath?.trim().orEmpty()
            if (raw.isNotEmpty()) {
                val remotePath = if (raw.contains("/")) raw else "Dokumentuak/Argazkiak/$raw"
                ivIcon.tag = remotePath

                Log.d(TAG, "bind(): FTPS miniatura -> id=${item.id} path=$remotePath")

                val cached = cacheGet(remotePath)
                if (cached != null) {
                    setBitmapIfStillSame(remotePath, cached)
                } else {
                    scope.launch(Dispatchers.IO) {
                        val res = ftps.download(remotePath)
                        res.onSuccess { bytes ->
                            cachePut(remotePath, bytes)
                            setBitmapIfStillSame(remotePath, bytes)
                        }.onFailure { e ->
                            Log.w(TAG, "bind(): FTPS miniatura failure id=${item.id}: ${e.message}")
                        }
                    }
                }
            }

            btnCart.setOnClickListener {
                Log.d("ListAdapter", "VH: cart klik -> title=${item.title}")
                onBuy(item)
            }

            // Aukeran: card osoa klik ere erosketara bidaltzeko (zure komentarioa mantenduta).
            // itemView.setOnClickListener { onBuy(item) }
        }

        // 🔥 MAIN thread + tint OFF + tag check
        private fun setBitmapIfStillSame(key: String, bytes: ByteArray) {
            scope.launch(Dispatchers.Main) {
                val current = ivIcon.tag as? String
                if (current != key) return@launch

                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                if (bmp == null) {
                    Log.w(TAG, "setBitmapIfStillSame(): decode null key=$key")
                    return@launch
                }

                ImageViewCompat.setImageTintList(ivIcon, null)
                ivIcon.setImageBitmap(bmp)
            }
        }
    }
}
