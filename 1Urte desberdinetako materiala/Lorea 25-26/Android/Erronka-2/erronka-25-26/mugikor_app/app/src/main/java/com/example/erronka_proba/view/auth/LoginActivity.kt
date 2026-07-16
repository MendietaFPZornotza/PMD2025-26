package com.example.erronka_proba.view.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.erronka_proba.R
import com.example.erronka_proba.controller.auth.LoginController
import com.example.erronka_proba.data.repo.LastLoginRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

/**
 * Fitxategia: LoginActivity.kt
 *
 * Zertarako da?
 * - Login pantaila (UI): email/pasahitza sartu, saioa hasi edo signup-era joan.
 *
 * Zer egiten du?
 * - UI osagaiak lotu (findViewById)
 * - LoginController-en bidez ekintzak delegatu:
 *   - Signup botoia -> controller.onSignupClicked()
 *   - Login botoia -> coroutine -> controller.onLoginClicked()
 * - LastLoginRepository erabiliz azken email-a auto-bete (Room).
 *
 * Nola erabiltzen da?
 * - SplashActivity-tik normalean hemen hasten da flow-a.
 *
 * Log-ak:
 * - Pantailaren lifecycle-a, botoi klikak, auto-fill prozesua eta loading egoera.
 */
class LoginActivity : AppCompatActivity(), LoginController.View {

    companion object {
        private const val TAG = "LoginActivity"
    }

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPass: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnGoSignup: MaterialButton

    private lateinit var controller: LoginController
    private lateinit var lastLoginRepo: LastLoginRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate(): LoginActivity")

        setContentView(R.layout.activity_login)

        // Estetika: status bar kolorea (portaera originala)
        window.statusBarColor = getColor(R.color.primary)

        // UI osagaiak
        etEmail = findViewById(R.id.etEmail)
        etPass = findViewById(R.id.etPass)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoSignup = findViewById(R.id.btnGoSignup)

        // Controller
        controller = LoginController(this)

        // Repo (Room) azken email-a lortzeko
        lastLoginRepo = LastLoginRepository(this)

        // Signup-era joan
        btnGoSignup.setOnClickListener {
            Log.d(TAG, "btnGoSignup klik")
            controller.onSignupClicked()
        }

        // Login egin (coroutine batean)
        btnLogin.setOnClickListener {
            Log.d(TAG, "btnLogin klik -> onLoginClicked()")
            lifecycleScope.launch {
                controller.onLoginClicked()
            }
        }

        // Auto-rellenar (azken email-a)
        lifecycleScope.launch {
            Log.d(TAG, "Auto-fill: lastLoginRepo.getLastEmail()")
            val last = lastLoginRepo.getLastEmail()
            Log.d(TAG, "Auto-fill: lastEmailLen=${last.length}")

            if (last.isNotBlank() && etEmail.text.isNullOrBlank()) {
                Log.i(TAG, "Auto-fill aplikatzen")
                etEmail.setText(last)
            }
        }
    }

    // ---- LoginController.View ----

    override fun getEmail(): String = etEmail.text?.toString().orEmpty()

    override fun getPass(): String = etPass.text?.toString().orEmpty()

    /**
     * Loading egoera: botoiak desgaitu eta testua aldatu.
     */
    override fun setLoading(isLoading: Boolean) {
        Log.d(TAG, "setLoading(): $isLoading")

        btnLogin.isEnabled = !isLoading
        btnGoSignup.isEnabled = !isLoading

        btnLogin.text = if (isLoading) "Konektatzen..." else "Sartu"
    }

    override fun showMessage(msg: String) {
        Log.d(TAG, "showMessage(): $msg")
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun goTo(intent: Intent, finish: Boolean) {
        Log.i(TAG, "goTo(): ${intent.component?.className} finish=$finish")
        startActivity(intent)
        if (finish) finish()
    }

    override fun context(): Context = this
}