package com.example.mvc_radiobutton.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mvc_radiobutton.controller.CalculatorController
import com.example.mvc_radiobutton.ui.theme.Mvc_radiobuttonTheme
import com.example.mvc_radiobutton.R
import com.example.mvc_radiobutton.model.CalculatorModel


// MainActivity View-a da, eta Controller-en CalculatorView interfazea inplementatzen du
class MainActivity : ComponentActivity(), CalculatorController.CalculatorView {

    private lateinit var controller: CalculatorController
    private lateinit var editNum1: EditText
    private lateinit var editNum2: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var buttonCalculate: Button
    private lateinit var textResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Layout-eko elementuak hartu
        editNum1 = findViewById(R.id.editTextNumber1)
        editNum2 = findViewById(R.id.editTextNumber2)
        radioGroup = findViewById(R.id.radioGroup)
        buttonCalculate = findViewById(R.id.buttonCalculate)
        textResult = findViewById(R.id.textViewResult)

        // Controller sortu
        controller = CalculatorController(this)

        // Botoia sakatzean: Controller-era bidali datuak
        buttonCalculate.setOnClickListener {
            val num1 = editNum1.text.toString()
            val num2 = editNum2.text.toString()

            // Hautatutako RadioButton-aren testua hartu
            val selectedId = radioGroup.checkedRadioButtonId
            val operation = if (selectedId != -1) {
                findViewById<RadioButton>(selectedId).text.toString()
            } else null

            // Controller-era bidali kalkulua egiteko
            controller.onCalculateClicked(num1, num2, operation)
        }
    }

    // Emaitza pantailan erakusteko metodoa
    override fun showResult(result: String) {
        textResult.text = result
    }

    // Errore mezua erakusteko metodoa
    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}