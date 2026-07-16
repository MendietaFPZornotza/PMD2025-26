package com.example.erronka_proba.view.purchase

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.erronka_proba.R
import com.example.erronka_proba.model.domain.Event
import com.google.android.material.button.MaterialButton

/**
 * Fitxategia: TicketQrActivity.kt
 *
 * Zertarako da?
 * - Erosketa ondoko pantaila: deskarga kodea erakutsi eta ekintza azkarrak eman.
 *
 * Zer egiten du?
 * - Extras-etik:
 *   - Event
 *   - download code
 *   - summary (aukerakoa)
 * - Kodea kopiatu clipboard-era.
 * - Home-ra bueltatu (CLEAR_TOP).
 * - NireProfila-ra joan (Nire sarrerak ikusteko).
 *
 * Log-ak:
 * - Lifecycle, extras, copy/home/mytickets klikak.
 */
class TicketQrActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "TicketQrActivity"
        const val EXTRA_DOWNLOAD_CODE = "EXTRA_DOWNLOAD_CODE"
        const val EXTRA_SUMMARY = "EXTRA_SUMMARY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate(): TicketQrActivity")

        setContentView(R.layout.activity_ticket_qr)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        val event = intent.getSerializableExtra(EventDetailActivity.EXTRA_EVENT) as Event
        val code = intent.getStringExtra(EXTRA_DOWNLOAD_CODE) ?: "-"
        val summary = intent.getStringExtra(EXTRA_SUMMARY).orEmpty()

        Log.d(TAG, "Extras: eventId=${event.id} codeLen=${code.length} summaryLen=${summary.length}")

        findViewById<TextView>(R.id.tvDownloadCode).text = code

        // Kodea kopiatu
        findViewById<MaterialButton>(R.id.btnCopy).setOnClickListener {
            Log.d(TAG, "btnCopy klik -> clipboard")

            val cm = getSystemService(ClipboardManager::class.java)
            cm.setPrimaryClip(ClipData.newPlainText("download_code", code))

            android.widget.Toast.makeText(this, "Kopiatuta!", android.widget.Toast.LENGTH_SHORT).show()
        }

        // Home-ra
        findViewById<MaterialButton>(R.id.btnHome).setOnClickListener {
            Log.d(TAG, "btnHome klik -> MainActivity (CLEAR_TOP)")

            startActivity(
                Intent(this, com.example.erronka_proba.view.home.MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }

        // Nire sarrerak / profila
        findViewById<MaterialButton>(R.id.btnMyTickets).setOnClickListener {
            Log.d(TAG, "btnMyTickets klik -> NireProfilaActivity")

            startActivity(
                Intent(this, com.example.erronka_proba.view.profile.NireProfilaActivity::class.java)
            )
            finish()
        }
    }
}