package com.example.erronka_proba.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erronka_proba.R
import com.example.erronka_proba.data.ftps.FtpsImageClient
import com.example.erronka_proba.model.ui.HomeItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.LinkedHashMap

/**
 * Fitxategia: HomeAdapter.kt
 *
 * Zertarako da?
 * - Home pantailako RecyclerView-a betetzeko (atal-buruak + ekitaldi txartelak).
 *
 * Zer egiten du?
 * - 2 item mota erakusten ditu:
 *   1) Section: atalaren izenburua + "Guztiak ikusi"
 *   2) EventCard: ekitaldiaren txartela + cart botoia
 *
 * Nola erabiltzen da?
 * - MainActivity-k adapter hau sortzen du callback-ekin:
 *   - onSeeAllClick: atal bateko "Guztiak ikusi"
 *   - onEventClick: card-aren klik nagusia
 *   - onCartClick: karrito/buy botoia
 *
 * Log-ak:
 * - Kontsolan ikusteko: viewholder sortzeak, bind-ak eta klik-ak.
 */
class HomeAdapter(
    private val scope: CoroutineScope,
    private val ftps: FtpsImageClient,
    private val onSeeAllClick: (String) -> Unit,
    private val onEventClick: (HomeItem.EventCard) -> Unit,
    private val onCartClick: (HomeItem.EventCard) -> Unit
) : ListAdapter<HomeItem, RecyclerView.ViewHolder>(Diff) {

    companion object {
        private const val TAG = "HomeAdapter"

        // RecyclerView item motak
        private const val TYPE_SECTION = 0
        private const val TYPE_EVENT = 1

        /**
         * DiffUtil:
         * - ListAdapter-ek zerrendaren aldaketak kalkulatzeko erabiltzen du.
         *
         * Oharra:
         * - Zure bertsio originalean "oldItem == newItem" erabiltzen da.
         * - Berdin mantentzen dut portaera, funtzionalitatea ez aldatzeko.
         */
        val Diff = object : DiffUtil.ItemCallback<HomeItem>() {
            override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean = oldItem == newItem
        }

        // 🔥 Miniatura cache sinplea (LRU modukoa).
        // Oharra: 50 nahikoa da HOME-rako; handitu nahi baduzu, hemen.
        private const val CACHE_MAX = 50
        private val cache = object : LinkedHashMap<String, ByteArray>(CACHE_MAX, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, ByteArray>?): Boolean =
                size > CACHE_MAX
        }

        private fun cacheGet(key: String): ByteArray? = synchronized(cache) { cache[key] }
        private fun cachePut(key: String, value: ByteArray) = synchronized(cache) { cache[key] = value }
    }

    /**
     * Posizio bakoitzean zein view mota den esaten dio RecyclerView-ari.
     */
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeItem.Section -> TYPE_SECTION
            is HomeItem.EventCard -> TYPE_EVENT
        }
    }

    /**
     * ViewHolder-a sortzen du item motaren arabera.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "onCreateViewHolder(): viewType=$viewType")

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SECTION -> {
                val v = inflater.inflate(R.layout.item_section_header, parent, false)
                SectionVH(v, onSeeAllClick)
            }
            else -> {
                val v = inflater.inflate(R.layout.item_event_card, parent, false)
                EventVH(v, scope, ftps, onEventClick, onCartClick)
            }
        }
    }

    /**
     * Datuak ViewHolder-ean margotzen ditu (bind).
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HomeItem.Section -> {
                Log.v(TAG, "onBindViewHolder(): Section -> ${item.title}")
                (holder as SectionVH).bind(item)
            }
            is HomeItem.EventCard -> {
                Log.v(TAG, "onBindViewHolder(): EventCard -> id=${item.id}, title=${item.title}")
                (holder as EventVH).bind(item)
            }
        }
    }

    /**
     * ViewHolder: atalaren izenburua + "Guztiak ikusi".
     */
    class SectionVH(
        itemView: View,
        private val onSeeAllClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvSection: TextView = itemView.findViewById(R.id.tvSection)
        private val tvSeeAll: TextView = itemView.findViewById(R.id.tvSeeAll)

        /**
         * Atalaren titulua ezarri eta "Guztiak ikusi" listener-a.
         */
        fun bind(item: HomeItem.Section) {
            tvSection.text = item.title

            // Listener-a bind bakoitzean jartzen da, RecyclerView-ak item-ak birziklatzen dituelako.
            tvSeeAll.setOnClickListener {
                Log.d("HomeAdapter", "SectionVH: 'Guztiak ikusi' klik -> ${item.title}")
                onSeeAllClick(item.title)
            }
        }
    }

    /**
     * ViewHolder: ekitaldi txartela.
     * - Card osoa klik: onEventClick
     * - Cart botoia klik: onCartClick
     */
    class EventVH(
        itemView: View,
        private val scope: CoroutineScope,
        private val ftps: FtpsImageClient,
        private val onEventClick: (HomeItem.EventCard) -> Unit,
        private val onCartClick: (HomeItem.EventCard) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val btnCart: View = itemView.findViewById(R.id.btnCart)

        /**
         * Ekitaldiaren datuak UI-n margotzen ditu eta klikak konektatzen ditu.
         */
        fun bind(item: HomeItem.EventCard) {
            // 🔥 Lehenengo: default icon-a jarri, bestela scroll-ean irudi zaharrak agertzen dira.
            ivIcon.setImageResource(item.iconRes)
            // 🔥 XML-n tint zuria dago; icon-a bai, baina bitmap jartzean kendu egingo dugu.
            ImageViewCompat.setImageTintList(ivIcon, itemView.context.getColorStateList(R.color.white))

            tvTitle.text = item.title
            tvDate.text = item.dateTime
            tvPrice.text = item.price

            // 🔥 Miniatura FTPS: badago, kargatu eta ordezkatu
            val raw = item.imagePath?.trim().orEmpty()
            if (raw.isNotEmpty()) {
                val remotePath = if (raw.contains("/")) raw else "Dokumentuak/Argazkiak/$raw"

                // 🧷 Tag bidez: RecyclerView recycling-a kontrolatzeko (irudiak ez nahasteko)
                ivIcon.tag = remotePath

                Log.d(TAG, "bind(): FTPS miniatura -> id=${item.id} path=$remotePath")

                // 1) Cache
                val cached = cacheGet(remotePath)
                if (cached != null) {
                    setBitmapIfStillSame(ivIcon, remotePath, cached)
                } else {
                    // 2) Download (IO)
                    scope.launch(Dispatchers.IO) {
                        val res = ftps.download(remotePath)
                        res.onSuccess { bytes ->
                            cachePut(remotePath, bytes)
                            setBitmapIfStillSame(ivIcon, remotePath, bytes)
                        }.onFailure { e ->
                            Log.w(TAG, "bind(): FTPS miniatura failure id=${item.id}: ${e.message}")
                        }
                    }
                }
            }

            itemView.setOnClickListener {
                Log.d(TAG, "EventVH: card klik -> id=${item.id}, title=${item.title}")
                onEventClick(item)
            }

            btnCart.setOnClickListener {
                Log.d(TAG, "EventVH: cart klik -> id=${item.id}, title=${item.title}")
                onCartClick(item)
            }
        }

        // 🔥 GARRANTZITSUA:
        // - Bitmap-a MAIN thread-ean jarri
        // - tint kendu (bestela irudia zuriz “pintatzen” da)
        // - tag berdina dela egiaztatu (recycling-a)
        private fun setBitmapIfStillSame(iv: ImageView, key: String, bytes: ByteArray) {
            scope.launch(Dispatchers.Main) {
                val current = iv.tag as? String
                if (current != key) return@launch

                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                if (bmp == null) {
                    Log.w(TAG, "setBitmapIfStillSame(): decode null key=$key")
                    return@launch
                }

                // 🔥 Tint OFF (bestela irudia ez da “ikusten”)
                ImageViewCompat.setImageTintList(iv, null)
                iv.setImageBitmap(bmp)
            }
        }
    }
}
