package com.example.erronka_proba.view.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.erronka_proba.R
import com.example.erronka_proba.controller.auth.SignupController
import com.example.erronka_proba.data.tcp.AuthTcpService
import com.example.erronka_proba.data.tcp.SessionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

/**
 * Fitxategia: SignupActivity.kt
 *
 * Zertarako da?
 * - Kontua sortzeko pantaila (UI): izena/email/pasahitzak sartu eta kontua sortu.
 *
 * Zer egiten du?
 * - UI osagaiak lotu.
 * - SignupController-en bidez signup prozesua abiarazi.
 * - "Atzera" botoian finish().
 *
 * Nola erabiltzen da?
 * - LoginActivity-tik nabigatuta iristen da normalean.
 *
 * Log-ak:
 * - Lifecycle, botoi klikak eta loading egoera.
 */
class SignupActivity : AppCompatActivity(), SignupController.View {

    companion object {
        private const val TAG = "SignupActivity"
    }

    private lateinit var controller: SignupController

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPass: TextInputEditText
    private lateinit var etPass2: TextInputEditText
    private lateinit var btnSignup: MaterialButton
    private lateinit var btnBackLogin: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate(): SignupActivity")

        setTheme(R.style.Theme_ErronkaProba)
        setContentView(R.layout.activity_signup)

        // UI osagaiak
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPass = findViewById(R.id.etPass)
        etPass2 = findViewById(R.id.etPass2)
        btnSignup = findViewById(R.id.btnSignup)
        btnBackLogin = findViewById(R.id.btnBackLogin)

        // Controller (scope: lifecycleScope)
        controller = SignupController(
            view = this,
            scope = lifecycleScope,
            auth = AuthTcpService(),
            session = SessionManager(this)
        )

        // Atzera login-era
        btnBackLogin.setOnClickListener {
            Log.d(TAG, "btnBackLogin klik -> finish()")
            finish()
        }

        // Kontua sortu
        btnSignup.setOnClickListener {
            Log.d(TAG, "btnSignup klik -> controller.onSignupClicked()")
            controller.onSignupClicked()
        }
    }

    // ---- SignupController.View ----

    override fun getName(): String = etName.text?.toString().orEmpty()

    override fun getEmail(): String = etEmail.text?.toString().orEmpty()

    override fun getPass1(): String = etPass.text?.toString().orEmpty()

    override fun getPass2(): String = etPass2.text?.toString().orEmpty()

    override fun setLoading(loading: Boolean) {
        Log.d(TAG, "setLoading(): $loading")
        btnSignup.isEnabled = !loading
        btnSignup.text = if (loading) "Sortzen..." else "Sortu kontua"
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