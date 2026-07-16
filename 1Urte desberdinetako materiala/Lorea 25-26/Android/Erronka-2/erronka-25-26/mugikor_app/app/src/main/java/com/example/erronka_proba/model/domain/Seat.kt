package com.example.erronka_proba.model.domain

/**
 * Fitxategia: SeatCell.kt
 *
 * Zertarako da?
 * - Eserlekuen mapa (seat map) osatzeko “cell” motak definitzeko.
 *
 * Zer egiten du?
 * - Sealed class batek 3 aukera ematen ditu:
 *   - SeatItem(code, state): eserleku erreal bat
 *   - Aisle: pasabidea
 *   - Empty: hutsunea (azken fila betetzeko, aforoa ez bada 10-en multiploa)
 *
 * Nola erabiltzen da?
 * - EventDetailController.buildSeats() funtzioak SeatCell zerrenda sortzen du.
 * - SeatAdapter-ek viewType-a aukeratzen du (seat/aisle/empty) eta klik logika aplikatzen du.
 */
sealed class SeatCell {

    /**
     * Eserleku erreal bat: kodea (A1...) eta egoera.
     */
    data class SeatItem(val code: String, var state: State) : SeatCell()

    /**
     * Pasabidea.
     */
    data object Aisle : SeatCell()

    /**
     * Hutsunea: azken fila “rejilla perfektua” mantentzeko.
     */
    data object Empty : SeatCell()

    /**
     * Eserlekuaren egoerak:
     * - FREE: libre
     * - SELECTED: erabiltzaileak aukeratua
     * - OCCUPIED: okupatua (ezin da aukeratu)
     */
    enum class State { FREE, SELECTED, OCCUPIED }
}