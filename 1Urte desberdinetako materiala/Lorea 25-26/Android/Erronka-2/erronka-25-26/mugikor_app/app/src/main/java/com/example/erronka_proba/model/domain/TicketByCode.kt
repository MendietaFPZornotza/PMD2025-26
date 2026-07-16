package com.example.erronka_proba.model.domain

/**
 * Fitxategia: TicketByCode.kt
 *
 * Zertarako da?
 * - Deskarga kode baten bidez lortutako ticket informazioa modelatzeko.
 *
 * Zer egiten du?
 * - Ticket baten xehetasunak gordetzen ditu:
 *   - eventId, title, date, time, room
 *   - seatCode (eserlekuaren kodea)
 *   - path (gero PDF-aren ruta/ftp ruta izango dena)
 *
 * Nola erabiltzen da?
 * - GET_TICKETS_BY_CODE bezalako erantzunak parseatu ondoren erabil daiteke.
 */
data class TicketByCode(
    val eventId: Int,
    val title: String,
    val date: String,
    val time: String,
    val room: String,
    val seatCode: String,
    val path: String // gero FTP/remote path-a
)