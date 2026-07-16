package com.example.mvc_linearlayouth.view

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
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
import com.example.mvc_linearlayouth.controller.ButtonController
import com.example.mvc_linearlayouth.ui.theme.Mvc_linearlayoutHTheme
import com.example.mvc_linearlayouth.R

class MainActivity : ComponentActivity(), ButtonController.ButtonView {

    private lateinit var controller: ButtonController
    private lateinit var textLastPressed: TextView
    private lateinit var buttonClear: Button
    private lateinit var buttons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controller = ButtonController(this, this)

        // XML-n definitutako botoiak bildu
        buttons = listOf(
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3)
        )

        textLastPressed = findViewById(R.id.textLastPressed)
        buttonClear = findViewById(R.id.buttonClear)

        // Botoien klik-ekintzak Controller-era bidali
        buttons.forEach { button ->
            button.setOnClickListener { controller.onButtonClicked(button.text.toString()) }
        }

        // Garbitu botoiaren ekintza
        buttonClear.setOnClickListener { controller.onClearClicked() }
    }

    // Controller-ak deitzen du testua eguneratzeko
    override fun updateLastPressed(text: String) {
        textLastPressed.text = text
    }

    // Controller-ak deitzen du garbitzeko
    override fun clearDisplay() {
        textLastPressed.text = "Ez da botoirik sakatu"
    }
}