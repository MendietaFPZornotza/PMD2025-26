package com.example.erronka_proba.controller.purchase

import android.content.Intent
import android.util.Log
import com.example.erronka_proba.data.repo.PurchaseRepository
import com.example.erronka_proba.data.tcp.SessionManager
import com.example.erronka_proba.model.domain.Event
import com.example.erronka_proba.view.purchase.EventDetailActivity
import com.example.erronka_proba.view.purchase.TicketQrActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Fitxategia: PurchaseConfirmController.kt
 *
 * Zertarako da?
 * - Erosketa berrespen pantailako logika kudeatzeko: laburpena erakutsi, email-a balidatu eta erosketa egin.
 *
 * Zer egiten du?
 * - onCreate(event, seats, total):
 *   - Laburpena eraiki eta UI-n erakutsi
 *   - Session-etik email-a aurre-bete (badago) eta balidazioa egin
 * - onEmailChanged(email):
 *   - Email balidatu eta erosteko botoia gaitu/desgaitu
 * - onBuyClicked(event, seats, email):
 *   - UserId balidatu
 *   - Repo.buy() deitu
 *   - Success -> TicketQrActivity-ra (downloadCode + summary)
 *
 * Log-ak:
 * - Email balidazioa, userId, repo.buy() erantzuna eta nabigazioa jarraitzeko.
 */
class PurchaseConfirmController(
    private val view: View,
    private val scope: CoroutineScope,
    private val session: SessionManager,
    private val repo: PurchaseRepository,
    private val summary: String
) {

    companion object {
        private const val TAG = "PurchaseConfirmController"
    }

    /**
     * Email balidazioa Android-en Patterns erabiliz.
     */
    private fun isValidEmail(s: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(s.trim()).matches()

    interface View {
        fun render(summaryText: String, buttonText: String)

        fun setEmailPrefill(email: String)
        fun showEmailError(msg: String?)
        fun setBuyEnabled(enabled: Boolean)

        fun goTo(intent: Intent, finish: Boolean = false)
        fun context(): android.content.Context
        fun showMessage(msg: String)
        fun setLoading(loading: Boolean)
    }

    /**
     * Pantaila sortzean: laburpena erakutsi eta email-a prestatu.
     */
    fun onCreate(event: Event, seats: ArrayList<String>, total: Double) {
        val totalRounded = (total * 100.0).roundToInt() / 100.0

        Log.i(TAG, "onCreate(): eventId=${event.id}, seats=${seats.size}, total=$totalRounded")

        val summaryText =
            "Ekitaldia: ${event.title}\n" +
                    "Data: ${event.date} - ${event.time}\n" +
                    "Lekua: ${event.room}\n" +
                    "Eserlekua: ${seats.joinToString(", ")}\n\n" +
                    "Guztira: ${totalRounded}€"

        view.render(summaryText, "Erosi (${totalRounded}€)")

        val emailFromSession = session.getEmail().trim()
        Log.d(TAG, "Session email='${emailFromSession}'")

        if (emailFromSession.isNotBlank()) {
            view.setEmailPrefill(emailFromSession)
            onEmailChanged(emailFromSession)
        } else {
            Log.w(TAG, "Session email hutsik -> buy disabled")
            view.setBuyEnabled(false)
        }
    }

    /**
     * Email-a aldatzen denean: balidatu eta botoia eguneratu.
     */
    fun onEmailChanged(email: String) {
        val e = email.trim()
        Log.d(TAG, "onEmailChanged(): '$e'")

        when {
            e.isBlank() -> {
                view.showEmailError("Email-a derrigorrezkoa da")
                view.setBuyEnabled(false)
            }
            !isValidEmail(e) -> {
                view.showEmailError("Email formatua ez da zuzena")
                view.setBuyEnabled(false)
            }
            else -> {
                view.showEmailError(null)
                view.setBuyEnabled(true)
            }
        }
    }

    /**
     * Erosi botoia sakatzean:
     * - Email eta userId balidatu
     * - Repo.buy() exekutatu
     * - Success: TicketQrActivity-ra
     */
    fun onBuyClicked(event: Event, seats: ArrayList<String>, email: String) {
        val e = email.trim()
        Log.i(TAG, "onBuyClicked(): eventId=${event.id}, seats=${seats.size}, email='$e'")

        if (!isValidEmail(e)) {
            Log.w(TAG, "Email ez da baliozkoa -> berriz balidatzen")
            onEmailChanged(e)
            return
        }

        val userId = session.getUserId()
        Log.d(TAG, "userId=$userId")

        if (userId <= 0) {
            Log.e(TAG, "userId okerra (<=0)")
            view.showMessage("Erabiltzailea ez dago ondo (userId hutsik)")
            return
        }

        view.setLoading(true)
        view.setBuyEnabled(false)

        scope.launch {
            Log.d(TAG, "repo.buy(): userId=$userId eventId=${event.id} seats=${seats.size}")

            val res = repo.buy(
                userId = userId,
                eventId = event.id,
                email = e,
                seats = seats
            )

            view.setLoading(false)
            view.setBuyEnabled(true)

            res.fold(
                onSuccess = { downloadCode ->
                    Log.i(TAG, "buy() success: downloadCode='$downloadCode'")

                    val i = Intent(view.context(), TicketQrActivity::class.java).apply {
                        putExtra(EventDetailActivity.EXTRA_EVENT, event)
                        putExtra(TicketQrActivity.EXTRA_DOWNLOAD_CODE, downloadCode)
                        putExtra(TicketQrActivity.EXTRA_SUMMARY, summary) // parametro moduan zetorren summary
                    }

                    view.goTo(i, finish = true)
                },
                onFailure = { ex ->
                    Log.e(TAG, "buy() failure: ${ex.message}", ex)
                    view.showMessage("Erosketa huts egin du: ${ex.message ?: "errorea"}")
                }
            )
        }
    }
}