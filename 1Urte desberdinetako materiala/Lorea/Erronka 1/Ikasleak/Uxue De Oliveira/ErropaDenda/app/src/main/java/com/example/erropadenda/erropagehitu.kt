package com.example.erropadenda

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class erropagehitu : AppCompatActivity() {

      lateinit var  erroizena: EditText
      lateinit var spmota: Spinner
      lateinit var taS: CheckBox
      lateinit var taM: CheckBox
      lateinit var taL: CheckBox
      lateinit var taXl: CheckBox
      lateinit var kolorea: EditText
      lateinit var prezioa: EditText
      lateinit var chBai:CheckBox
      lateinit var chEz: CheckBox
      lateinit var btnGehitu: Button
      lateinit var btnBuelta: Button

      val  ListaMota= arrayOf("Kamiseta","Praka","Jaka","Abrigoa","Eskularruak","Galtzerdiak")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_erropagehitu)

        //Menuaren tituloa kentzeko
        supportActionBar?.setDisplayShowTitleEnabled(false)

        erroizena=findViewById(R.id.ediIzena)
        spmota=findViewById(R.id.spErMota)

        kolorea=findViewById(R.id.ediKolorea)
        prezioa=findViewById(R.id.ediPrezioa)
        taS=findViewById(R.id.chS)
        taM=findViewById(R.id.chM)
        taL=findViewById(R.id.chL)
        taXl=findViewById(R.id.chXL)
        chBai=findViewById(R.id.chBai)
        chEz=findViewById(R.id.chEz)
        btnGehitu=findViewById(R.id.btnErGehi)
        btnBuelta=findViewById(R.id.btnBuelta)
        val adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item,ListaMota)
        spmota.adapter=adapter


        // Hemen egiten da bakarrik checkBox bat aukeratu ahal dela talletan

        taS.setOnClickListener {  handleCheckBoxSelection(taS) }
        taM.setOnClickListener { handleCheckBoxSelection(taM) }
        taL.setOnClickListener { handleCheckBoxSelection(taL) }
        taXl.setOnClickListener { handleCheckBoxSelection(taXl) }

        // Hemen egiten da bakarrik checkBox bat aukeratu ahal dela eskuragarritasunean
        chBai.setOnClickListener { handleCheckBoxSelection(chBai, chEz) }
        chEz.setOnClickListener { handleCheckBoxSelection(chEz, chBai) }


        //gehitu botoia

        btnGehitu.setOnClickListener {
            gordeProduktua()
        }
        //Erropa zerrendara
        btnBuelta.setOnClickListener {
            val i= Intent(this,erropaZerrenda::class.java)
            startActivity(i)

        }


    }

    // Bakarrik talla bat aukeratzeko
    private fun handleCheckBoxSelection(selectedCheckBox: CheckBox) {
        listOf(taS, taM, taL, taXl).forEach { checkBox ->
            checkBox.isChecked = checkBox == selectedCheckBox
        }
    }

    // Bakarrik bai edo ez aukeratzeko
    private fun handleCheckBoxSelection(selectedCheckBox: CheckBox, otherCheckBox: CheckBox) {
        if (selectedCheckBox.isChecked) {
            otherCheckBox.isChecked = false
        }
    }




    fun gordeProduktua(){
        var noSele=false
        val admin = AdminSQLiteOpenHelper(this,"administracion",null,1)
        val bd=admin.writableDatabase

        val nom=erroizena.text.toString()
        val mota=findViewById<Spinner>(R.id.spErMota).selectedItem.toString()
        val kolor=kolorea.text.toString()
        val pre=prezioa.text.toString()

        val esku= when{

            chBai.isChecked-> "bai"
            chEz.isChecked->"ez"
            else -> ""
        }
        val ta = when {
            taS.isChecked -> "S"
            taM.isChecked -> "M"
            taL.isChecked -> "L"
            taXl.isChecked -> "XL"
            else -> ""
        }

        if(nom=="" || mota=="" || kolor=="" || pre=="" ){
            noSele=true
        }
        if( pre=="0"||pre=="0.0"){
            toastAgertu("prezioa ezin da izan 0")
            noSele=true
        }

        if(!noSele){
            val registro= ContentValues()
            registro.put("izena",nom)
            registro.put("mota",mota)
            registro.put("talla",ta)
            registro.put("kolorea",kolor)
            registro.put("prezioa",pre)
            registro.put("eskuragarritasuna",esku)
            bd.insert("productos",null, registro)
            bd.close()
            toastAgertu("datu basean produktoa gorde da")

        }
        else{
            toastAgertu("ez dira datu guztiak sartu")
        }



    }
    fun toastAgertu(mezua : String){
        val t = Toast.makeText(this,mezua, Toast.LENGTH_SHORT)
        t.show()

    }

    //menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true //super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //  return super.onOptionsItemSelected(item)
        return when (item.itemId) {

            //Erropa zerrendara joaten da
            R.id.meErroZerre->{
                val i = Intent(this, erropaZerrenda::class.java)
                startActivity(i)

                true
            }
            //Erropa gehitzeko orrira joaten da
            R.id.meErrogehi -> {
                val i = Intent(this, erropagehitu::class.java)
                startActivity(i)

                true
            }
            R.id.meIr -> {

                finishAffinity()

                true
            }

            R.id.meSaItxi -> {
                finish()

                true

            }

            else -> return super.onOptionsItemSelected(item)
        }
    }



}