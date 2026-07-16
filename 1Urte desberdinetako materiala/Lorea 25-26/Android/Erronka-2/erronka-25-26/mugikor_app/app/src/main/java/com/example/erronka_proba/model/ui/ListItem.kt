package com.example.erronka_proba.model.ui

/**
 * Fitxategia: ListItem.kt
 *
 * Zertarako da?
 * - Menu zerrendetan (Pelikulak/Antzerkiak/Kontzertuak) erakusten den item-a modelatzeko (UI eredua).
 *
 * Zer egiten du?
 * - Ekitaldiaren datu laburrak gordetzen ditu zerrendan erakusteko:
 *   - iconRes, title, room, date, time, price, genre
 * - synopsis eta aforo ere gordetzen ditu (detail-era pasatzeko erabil daiteke).
 *
 * Nola erabiltzen da?
 * - EventTcpService -> mapper (toListItem) -> ListItem
 * - ListAdapter-ek erakusten du.
 */
data class ListItem(
    val id: Int,
    val iconRes: Int,
    val title: String,
    val room: String,
    val date: String,
    val time: String,
    val price: String,
    val genre: String,
    val synopsis: String = "",
    val aforo: Int,
    val imagePath: String? = null
)