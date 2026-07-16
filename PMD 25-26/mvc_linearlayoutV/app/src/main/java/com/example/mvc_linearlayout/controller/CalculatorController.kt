package com.example.mvc_linearlayout.controller

import com.example.mvc_linearlayout.model.CalculatorModel

class CalculatorController(private val view: CalculatorView) {

    private val model = CalculatorModel()

    interface CalculatorView {
        fun showResult(result: Double)
        fun showError(message: String)
    }

    fun onAddClicked(aText: String, bText: String) {
        try {
            val a = aText.toDouble()
            val b = bText.toDouble()
            val result = model.addNumbers(a, b)
            view.showResult(result)
        } catch (e: Exception) {
            view.showError("Zenbaki baliogabeak sartu dira!")
        }
    }
}