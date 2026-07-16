package com.example.praktika04

import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    lateinit var Herrialdea:TextView
    lateinit var Biztanleak:ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val Hiriak = arrayOf( "Argentina", "Chile", "Paraguay", "Bolivia", "Peru",
            "Ecuador", "Brasil", "Colombia", "Venezuela", "Uruguay")
        val habitantes = arrayOf(
            "40000000", "17000000", "6500000", "10000000", "30000000",
            "14000000", "183000000", "44000000", "29000000", "3500000"
        )
        Herrialdea=findViewById(R.id.txtHerrialdea)
        Biztanleak=findViewById(R.id.listBiztanleak)


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Hiriak)
        Biztanleak.setAdapter(adapter)
        Biztanleak.setOnItemClickListener(OnItemClickListener { parent, v, posicion, id ->
            Herrialdea.setText(
                (Biztanleak.getItemAtPosition(posicion)).toString() + "-ko biztanle kopurua: " + habitantes[posicion])

        })

    }
}