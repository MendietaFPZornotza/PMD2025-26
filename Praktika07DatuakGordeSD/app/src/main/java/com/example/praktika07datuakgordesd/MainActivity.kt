package com.example.praktika07datuakgordesd

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class MainActivity : AppCompatActivity() {
    //Definizioak
    lateinit var SarreraD: EditText
    lateinit var Textua: EditText
    lateinit var Gorde: Button
    lateinit var Berreskuratu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Hasieraketa
        SarreraD=findViewById(R.id.txtSartu)
        Textua=findViewById(R.id.txtTextua)
        Gorde=findViewById(R.id.btnGorde)
        Berreskuratu=findViewById(R.id.btnBerreskuratu)

        //Botoiak onClick
        Gorde.setOnClickListener {
            DatuakGorde()
        }
        Berreskuratu.setOnClickListener {
            DatuakBerreskuratu()
        }
    }

    fun DatuakGorde() {
        val FitxIzena: String = SarreraD.getText().toString()
        val edukia: String = Textua.getText().toString()
        try {
            val file = File(getExternalFilesDir(null), FitxIzena)
            val osw = OutputStreamWriter(
                FileOutputStream(file)
            )
            osw.write(edukia)
            osw.flush()
            osw.close()
            Toast.makeText(this, "Datu guztiak gorde dira", Toast.LENGTH_LONG).show()
            SarreraD.setText("")
            Textua.setText("")
        } catch (ioe: IOException) {
            Toast.makeText(this, "Datuak ez dira gorde.", Toast.LENGTH_LONG).show()
        }
    }
    fun DatuakBerreskuratu() {
        val file = File(getExternalFilesDir(null), SarreraD.getText().toString())
        try {
            val fIn = FileInputStream(file)
            val fitxategia = InputStreamReader(fIn)
            val br = BufferedReader(fitxategia)
            var lerroa = br.readLine()
            var testua = ""
            while (lerroa != null) {
                testua = "$testua$lerroa "
                lerroa = br.readLine()
            }
            br.close()
            fitxategia.close()
            Textua.setText(testua)
        } catch (e: IOException) {
            Toast.makeText(this, "Datuak ezin izan dira irakurri", Toast.LENGTH_LONG).show()
        }
    }
}