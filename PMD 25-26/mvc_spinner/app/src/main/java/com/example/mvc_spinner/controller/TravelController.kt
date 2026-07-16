package com.example.mvc_spinner.controller

import com.example.mvc_spinner.model.TravelModel
import java.text.DecimalFormat


// Controller: Model eta View arteko komunikazioa koordinatzen du
class TravelController(private val view: TravelView) {

    // Model instantzia hemen sortzen da
    private val model = TravelModel()

    // View-k inplementatu beharreko interfaze sinplea
    interface TravelView {
        fun showResult(message: String)
    }

    // Botoia sakatzean logika hemen kudeatzen da
    fun onCalculateClicked(destination: String, includeFlight: Boolean) {
        if (destination.isEmpty()) {
            //view.showResult("Mesedez, hautatu helmuga bat.")
            return
        }

        // Model-era bidali kalkulua egiteko
        val cost = model.calculateCost(destination, includeFlight)
        val format = DecimalFormat("#.##")
        val formattedCost = format.format(cost)

        // Mezua prestatu
        val message = if (includeFlight) {
            "Helmuga: $destination\nHegazkin txartelak barne.\nGuztira: €$formattedCost"
        } else {
            "Helmuga: $destination\nHegazkin txartelik gabe.\nGuztira: €$formattedCost"
        }

        // View-an emaitza erakutsi
        view.showResult(message)
    }
}