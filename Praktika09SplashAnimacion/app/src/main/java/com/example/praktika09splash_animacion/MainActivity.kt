package com.example.praktika09splash_animacion

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val screemSplash=installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread.sleep(2400)
        screemSplash.setKeepOnScreenCondition{false}
        //val intent= Intent(this,ActivityDetail::class.java)
        //startActivity(intent)
        //finish()



    }
}