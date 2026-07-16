package com.example.mvc_scrollview.controller

import android.content.Context
import android.widget.Toast
import com.example.mvc_scrollview.model.ScrollModel

// CONTROLLER: logika eta komunikazioaren arduraduna (View ↔ Model)
class ScrollController(private val view: ScrollViewLayer, private val context: Context) {

    private val model = ScrollModel()

    // View-k inplementatu behar duen interfazea
    interface ScrollViewLayer {
        fun updateLastPressed(text: String)
    }

    // Botoi bat sakatzean: Model eguneratu eta View berri
    fun onButtonClicked(text: String) {
        model.lastPressed = text
        view.updateLastPressed("Azken botoia: $text")
        Toast.makeText(context, "$text sakatu duzu!", Toast.LENGTH_SHORT).show()
    }
}