package com.example.mvc_layouts.model

/**
* Erabiltzaileen datuak eta kudeaketa (Model)
*/
class UserModel {
   /**
    * Erabiltzaile baten datuak adierazten dituen klasea
    */
    data class Erabiltzailea(
        val izena: String,
        val abizena: String,
        val adina: Int
    )
    // Erabiltzaileen zerrenda
    // Erabiltzaileen zerrenda
    private val erabiltzaileak = mutableListOf<Erabiltzailea>()

    /**
     * Erabiltzaile berria gehitu zerrendara
     */
    fun gehituErabiltzailea(izena: String, abizena: String, adina: Int) {
        erabiltzaileak.add(Erabiltzailea(izena, abizena, adina))
    }

    /**
     * Erabiltzaile guztiak bueltatu
     */
    fun lortuErabiltzaileak(): List<Erabiltzailea> {
        return erabiltzaileak
    }
}