package com.example.mvc_radiobutton.controller

import com.example.mvc_radiobutton.model.CalculatorModel

// Controller-ak Model eta View artean komunikazioa kudeatzen du
class CalculatorController(private val view: CalculatorView) {

    // Model instantzia sortzen da hemen
    private val model = CalculatorModel()

    // View interfazeak zein ekintza egin behar dituen zehazten du
    interface CalculatorView {
        fun showResult(result: String)        // Emaitza pantailan erakutsi
        fun showError(message: String)        // Errorea erakutsi (balio okerrak)
    }

    // Kalkulua egin erabiltzaileak botoia sakatzean
    fun onCalculateClicked(
        num1Text: String,
        num2Text: String,
        operation: String?
    ) {
        try {
            // Sartutako testuak zenbaki bihurtu
            val num1 = num1Text.toDouble()
            val num2 = num2Text.toDouble()

            // Zein eragiketa aukeratu den egiaztatu
            val result = when (operation) {
                "Batu" -> model.add(num1, num2)
                "Kendu" -> model.subtract(num1, num2)
                else -> {
                    view.showError("Hautatu eragiketa bat")
                    return
                }
            }

            // Emaitza erakutsi View-an
            view.showResult("Emaitza: $result")

        } catch (e: NumberFormatException) {
            // Erabiltzaileak ez badu zenbaki baliodunik sartu
            view.showError("Sartu zenbaki baliodunak")
        }
    }
}