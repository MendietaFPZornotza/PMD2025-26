package com.example.mvc_menu.controller

import android.content.Context
import android.view.Menu
import android.widget.Toast
import com.example.mvc_menu.model.MenuModel
import com.example.mvc_menu.R

/**
 * Controller-a: Menuaren egitura sortu eta ekintzak kudeatzen ditu.
 * View-k menuaren inflazioa edo aukeraketa pasatzen dio controller-era, baina ez du MenuModel-en
 * egiturara egiten erreferentzia zuzena (ez du inportatzen).
 */
class MenuController(private val context: Context) {

    // Menu aukerak definitzen ditu modu pribatuan
    private fun createMenuList(): List<MenuModel> {
        return listOf(
            MenuModel(R.id.ongietorri, "Ongi etorri"),
            MenuModel(R.id.garbitu, "Garbitu"),
            MenuModel(R.id.irten, "Irten")
        )
    }

    /**
     * Menu objektua betetzen du MenuModel zerrendaren arabera.
     * Horrela View-k ez du MenuModel-era edo menu datuetara egiten erreferentziarik.
     */
    fun populateMenu(menu: Menu) {
        val items = createMenuList()
        menu.clear()
        items.forEach { model ->
            menu.add(Menu.NONE, model.id, Menu.NONE, model.izena)
        }
    }

    /**
     * Menu item bat aukeratu denean dei daitekeen metodoa.
     * onClear: View-k pasatzen duen funtzioa testua garbitzeko.
     * onExit: View-k pasatzen duen funtzioa aplikazioa ixteko.
     */
    fun handleMenuAction(itemId: Int, onClear: () -> Unit, onExit: () -> Unit) {
        when (itemId) {
            R.id.ongietorri -> {
                // Toast mezu bat erakutsi
                Toast.makeText(context, "Aplikaziora ongi etorri!", Toast.LENGTH_SHORT).show()
            }
            R.id.garbitu -> {
                // Clear callback exekutatu (View-k pasatzen du zer egin behar den)
                onClear()
            }
            R.id.irten -> {
                // Exit callback exekutatu (View-k pasatzen du aplikazioa itxi)
                onExit()
            }
            else -> {
                // Ezagutzen ez den id bat
            }
        }
    }
}