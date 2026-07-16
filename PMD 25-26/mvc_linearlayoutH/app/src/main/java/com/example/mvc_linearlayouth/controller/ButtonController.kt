package com.example.mvc_linearlayouth.controller

import android.content.Context
import android.widget.Toast
import com.example.mvc_linearlayouth.model.ButtonModel

// CONTROLLER: logika eta komunikazioaren arduraduna (View ↔ Model)
class ButtonController(private val view: ButtonView, private val context: Context) {

    private val model = ButtonModel()

    // View-k inplementatu behar duen interfazea
    interface ButtonView {
        fun updateLastPressed(text: String)
        fun clearDisplay()
    }

    // Botoia sakatzean
    fun onButtonClicked(text: String) {
        model.lastPressed = text
        view.updateLastPressed("Azken botoia: $text")
        Toast.makeText(context, "$text sakatu duzu!", Toast.LENGTH_SHORT).show()
    }

    // Garbitu botoia sakatzean
    fun onClearClicked() {
        model.lastPressed = ""
        view.clearDisplay()
        Toast.makeText(context, "Garbituta!", Toast.LENGTH_SHORT).show()
    }
}