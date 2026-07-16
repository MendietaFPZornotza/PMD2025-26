package com.example.praktika01

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

  //Aldagaien definizioak
    lateinit var Number1: EditText
    lateinit var Number2: EditText
    lateinit var Batuketa: RadioButton
    lateinit var Kenketa: RadioButton
    lateinit var Eragiketa:Button
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
        Batuketa=findViewById(R.id.rbBatuketa)
        Kenketa=findViewById(R.id.rbKenketa)
        Eragiketa=findViewById(R.id.btnEragiketa)
        Emaitza=findViewById(R.id.txtEmaitza)

        Eragiketa.setOnClickListener(View.OnClickListener {
            val zenb1 = Number1.text.toString().toInt()
            val zenb2 = Number2.text.toString().toInt()
            var ema=0
            if(Batuketa.isChecked)  {
                //ema =(zenb1+zenb2)
                ema=gehiketa(zenb1,zenb2)
                Log.d("Oharra", "Batuketaren emaitza: " +ema)
            }
            else if (Kenketa.isChecked) {
                //ema =(zenb1+zenb2)
                ema=kenketa(zenb1,zenb2)
                Log.d("Oharra", "Kenketaren emaitza: " +ema)
            }
            Emaitza.text=ema.toString()
        })
    }
   fun gehiketa (zenb1:Int,zenb2:Int):Int{
       return zenb1+zenb2
    }
    fun kenketa (zenb1:Int,zenb2:Int):Int{
        return zenb1-zenb2
    }
}