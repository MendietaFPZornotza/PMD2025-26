package com.example.mvc_listapp.model

class ItemModel {

    private val items = mutableListOf<String>()

    interface Listener {
        fun onListChanged(newList: List<String>)
    }

    private val listeners = mutableListOf<Listener>()

    fun addListener(l: Listener) {
        if (!listeners.contains(l)) listeners.add(l)
    }

    fun removeListener(l: Listener) {
        listeners.remove(l)
    }

    fun addItem(item: String) {
        items.add(item)
        notifyListeners()
    }

    fun clearItems() {
        items.clear()
        notifyListeners()
    }

    fun getItems(): List<String> = items

    private fun notifyListeners() {
        for (l in listeners) {
            l.onListChanged(items)
        }
    }
}