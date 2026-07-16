package com.example.erronka_proba.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * Fitxategia: SplashActivity.kt
 *
 * Zertarako da?
 * - Aplikazioaren “sarrera” azkarra: zuzenean LoginActivity-ra bidaltzen du.
 *
 * Zer egiten du?
 * - onCreate(): LoginActivity abiarazi eta berehala finish().
 *
 * Nola erabiltzen da?
 * - Launcher activity izan daiteke manifest-en.
 *
 * Log-ak:
 * - Splash -> Login nabigazioa ikusteko.
 */
class SplashActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SplashActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate(): Splash -> LoginActivity")

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}