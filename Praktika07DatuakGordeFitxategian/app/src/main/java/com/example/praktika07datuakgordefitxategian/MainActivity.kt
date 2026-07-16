package com.example.praktika07datuakgordefitxategian

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class MainActivity : AppCompatActivity() {

    //Definizioak
    lateinit var Notak: EditText
    lateinit var Gorde: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Hasieraketa
        Notak=findViewById(R.id.txtNotaI)
        Gorde=findViewById(R.id.btnGorde)
        val fitxategiak = fileList()
        //Log.d("Oharra", archivos))

        if (ExistitzenDa(fitxategiak, "notas.txt")) try {
            val fitxategia = InputStreamReader(openFileInput("notas.txt"))
            val br = BufferedReader(fitxategia)
            var lerroa = br.readLine()
            var textua = ""
            while (lerroa != null) {
                textua = textua + lerroa + "\n"
                lerroa = br.readLine()
            }
            br.close()
            fitxategia.close()
            Notak.setText(textua)
        } catch (e: IOException) {
        }
        Gorde.setOnClickListener {
            GordeFitxategian()
             }
    }
    fun GordeFitxategian() {
        try {
            val fitxategia = OutputStreamWriter(openFileOutput("notas.txt", MODE_PRIVATE))
            fitxategia.write(Notak.getText().toString())
            fitxategia.flush()
            fitxategia.close()
            }
        catch (e: IOException) {
            val t = Toast.makeText(this, "Arazoren bat egon da Fitxategia irekitzean", Toast.LENGTH_SHORT)
            t.show()
            }
        val t = Toast.makeText(this, "Datuak Gorde dira", Toast.LENGTH_SHORT)
        t.show()
        finish()
    }
    private fun ExistitzenDa(fitxategiak: Array<String>, bilatufitx: String): Boolean {
        for (f in fitxategiak.indices)  if (bilatufitx == fitxategiak[f]) return true
        return false
    }


}