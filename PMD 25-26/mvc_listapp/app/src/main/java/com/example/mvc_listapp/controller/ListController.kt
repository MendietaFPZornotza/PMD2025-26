package com.example.mvc_listapp.controller

import com.example.mvc_listapp.model.ItemModel

class ListController(
    private val model: ItemModel,
    private val view: ListView
) : ItemModel.Listener {

    interface ListView {
        fun showList(items: List<String>)
        fun showMessage(msg: String)
    }

    init {
        model.addListener(this)
        view.showList(model.getItems())
    }

    fun onAddClicked(text: String) {
        if (text.isBlank()) {
            view.showMessage("Ezin da elementu hutsik gehitu.")
        } else {
            model.addItem(text)
            view.showMessage("Elementua gehituta!")
        }
    }

    fun onClearClicked() {
        model.clearItems()
        view.showMessage("Zerrenda garbituta.")
    }

    override fun onListChanged(newList: List<String>) {
        view.showList(newList)
    }

    fun onDestroy() {
        model.removeListener(this)
    }
}