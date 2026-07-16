package com.example.erronka_proba.view.purchase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.erronka_proba.R
import com.example.erronka_proba.controller.purchase.PurchaseConfirmController
import com.example.erronka_proba.data.repo.PurchaseRepository
import com.example.erronka_proba.data.tcp.PurchaseTcpService
import com.example.erronka_proba.data.tcp.SessionManager
import com.example.erronka_proba.model.domain.Event
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Fitxategia: PurchaseConfirmActivity.kt
 *
 * Zertarako da?
 * - Erosketa baieztatzeko pantaila: laburpena erakutsi, email-a balidatu eta erosketari ekin.
 *
 * Zer egiten du?
 * - Extras: event, seats, total hartu.
 * - PurchaseConfirmController-en bidez:
 *   - render() -> laburpena + botoi testua
 *   - onEmailChanged() -> balidazioa
 *   - onBuyClicked() -> erosketaren TCP flow-a
 *
 * Log-ak:
 * - Lifecycle, extras, email aldaketak, buy klik eta loading.
 */
class PurchaseConfirmActivity : AppCompatActivity(), PurchaseConfirmController.View {

    companion object {
        private const val TAG = "PurchaseConfirmActivity"
    }

    private lateinit var controller: PurchaseConfirmController
    private lateinit var toolbar: MaterialToolbar

    private lateinit var tvSummary: TextView
    private lateinit var tilSendEmail: TextInputLayout
    private lateinit var etSendEmail: TextInputEditText
    private lateinit var btnBuy: MaterialButton

    private lateinit var event: Event
    private lateinit var seats: ArrayList<String>
    private var total: Double = 0.0

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate(): PurchaseConfirmActivity")

        setContentView(R.layout.activity_purchase_confirm)

        session = SessionManager(this)

        // 1) Views lehenengo (portaera originala)
        toolbar = findViewById(R.id.toolbar)
        tvSummary = findViewById(R.id.tvSummary)
        tilSendEmail = findViewById(R.id.tilSendEmail)
        etSendEmail = findViewById(R.id.etSendEmail)
        btnBuy = findViewById(R.id.btnBuy)

        applyInsetsToToolbar(toolbar)
        toolbar.setNavigationOnClickListener {
            Log.d(TAG, "Toolbar back -> finish()")
            finish()
        }

        // 2) Extras
        val e = intent.getSerializableExtra(EventDetailActivity.EXTRA_EVENT) as? Event
        if (e == null) {
            Log.e(TAG, "EXTRA_EVENT null -> finish()")
            finish()
            return
        }

        event = e
        seats = intent.getStringArrayListExtra(EventDetailActivity.EXTRA_SEATS) ?: arrayListOf()
        total = intent.getDoubleExtra(EventDetailActivity.EXTRA_TOTAL, 0.0)

        Log.d(TAG, "Extras: eventId=${event.id} seats=${seats.size} total=$total")

        // 3) Controller
        controller = PurchaseConfirmController(
            view = this,
            scope = lifecycleScope,
            session = session,
            repo = PurchaseRepository(PurchaseTcpService()),
            summary = ""
        )
        controller.onCreate(event, seats, total)

        // 4) Listener-ak
        etSendEmail.addTextChangedListener {
            val text = it?.toString().orEmpty()
            Log.v(TAG, "Email changed: len=${text.trim().length}")
            controller.onEmailChanged(text)
        }

        btnBuy.setOnClickListener {
            Log.d(TAG, "btnBuy klik -> onBuyClicked()")
            controller.onBuyClicked(event, seats, etSendEmail.text?.toString().orEmpty())
        }
    }

    private fun applyInsetsToToolbar(toolbar: MaterialToolbar) {
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.updatePadding(top = top)
            insets
        }
    }

    // ---- PurchaseConfirmController.View ----

    override fun render(summaryText: String, buttonText: String) {
        Log.d(TAG, "render(): buttonText='$buttonText'")
        tvSummary.text = summaryText
        btnBuy.text = buttonText
    }

    override fun setEmailPrefill(email: String) {
        Log.d(TAG, "setEmailPrefill(): len=${email.length}")
        etSendEmail.setText(email)
        etSendEmail.setSelection(email.length)
    }

    override fun showEmailError(msg: String?) {
        Log.d(TAG, "showEmailError(): ${msg ?: "null"}")
        tilSendEmail.error = msg
    }

    override fun setBuyEnabled(enabled: Boolean) {
        Log.d(TAG, "setBuyEnabled(): $enabled")
        btnBuy.isEnabled = enabled
    }

    override fun setLoading(loading: Boolean) {
        Log.d(TAG, "setLoading(): $loading")
        btnBuy.isEnabled = !loading
        btnBuy.text = if (loading) "..." else btnBuy.text
    }

    override fun goTo(intent: Intent, finish: Boolean) {
        Log.i(TAG, "goTo(): ${intent.component?.className} finish=$finish")
        startActivity(intent)
        if (finish) finish()
    }

    override fun context(): Context = this

    override fun showMessage(msg: String) {
        Log.d(TAG, "showMessage(): $msg")
        android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_SHORT).show()
    }
}