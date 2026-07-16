package com.example.ikasleak

import android.os.Bundle
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

        screemSplash.setKeepOnScreenCondition{false}

    }
}