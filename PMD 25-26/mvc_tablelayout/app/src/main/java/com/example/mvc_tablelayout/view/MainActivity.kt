package com.example.mvc_tablelayout.view

import android.os.Bundle
import android.widget.Button
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
import com.example.mvc_tablelayout.controller.ButtonController
import com.example.mvc_tablelayout.ui.theme.Mvc_tablelayoutTheme
import com.example.mvc_tablelayout.R


// VIEW: interfazea erakusten du eta Controller-era bidaltzen ditu ekintzak
class MainActivity : ComponentActivity(), ButtonController.ButtonView {

    private lateinit var controller: ButtonController
    private lateinit var textInfo: TextView
    private lateinit var buttons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controller = ButtonController(this, this)

        // XML-n definitutako botoiak bildu (6 botoi)
        buttons = listOf(
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6)
        )

        textInfo = findViewById(R.id.textInfo)

        // Botoi bakoitzaren klik-ekintza Controller-era bidali
        buttons.forEach { button ->
            button.setOnClickListener {
                controller.onButtonClicked(button.text.toString())
            }
        }
    }

    // Controller-ak deitzen du azken botoiaren informazioa eguneratzeko
    override fun updateLastPressed(text: String) {
        textInfo.text = text
    }
}