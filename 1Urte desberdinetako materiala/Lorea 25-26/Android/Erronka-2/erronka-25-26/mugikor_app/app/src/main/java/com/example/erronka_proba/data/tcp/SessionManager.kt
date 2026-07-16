package com.example.erronka_proba.data.tcp

import android.content.Context
import android.util.Log

/**
 * Fitxategia: SessionManager.kt
 *
 * Zertarako da?
 * - Aplikazioaren “saio” datuak SharedPreferences-en gordetzeko (user_id, name, email).
 *
 * Zer egiten du?
 * - saveUser(): erabiltzailearen oinarrizko datuak gordetzen ditu.
 * - getUserId()/getName()/getEmail(): gordetako balioak irakurtzen ditu.
 * - clear(): saioa garbitzen du (logout-en tipikoa).
 *
 * Nola erabiltzen da?
 * - Login/Signup ondoren saveUser()
 * - App-eko beste pantaila/controller-etan getUserId/email/name
 *
 * Log-ak:
 * - Gordetze/garbitze ekintzak jarraitzeko.
 * - Kontuz: ez dugu datu sentikorrik gehiegi log-ean erakutsiko (email-a luzera bakarrik).
 */
class SessionManager(ctx: Context) {

    companion object {
        private const val TAG = "SessionManager"
        private const val SP_NAME = "session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
    }

    private val sp = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    /**
     * Erabiltzailearen datuak gordetzen ditu.
     */
    fun saveUser(id: Long, name: String, email: String) {
        Log.i(TAG, "saveUser(): id=$id nameLen=${name.length} emailLen=${email.length}")

        sp.edit()
            .putLong(KEY_USER_ID, id)
            .putString(KEY_NAME, name)
            .putString(KEY_EMAIL, email)
            .apply()
    }

    /**
     * Saioaren userId-a.
     */
    fun getUserId(): Long = sp.getLong(KEY_USER_ID, 0L)

    /**
     * Saioaren izena.
     */
    fun getName(): String = sp.getString(KEY_NAME, "") ?: ""

    /**
     * Saioaren email-a.
     */
    fun getEmail(): String = sp.getString(KEY_EMAIL, "") ?: ""

    /**
     * Saioa garbitu (logout).
     */
    fun clear() {
        Log.w(TAG, "clear(): SharedPreferences garbitzen")
        sp.edit().clear().apply()
    }
}