package com.example.proiekto_denda

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var otherRadioButton: RadioButton
    private lateinit var citySpinner: Spinner
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Ikuspegiak inicializatu
        usernameEditText = findViewById(R.id.usernameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        maleRadioButton = findViewById(R.id.maleRadioButton)
        femaleRadioButton = findViewById(R.id.femaleRadioButton)
        otherRadioButton = findViewById(R.id.otherRadioButton)
        citySpinner = findViewById(R.id.citySpinner)
        registerButton = findViewById(R.id.registerButton)
        loginLink = findViewById(R.id.loginLink)

        dbHelper = DBHelper(this)

        // Spinner-a hiri aukerekin bete
        val cities = arrayOf("Madrid", "Barcelona", "Valencia")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        // Erregistro botoia konfiguratu
        registerButton.setOnClickListener {
            registerUser()
        }

        // Login-era joateko esteka konfiguratu
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser() {
        // Erabiltzaileak sartutako balioak lortu
        val username = usernameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val gender = when {
            maleRadioButton.isChecked -> "Gizonezkoa"
            femaleRadioButton.isChecked -> "Emakumezkoa"
            else -> "Bestea"
        }
        val city = citySpinner.selectedItem.toString()

        // Posta elektronikoa balidatu
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Mesedez, sartu posta elektroniko baliodun bat", Toast.LENGTH_SHORT).show()
            return
        }

        // Posta elektronikoa dagoeneko erregistratuta dagoela egiaztatu
        if (dbHelper.checkEmailExists(email)) {
            Toast.makeText(this, "Posta elektroniko hau dagoeneko erregistratuta dago", Toast.LENGTH_SHORT).show()
            return
        }

        // Datuak datu-basean sartu
        val isInserted = dbHelper.insertUser(username, email, password, gender, city)

        if (isInserted) {
            Toast.makeText(this, "Erabiltzailea ongi erregistratu da", Toast.LENGTH_SHORT).show()
            // Login-era bideratu
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Errorea erabiltzailea erregistratzean", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@")
    }
}
