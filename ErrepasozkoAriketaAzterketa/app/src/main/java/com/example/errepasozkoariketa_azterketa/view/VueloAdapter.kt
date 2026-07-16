package com.example.errepasozkoariketa_azterketa.view


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VueloAdapter(
    private var lista: List<String>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<VueloAdapter.Holder>() {

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val text = v.findViewById<TextView>(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val p = lista[position].split(";")

        val codigo = p[0]
        val origen = p[2]
        val destino = p[3]
        val fecha = p[4]

        holder.text.text =
            "$codigo - $origen → $destino ($fecha)"

        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }

    override fun getItemCount(): Int = lista.size

    fun update(newList: List<String>) {
        lista = newList
        notifyDataSetChanged()
    }
}