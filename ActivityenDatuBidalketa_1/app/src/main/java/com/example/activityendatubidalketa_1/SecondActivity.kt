package com.example.activityendatubidalketa_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    lateinit var Itzuli: Button
    lateinit var data: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        Itzuli = findViewById<Button>(R.id.btnItzuli)
        Itzuli.setOnClickListener {
            data = Intent()
            data.putExtra("MEZUA", "Ongi da, bueltan nago!")
            setResult(Activity.RESULT_OK, data)
            finish() // itzuli lehen Activity-ra
        }
    }
}