package com.example.jostailudendajosu

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


//Adaptadorea produktuentzat
class ProduktuaAdapter(private val produktuakList: List<Produktuak>) : RecyclerView.Adapter<ProduktuaAdapter.ProduktuaViewHolder>() {

    inner class ProduktuaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val izenburua: TextView = itemView.findViewById(R.id.izenburuaTextView)
        val mota: TextView = itemView.findViewById(R.id.motaTextView)
        val adin: TextView = itemView.findViewById(R.id.adinTextView)
        val jatorria: TextView = itemView.findViewById(R.id.jatorriaTextView)
        val prezioa: TextView = itemView.findViewById(R.id.prezioaTextView)
        val eskuragarri: CheckBox = itemView.findViewById(R.id.eskuragarriCheckBox)
        val productImageView: ImageView = itemView.findViewById(R.id.imageView2)

        //Funtzioa produktuak sortzeko
        fun bind(produktu: Produktuak) {
            izenburua.text = produktu.izenburua
            mota.text = produktu.mota
            adin.text = produktu.adin.toString()
            jatorria.text = produktu.jatorria
            prezioa.text = "%.2f".format(produktu.prezioa)
            eskuragarri.isChecked = produktu.eskuragarri

            //Log.d("ProduktuaAdapter", "Product ID: ${produktu.codigo}")
            //Argazkia jartzeko item bakoitzean
            Log.d("ProduktuaAdapter", "Jatorri: ${produktu.mota}")
            when (produktu.mota) {
                "Hezkuntza" -> productImageView.setImageResource(R.drawable.hezkuntza)
                "Umeentzat" -> productImageView.setImageResource(R.drawable.umeentzat)
                "Zientzia" -> productImageView.setImageResource(R.drawable.zientzia)
                else -> {
                    Log.d("ProduktuaAdapter", "No matching image for: ${produktu.mota}")

                }
            }
            //Gainean Klikatzeko produktua alda dezan
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra("PRODUCT_ID", produktu.codigo)
                //Log.d("ProductDetailActivity", "Product ID: ${produktu.codigo}")
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduktuaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.produktu_item, parent, false)
        return ProduktuaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProduktuaViewHolder, position: Int) {
        val produktua = produktuakList[position]
        holder.bind(produktua)
    }
    override fun getItemCount(): Int {
        return produktuakList.size
    }
}
