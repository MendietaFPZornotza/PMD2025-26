package com.example.mvc_counter.view

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
import com.example.mvc_counter.R
import com.example.mvc_counter.controller.CounterController
import com.example.mvc_counter.model.CounterModel
import com.example.mvc_counter.ui.theme.Mvc_counterTheme

class MainActivity : ComponentActivity() {

    private lateinit var tvCount: TextView
    private lateinit var btnInc: Button
    private lateinit var btnDec: Button
    private lateinit var btnReset: Button


    private lateinit var controller: CounterController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // VIEW elementuak hartu
        tvCount = findViewById(R.id.tvCount)
        btnInc = findViewById(R.id.btnInc)
        btnDec = findViewById(R.id.btnDec)
        btnReset = findViewById(R.id.btnReset)


    }
}