package com.example.activityendatubidalketa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var Ireki: Button
    lateinit var Izena: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Ireki = findViewById<Button>(R.id.btnIreki)
        Izena = findViewById<EditText>(R.id.txtIzena)


        Ireki.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("IZENA", Izena.text.toString())
            startActivity(intent)
        }
    }
}