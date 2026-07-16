package com.example.mvc_eserlekuak.controller


import com.example.mvc_eserlekuak.model.Antzokia
import com.example.mvc_eserlekuak.model.Eserlekua

// DTO bat View-rako
data class EserlekuaDisplay(
    val ilara: Int,
    val zutabea: Int,
    val okupatua: Boolean,
    val hautatua: Boolean
)

class EserlekuKontrolatzailea {

    private val antzokia: Antzokia = Antzokia(6, 8)

    // View-rentzat datuak prestatu
    fun getEserlekuakDisplay(): List<EserlekuaDisplay> {
        return antzokia.eserlekuak.map {
            EserlekuaDisplay(
                ilara = it.ilara,
                zutabea = it.zutabea,
                okupatua = it.okupatua,
                hautatua = it.hautatua
            )
        }
    }

    // View klik bat bidali duenean
    fun onEserlekuaClicked(index: Int) {
        val eserlekua: Eserlekua = antzokia.eserlekuak[index]
        if (!eserlekua.okupatua) {
            eserlekua.hautatua = !eserlekua.hautatua
        }
    }
}