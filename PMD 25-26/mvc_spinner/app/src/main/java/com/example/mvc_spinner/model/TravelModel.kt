package com.example.mvc_spinner.model

// Eredua: bidaien datuak eta kostuaren kalkulua gordetzen ditu
class TravelModel {

    // Helmuga bakoitzaren oinarrizko kostua
    private val baseCosts = mapOf(
        "Paris" to 850.0,
        "Tokio" to 1200.0,
        "New York" to 950.0,
        "Londres" to 780.0,
        "Roma" to 670.0
    )

    // Hegazkin txartelaren gehigarria
    private val flightExtra = 300.0

    // Kostua kalkulatzeko metodoa
    fun calculateCost(destination: String, includeFlight: Boolean): Double {
        val base = baseCosts[destination] ?: 0.0
        return if (includeFlight) base + flightExtra else base
    }
}