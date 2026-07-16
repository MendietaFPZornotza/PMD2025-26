package com.example.mvc_sharedprefs_izenanitzak.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.mvc_sharedprefs_izenanitzak.controller.PrefsController
import com.example.mvc_sharedprefs_izenanitzak.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: PrefsController
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = PrefsController(this)

        // Adapterra sortu izenak ListView batean erakusteko
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        binding.lvNames.adapter = adapter

        configView()

        // "Gorde" botoia
        binding.btnSave.setOnClickListener {
            val input = binding.etName.text.toString().trim()
            if (input.isNotEmpty() && !controller.nameExists(input)) {
                controller.addName(input)
                binding.etName.text.clear()
                configView()
            }
        }

        // "Ezabatu" botoia
        binding.btnDelete.setOnClickListener {
            val input = binding.etName.text.toString().trim()
            if (input.isNotEmpty() && controller.nameExists(input)) {
                controller.removeName(input)
                binding.etName.text.clear()
                configView()
            }
        }
    }

    private fun configView() {
        val names = controller.getNames()
        adapter.clear()
        adapter.addAll(names)
        adapter.notifyDataSetChanged()

        if (names.isEmpty()) {
            binding.tvInfo.text = "Ez dago izenik gordeta. Sartu izena eta sakatu Gorde."
        } else {
            binding.tvInfo.text = "Gordetako izenak:"
        }
    }
}