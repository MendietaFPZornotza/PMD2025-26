package com.example.mvc_jarduera1.model

// Eredua: testuaren egoera gordetzen du (“Kaixo Mundua” ala “Sakatu botoia”)
class MessageModel {

    // Egungo mezua gordetzeko aldagaia
    private var message = "Sakatu botoia"

    // Mezua lortzeko metodoa
    fun getMessage(): String {
        return message
    }

    // Mezua aldatu (“Kaixo Mundua” ↔ “Sakatu botoia”)
    fun toggleMessage() {
        if (message == "Sakatu botoia") {
            message = "Kaixo Mundua"
        } else {
            message = "Sakatu botoia"
        }
    }
}