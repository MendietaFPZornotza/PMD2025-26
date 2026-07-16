package com.example.mvc_counter_bateratuta

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mvc_counter_bateratuta.model.CounterModel
import com.example.mvc_counter_bateratuta.ui.theme.MVC_Counter_BateratutaTheme

class MainActivity : AppCompatActivity(), CounterModel.Listener {

    private lateinit var model: CounterModel
    private lateinit var textCount: TextView
    private lateinit var btnPlus: Button
    private lateinit var btnMinus: Button
    private lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // MODEL sortu
        model = CounterModel()

        // VIEW elementuak hartu
        textCount = findViewById(R.id.textCount)
        btnPlus = findViewById(R.id.btnPlus)
        btnMinus = findViewById(R.id.btnMinus)
        btnReset = findViewById(R.id.btnReset)

        // CONTROLLER botoiaren ekintzak
        btnPlus.setOnClickListener { model.increment() }
        btnMinus.setOnClickListener { model.decrement() }
        btnReset.setOnClickListener { model.reset() }

        // Model-eko aldaketak entzuteko izena eman
        model.addListener(this)

        // Hasierako balioa erakutsi
        textCount.text = model.getCount().toString()
    }

    // Model-ak aldaketa bidaltzen duenean metodo hau exekutatzen da
    override fun onCountChanged(newCount: Int) {
        textCount.text = newCount.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Memoria garbitzeko
        model.removeListener(this)
    }
}