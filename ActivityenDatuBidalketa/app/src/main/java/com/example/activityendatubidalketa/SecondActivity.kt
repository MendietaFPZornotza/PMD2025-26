package com.example.activityendatubidalketa

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


        val izenaJasoa = intent.getStringExtra("IZENA")

        val mezua = findViewById<TextView>(R.id.txMezua)
        mezua.text = "Kaixo, $izenaJasoa!"

       // Toast.makeText(this, "Bigarren Activity ireki da!", Toast.LENGTH_SHORT).show()
    }
}