package com.example.mvc_vistacontrollerbatera.view_controller

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mvc_vistacontrollerbatera.R
import com.example.mvc_vistacontrollerbatera.model.CounterModel


class MainActivity : AppCompatActivity(), CounterModel.Listener {

    private lateinit var model: CounterModel
    private lateinit var textCount: TextView
    private lateinit var btnPlus: Button
    private lateinit var btnMinus: Button
    private lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 🔹 MODEL sortu
        model = CounterModel()

        // 🔹 VIEW elementuak hartu
        textCount = findViewById(R.id.textCount)
        btnPlus = findViewById(R.id.btnPlus)
        btnMinus = findViewById(R.id.btnMinus)
        btnReset = findViewById(R.id.btnReset)

        // 🔹 CONTROLLER → botoiaren ekintzak
        btnPlus.setOnClickListener { model.increment() }
        btnMinus.setOnClickListener { model.decrement() }
        btnReset.setOnClickListener { model.reset() }

        // 🔹 Model-eko aldaketak entzuteko izena eman
        model.addListener(this)

        // Hasierako balioa erakutsi
        textCount.text = model.getCount().toString()
    }

    // 🔹 Model-ak aldaketa bidaltzen duenean metodo hau exekutatzen da
    override fun onCountChanged(newCount: Int) {
        textCount.text = newCount.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Memoria garbitzeko
        model.removeListener(this)
    }
}