package com.example.bizizikloapmd

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class BigarrenActivity : AppCompatActivity() {

    private val TAG = "Lifecycle"
    lateinit var itzuli: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bigarren_activity)

        Log.d(TAG, "BigarrenActivity onCreate")

        itzuli = findViewById<Button>(R.id.btnItzuli)

        itzuli.setOnClickListener {
            finish() // vuelve a MainActivity
        }
    }

    override fun onStart()   { super.onStart();   Log.d(TAG, "BigarrenActivity onStart") }
    override fun onResume()  { super.onResume();  Log.d(TAG, "BigarrenActivity onResume") }
    override fun onPause()   { super.onPause();   Log.d(TAG, "BigarrenActivity onPause") }
    override fun onStop()    { super.onStop();    Log.d(TAG, "BigarrenActivity onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "BigarrenActivity onDestroy") }
}