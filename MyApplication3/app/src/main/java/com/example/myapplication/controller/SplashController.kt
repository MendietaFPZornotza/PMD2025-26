package com.example.myapplication.controller

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.myapplication.model.AppModel
import com.example.myapplication.view.MainActivity

// Splash pantailaren logika kudeatzen duen kontrolatzailea
class SplashController(private val context: Context) {

    private val model = AppModel()

    fun hasiAplikazioa() {
        // Handler bidez 3 segundoren buruan MainActivity abiarazi
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }, model.delayTime)
    }

    fun lortuAppIzena(): String = model.appName
}