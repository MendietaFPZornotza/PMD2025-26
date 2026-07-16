package com.example.praktika07_datuakgorde

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    //Aldagaien definizioak

    lateinit var LbIzena: TextView
    lateinit var Izena: EditText
    lateinit var Gorde: Button
    lateinit var Ezabatu: Button
    val EMPTY_VALUE = ""
    lateinit var prefs: Prefs


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = Prefs(applicationContext)

        //Hasieraketa
        val EMPTY_VALUE = ""
        LbIzena=findViewById(R.id.lbIzena)
        Izena=findViewById(R.id.txtIzena)
        Gorde=findViewById(R.id.btnGorde)
        Ezabatu=findViewById(R.id.btnEzabatu)

        configView()

        Gorde.setOnClickListener {
            prefs.name = Izena.text.toString()
            configView() }
        Ezabatu.setOnClickListener {
            prefs.name = EMPTY_VALUE
            configView()}
    }



    fun isSavedName():Boolean{
        val myName = prefs.name
        //Log.d("Oharra",myName)
        return myName != EMPTY_VALUE
    }
    fun configView(){
        if(isSavedName()) showProfile() else showGuest()
    }
    fun showProfile(){
        LbIzena.visibility = View.VISIBLE
        //Log.d("Oharra",SharedApp.prefs.name)
        LbIzena.text = prefs.name
        Ezabatu.visibility = View.VISIBLE
        Izena.visibility = View.INVISIBLE
        Gorde.visibility = View.INVISIBLE
    }
    fun showGuest(){
        LbIzena.visibility = View.INVISIBLE
        Ezabatu.visibility = View.INVISIBLE
        Izena.visibility = View.VISIBLE
        Gorde.visibility = View.VISIBLE
    }
}
