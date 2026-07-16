package com.example.erronka_proba.model.mapper

import com.example.erronka_proba.R
import com.example.erronka_proba.model.domain.Event
import com.example.erronka_proba.model.domain.EventCategory
import com.example.erronka_proba.data.tcp.EventNet
import com.example.erronka_proba.model.ui.HomeItem
import com.example.erronka_proba.model.ui.ListItem

/**
 * Decide iconos por categoría (tú lo tienes con drawables).
 */
fun iconFor(category: EventCategory): Int = when (category) {
    EventCategory.MOVIES -> R.drawable.ic_movie
    EventCategory.THEATRE -> R.drawable.ic_theatre
    EventCategory.CONCERTS -> R.drawable.ic_mic
}

fun EventNet.toListItem(category: EventCategory): ListItem =
    ListItem(
        id = id,
        iconRes = iconFor(category),
        title = title,
        room = room,
        date = date,
        time = time,
        price = "${price}€",
        genre = genre,
        synopsis = synopsis,
        aforo = aforo,
        imagePath = imagePath
    )

fun EventNet.toHomeCard(category: EventCategory): HomeItem.EventCard =
    HomeItem.EventCard(
        id = id,
        iconRes = iconFor(category),
        imageRes = iconFor(category),
        title = title,
        dateTime = "$date · $time",
        price = "${price}€",
        genre = genre,
        room = room,
        synopsis = synopsis,
        aforo = aforo,
        imagePath = imagePath
    )

fun EventNet.toDomainEvent(category: EventCategory): Event =
    Event(
        id = id,
        title = title,
        imageRes = iconFor(category),
        synopsis = synopsis,
        genre = genre,
        room = room,
        date = date,
        time = time,
        pricePerTicket = price,
        aforo = aforo,
        imagePath = imagePath
    )
