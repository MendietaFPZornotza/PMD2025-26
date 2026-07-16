package com.example.mvc_sd.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mvc_sd.controller.SDFileController
import com.example.mvc_sd.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: SDFileController

    private val STORAGE_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Storage baimena egiaztatu
        if (!checkStoragePermission()) requestStoragePermission()

        // Controller sortu
        controller = SDFileController(this)

        // ListView kargatu
        loadNames()

        // Gorde botoia sakatzean
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            if (name.isNotEmpty()) {
                controller.saveName(name)
                binding.etName.text.clear()
                Toast.makeText(this, "Izen berria gehitu da: $name", Toast.LENGTH_SHORT).show()
                loadNames()
            } else {
                Toast.makeText(this, "Sartu izena mesedez", Toast.LENGTH_SHORT).show()
            }
        }

        // ListView elementu bat sakatzean → ezabatu
        binding.listNames.setOnItemClickListener { _, _, position, _ ->
            val selectedName = binding.listNames.adapter.getItem(position) as String
            controller.deleteName(selectedName)
            Toast.makeText(this, "Izen ezabatu da: $selectedName", Toast.LENGTH_SHORT).show()
            loadNames()
        }
    }

    // ListView eguneratzeko metodoa
    private fun loadNames() {
        val names = controller.getAllNames()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
        binding.listNames.adapter = adapter
    }

    // Storage baimena egiaztatzeko
    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    // Baimena eman edo ez erantzuteko
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Storage baimena onartu da", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Storage baimena beharrezkoa da", Toast.LENGTH_LONG).show()
            }
        }
    }
}