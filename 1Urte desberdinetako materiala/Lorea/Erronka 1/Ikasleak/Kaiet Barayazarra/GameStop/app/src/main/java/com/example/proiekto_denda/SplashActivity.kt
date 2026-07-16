package com.example.proiekto_denda // Ziurtatu paketea manifest-an erabiltzen duzunarekin bat datorrela

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 3 segundo itxaron hurrengo jarduera batera pasatzeko
        Handler().postDelayed({
            // Intent-a LoginActivity-ra joateko
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Amaitu jarduera hau, eta ez utzi pilan geratzen
        }, 3000) // 3000 ms = 3 segundo
    }
}
