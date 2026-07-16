package com.example.erronka_proba.model.domain

/**
 * Fitxategia: Ticket.kt
 *
 * Zertarako da?
 * - Ticket baten domain eredua (oinarrizkoa).
 *
 * Zer egiten du?
 * - Ticket-aren datu laburrak gordetzen ditu:
 *   - id, eventId
 *   - title (ekitaldiaren izena)
 *   - room (aretoa)
 *   - count (kopurua)
 *
 * Nola erabiltzen da?
 * - Pantaila batzuetan edo repo/mapperretan erabil daiteke (zure proiektuaren arabera).
 */
data class Ticket(
    val id: Int,
    val eventId: Int,
    val title: String,
    val room: String,
    val count: Int
)