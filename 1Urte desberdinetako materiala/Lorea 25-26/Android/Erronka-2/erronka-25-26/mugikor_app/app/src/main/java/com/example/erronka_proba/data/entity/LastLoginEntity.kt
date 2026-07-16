package com.example.erronka_proba.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Fitxategia: LastLoginEntity.kt
 *
 * Zertarako da?
 * - Room datu-basean azken login-aren informazioa gordetzeko.
 *
 * Zer egiten du?
 * - "last_login" taulan erregistro bakarra mantentzen du (id=1 lehenetsia).
 * - Erabiltzailearen userId eta email-a gordetzen ditu.
 *
 * Nola erabiltzen da?
 * - Normalean LastLoginRepository/DAO-k id=1 erregistroa sortu/eguneratzen du.
 * - Helburua: aplikazioa berriro irekitzean azken erabiltzailearen email-a gogoratzea.
 */
@Entity(tableName = "last_login")
data class LastLoginEntity(
    /**
     * Erregistro bakarra izan dadin, id finkoa erabiltzen da (portaera originala).
     */
    @PrimaryKey val id: Int = 1,

    /**
     * Azken saioa hasi duen erabiltzailearen ID-a.
     */
    val userId: Long,

    /**
     * Azken saioa hasi duen erabiltzailearen email-a.
     */
    val email: String
)