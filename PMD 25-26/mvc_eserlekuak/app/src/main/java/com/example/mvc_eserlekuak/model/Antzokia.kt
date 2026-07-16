package com.example.mvc_eserlekuak.model

class Antzokia(ilaraKop: Int, zutabeKop: Int) {

    val eserlekuak: MutableList<Eserlekua> = mutableListOf()

    init {
        for (ilara in 0 until ilaraKop) {
            for (zutabea in 0 until zutabeKop) {
                val okupatua = Math.random() < 0.2
                eserlekuak.add(Eserlekua(ilara, zutabea, okupatua))
            }
        }
    }
}