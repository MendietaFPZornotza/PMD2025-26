package com.example.mvc_tablelayout.controller

import android.content.Context
import android.widget.Toast
import com.example.mvc_tablelayout.model.ButtonModel

// CONTROLLER: View eta Model arteko komunikazioa eta logika kudeatzen du
class ButtonController(private val view: ButtonView, private val context: Context) {

    private val model = ButtonModel()

    // View-k inplementatu behar duen interfazea
    interface ButtonView {
        fun updateLastPressed(text: String)
    }

    // Botoi bat sakatzean
    fun onButtonClicked(text: String) {
        model.lastPressed = text
        view.updateLastPressed("Azken botoia: $text")
        Toast.makeText(context, "$text botoia sakatu duzu!", Toast.LENGTH_SHORT).show()
    }
}