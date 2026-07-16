package com.example.myapplication.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.controller.SplashController

class SplashActivity : ComponentActivity() {
    private lateinit var controller: SplashController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Kontrolatzailea sortu
        controller = SplashController(this)

        // Aplikazioaren izena pantailan erakutsi (nahiz eta xml-an dagoen jada)
        val appName = controller.lortuAppIzena()
        title = appName

        // 3 segundoren buruan MainActivity-ra joan
        controller.hasiAplikazioa()
    }
}