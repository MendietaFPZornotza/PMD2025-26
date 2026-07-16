package com.example.a2finalaazterketa.view

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2finalaazterketa.controller.BideoJokoKontroler
import com.example.a2finalaazterketa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val controller = BideoJokoKontroler()

    private var lista = ArrayList<String>()
    private var selected = -1

    private lateinit var adapter: BideoJokoaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        load()
        setupRecycler()
        events()
    }

    // --------------------------
    // SPINNER
    // --------------------------
    private fun setupSpinner() {
        binding.spinnerGeneroa.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            controller.getGeneroak()
        )
    }

    // --------------------------
    // LOAD
    // --------------------------
    private fun load() {
        lista = controller.getAll(this)
    }

    // --------------------------
    // RECYCLER
    // --------------------------
    private fun setupRecycler() {
        adapter = BideoJokoaAdapter(lista) { pos ->
            selected = pos
            fill(pos)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    // --------------------------
    // EVENTS
    // --------------------------
    private fun events() {

        binding.btnGehitu.setOnClickListener {

            val error = controller.validate(
                binding.etIzena.text.toString(),
                binding.spinnerGeneroa.selectedItem.toString(),
                binding.etUrtea.text.toString(),
                binding.etPlataforma.text.toString(),
                binding.etEnpresa.text.toString()
            )

            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val logicError = controller.validateLogic(
                binding.spinnerGeneroa.selectedItem.toString(),
                binding.checkMultiplayer.isChecked
            )

            if (logicError != null) {
                Toast.makeText(this, logicError, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            controller.add(
                this,
                binding.etIzena.text.toString(),
                binding.spinnerGeneroa.selectedItem.toString(),
                binding.etUrtea.text.toString(),
                binding.etPlataforma.text.toString(),
                binding.etEnpresa.text.toString(),
                binding.checkMultiplayer.isChecked
            )

            Toast.makeText(this, "Gehituta", Toast.LENGTH_SHORT).show()

            refresh()
            clear()
        }

        binding.btnEditatu.setOnClickListener {

            if (selected == -1) return@setOnClickListener

            controller.edit(
                this,
                selected,
                binding.etIzena.text.toString(),
                binding.spinnerGeneroa.selectedItem.toString(),
                binding.etUrtea.text.toString(),
                binding.etPlataforma.text.toString(),
                binding.etEnpresa.text.toString(),
                binding.checkMultiplayer.isChecked
            )

            Toast.makeText(this, "Editatuta", Toast.LENGTH_SHORT).show()

            refresh()
            clear()
        }

        binding.btnEzabatu.setOnClickListener {

            if (selected == -1) return@setOnClickListener

            controller.delete(this, selected)

            Toast.makeText(this, "Ezabatuta", Toast.LENGTH_SHORT).show()

            refresh()
            clear()
        }
    }

    // --------------------------
    // HELPERS
    // --------------------------
    private fun refresh() {
        lista = controller.getAll(this)
        adapter.update(lista)
        selected = -1
    }

  /**  private fun fill(pos: Int) {
        val model = controller.parse(lista[pos])

        binding.etIzena.setText(model.izena)
        binding.etUrtea.setText(model.urtea.toString())
        binding.etPlataforma.setText(model.plataformak)
        binding.etEnpresa.setText(model.enpresa)
        binding.checkMultiplayer.isChecked = model.multiplayer

        binding.spinnerGeneroa.setSelection(
            controller.getIndex(model.generoa.name)
        )
    }**/
  private fun fill(pos: Int) {

      val p = controller.getFields(lista[pos])

      binding.etIzena.setText(p[0])
      binding.etUrtea.setText(p[2])
      binding.etPlataforma.setText(p[3])
      binding.etEnpresa.setText(p[4])
      binding.checkMultiplayer.isChecked = p[5].toBoolean()

      binding.spinnerGeneroa.setSelection(
          controller.getIndex(p[1])
      )
  }

    private fun clear() {
        binding.etIzena.text.clear()
        binding.etUrtea.text.clear()
        binding.etPlataforma.text.clear()
        binding.etEnpresa.text.clear()
        binding.checkMultiplayer.isChecked = false
        selected = -1
    }
}