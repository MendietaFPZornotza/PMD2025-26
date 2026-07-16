package com.example.erropadenda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class login : AppCompatActivity() {
    lateinit var   emailTxt: EditText
    lateinit var   passTxt: EditText
    lateinit var  btnSaHa: Button
    lateinit var  btnErre: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       val screenSplash=installSplashScreen()
        setContentView(R.layout.login)

        emailTxt=findViewById (R.id.txtEmail)
        passTxt=findViewById(R.id.txtPass)
        btnSaHa=findViewById(R.id.btnHasi)
        btnErre=findViewById(R.id.btnErregis)

        //-----SPLASH-------


        screenSplash.setKeepOnScreenCondition{false}


       //--------------------

        //Erregistro pantailara eraman
        btnErre.setOnClickListener {

            val i= Intent(this,erregistro::class.java)
            startActivity(i)

        }

        //Konprobatu emaila eta pasahitza ondo idatzita dauden
        btnSaHa.setOnClickListener{

            if(emailTxt.text.toString()=="" && passTxt.text.toString()==""){
                toastAgertu("ez duzu email eta pasahitza sartu")
                return@setOnClickListener
            }
            if (emailTxt.text.toString()=="" || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailTxt.text.toString()).matches()){
                toastAgertu("ez duzu email-a ondo sartu ")
                return@setOnClickListener
            }
            if(passTxt.text.toString()==""){
                toastAgertu("ez duzu pasahitza sartu")
                return@setOnClickListener

            }

            login()

        }
    }

    fun login(){

        val ema=emailTxt.text.toString()
        val contra=passTxt.text.toString()
        val admin = AdminSQLiteOpenHelper(this,"administracion",null,1)
        val bd=admin.writableDatabase

        val lerroa=bd.rawQuery("select * from usuarios where email='$ema' and pasahitza='$contra'",null)
        if(lerroa.moveToFirst()){
            toastAgertu("Ongi etorri")
            emailTxt.setText("")
            passTxt.setText("")

            val i= Intent(this,erropaZerrenda::class.java)
            startActivity(i)

        }else{
            toastAgertu("erabiltzaile hori ez dago gure datubasean")
        }
    }
    fun toastAgertu(mezua : String){
        val t = Toast.makeText(this,mezua, Toast.LENGTH_SHORT)
        t.show()

    }

}