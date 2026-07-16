package com.example.erronka_proba.model.domain

import java.io.Serializable

/**
 * Fitxategia: Event.kt
 *
 * Zertarako da?
 * - Ekitaldi baten “domain” eredua (aplikazioaren logikan erabiltzen den objektu nagusietakoa).
 *
 * Zer egiten du?
 * - Ekitaldiaren datu guztiak biltzen ditu:
 *   - identifikazioa (id), izena (title)
 *   - irudi baliabidea (imageRes)
 *   - sinopsia, generoa, aretoa
 *   - data/ordua
 *   - prezioa tiketa bakoitzeko (pricePerTicket)
 *   - aforoa
 *   - takenSeats (aukerakoa): okupatutako eserlekuen kodeak
 *
 * Nola erabiltzen da?
 * - Intent extras bidez pasatzen da Activity artean (Serializable delako).
 * - EventDetail / Purchase flow-an asko erabiltzen da.
 */
data class Event(
    val id: Int,
    val title: String,
    val imageRes: Int,
    val synopsis: String,
    val genre: String,
    val room: String,
    val date: String,
    val time: String,
    val pricePerTicket: Double,
    val aforo: Int,
    val takenSeats: List<String> = emptyList(),
    val imagePath: String?
) : java.io.Serializable