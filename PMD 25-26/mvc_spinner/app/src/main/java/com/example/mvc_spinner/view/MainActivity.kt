package com.example.mvc_spinner.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.mvc_spinner.controller.TravelController
import com.example.mvc_spinner.model.TravelModel
import com.example.mvc_spinner.R

class MainActivity : ComponentActivity(), TravelController.TravelView {

    private lateinit var controller: TravelController
    private lateinit var spinnerDestinations: Spinner
    private lateinit var switchIncludeFlight: Switch
    private lateinit var buttonCalculate: Button
    private lateinit var textResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Layout elementuak hartu
        spinnerDestinations = findViewById(R.id.spinnerHelmuga)
        switchIncludeFlight = findViewById(R.id.switchFlight)
        buttonCalculate = findViewById(R.id.buttonCalculate)
        textResult = findViewById(R.id.textResult)

        // Controller instantziatu
        controller = TravelController(this)

        // Spinner-a datuekin bete
        val destinations = listOf("Paris", "Tokio", "New York", "Londres", "Roma")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, destinations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDestinations.adapter = adapter

        // Botoiaren ekintza: aukeratutako datuak Controller-era bidali
        buttonCalculate.setOnClickListener {
            val destination = spinnerDestinations.selectedItem.toString()
            val includeFlight = switchIncludeFlight.isChecked

            controller.onCalculateClicked(destination, includeFlight)
        }
    }

    // Interfazeko metodoa: emaitza erakusteko
    override fun showResult(message: String) {
        textResult.text = message
    }
}