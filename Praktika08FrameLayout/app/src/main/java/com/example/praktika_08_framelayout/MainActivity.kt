package com.example.praktika_08_framelayout

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    //Definizioak
    lateinit var Irudia: ImageView
    lateinit var Deitu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Hasieraketa
        Irudia=findViewById(R.id.imgTelefonoa)
        Deitu=findViewById(R.id.btnDeitu)

        Deitu.setOnClickListener(View.OnClickListener{
            Deitu.setVisibility(View.INVISIBLE);
            Irudia.setVisibility(View.VISIBLE);
        })

    }
}