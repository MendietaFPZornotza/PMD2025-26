package com.example.mvc_listview.model

// Eredua: zerrendako elementuak gordetzen ditu
class ItemModel {

    // Datu-egitura: String-en zerrenda aldagarria
    private val items = mutableListOf<String>()

    // Elementu bat gehitu
    fun addItem(text: String) {
        items.add(text)
    }

    // Elementu bat kendu posizioaren arabera
    fun removeItem(position: Int) {
        if (position in items.indices) {
            items.removeAt(position)
        }
    }

    // Uneko elementu guztiak itzuli (irakurtzeko)
    fun getItems(): List<String> {
        return items
    }
}