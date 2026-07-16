package com.example.praktika02

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    //Aldagaien definizioak
    lateinit var Number1: EditText
    lateinit var Number2: EditText
    lateinit var Biderketa: CheckBox
    lateinit var Zatiketa: CheckBox
    lateinit var Kalkulatu: Button
    lateinit var Emaitza: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
        //val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //insets
        //}
        //Hasieraketak
        Number1=findViewById(R.id.txtNumber1)
        Number2=findViewById(R.id.txtNumber2)
        Biderketa=findViewById(R.id.cbBiderketa)
        Zatiketa=findViewById(R.id.cbZatiketa)
        Kalkulatu=findViewById(R.id.btnKalkulatu)
        Emaitza=findViewById(R.id.txtEmaitza)

        Biderketa.setOnClickListener(View.OnClickListener {
            if(Biderketa.isChecked)  {
                Zatiketa.isChecked=false
            }
        })
        Zatiketa.setOnClickListener(View.OnClickListener {
            if(Zatiketa.isChecked)  {
                Biderketa.isChecked=false
            }
        })
        Kalkulatu.setOnClickListener(View.OnClickListener {
            val zenb1 = Number1.text.toString().toInt()
            val zenb2 = Number2.text.toString().toInt()
            var ema=0

            if(Biderketa.isChecked)  {
                Zatiketa.isChecked=false
                ema =(zenb1*zenb2)
                //ema=gehiketa(zenb1,zenb2)
                Log.d("Oharra", "Batuketaren emaitza: " +ema)
            }
            else if (Zatiketa.isChecked) {
                Biderketa.isChecked=false
                ema =(zenb1/zenb2)
                //ema=kenketa(zenb1,zenb2)
                Log.d("Oharra", "Kenketaren emaitza: " +ema)
            }
            Emaitza.text=ema.toString()
        })
    }

}