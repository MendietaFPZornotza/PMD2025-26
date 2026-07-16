package com.example.mvc_eserlekuak.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mvc_eserlekuak.ui.theme.Mvc_eserlekuakTheme



import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvc_eserlekuak.controller.EserlekuKontrolatzailea
import com.example.mvc_eserlekuak.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var kontrolatzailea: EserlekuKontrolatzailea

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kontrolatzailea = EserlekuKontrolatzailea()

        val eserlekuakDTO = kontrolatzailea.getEserlekuakDisplay()

        binding.recyclerEserlekuak.layoutManager = GridLayoutManager(this, 8)
        binding.recyclerEserlekuak.adapter =
            EserlekuAdapter(eserlekuakDTO, kontrolatzailea, binding.txtEserlekuaHautatua)
    }
}