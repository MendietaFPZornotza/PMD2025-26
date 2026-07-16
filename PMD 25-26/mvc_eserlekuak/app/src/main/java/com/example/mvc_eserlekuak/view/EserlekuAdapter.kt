package com.example.mvc_eserlekuak.view


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvc_eserlekuak.R
import com.example.mvc_eserlekuak.controller.EserlekuKontrolatzailea
import com.example.mvc_eserlekuak.controller.EserlekuaDisplay


class EserlekuAdapter(
    private var eserlekuak: List<EserlekuaDisplay>,
    private val kontrolatzailea: EserlekuKontrolatzailea,
    private val txtHautatua: TextView
) : RecyclerView.Adapter<EserlekuAdapter.EserlekuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EserlekuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_eserlekua, parent, false)
        return EserlekuViewHolder(view)
    }

    override fun onBindViewHolder(holder: EserlekuViewHolder, position: Int) {
        val eserlekua = eserlekuak[position]

        holder.img.setImageResource(
            when {
                eserlekua.okupatua -> R.drawable.eserlekuokupatua
                eserlekua.hautatua -> R.drawable.eserlekuhautatua
                else -> R.drawable.eserlekulibrea
            }
        )

        holder.img.setOnClickListener {
            // Controller-era bidali klik-a
            kontrolatzailea.onEserlekuaClicked(position)

            // Berriro DTO-ak hartu eta UI freskatu
            eserlekuak = kontrolatzailea.getEserlekuakDisplay()
            notifyDataSetChanged()

            // TextView eguneratu
            val updated = eserlekuak[position]
            if (updated.hautatua) {
                txtHautatua.text = "Eserlekua hautatua: Ilara ${updated.ilara + 1}, Zutabea ${updated.zutabea + 1}"
            } else {
                txtHautatua.text = "Ez da eserlekua hautatu"
            }
        }
    }

    override fun getItemCount() = eserlekuak.size

    class EserlekuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgEserlekua)
    }
}