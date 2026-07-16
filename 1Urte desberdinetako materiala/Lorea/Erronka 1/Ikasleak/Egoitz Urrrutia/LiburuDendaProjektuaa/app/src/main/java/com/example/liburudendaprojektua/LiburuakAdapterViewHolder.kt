package com.example.liburudendaprojektua

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LiburuakAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textIzenburua: TextView = itemView.findViewById(R.id.textIzenburua)
    private val textEgilea: TextView = itemView.findViewById(R.id.textEgilea)
    private val textPrezioa: TextView = itemView.findViewById(R.id.textPrezioa)
    private val buttonDetalles: Button = itemView.findViewById(R.id.xehetasunak2)

    fun bind(liburua: Liburua) {
        textIzenburua.text = "Izenburua: ${liburua.izenburua}"
        textEgilea.text = "Egilea: ${liburua.egilea}"
        textPrezioa.text = "Prezioa: ${liburua.prezioa} €"

        // Liburuaren xehetasunak ikusteko botoia
        buttonDetalles.setOnClickListener {
            val intent = Intent(itemView.context, XehetasunakActivity::class.java)
            intent.putExtra("id", liburua.id) // Liburuaren id-a bidaltzen du XehetasunakActivity klaseari
            itemView.context.startActivity(intent)
        }
    }
}