package com.example.mvc_jarduera1.view

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
import com.example.mvc_jarduera1.ui.theme.Mvc_Jarduera1Theme
import com.example.mvc_jarduera1.controller.MessageController
import com.example.mvc_jarduera1.model.MessageModel
import com.example.mvc_jarduera1.R


class MainActivity : ComponentActivity(), MessageController.MessageView {

    private lateinit var controller: MessageController
    private lateinit var textView: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Layout-eko elementuak hartu
        textView = findViewById(R.id.tvMessage)
        button = findViewById(R.id.btnOk)

        // Controller-a sortu (modela bere barruan dauka)
        controller = MessageController(this)

        // Hasierako mezua erakutsi
        controller.initialize()

        // Botoia sakatzean: Controller-era jakinarazi
        button.setOnClickListener {
            controller.onButtonClicked()
        }
    }

    // Interfazearen metodoa: mezua pantailan erakusteko
    override fun showMessage(message: String) {
        textView.text = message
    }
}