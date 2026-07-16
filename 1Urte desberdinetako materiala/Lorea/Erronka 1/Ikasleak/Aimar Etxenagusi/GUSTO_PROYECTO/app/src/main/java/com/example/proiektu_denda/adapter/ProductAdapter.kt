package com.example.proiektu_denda

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.proiektu_denda.modelo.Producto

class ProductAdapter(private val productList: List<Producto>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    //Hartzen dira datu bakoitzak eta variableetan gordetzen dira
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val izena: TextView = itemView.findViewById(R.id.izena)
        val mota: TextView = itemView.findViewById(R.id.mota)
        val prezioa: TextView = itemView.findViewById(R.id.prezioa)
        val eskuragarri: TextView = itemView.findViewById(R.id.eskuragarri)
        val imagen: ImageView = itemView.findViewById(R.id.imageView3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.producto_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        Log.d("ProductList", "Price in adapter: ${product.prezioa}")

        //Hau da listako item-a egiteko
        holder.izena.text = product.izena
        holder.mota.text = product.mota
        holder.prezioa.text = "€${product.prezioa}"
        if (product.eskuragarritasuna == "Bai") {
            holder.eskuragarri.text = "Eskuragarri"
            holder.eskuragarri.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        } else {
            holder.eskuragarri.text = "Ez eskuragarri"
            holder.eskuragarri.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        }

        //item-eko argazkia
        when (product.mota) {
            "Barazkiak eta frutak" -> holder.imagen.setImageResource(R.drawable.barazkiak)
            "Haragiak" -> holder.imagen.setImageResource(R.drawable.haragia)
            "Arrainak eta itsaskiak" -> holder.imagen.setImageResource(R.drawable.pescado)
            "Esnekiak" -> holder.imagen.setImageResource(R.drawable.lacteos)
            "Zerealak eta haziak" -> holder.imagen.setImageResource(R.drawable.cereales)
        }

        //listan produktua klikatzean, aukeratutako produktuaren datuak bidaltzeko
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("productId", product.id)
                putExtra("productName", product.izena)
                putExtra("productType", product.mota)
                putExtra("productPrice", product.prezioa)
                putExtra("productOrigin", product.jatorria) // Si tienes ese dato
                putExtra("productIngredients", product.osagaiNagusia) // Si tienes ese dato
                putExtra("productAvailability", product.eskuragarritasuna)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = productList.size
}
