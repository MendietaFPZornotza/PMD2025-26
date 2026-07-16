package com.example.erronka.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.erronka.R
import com.example.erronka.databinding.ActivitySelectionBinding
class SelectionActivity : AppCompatActivity() {

        private lateinit var binding: ActivitySelectionBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivitySelectionBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Acción del botón "Kontsultak"
            binding.btnKontsultak.setOnClickListener {
                Toast.makeText(this, "Has pulsado Kontsultak", Toast.LENGTH_SHORT).show()
                // Aquí podrías abrir otra Activity:
                startActivity(Intent(this, PrimaryActivity::class.java))
            }

            // Acción del botón "Gestioa"
            binding.btnGestioa.setOnClickListener {
                Toast.makeText(this, "Has pulsado Gestioa", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ManagementActivity::class.java))
            }
        }
    }