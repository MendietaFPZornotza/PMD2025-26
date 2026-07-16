package com.example.mvc_jarduera1.controller

import com.example.mvc_jarduera1.model.MessageModel

// Controller-ak View eta Model artean komunikazioa kudeatzen du
class MessageController(private val view: MessageView) {

    // Model instantzia hemen sortzen da (View-ak ez du zuzenean ezagutzen)
    private val model = MessageModel()

    // View interfazeak zein ekintza egin behar dituen definitzen du
    interface MessageView {
        fun showMessage(message: String) // Pantailan mezua erakutsi
    }

    // Hasierako mezua erakutsi (aplikazioa abiatzean)
    fun initialize() {
        view.showMessage(model.getMessage())
    }

    // Botoia sakatzean: mezua aldatu eta eguneratu
    fun onButtonClicked() {
        model.toggleMessage()
        view.showMessage(model.getMessage())
    }
}