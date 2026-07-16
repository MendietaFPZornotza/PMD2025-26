package com.example.liburudendaprojektua

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.liburudendaprojektua.LiburuakAdapterViewHolder

class LiburuaAdapter(private val liburuak: List<Liburua>) :
    RecyclerView.Adapter<LiburuakAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiburuakAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_liburuak_erakutsi, parent, false)
        return LiburuakAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: LiburuakAdapterViewHolder, position: Int) {
        val liburua = liburuak[position]
        holder.bind(liburua)


        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, XehetasunakActivity::class.java).apply {
                // Id-a bidaltzen du XehetasunakActivity klaseari
                putExtra("id", liburua.id)
            }
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int = liburuak.size
}
