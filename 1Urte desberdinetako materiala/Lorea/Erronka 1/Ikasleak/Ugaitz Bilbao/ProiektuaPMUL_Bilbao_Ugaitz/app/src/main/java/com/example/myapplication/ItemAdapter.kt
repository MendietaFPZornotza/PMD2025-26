package com.example.myapplication

import Produktuak
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NombreAdapter(private var produktuak: List<Produktuak>) :
    RecyclerView.Adapter<NombreAdapter.NombreViewHolder>() {

    private val selectedItems = mutableSetOf<Produktuak>() // Almacena los productos seleccionados

    class NombreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val elizenburua: TextView = itemView.findViewById(R.id.ELIzenburua)
        val elkategorioa: TextView = itemView.findViewById(R.id.ELKategoria)
        val elprezioa: TextView = itemView.findViewById(R.id.ELPrezioa)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NombreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produktuak, parent, false)
        return NombreViewHolder(view)
    }

    override fun onBindViewHolder(holder: NombreViewHolder, position: Int) {
        val produktua = produktuak[position]


        holder.elizenburua.text = "Izena: ${produktua.izenburua}"
        holder.elkategorioa.text = "Mota: ${produktua.kategoria}"
        holder.elprezioa.text = "Prezioa: ${produktua.prezioa} €"


        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectedItems.contains(produktua)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(produktua)
            } else {
                selectedItems.remove(produktua)
            }
        }
    }

    override fun getItemCount(): Int {
        return produktuak.size
    }


    fun getSelectedItems(): List<Produktuak> {
        return selectedItems.toList()
    }


    fun updateData(newData: List<Produktuak>) {
        produktuak = newData
        notifyDataSetChanged()
    }


    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }
}