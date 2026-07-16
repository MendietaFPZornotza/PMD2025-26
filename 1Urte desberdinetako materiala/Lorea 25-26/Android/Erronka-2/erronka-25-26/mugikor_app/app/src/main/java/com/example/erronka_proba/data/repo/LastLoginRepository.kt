package com.example.erronka_proba.data.repo

import android.content.Context
import android.util.Log
import com.example.erronka_proba.data.AppDatabase
import com.example.erronka_proba.data.entity.LastLoginEntity

/**
 * Fitxategia: LastLoginRepository.kt
 *
 * Zertarako da?
 * - Room DB-an azken login-aren informazioa kudeatzeko (userId + email).
 *
 * Zer egiten du?
 * - getLastUserId(): azken erabiltzailearen ID-a itzultzen du (ez badago, 0L).
 * - getLastEmail(): azken email-a itzultzen du (ez badago, "" ).
 * - saveLastLogin(): id=1 erregistro bakarra eguneratzen du.
 * - clear(): taula hustu.
 *
 * Nola erabiltzen da?
 * - Login/Signup ondoren saveLastLogin() deitzen da.
 * - Aplikazioa irekitzean, getLastEmail() erabil daiteke email-a aurre-betzeko.
 *
 * Log-ak:
 * - DB irakurri/idatzi pausuak jarraitzeko.
 */
class LastLoginRepository(ctx: Context) {

    companion object {
        private const val TAG = "LastLoginRepository"
        private const val FIXED_ROW_ID = 1
    }

    private val dao = AppDatabase.get(ctx).lastLoginDao()

    /**
     * Azken erabiltzailearen ID-a itzultzen du.
     * - null bada, 0L (portaera originala).
     */
    suspend fun getLastUserId(): Long {
        val id = dao.getUserId() ?: 0L
        Log.d(TAG, "getLastUserId(): $id")
        return id
    }

    /**
     * Azken email-a itzultzen du.
     * - null bada, "" (portaera originala).
     */
    suspend fun getLastEmail(): String {
        val email = dao.getEmail().orEmpty()
        Log.d(TAG, "getLastEmail(): len=${email.length}")
        return email
    }

    /**
     * Azken login-a gordetzen du (id=1 erregistro finkoa).
     * Portaera originala: beti id=1.
     */
    suspend fun saveLastLogin(userId: Long, email: String) {
        Log.i(TAG, "saveLastLogin(): userId=$userId emailLen=${email.length}")

        dao.save(
            LastLoginEntity(
                id = FIXED_ROW_ID,
                userId = userId,
                email = email
            )
        )
    }

    /**
     * Last login taula hustu.
     */
    suspend fun clear() {
        Log.w(TAG, "clear(): last_login taula garbitzen")
        dao.clear()
    }
}