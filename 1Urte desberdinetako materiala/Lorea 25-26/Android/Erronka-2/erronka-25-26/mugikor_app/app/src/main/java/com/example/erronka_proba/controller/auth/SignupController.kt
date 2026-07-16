package com.example.erronka_proba.controller.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.erronka_proba.data.repo.LastLoginRepository
import com.example.erronka_proba.data.tcp.AuthTcpService
import com.example.erronka_proba.data.tcp.SessionManager
import com.example.erronka_proba.view.home.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Fitxategia: SignupController.kt
 *
 * Zertarako da?
 * - Signup (kontua sortu) pantailako logika kudeatzeko.
 *
 * Zer egiten du?
 * - Erabiltzaileak "Signup" sakatzean:
 *   1) Datuak irakurri (izena/email/pasahitzak)
 *   2) Balidazioak (hutsik ez, pasahitzak berdin, email formatua, luzera)
 *   3) TCP bidez signup egin (AuthTcpService)
 *   4) Erantzuna interpretatu:
 *      - SIGNUP_OK -> Session gorde + azken login-a (Room) gorde + MainActivity-ra
 *      - SIGNUP_EXISTS / SIGNUP_FAIL / bestela -> mezua
 *
 * Nola erabiltzen da?
 * - scope.launch erabiltzen du sare/TCP deia egiteko.
 * - View interfazeak UI ekintzak eta datuak hornitzen ditu.
 *
 * Log-ak:
 * - Balidazioak, TCP erantzuna eta flow-a kontsolan jarraitzeko.
 */
class SignupController(
    private val view: View,
    private val scope: CoroutineScope,
    private val auth: AuthTcpService,
    private val session: SessionManager,
    private val lastLoginRepo: LastLoginRepository = LastLoginRepository(view.context())
) {

    companion object {
        private const val TAG = "SignupController"
    }

    interface View {
        fun getName(): String
        fun getEmail(): String
        fun getPass1(): String
        fun getPass2(): String

        fun setLoading(loading: Boolean)
        fun showMessage(msg: String)
        fun goTo(intent: Intent, finish: Boolean = false)

        fun context(): Context
    }

    /**
     * Signup botoia sakatzean deitzen da.
     * - Balidazioak egin eta ondoren coroutine batean TCP signup deia.
     */
    fun onSignupClicked() {
        Log.i(TAG, "onSignupClicked(): signup prozesua hasi")

        val name = view.getName().trim()
        val email = view.getEmail().trim()
        val p1 = view.getPass1()
        val p2 = view.getPass2()

        Log.d(TAG, "Datuak: nameBlank=${name.isBlank()}, emailBlank=${email.isBlank()}, p1Blank=${p1.isBlank()}, p2Blank=${p2.isBlank()}")

        // 1) Balidazioak (portaera originala)
        if (name.isBlank() || email.isBlank() || p1.isBlank() || p2.isBlank()) {
            Log.w(TAG, "Balidazioa huts: datu guztiak ez daude")
            view.showMessage("Datu guztiak bete")
            return
        }

        if (p1 != p2) {
            Log.w(TAG, "Balidazioa huts: pasahitzak ez dira berdinak")
            view.showMessage("Pasahitzak ez dira berdinak")
            return
        }

        if (!isValidEmail(email)) {
            Log.w(TAG, "Balidazioa huts: email formatu okerra -> $email")
            view.showMessage("Email formatua ez da zuzena (_@_._)")
            return
        }

        if (!isValidPass(p1)) {
            Log.w(TAG, "Balidazioa huts: pasahitza laburregia (>=8 behar)")
            view.showMessage("Pasahitza 8 karakterekoa izan behar da")
            return
        }

        view.setLoading(true)
        Log.d(TAG, "TCP signup deia prestatzen")

        scope.launch {
            // Zure kodean existitzen da, nahiz eta gero ez erabili: mantentzen dut (funtzionalitatea ez ukitzeko).
            val lastUserId = lastLoginRepo.getLastUserId()
            Log.v(TAG, "Room lastUserId (ez da erabiltzen gero): $lastUserId")

            val res = auth.signup(name, email, p1)

            view.setLoading(false)

            res.fold(
                onSuccess = { line ->
                    Log.i(TAG, "TCP success: line='$line'")

                    val parts = line.split(";")
                    val code = parts.getOrNull(0) ?: ""

                    when (code) {
                        "SIGNUP_OK" -> {
                            Log.i(TAG, "SIGNUP_OK jaso da")

                            // Zure komentarioa mantenduta: protokoloaren arabera
                            // SIGNUP_OK;id;name;email
                            val serverId = parts.getOrNull(1)?.toLongOrNull() ?: 0L
                            val serverName = parts.getOrNull(2)?.ifBlank { name } ?: name
                            val serverEmail = parts.getOrNull(3)?.ifBlank { email } ?: email

                            Log.d(TAG, "Server datuak: id=$serverId name=$serverName email=$serverEmail")

                            // Saioa gorde
                            session.saveUser(serverId, serverName, serverEmail)

                            // Azken login-a (Room) corrutina barruan
                            lastLoginRepo.saveLastLogin(serverId, serverEmail)

                            // Home-ra
                            view.goTo(Intent(view.context(), MainActivity::class.java), finish = true)
                        }

                        "SIGNUP_EXISTS" -> {
                            Log.w(TAG, "SIGNUP_EXISTS: email existitzen da")
                            view.showMessage("Email hori jada existitzen da")
                        }

                        "SIGNUP_FAIL" -> {
                            Log.w(TAG, "SIGNUP_FAIL: ezin izan da kontua sortu")
                            view.showMessage("Ezin izan da kontua sortu")
                        }

                        else -> {
                            Log.w(TAG, "Erantzun ezezaguna: $line")
                            view.showMessage("Erantzun ezezaguna: $line")
                        }
                    }
                },
                onFailure = { e ->
                    Log.e(TAG, "TCP failure: ${e.message}", e)
                    view.showMessage("TCP errorea: ${e.message ?: "ezezaguna"}")
                }
            )
        }
    }

    /**
     * Email balidazioa (regex sinplea).
     */
    private fun isValidEmail(email: String): Boolean {
        val regex = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
        return regex.matches(email)
    }

    /**
     * Pasahitzaren gutxieneko baldintza:
     * - 8 karaktere edo gehiago
     */
    private fun isValidPass(pass: String): Boolean = pass.length >= 8
}