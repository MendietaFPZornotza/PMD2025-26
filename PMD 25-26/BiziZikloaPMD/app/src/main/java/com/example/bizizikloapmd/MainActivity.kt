package com.example.bizizikloapmd

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
   lateinit var Sakatu: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Log.d("Lifecycle", "MainActivity onCreate() exekutatu da")
        Sakatu= findViewById<Button>(R.id.btnKaixo)
        Sakatu.setOnClickListener {
            val intent = Intent(this, BigarrenActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "MainActivity onStart() exekutatu da")
    }
    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "MainActivity onResume() exekutatu da")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "MainActivity onPause() exekutatu da")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "MainActivity onStop() exekutatu da")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "MainActivity onDestroy() exekutatu da")
    }
}