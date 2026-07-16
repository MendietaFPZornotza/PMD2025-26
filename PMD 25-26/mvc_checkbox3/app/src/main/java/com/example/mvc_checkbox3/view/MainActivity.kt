package com.example.mvc_checkbox3.view

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
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
import com.example.mvc_checkbox3.R
import com.example.mvc_checkbox3.controller.FoodController
import com.example.mvc_checkbox3.ui.theme.Mvc_checkbox3Theme

class MainActivity : ComponentActivity(), FoodController.FoodView {

    private lateinit var checkPizza: CheckBox
    private lateinit var checkSushi: CheckBox
    private lateinit var checkPasta: CheckBox
    private lateinit var checkEnsalada: CheckBox
    private lateinit var buttonCalculate: Button
    private lateinit var textResult: TextView

    private lateinit var controller: FoodController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPizza = findViewById(R.id.checkPizza)
        checkSushi = findViewById(R.id.checkSushi)
        checkPasta = findViewById(R.id.checkPasta)
        checkEnsalada = findViewById(R.id.checkEnsalada)
        buttonCalculate = findViewById(R.id.buttonCalculate)
        textResult = findViewById(R.id.textResult)


        controller = FoodController(this)

        buttonCalculate.setOnClickListener {
            controller.onCalculateClicked()
        }
    }

    override fun getSelectedNames(): List<String> {
        val lista = mutableListOf<String>()
        if (checkPizza.isChecked) lista.add("Pizza")
        if (checkSushi.isChecked) lista.add("Sushi")
        if (checkPasta.isChecked) lista.add("Pasta")
        if (checkEnsalada.isChecked) lista.add("Ensalada")
        return lista
    }

    override fun showResult(text: String) {
        textResult.text = text
    }
}