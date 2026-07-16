package com.example.mvc_listview.controller

import com.example.mvc_listview.model.ItemModel

// Controller: Model eta View artean komunikazioa kudeatzen du
class ListController(private val view: ListView)   // View interfazea jasotzen du
 {

    // Modela hemen sortzen da (View-ak ez du zuzenean ukitzen)
    private val model = ItemModel()

    // View interfazeak zer egin behar duen definitzen du
    interface ListView {
        fun updateList(items: List<String>)   // Zerrenda eguneratzeko
        fun clearInput()                      // Testu-eremua garbitzeko
    }

    // Elementu bat gehitu eta View eguneratu
    fun addItem(text: String) {
        if (text.isNotBlank()) {
            model.addItem(text)
            view.updateList(model.getItems()) // Model-eko datuak erakutsi
            view.clearInput()
        }
    }

    // Elementu bat kendu eta View eguneratu
    fun removeItem(position: Int) {
        model.removeItem(position)
        view.updateList(model.getItems())
    }
}