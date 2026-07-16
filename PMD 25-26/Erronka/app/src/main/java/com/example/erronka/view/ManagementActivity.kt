package com.example.erronka.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.erronka.R
import com.example.erronka.databinding.ActivityManagementBinding
class ManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManagementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Ejemplo: llenar spinner de autores
        val autores = listOf("Gabriel García Márquez", "Haruki Murakami", "Jane Austen")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, autores)
        binding.spnAuthor.adapter = adapter

        // Manejo de botones
        binding.btnNewAuthor.setOnClickListener {
            Toast.makeText(this, "Añadir autor nuevo", Toast.LENGTH_SHORT).show()
        }

        binding.btnSaveBook.setOnClickListener {
            Toast.makeText(this, "Libro guardado", Toast.LENGTH_SHORT).show()
        }

        binding.btnAcceptAuthor.setOnClickListener {
            Toast.makeText(this, "Autor aceptado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.management_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_book_new -> { binding.lblOption.text = "New Book"; true }
            R.id.menu_writer_new -> { binding.lblOption.text = "New Writer"; true }
            R.id.menu_genre_new -> { binding.lblOption.text = "New Genre"; true }
            R.id.menu_switch_selection -> { finish(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}