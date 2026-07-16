package com.example.mvc_file.view

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mvc_file.R
import com.example.mvc_file.controller.FileController
import com.example.mvc_file.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // ViewBinding objektua UI elementuak lotzeko
    private lateinit var binding: ActivityMainBinding

    // Controller objektua
    private val controller = FileController(this) // Context gabe, Modela barne

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layout-a lotu
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hasierako zerrenda kargatu
        loadNames()

        // "Gorde" botoia sakatzean
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()

            if (name.isNotEmpty()) {
                controller.saveName(name) // Controller bidez izena gorde
                binding.etName.text.clear() // EditText garbitu
                loadNames() // ListView eguneratu
            }
        }

        // ListView-ko elementu bat sakatzean → ezabatu izena
        binding.listNames.setOnItemClickListener { _, _, position, _ ->
            val selectedName = binding.listNames.adapter.getItem(position) as String
            controller.deleteName(selectedName) // Controller bidez ezabatu
            loadNames() // ListView berriro eguneratu
        }
    }

    // ListView-a eguneratzeko metodoa
    private fun loadNames() {
        val names = controller.getAllNames() // Controller bidez izenak lortu

        // Adapter sinple bat sortu
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            names
        )

        // ListView-ra lotu
        binding.listNames.adapter = adapter
    }
}