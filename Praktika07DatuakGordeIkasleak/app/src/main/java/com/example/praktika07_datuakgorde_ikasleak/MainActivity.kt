package com.example.praktika07_datuakgorde_ikasleak

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


class MainActivity : AppCompatActivity() {
    //Definizioak
    lateinit var Izena: EditText
    lateinit var Herrialdea: EditText
    lateinit var Adina: EditText
    lateinit var GordeZ : Button
    lateinit var GordeF : Button
    lateinit var Lista: Spinner
    lateinit var Irakurri : Button
    lateinit var Erakutsi : Button
    lateinit var ListaDat: Array<Datuak>
    lateinit var ListaIzena: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Hasieraketa
        Izena=findViewById(R.id.txtNombre)
        Herrialdea=findViewById(R.id.txtLugar)
        Adina=findViewById(R.id.txtEdad)
        Lista=findViewById(R.id.lstNombres)
        GordeZ=findViewById(R.id.btnGuardar)
        GordeF=findViewById(R.id.btnGuardarDisco)
        Irakurri=findViewById(R.id.btnLeer)
        Erakutsi=findViewById(R.id.btnMostrar)

        ListaIzena = arrayOf<String>()
        ListaDat = arrayOf<Datuak>()

        GordeZ.setOnClickListener(View.OnClickListener {
            ZerrendanGorde()
        })
        GordeF.setOnClickListener(View.OnClickListener {
            FitxategianGorde()
        })
        Irakurri.setOnClickListener(View.OnClickListener {
            FitxategitikIrakurri()
        })
        Erakutsi.setOnClickListener(View.OnClickListener {
            ZerrendakoDatuaErakutsi()
        })
    }
    fun ZerrendanGorde(){
        // Gehitu Datuak objetu berria ListaDat zerrendara
        var Dat = Datuak(
            Izena.getText().toString(),
            Herrialdea.getText().toString(),
            Adina.getText().toString().toInt()
        )
        ListaDat +=Dat

        // Spinner-ei izena gehitu
        ListaIzena +=(Izena.getText().toString())
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ListaIzena)
        Lista.setAdapter(adapter)

        // Mezua erakutsi
        var abisua = Toast.makeText(this, "Zerrendara gehitu da!", Toast.LENGTH_LONG)
        abisua.setGravity(Gravity.CENTER or Gravity.LEFT, 0, 0)
        abisua.show()

        // Elementuak garbitu
        Izena.setText("");
        Herrialdea.setText("");
        Adina.setText("");
    }
    fun ZerrendakoDatuaErakutsi(){
        // Sortu Alert Builder (leiho estandar bat)
        val constructor: AlertDialog.Builder = AlertDialog.Builder(this)
        //Leihoari izena  eta botoia jarri
        constructor.setTitle("Gorde")
        constructor.setPositiveButton("Onartu", null)

        val index: Long = Lista.getSelectedItemId()
        if (index > -1) {
            // Leihoan agertu den mezua sortzen dut
            constructor.setMessage(
                (("Izena: " + ListaDat.get(index.toInt()).izena
                        ).toString() + " - Herrialdea: " + ListaDat.get(index.toInt()).herrialdea
                        ).toString() + " - Adina: " + ListaDat.get(index.toInt()).adina
            )
        } else {
            constructor.setMessage("Listako ")
        }
        // Leihoa erakutsi.
        val ventanaMensaje = constructor.create()
        ventanaMensaje.show()
    }
    fun FitxategianGorde(){
        // El objeto File me permite acceder el archivo (en este caso, para escribir en él)
        // (obtengo la ruta donde almacenarlo; en la carpeta de la app)
        val ruta = applicationContext.filesDir
        // Éste es el nombre del archivo
        val nombreArch = "archivo.tpo"
        // El acceso a archivo tiene que ir en un try catch por si sucede algo inesperado
        try {
            val FitxIdatzi = FileOutputStream(File(ruta, nombreArch))
            // Tengo que usar un ObjectOutputStream porque el almacenamiento interno es un archivo de bytes
            // y este objeto e hgts el que me permite convertir de objeto a byte. Si fuera un String u otra cosa,
            // bastaría escribirArch.write(lacadena.getBytes())
            // suponiendo que lacadena es un String que contiene el texto a guardar.
            val streamArch = ObjectOutputStream(FitxIdatzi)
            streamArch.writeObject(ListaDat)
            streamArch.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace() // Si hay error, que muestre datos sobre el fallo
        }

    }
    fun FitxategitikIrakurri(){
        // File objektua sortu biltegiratzeko bidearekin
        val ruta = applicationContext.filesDir
        // Fitxategiaren izena
        val nombreArch = "archivo.tpo"

        // Fitxategiko datuak irakurri
        try {
            // FileInputStream me permite abri el archivo para leer de él
            val leeArch = FileInputStream(File(ruta, nombreArch))

            // El ObjectInputStream me pemite traducir el arreglo de bytes al Arraylist
            var streamArch = ObjectInputStream(leeArch)

            // Leo todo y lleno la lista
            ListaDat = arrayOf<Datuak>()
            ListaDat = streamArch.readObject() as Array<Datuak>
            // Cierro el stream
            streamArch.close()
            // Lleno la lista de nombres (strings) con los nombres de la lista de datos
            ListaIzena = arrayOf<String>()
            for (i in 0 until ListaDat.size) {
                ListaIzena +=(ListaDat.get(i).izena)
            }
            // Lleno el Spinner de la nueva lista
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ListaIzena)
            Lista.setAdapter(adapter)
        } catch (e: Exception) {
            e.printStackTrace();        // Si hay error, que muestre datos sobre el fallo
        }
    }
}