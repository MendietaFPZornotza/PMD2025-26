package com.example.mvc_passwordapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mvc_passwordapp.controller.PasswordController
import com.example.mvc_passwordapp.ui.theme.Mvc_passwordappTheme
import com.example.mvc_passwordapp.R
// VIEW: MainActivity View geruza da.
// Gogoan: hemen ez dago PasswordModel-era erreferentziarik; Controller-era bakarrik deitzen da.
class MainActivity : ComponentActivity() , PasswordController.PasswordView {

    private lateinit var controller: PasswordController
    private lateinit var editPassword: EditText
    private lateinit var buttonSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI elementuak lotu
        editPassword = findViewById(R.id.editPassword)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        // MVC: Controller sortu eta View-interfaze hau pasatu (ez Model)
        controller = PasswordController(this)

        // Botoiaren ekintza: Controller-era soilik pasatzen dugu inputa
        buttonSubmit.setOnClickListener {
            val input = editPassword.text.toString()
            controller.validatePassword(input) // View -> Controller
        }
    }

    // Controller-ek deitzen dituen metodoak inplementatu (View aldetik)
    override fun showSuccess() {
        // Pasahitza zuzena izan da: WelcomeActivity abiarazi
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    override fun showError() {
        // Pasahitz okerra: Toast bidez adierazi
        Toast.makeText(this, "Pasahitz okerra sartu duzu!", Toast.LENGTH_SHORT).show()
    }
}