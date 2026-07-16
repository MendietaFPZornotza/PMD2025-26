package com.example.mvc_layouts.view

import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mvc_layouts.R
import com.example.mvc_layouts.controller.UserController
import com.example.mvc_layouts.databinding.ActivityMainBinding
/**
 * Erabiltzailearen interfazea kudeatzen duen View-a
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val kontrolatzailea = UserController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * GEHITU botoia
         */
        binding.btnGehitu.setOnClickListener {
            val izena = binding.etIzena.text.toString()
            val abizena = binding.etAbizena.text.toString()
            val adina = binding.etAdina.text.toString().toIntOrNull() ?: 0

            kontrolatzailea.gehituErabiltzailea(izena, abizena, adina)

            // Garbiketa
            binding.etIzena.text.clear()
            binding.etAbizena.text.clear()
            binding.etAdina.text.clear()
            binding.etIzena.requestFocus()
        }

        /**
         * IKUSI GUZTIAK botoia
         */
        binding.btnIkusi.setOnClickListener {

            // Taula garbitu goiburua izan ezik
            binding.taulaErabiltzaileak.removeViews(
                1,
                binding.taulaErabiltzaileak.childCount - 1
            )

            // Erabiltzaile lista osoa lortu kontrolerretik
            val erabiltzaileak = kontrolatzailea.lortuErabiltzaileak()

            // Erabiltzaile bakoitza gehitu
            for ((izena, abizena, adina) in erabiltzaileak) {

                val fila = TableRow(this)

                val tvIzena = TextView(this)
                tvIzena.text = izena

                val tvAbizena = TextView(this)
                tvAbizena.text = abizena

                val tvAdina = TextView(this)
                tvAdina.text = adina.toString()

                fila.addView(tvIzena)
                fila.addView(tvAbizena)
                fila.addView(tvAdina)

                binding.taulaErabiltzaileak.addView(fila)
            }
        }
    }
}