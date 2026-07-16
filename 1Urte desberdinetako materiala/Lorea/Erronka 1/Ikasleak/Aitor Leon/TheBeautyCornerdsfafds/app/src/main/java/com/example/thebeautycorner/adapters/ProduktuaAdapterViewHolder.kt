package com.example.thebeautycorner.adapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thebeautycorner.R

// Elementuak kargatzeko funtzioa
class ProduktuaAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nombre: TextView = itemView.findViewById(R.id.txtProdIzena)
    val mota : TextView = itemView.findViewById(R.id.txtProdMota)
    val precio: TextView = itemView.findViewById(R.id.txtProdPrezioa)
    val btnAukeratu : Button = itemView.findViewById(R.id.btnAukeratu)
    val btnBorratu : Button = itemView.findViewById(R.id.btnBorratu)
}
