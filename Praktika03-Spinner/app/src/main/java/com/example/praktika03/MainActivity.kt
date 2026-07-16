package com.example.praktika03

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    //Aldagaien definizioak
    lateinit var Number1: EditText
    lateinit var Number2: EditText
    lateinit var Lista: Spinner
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
        //Hasieraketa
        Number1=findViewById(R.id.txtNumber1)
        Number2=findViewById(R.id.txtNumber2)
        Lista=findViewById(R.id.SLista)
        Kalkulatu=findViewById(R.id.btnKalkulatu)
        Emaitza=findViewById(R.id.txtEmaitza)

        //Lista hasieratu eta bete
        val ListakoAukerak = arrayOf("sumar", "restar", "multiplicar", "dividir")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ListakoAukerak)
        Lista.setAdapter(adapter)

        Kalkulatu.setOnClickListener(View.OnClickListener {


            val zenb1= Number1.text.toString().toInt()
            val zenb2= Number2.text.toString().toInt()
            var ema=0

            //Log.d("Oharra", "Aukera " +Lista.selectedItem.toString())
            //IF erabiliz
            /*if (Lista.selectedItem.equals("sumar")) {
                ema = zenb1 + zenb2
                Log.d("Oharra", "Batuketaren emaitza: " +ema)
                Emaitza.text =ema.toString()
            } else if (Lista.selectedItem.equals("restar")) {
                ema = zenb1 - zenb2
                Log.d("Oharra", "Kenketaren emaitza: " +ema)
                Emaitza.text =ema.toString()
            } else if (Lista.selectedItem.equals("multiplicar")) {
                ema = zenb1 * zenb2
                Log.d("Oharra", "Biderketaren emaitza: " +ema)
                Emaitza.text =ema.toString()
            } else if (Lista.selectedItem.equals("dividir")) {
                ema = zenb1 / zenb2
                Log.d("Oharra", "Zatiketaren emaitza: " +ema)
                Emaitza.text =ema.toString()
            }
            */
            //WHEN erabiliz
            when (Lista.selectedItem.toString()) {
                "sumar" -> ema = zenb1 + zenb2
                "restar" -> ema = zenb1 - zenb2
                "multiplicar" ->  ema = zenb1 * zenb2
                "dividir" -> ema = zenb1 / zenb2
            }
            Emaitza.text=ema.toString()
        })
    }
}