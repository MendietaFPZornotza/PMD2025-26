package com.example.praktika00

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var Sakatu:Button
    lateinit var Testua:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            //val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            //insets




        //}
        Sakatu= findViewById(R.id.btnSakatu)
        Testua= findViewById(R.id.txtTextua)
        Sakatu.setOnClickListener {
            Log.d("Oharra",  "Botoian sakatu da")
            if (Testua.text=="Kaixo Mundua") { Testua.text= "Sakatu Botoia" }
                else
            { Testua.text= "Kaixo Mundua"}
        }
    }
}