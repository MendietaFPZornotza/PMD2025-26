package com.example.erronka_proba.view.purchase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.erronka_proba.BuildConfig
import com.example.erronka_proba.R
import com.example.erronka_proba.adapter.SeatAdapter
import com.example.erronka_proba.controller.purchase.EventDetailController
import com.example.erronka_proba.data.tcp.TCPClient
import com.example.erronka_proba.model.domain.Event
import com.example.erronka_proba.model.domain.SeatCell
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch


/**
 * Fitxategia: EventDetailActivity.kt
 *
 * Zertarako da?
 * - Ekitaldi baten xehetasunak erakutsi eta eserlekuak aukeratzeko pantaila.
 *
 * Zer egiten du?
 * - Intent extras-etik Event jasotzen du (Serializable).
 * - EventDetailController-ekin eserlekuak kargatu/eguneratu eta “continue” flow-a kudeatu.
 * - SeatAdapter + GridLayoutManager (11 zutabe: 5 + pasabidea + 5) erabiliz mapa erakutsi.
 *
 * Log-ak:
 * - Lifecycle, extras, eserleku render, continue klik eta summary update.
 */
class EventDetailActivity : AppCompatActivity(), EventDetailController.View {

    companion object {
        const val EXTRA_EVENT = "extra_event"
        const val EXTRA_SEATS = "extra_seats"
        const val EXTRA_TOTAL = "extra_total"

        private const val TAG = "EventDetailActivity"
        private const val GRID_COLS = 11
    }

    private lateinit var controller: EventDetailController
    private lateinit var toolbar: MaterialToolbar
    private lateinit var seatAdapter: SeatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate(): EventDetailActivity")

        setContentView(R.layout.activity_event_detail)

        val event = intent.getSerializableExtra(EXTRA_EVENT) as Event
        Log.d(TAG, "Extras: eventId=${event.id} titleLen=${event.title.length}")

        toolbar = findViewById(R.id.toolbar)
        applyInsetsToToolbar(toolbar)
        toolbar.setNavigationOnClickListener {
            Log.d(TAG, "Toolbar back -> finish()")
            finish()
        }

        controller = EventDetailController(
            view = this,
            scope = lifecycleScope,
            seatsService = com.example.erronka_proba.data.tcp.SeatsTcpService()
        )
        controller.onCreate(event)

        findViewById<MaterialButton>(R.id.btnContinue).setOnClickListener {
            Log.d(TAG, "btnContinue klik -> controller.onContinue()")
            controller.onContinue()
        }
    }

    private fun applyInsetsToToolbar(toolbar: MaterialToolbar) {
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.updatePadding(top = top)
            insets
        }
    }

    override fun renderEvent(event: Event) {
        Log.d(TAG, "renderEvent(): id=${event.id}")

        findViewById<MaterialToolbar>(R.id.toolbar).title = event.title
        val iv = findViewById<ImageView>(R.id.ivPoster)

        // 1) Lehenetsi: betiko irudia (icon)
        iv.setImageResource(event.imageRes)

        findViewById<TextView>(R.id.tvTitle).text = event.title
        findViewById<TextView>(R.id.tvMeta).text = "${event.genre} · ${event.room} · ${event.date} - ${event.time}"
        findViewById<TextView>(R.id.tvSynopsis).text = event.synopsis
        findViewById<TextView>(R.id.tvPrice).text = "Prezioa: ${event.pricePerTicket}€"

        // 2) FTPS bidezko irudia badago, kargatu eta ordezkatu
        val path = event.imagePath?.trim().orEmpty()
        if (path.isNotEmpty()) {
            loadPosterByFtps(iv, path)
        }
    }

    /**
     * Irudia FTPS bidez deskargatu eta ImageView-ean jarri.
     *
     * Oharra:
     * - Huts egiten badu, ez dugu crash egin nahi: lehenetsitako icon-a mantendu.
     */
    private fun loadPosterByFtps(iv: ImageView, remotePath: String) {
        lifecycleScope.launch {
            try {
                val client = com.example.erronka_proba.data.ftps.FtpsImageClient(
                    host = BuildConfig.FTPS_HOST,
                    port = BuildConfig.FTPS_PORT,
                    user = BuildConfig.FTPS_USER,
                    pass = BuildConfig.FTPS_PASS
                )

                val res = client.download(remotePath)
                res.fold(
                    onSuccess = { bytes ->
                        val bmp = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        if (bmp != null) iv.setImageBitmap(bmp)
                        else Log.w(TAG, "loadPosterByFtps(): Bitmap decode null")
                    },
                    onFailure = { e ->
                        Log.w(TAG, "loadPosterByFtps(): failure ${e.message}")
                    }
                )
            } catch (e: Exception) {
                Log.w(TAG, "loadPosterByFtps(): exception ${e.message}")
            }
        }
    }



    override fun showSeats(seats: List<SeatCell>) {
        Log.i(TAG, "showSeats(): cells=${seats.size}")

        val rv = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvSeats)
        rv.layoutManager = GridLayoutManager(this, GRID_COLS)

        seatAdapter = SeatAdapter(seats) {
            Log.v(TAG, "SeatAdapter onChanged() -> controller.onSeatsChanged()")
            controller.onSeatsChanged()
        }
        rv.adapter = seatAdapter
    }

    override fun updateSummary(seatsText: String, totalText: String) {
        Log.v(TAG, "updateSummary()")
        findViewById<TextView>(R.id.tvSeatsChosen).text = seatsText
        findViewById<TextView>(R.id.tvTotal).text = totalText
    }

    override fun showSeatError() {
        Log.w(TAG, "showSeatError(): eserlekurik ez aukeratuta")
        findViewById<TextView>(R.id.tvSeatsChosen).text = "Eserlekua: - (aukeratu gutxienez 1)"
    }

    override fun goTo(intent: Intent) {
        Log.i(TAG, "goTo(): ${intent.component?.className}")
        startActivity(intent)
    }

    override fun context(): Context = this

    override fun setBuyEnabled(enabled: Boolean) {
        Log.d(TAG, "setBuyEnabled(): $enabled")
        findViewById<MaterialButton>(R.id.btnContinue).isEnabled = enabled
    }

    override fun showMessage(msg: String) {
        // Originalean TODO zegoen; crash egin dezake deitzen bada.
        Log.w(TAG, "showMessage(): TODO dago -> msg='$msg'")
        TODO("Not yet implemented")
    }

    override fun disableBuy(bool: Boolean) {
        Log.d(TAG, "disableBuy(): $bool")
        findViewById<MaterialButton>(R.id.btnContinue).isEnabled = bool
    }


}