package com.example.splashexample

import android.R.attr.delay
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 2 segundoz itxaron eta gero pantaila nagusira joan
        lifecycleScope.launch {
            delay(2000) // 2000 milisegundo = 2 segundu
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish() // splash pantaila itxi
        }
    }
}