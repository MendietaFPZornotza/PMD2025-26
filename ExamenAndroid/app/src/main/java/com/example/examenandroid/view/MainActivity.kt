package com.example.examenandroid.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examenandroid.R
import com.example.examenandroid.controller.EventoController
import com.example.examenandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: EventoController
    private lateinit var adapter: EventoAdapter

    private var idEventoSeleccionado: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = EventoController(this)

        adapter = EventoAdapter(controller.obtenerEventos().toMutableList()) { evento ->
            idEventoSeleccionado = evento.id
            cargarEventoEnFormulario(evento)
        }

        binding.recyclerEventos.layoutManager = LinearLayoutManager(this)
        binding.recyclerEventos.adapter = adapter
    }

    /* ================= MENUA ================= */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_eventos, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_agregar -> agregarEvento()
            R.id.menu_editar -> editarEvento()
            R.id.menu_eliminar -> eliminarEvento()
        }
        return super.onOptionsItemSelected(item)
    }

    /* ================= CRUD ================= */

    private fun agregarEvento() {
        controller.agregarEvento(
            nombre = binding.etNombre.text.toString(),
            descripcion = binding.etDescripcion.text.toString(),
            fecha = binding.etFecha.text.toString(),
            lugar = binding.etLugar.text.toString(),
            tipo = binding.etTipo.text.toString()
        )
        adapter.update(controller.obtenerEventos())
        limpiarFormulario()
        Toast.makeText(this, "Evento gehitu da", Toast.LENGTH_SHORT).show()
    }

    private fun editarEvento() {
        val id = idEventoSeleccionado ?: run {
            Toast.makeText(this, "Aukeratu eventoa lehenengo", Toast.LENGTH_SHORT).show()
            return
        }
        controller.actualizarEvento(
            id = id,
            nombre = binding.etNombre.text.toString(),
            descripcion = binding.etDescripcion.text.toString(),
            fecha = binding.etFecha.text.toString(),
            lugar = binding.etLugar.text.toString(),
            tipo = binding.etTipo.text.toString()
        )
        adapter.update(controller.obtenerEventos())
        limpiarFormulario()
        idEventoSeleccionado = null
        Toast.makeText(this, "Evento editatu da", Toast.LENGTH_SHORT).show()
    }

    private fun eliminarEvento() {
        val id = idEventoSeleccionado ?: run {
            Toast.makeText(this, "Aukeratu eventoa lehenengo", Toast.LENGTH_SHORT).show()
            return
        }
        controller.eliminarEventoPorId(id)
        adapter.update(controller.obtenerEventos())
        limpiarFormulario()
        idEventoSeleccionado = null
        Toast.makeText(this, "Evento ezabatu da", Toast.LENGTH_SHORT).show()
    }

    /* ================= FORMULARIO ================= */

    private fun cargarEventoEnFormulario(evento: com.example.examenandroid.model.Evento) {
        binding.etNombre.setText(evento.nombre)
        binding.etDescripcion.setText(evento.descripcion)
        binding.etFecha.setText(evento.fecha)
        binding.etLugar.setText(evento.lugar)
        binding.etTipo.setText(evento.tipo)
    }

    private fun limpiarFormulario() {
        binding.etNombre.text?.clear()
        binding.etDescripcion.text?.clear()
        binding.etFecha.text?.clear()
        binding.etLugar.text?.clear()
        binding.etTipo.text?.clear()
    }
}