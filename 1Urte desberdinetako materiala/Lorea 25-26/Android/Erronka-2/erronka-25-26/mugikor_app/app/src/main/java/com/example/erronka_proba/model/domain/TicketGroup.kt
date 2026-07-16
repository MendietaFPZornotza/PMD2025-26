package com.example.erronka_proba.model.domain

/**
 * Fitxategia: TicketGroup.kt
 *
 * Zertarako da?
 * - "Nire sarrerak" pantailan agertzen den ticket-talde bat modelatzeko.
 *
 * Zer egiten du?
 * - Erosketa/deskarga kode beraren azpian multzokatzen den informazioa gordetzen du:
 *   - downloadCode: deskargarako kodea
 *   - eventId + ekitaldiaren datuak (title/date/time/room)
 *   - count: zenbat sarrera dauden multzo horretan
 *   - boughtAt: erosketa data/ordua (string gisa)
 *
 * Nola erabiltzen da?
 * - TicketsTcpService.myTickets() parse-etik sortzen da.
 * - MyTicketsAdapter-ek erakusten du.
 */
data class TicketGroup(
    val downloadCode: String,
    val eventId: Int,
    val title: String,
    val date: String,     // eDate
    val time: String,     // eTime
    val room: String,
    val count: Int,
    val boughtAt: String  // erosketa data
)