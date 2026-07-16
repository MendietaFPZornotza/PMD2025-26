package com.example.mvc_scrollview.view

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
import com.example.mvc_scrollview.controller.ScrollController
import com.example.mvc_scrollview.ui.theme.Mvc_scrollviewTheme
import com.example.mvc_scrollview.R

// VIEW: interfazea erakusten du eta Controller-era bidaltzen ditu ekintzak
class MainActivity : ComponentActivity() , ScrollController.ScrollViewLayer {

    private lateinit var controller: ScrollController
    private lateinit var textLastPressed: TextView
    private lateinit var buttons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controller = ScrollController(this, this)

        // XML-n definitutako botoiak bildu
        buttons = listOf(
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5)
        )

        textLastPressed = findViewById(R.id.textLastPressed)

        // Botoi bakoitzaren klik-ekintza Controller-era bidali
        buttons.forEach { button ->
            button.setOnClickListener {
                controller.onButtonClicked(button.text.toString())
            }
        }
    }

    // Controller-ak deitzen du azken botoi sakatua eguneratzeko
    override fun updateLastPressed(text: String) {
        textLastPressed.text = text
    }
}