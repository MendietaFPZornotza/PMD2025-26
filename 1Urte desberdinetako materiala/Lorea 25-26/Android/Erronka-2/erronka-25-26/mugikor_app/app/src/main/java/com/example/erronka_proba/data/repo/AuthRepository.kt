package com.example.erronka_proba.data.repo

import android.util.Log
import com.example.erronka_proba.data.tcp.TCPClient
import com.example.erronka_proba.model.LoginResult

/**
 * Fitxategia: AuthRepository.kt
 *
 * Zertarako da?
 * - Autentikazioarekin lotutako datu/konexio logika biltzeko (login + signup).
 *
 * Zer egiten du?
 * - TCPClient erabiliz zerbitzariarekin komunikatzen da.
 * - Zerbitzariaren erantzuna parseatzen du (";") eta LoginResult bihurtzen du.
 *
 * Nola erabiltzen da?
 * - Controller-ek repository honi deitzen dio:
 *   - login(email, pass)
 *   - signup(name, email, pass)
 *
 * Log-ak:
 * - Erantzun kodeak eta parse prozesua jarraitzeko.
 * - Kontuz: ez dugu pasahitza log-ean erakutsiko.
 */
class AuthRepository(
    private val client: TCPClient = TCPClient()
) {

    companion object {
        private const val TAG = "AuthRepository"
    }

    /**
     * Login egiten du TCP bidez.
     *
     * Protokoloa (portaera originala):
     * - "OK" edo "LOGIN_OK" -> LoginResult.Ok
     * - "FAIL" edo "LOGIN_FAIL" -> LoginResult.Fail (mezu finkoa)
     * - Bestela -> Fail (erantzun ezezaguna)
     * - Exception/Failure -> LoginResult.Error
     */
    suspend fun login(email: String, pass: String): LoginResult {
        Log.i(TAG, "login(): email='$email' (pass ez da log-ean agertzen)")

        val result = client.login(email, pass)

        return result.fold(
            onSuccess = { response ->
                Log.d(TAG, "login() response='$response'")

                val p = response.split(";")
                val code = p.getOrNull(0).orEmpty()

                when (code) {
                    "OK", "LOGIN_OK" -> {
                        val id = p.getOrNull(1)?.toLongOrNull() ?: 0L
                        val mail = p.getOrNull(2) ?: email
                        val name = p.getOrNull(3).orEmpty()

                        Log.i(TAG, "login() OK: id=$id email='$mail' nameLen=${name.length}")
                        LoginResult.Ok(id = id, name = name, email = mail)
                    }

                    "FAIL", "LOGIN_FAIL" -> {
                        Log.w(TAG, "login() FAIL: kredentzial okerrak")
                        LoginResult.Fail("Erabiltzaile edo pasahitza ez dira zuzenak")
                    }

                    else -> {
                        Log.w(TAG, "login() kode ezezaguna: '$code'")
                        LoginResult.Fail("Erantzun ezezaguna: $response")
                    }
                }
            },
            onFailure = { e ->
                Log.e(TAG, "login() failure: ${e.message}", e)
                LoginResult.Error("Konexio errorea: ${e.message ?: "ezezaguna"}")
            }
        )
    }

    /**
     * Signup (kontua sortu) egiten du TCP bidez.
     *
     * Protokoloa (portaera originala):
     * - "SIGNUP_OK" edo "OK" -> LoginResult.Ok
     * - "SIGNUP_FAIL" edo "FAIL" -> LoginResult.Fail (mezu finkoa)
     * - Bestela -> Fail (erantzun ezezaguna)
     * - Exception/Failure -> LoginResult.Error
     */
    suspend fun signup(name: String, email: String, pass: String): LoginResult {
        Log.i(TAG, "signup(): email='$email' nameLen=${name.length} (pass ez da log-ean agertzen)")

        val result = client.signup(name, email, pass)

        return result.fold(
            onSuccess = { response ->
                Log.d(TAG, "signup() response='$response'")

                val p = response.split(";")
                val code = p.getOrNull(0).orEmpty()

                when (code) {
                    "SIGNUP_OK", "OK" -> {
                        val id = p.getOrNull(1)?.toLongOrNull() ?: 0L
                        val rName = p.getOrNull(2) ?: name
                        val rEmail = p.getOrNull(3) ?: email

                        Log.i(TAG, "signup() OK: id=$id email='$rEmail' nameLen=${rName.length}")
                        LoginResult.Ok(id = id, name = rName, email = rEmail)
                    }

                    "SIGNUP_FAIL", "FAIL" -> {
                        Log.w(TAG, "signup() FAIL: ezin kontua sortu")
                        LoginResult.Fail("Ezin izan da kontua sortu")
                    }

                    else -> {
                        Log.w(TAG, "signup() kode ezezaguna: '$code'")
                        LoginResult.Fail("Erantzun ezezaguna: $response")
                    }
                }
            },
            onFailure = { e ->
                Log.e(TAG, "signup() failure: ${e.message}", e)
                LoginResult.Error("Konexio errorea: ${e.message ?: "ezezaguna"}")
            }
        )
    }
}