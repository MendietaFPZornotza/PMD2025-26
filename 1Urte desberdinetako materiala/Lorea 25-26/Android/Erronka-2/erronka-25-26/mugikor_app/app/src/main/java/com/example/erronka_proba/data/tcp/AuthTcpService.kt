package com.example.erronka_proba.data.tcp

import android.util.Log

/**
 * Fitxategia: AuthTcpService.kt
 *
 * Zertarako da?
 * - Autentikazioarekin lotutako TCP eskaerak bidaltzeko (login + signup).
 *
 * Zer egiten du?
 * - TCPClient-ari komando-lerroak bidaltzen dizkio:
 *   - LOGIN;<email>;<pass>
 *   - SIGNUP;<name>;<email>;<pass>
 *
 * Nola erabiltzen da?
 * - Repository-k (AuthRepository edo SignupController-eko auth) deitzen dio.
 * - Itzulera: Result<String> (zerbitzariaren erantzun lerroa edo errorea).
 *
 * Log-ak:
 * - Zein komando bidaltzen den ikusteko (pasahitza EZ da log-ean agertzen).
 */
class AuthTcpService(
    private val base: TCPClient = TCPClient()
) {

    companion object {
        private const val TAG = "AuthTcpService"
    }

    /**
     * Login eskaera bidaltzen du.
     * Kontuz: pasahitza ez dugu log-ean erakusten.
     */
    suspend fun login(email: String, pass: String): Result<String> {
        Log.d(TAG, "login(): email='$email' (pass ez da log-ean agertzen)")
        return base.sendLine("LOGIN;$email;$pass")
    }

    /**
     * Signup eskaera bidaltzen du.
     * Kontuz: pasahitza ez dugu log-ean erakusten.
     */
    suspend fun signup(name: String, email: String, pass: String): Result<String> {
        Log.d(TAG, "signup(): nameLen=${name.length} email='$email' (pass ez da log-ean agertzen)")
        return base.sendLine("SIGNUP;$name;$email;$pass")
    }
}