package com.example.errepasozkoariketa_azterketa.view

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.errepasozkoariketa_azterketa.controller.VueloControler
import com.example.errepasozkoariketa_azterketa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val controller = VueloControler()

    private var lista = ArrayList<String>()
    private var selected = -1

    private lateinit var adapter: VueloAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        load()
        setupRecycler()
        events()
    }

    private fun setupSpinner() {
        binding.spinnerTipo.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            controller.getTipos()
        )
    }

    private fun load() {
        lista = controller.getAll(this)
    }

    private fun setupRecycler() {
        adapter = VueloAdapter(lista) { pos ->
            selected = pos
            fill(pos)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun events() {

        binding.btnGehitu.setOnClickListener {

            if (!controller.validate(
                    binding.etCodigo.text.toString(),
                    binding.etOrigen.text.toString(),
                    binding.etFecha.text.toString()
                )
            ) {
                binding.etFecha.error = "Formato inválido (yyyy-MM-dd)"
                return@setOnClickListener
            }

            controller.add(
                this,
                binding.etCodigo.text.toString(),
                binding.spinnerTipo.selectedItem.toString(),
                binding.etOrigen.text.toString(),
                binding.etDestino.text.toString(),
                binding.etFecha.text.toString(),
                binding.etAerolinea.text.toString(),
                binding.checkDisponible.isChecked
            )

            refresh()
            clear()
        }

        binding.btnEditatu.setOnClickListener {

            if (selected == -1) return@setOnClickListener

            controller.edit(
                this,
                selected,
                binding.etCodigo.text.toString(),
                binding.spinnerTipo.selectedItem.toString(),
                binding.etOrigen.text.toString(),
                binding.etDestino.text.toString(),
                binding.etFecha.text.toString(),
                binding.etAerolinea.text.toString(),
                binding.checkDisponible.isChecked
            )

            refresh()
            clear()
        }

        binding.btnEzabatu.setOnClickListener {

            if (selected == -1) return@setOnClickListener

            controller.delete(this, selected)

            refresh()
            clear()
        }
    }

    private fun refresh() {
        lista = controller.getAll(this)
        adapter.update(lista)
        selected = -1
    }

    private fun fill(pos: Int) {
        val model = controller.parse(lista[pos])

        binding.etCodigo.setText(model.codigo)
        binding.etOrigen.setText(model.origen)
        binding.etDestino.setText(model.destino)
        binding.etFecha.setText(model.fecha)
        binding.etAerolinea.setText(model.aerolinea)
        binding.checkDisponible.isChecked = model.disponible

        binding.spinnerTipo.setSelection(
            controller.getIndex(model.tipo.name)
        )
    }

    private fun clear() {
        binding.etCodigo.text.clear()
        binding.etOrigen.text.clear()
        binding.etDestino.text.clear()
        binding.etFecha.text.clear()
        binding.etAerolinea.text.clear()
        binding.checkDisponible.isChecked = false
        selected = -1
    }
}