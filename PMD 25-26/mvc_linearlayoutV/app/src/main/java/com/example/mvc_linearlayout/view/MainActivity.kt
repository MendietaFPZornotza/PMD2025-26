package com.example.mvc_linearlayout.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
import com.example.mvc_linearlayout.controller.CalculatorController
import com.example.mvc_linearlayout.ui.theme.Mvc_linearlayoutTheme
import com.example.mvc_linearlayout.R
class MainActivity : ComponentActivity(), CalculatorController.CalculatorView {

    private lateinit var controller: CalculatorController
    private lateinit var editA: EditText
    private lateinit var editB: EditText
    private lateinit var textResult: TextView
    private lateinit var buttonAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editA = findViewById(R.id.editA)
        editB = findViewById(R.id.editB)
        textResult = findViewById(R.id.textResult)
        buttonAdd = findViewById(R.id.buttonAdd)

        controller = CalculatorController(this)

        buttonAdd.setOnClickListener {
            controller.onAddClicked(editA.text.toString(), editB.text.toString())
        }
    }

    override fun showResult(result: Double) {
        textResult.text = "Emaitza: $result"
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}