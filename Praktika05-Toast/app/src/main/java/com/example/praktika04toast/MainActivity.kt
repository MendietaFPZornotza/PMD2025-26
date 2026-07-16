package com.example.praktika04toast

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var num: Number
    lateinit var zenbakia: EditText
    lateinit var  egiaztatu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        zenbakia=findViewById(R.id.txtZenbakia)
        egiaztatu=findViewById(R.id.BtnEgiaztatu)
        num=(Math.random()*100001).toInt();
        val zenbkatea = num.toString()
        val abisua = Toast.makeText(this, zenbkatea, Toast.LENGTH_LONG )
        Log.d("Oharra", "Sartutakoa " +Toast.LENGTH_LONG)
        abisua.setGravity(Gravity.CENTER or Gravity.LEFT, 0, 0)
        abisua.show()
        egiaztatu.setOnClickListener(View.OnClickListener {
            val sartutakoa = zenbakia.text.toString().toInt()
            Log.d("Oharra", "Sartutakoa " +sartutakoa)
            if (sartutakoa === num) {
                var abisua = Toast.makeText(this, "Memoria ona duzu, Zorionak", Toast.LENGTH_LONG)
                abisua.setGravity(Gravity.CENTER or Gravity.LEFT, 0, 0)
                abisua.show()
            } else {
                var abisua =  Toast.makeText(this, "Memoria lantzen jarraitu beharko duzu.", Toast.LENGTH_LONG)
                //Toast.makeText(this,"Memoria lantzen jarraitu beharko duzu.",Toast.LENGTH_LONG).show();
                abisua.setGravity(Gravity.CENTER or Gravity.LEFT, 0, 0)
                abisua.show()
            }
        })
    }
}