package com.example.examenandroid.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.examenandroid.databinding.ItemEventoBinding
import com.example.examenandroid.model.Evento

class EventoAdapter(
    private var eventos: MutableList<Evento>,
    private val onClick: (Evento) -> Unit
) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    inner class EventoViewHolder(val binding: ItemEventoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val binding = ItemEventoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventos[position]
        holder.binding.tvNombre.text = evento.nombre
        holder.binding.tvDescripcion.text = evento.descripcion
        holder.binding.tvFecha.text = evento.fecha
        holder.binding.tvLugar.text = evento.lugar
        holder.binding.tvTipo.text = evento.tipo

        holder.itemView.setOnClickListener { onClick(evento) }
    }

    override fun getItemCount(): Int = eventos.size

    // Lista eguneratu
    fun update(newEventos: List<Evento>) {
        eventos.clear()
        eventos.addAll(newEventos)
        notifyDataSetChanged()
    }
}