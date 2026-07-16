package com.example.erronka_proba.controller.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.erronka_proba.data.repo.AuthRepository
import com.example.erronka_proba.data.repo.LastLoginRepository
import com.example.erronka_proba.data.tcp.SessionManager
import com.example.erronka_proba.model.LoginResult
import com.example.erronka_proba.view.auth.SignupActivity
import com.example.erronka_proba.view.home.MainActivity

/**
 * Fitxategia: LoginController.kt
 *
 * Zertarako da?
 * - Login pantailako ekintzak kudeatzeko (saioa hasi, signup-era joan).
 *
 * Zer egiten du?
 * - Erabiltzaileak "Login" sakatzen duenean:
 *   1) Email/pass balidatu
 *   2) Repo-rekin login egin
 *   3) Ondo bada: SessionManager-en erabiltzailea gorde + azken login-a (Room) gorde
 *   4) MainActivity-ra nabigatu
 *
 * Nola erabiltzen da?
 * - View interfazeak Activity/Fragment-etik behar diren datuak eta ekintzak ematen ditu.
 * - onLoginClicked() suspend da: normalean view-ak coroutine batean deitzen ditu.
 *
 * Log-ak:
 * - Validazioak, repo erantzuna eta nabigazioa kontsolan jarraitzeko.
 */
class LoginController(
    private val view: View,
    private val lastLoginRepo: LastLoginRepository = LastLoginRepository(view.context())
) {

    companion object {
        private const val TAG = "LoginController"
    }

    private val repo = AuthRepository()

    interface View {
        fun getEmail(): String
        fun getPass(): String

        fun setLoading(isLoading: Boolean)
        fun showMessage(msg: String)

        fun goTo(intent: Intent, finish: Boolean = false)
        fun context(): Context
    }

    /**
     * "Login" botoia sakatzean deitzen da.
     * - suspend denez, view-ak coroutine barruan deitu behar du.
     */
    suspend fun onLoginClicked() {
        Log.i(TAG, "onLoginClicked(): login prozesua hasi")

        // Oharra: zure kodean 'id' lastLoginRepo-tik irakurtzen da eta gero saveLastLogin-era pasatzen da
        // (res.id erabiliz gordetzen du SessionManager-en, baina Room-en id zaharra gordetzen zuen).
        // Ez dut logika aldatzen: berdin mantentzen dut.
        val id = lastLoginRepo.getLastUserId()

        val email = view.getEmail().trim()
        val pass = view.getPass()

        Log.d(TAG, "Balidazioa: emailBlank=${email.isBlank()}, passBlank=${pass.isBlank()}")

        if (email.isBlank() || pass.isBlank()) {
            view.showMessage("Emaila eta pasahitza bete")
            Log.w(TAG, "Balidazioa huts: email edo pass hutsik")
            return
        }

        view.setLoading(true)
        Log.d(TAG, "Repo login(): email=$email")

        val res = repo.login(email, pass)

        view.setLoading(false)
        Log.d(TAG, "Repo login() amaituta: resultType=${res::class.java.simpleName}")

        when (res) {
            is LoginResult.Ok -> {
                Log.i(TAG, "Login OK: id=${res.id}, name=${res.name}")

                // Saioaren datuak gordetzen dira
                SessionManager(view.context()).saveUser(
                    id = res.id,
                    name = res.name,
                    email = res.email
                )

                // Azken login-a (Room)
                // Portaera originala: id lastLoginRepo-tik hartutakoa erabiltzen du.
                lastLoginRepo.saveLastLogin(id, email)

                // Home-ra nabigatu
                val i = Intent(view.context(), MainActivity::class.java)
                view.goTo(i, finish = true)
            }

            is LoginResult.Fail -> {
                Log.w(TAG, "Login FAIL: ${res.msg}")
                view.showMessage(res.msg)
            }

            is LoginResult.Error -> {
                Log.e(TAG, "Login ERROR: ${res.msg}")
                view.showMessage(res.msg)
            }
        }
    }

    /**
     * "Signup" botoia sakatzean: SignupActivity-ra.
     */
    fun onSignupClicked() {
        Log.i(TAG, "onSignupClicked(): SignupActivity-ra nabigatzen")
        val i = Intent(view.context(), SignupActivity::class.java)
        view.goTo(i, finish = false)
    }
}