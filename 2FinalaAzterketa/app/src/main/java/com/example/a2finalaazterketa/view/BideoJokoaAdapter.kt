package com.example.a2finalaazterketa.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BideoJokoaAdapter(
    private var lista: List<String>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<BideoJokoaAdapter.Holder>() {

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val text: TextView = v.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val p = lista[position].split(";")

        val izena = p[0]
        val generoa = p[1]
        val urtea = p[2]
        val plataforma = p[3]
        val enpresa = p[4]
        val multi = if (p[5].toBoolean()) "Multi" else "Solo"

        val texto = "$izena ($generoa) - $urtea | $plataforma | $enpresa | $multi"

        holder.text.text = texto

        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }

    override fun getItemCount() = lista.size

    fun update(newList: List<String>) {
        lista = newList
        notifyDataSetChanged()
    }
}