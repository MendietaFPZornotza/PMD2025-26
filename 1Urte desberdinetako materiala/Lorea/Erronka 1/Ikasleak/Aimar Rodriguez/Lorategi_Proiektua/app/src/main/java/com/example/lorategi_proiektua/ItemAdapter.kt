package com.example.lorategi_proiektua

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val itemList: List<Lorea>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textIzena: TextView = itemView.findViewById(R.id.izenaView)
        val textMota: TextView = itemView.findViewById(R.id.motaView)
        val textPrezioa: TextView = itemView.findViewById(R.id.prezioView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.textIzena.text = currentItem.name
        holder.textMota.text = currentItem.type
        holder.textPrezioa.text = currentItem.price.toString() +"€"

        holder.itemView.setOnClickListener(){
            //coger todos los datos del item seleccionado
            val kod = currentItem.kodea
            System.out.println("KODEA(ItemAdapter): $kod")
            val izena = currentItem.name
            val mota = currentItem.type
            val jatorria = currentItem.origin
            val kolorea = currentItem.color
            val prezioa = currentItem.price
            val stock = currentItem.avaliable
            //mandar los datos a la siguiente actividad
            val intent = Intent(holder.itemView.context, ProductDetailActivity::class.java)
            intent.putExtra("kod", kod)
            intent.putExtra("izena", izena)
            intent.putExtra("mota", mota)
            intent.putExtra("jatorria", jatorria)
            intent.putExtra("kolorea", kolorea)
            intent.putExtra("prezioa", prezioa)
            intent.putExtra("stock", stock)
            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount() = itemList.size
}