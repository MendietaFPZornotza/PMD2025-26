package com.example.thebeautycorner.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.thebeautycorner.AdminSQLiteOpenHelper
import com.example.thebeautycorner.ProduktuaEditatu
import com.example.thebeautycorner.R
import com.example.thebeautycorner.model.ProdListatu

class ProduktuaAdapter(private val produktuak: List<ProdListatu>) : RecyclerView.Adapter<ProduktuaAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduktuaAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_unico_produktuak, parent, false)
        return ProduktuaAdapterViewHolder(view)
    }

    // Gure produktuen informazioa idazteko dagokion etiketarekin
    override fun onBindViewHolder(holder: ProduktuaAdapterViewHolder, position: Int) {
        val produktua = produktuak[position]

        // Para el campo "Izena"
        val izenaLabel = "Izena: "
        val izenaValue = produktua.izena
        holder.nombre.text = makeBoldLabel(izenaLabel, izenaValue)

        // Para el campo "Mota"
        val motaLabel = "Mota: "
        val motaValue = produktua.mota
        holder.mota.text = makeBoldLabel(motaLabel, motaValue)

        // Para el campo "Prezioa"
        val prezioaLabel = "Prezioa: "
        val prezioaValue = produktua.prezioa.toString() + "€"
        holder.precio.text = makeBoldLabel(prezioaLabel, prezioaValue)

        holder.btnAukeratu.setOnClickListener {
            // Produktuaren id-a gorde beste pantailan datuak kargatzeko
            val intent = Intent(holder.itemView.context, ProduktuaEditatu::class.java)
            intent.putExtra("productoId", produktua.id) // Pasar el ID del producto
            holder.itemView.context.startActivity(intent)
        }

        // Produktua borratu
        holder.btnBorratu.setOnClickListener {
            prodBorratu(produktua.id, position, holder)
        }
    }

    // Produktua borratzeko funtzioa
    private fun prodBorratu(idProd: Int, position: Int, holder: ProduktuaAdapterViewHolder) {
        val context = holder.itemView.context
        val admin = AdminSQLiteOpenHelper(context, "thebeautycorner", null, 2)
        val db = admin.writableDatabase

        // AlertDialog elementua sortu produktua benetan borratu nahi den galdtzeko
        val alertDialog = android.app.AlertDialog.Builder(context)
            .setTitle("Eliminazioa Konfirmatu")
            .setMessage("¿Seguru zaude produktua borratu nahi duzula?")
            .setPositiveButton("Bai") { _, _ ->
                try {
                    db.execSQL("DELETE FROM Produktuak WHERE kodea = ?", arrayOf(idProd.toString()))
                    db.close()

                    // Lista mutablea sortu gero eguneratzeko
                    (produktuak as MutableList).removeAt(position)
                    notifyItemRemoved(position)

                    Toast.makeText(context, "Produktua Borratu Da", Toast.LENGTH_SHORT).show()

                    val activity = holder.itemView.context as? Activity
                    activity?.recreate()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Errorea Produktua Borratzean", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Ez") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()  // Mostrar el cuadro de diálogo
    }


    // Textua normal izateko eta bere etiketa negritan egoteko funtzioa
    private fun makeBoldLabel(label: String, value: String): CharSequence {
        val spannable = android.text.SpannableString("$label$value")
        spannable.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            0,
            label.length,
            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    // Kolumnen zenbaki kantitatea kalkulatzeko funtzioa
    override fun getItemCount(): Int {
        return produktuak.size
    }
}
