package com.example.mvc_sharedprefs.view

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mvc_sharedprefs.R
import com.example.mvc_sharedprefs.controller.PrefsController
import com.example.mvc_sharedprefs.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Binding objektua UI elementuak erabiltzeko
    private lateinit var binding: ActivityMainBinding

    // Controller objektua
    private lateinit var controller: PrefsController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding inicializatu layoutarekin
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Controller sortu, context pasatuz
        controller = PrefsController(this)

        // Pantaila konfiguratu izenaren arabera
        configView()

        // "Gorde" botoiaren klik listenerra
        binding.btnSave.setOnClickListener {
            val input = binding.etName.text.toString().trim() // EditText-eko testua hartu eta espazioak kendu
            if (input.isNotEmpty()) { // Testua ez badago hutsik
                controller.saveName(input) // Controller bidez izena gorde
                configView() // UI eguneratu
            }
        }

        // "Ezabatu" botoiaren klik listenerra
        binding.btnDelete.setOnClickListener {
            controller.deleteName() // Controller bidez izena ezabatu
            configView() // UI berriro eguneratu
        }
    }

    // Pantaila konfiguratzeko metodoa
    private fun configView() {
        val name = controller.getName() // Controller bidez izena lortu
        if (name.isNullOrEmpty()) { // Izena ez badago
            binding.etName.visibility = View.VISIBLE // EditText erakutsi
            binding.btnSave.visibility = View.VISIBLE // "Gorde" botoia erakutsi
            binding.tvGreeting.visibility = View.GONE // Testua ezkutatu
            binding.btnDelete.visibility = View.GONE // "Ezabatu" botoia ezkutatu
        } else { // Izena badago
            binding.etName.visibility = View.GONE // EditText ezkutatu
            binding.btnSave.visibility = View.GONE // "Gorde" botoia ezkutatu
            binding.tvGreeting.visibility = View.VISIBLE // Testua erakutsi
            binding.tvGreeting.text = "Ongi etorri, $name!" // Testuan izena jarri
            binding.btnDelete.visibility = View.VISIBLE // "Ezabatu" botoia erakutsi
        }
    }
}