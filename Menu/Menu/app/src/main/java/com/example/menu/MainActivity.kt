package com.example.menu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var textViewMain: TextView
    private lateinit var relativeLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewMain = findViewById(R.id.textViewMain)
        relativeLayout = findViewById(R.id.relativeLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item1 -> {
                // Opción 1: Cambiar el texto del TextView
                textViewMain.text = "Has seleccionado Opción 1"
                true
            }
            R.id.item2 -> {
                // Opción 2: Abrir una nueva actividad
                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.subitem1 -> {
                // Sub Opción 1: Mostrar un diálogo
                showDialog()
                true
            }
            R.id.subitem2 -> {
                // Sub Opción 2: Cambiar el color de fondo
                changeBackgroundColor()
                true
            }
            R.id.item4 -> {
                // Opción 3: Salir de la aplicación
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(this)
            .setTitle("Diálogo")
            .setMessage("Has seleccionado Sub Opción 1")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun changeBackgroundColor() {
        // Lista de colores para cambiar el fondo de forma aleatoria
        val colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN)
        val randomColor = colors.random()
        relativeLayout.setBackgroundColor(randomColor)
    }
}
