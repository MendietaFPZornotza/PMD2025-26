package com.example.proiektua.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.proiektua.R
import com.example.proiektua.modelo.Product

class ProductAdapter(private val context: Context, private var productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // Método para actualizar la lista de productos
    fun updateProducts(newProducts: List<Product>) {
        productList = newProducts  // Reemplazar la lista de productos
        notifyDataSetChanged()  // Notifica al RecyclerView que los datos han cambiado
    }

    // Crear la vista para cada elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_product, parent, false)
        return ProductViewHolder(view)
    }

    private var onEditClickListener: ((Int) -> Unit)? = null

    fun setOnEditClickListener(listener: (Int) -> Unit) {
        onEditClickListener = listener
    }

    private var onDeleteClickListener: ((Int) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (Int) -> Unit) {
        onDeleteClickListener = listener
    }

    // Configurar cada elemento del RecyclerView
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)

        holder.editButton.setOnClickListener {
            onEditClickListener?.invoke(product.id) // Pasar el ID del producto
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClickListener?.invoke(product.id) // Lógica del botón Ezabatu
        }

        // Cambiar la imagen según el tipo de producto
        when (product.type) {
            "Zapatilak" -> holder.productImage.setImageResource(R.drawable.shoe_image)
            "Baloiak" -> holder.productImage.setImageResource(R.drawable.ball_image)
            "Entrenamendu Ekipamendua" -> holder.productImage.setImageResource(R.drawable.equipment_image)
            else -> holder.productImage.setImageResource(R.drawable.miniezineten)
        }
    }

    // Retornar el tamaño de la lista
    override fun getItemCount(): Int = productList.size

    // ViewHolder para cada producto
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.productName)
        private val typeTextView: TextView = itemView.findViewById(R.id.productType)
        private val priceTextView: TextView = itemView.findViewById(R.id.productPrice)
        private val availabilityTextView: TextView = itemView.findViewById(R.id.productAvailability)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val productImage: ImageView = itemView.findViewById(R.id.productImage)

        fun bind(product: Product) {
            nameTextView.text = product.name
            typeTextView.text = product.type
            priceTextView.text = String.format("€ %.2f", product.price)

            // Mostrar disponibilidad
            val availabilityText = if (product.availability == 1) {
                "Badago Stock-a"
            } else {
                "Ez dago eskuragarri"
            }
            availabilityTextView.text = availabilityText

            // Cambiar color de disponibilidad dependiendo del estado
            val availabilityColor = if (product.availability == 1) {
                ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark)
            } else {
                ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark)
            }
            availabilityTextView.setTextColor(availabilityColor)

            // Configurar el botón "Editar"
            editButton.setOnClickListener {
                // Aquí puedes agregar la lógica para editar el producto
            }
        }
    }
}

