package com.example.proiekto_denda

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DBHelper(this)

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val registerLink: TextView = findViewById(R.id.registerLink)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Posta elektronikoa balioztatu
            if (!isValidEmail(email)) {
                Toast.makeText(this, "Mesedez, sartu posta elektroniko baliodun bat", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dbHelper.checkUser(email, password)) {
                Toast.makeText(this, "Saioa hasi da arrakastaz", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Akatsak daude kredentzialetan", Toast.LENGTH_SHORT).show()
            }
        }

        // RegisterActivity-ra joan, erregistro lotura sakatuz
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@")
    }
}
