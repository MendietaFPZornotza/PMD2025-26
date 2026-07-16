package com.example.erronka_proba.model.domain

import java.io.Serializable

/**
 * Fitxategia: User.kt
 *
 * Zertarako da?
 * - Erabiltzailearen domain eredua.
 *
 * Zer egiten du?
 * - User baten oinarrizko datuak gordetzen ditu:
 *   - id, name, email
 *   - password (aukerakoa; lehenetsia "")
 *
 * Nola erabiltzen da?
 * - ProfileTcpService-k User bihurtzen du (getProfile/updateProfile).
 * - Serializable da, beraz Intent bidez ere pasa daiteke (behar izanez gero).
 *
 * Oharra:
 * - Password eremua existitzen da, baina normalean ez da gomendagarria plain text moduan eramatea.
 *   Hala ere, hemen ez dugu ezer aldatzen.
 */
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val password: String = ""
) : Serializable