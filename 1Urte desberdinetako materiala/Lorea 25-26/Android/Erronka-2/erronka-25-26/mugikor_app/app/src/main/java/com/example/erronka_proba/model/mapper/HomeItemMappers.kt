package com.example.erronka_proba.model.mapper

import android.R
import com.example.erronka_proba.model.domain.Event
import com.example.erronka_proba.model.ui.HomeItem

/**
 * Convierte una card del HOME al Event de dominio.
 * No tiene categoría, así que usa imageRes que ya trae la card.
 */
fun HomeItem.EventCard.toDomainEvent(): Event {
    val parts = dateTime.split("·").map { it.trim() }
    val date = parts.getOrNull(0) ?: ""
    val time = parts.getOrNull(1) ?: ""

    val priceValue = price
        .replace("€", "")
        .trim()
        .toDoubleOrNull() ?: 0.0

    return Event(
        id = id,
        title = title,
        imageRes = imageRes,
        synopsis = synopsis,
        genre = genre,
        room = room,
        date = date,
        time = time,
        pricePerTicket = priceValue,
        aforo = aforo,
        imagePath = imagePath
    )
}
