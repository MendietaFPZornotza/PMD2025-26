package com.example.praktika05

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var botoia:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        botoia= findViewById(R.id.btnIgaro)
        botoia.setOnClickListener(View.OnClickListener {
            //val i = Intent(this, Datuak::class.java)
            //startActivity(i)
            var abisua =  Toast.makeText(this, "Memoria lantzen jarraitu beharko duzu.", Toast.LENGTH_SHORT)
            //Toast.makeText(this,"Memoria lantzen jarraitu beharko duzu.",Toast.LENGTH_LONG).show();
            abisua.setGravity(Gravity.CENTER , 0, 500)
            abisua.show()
        })
    }
}