package com.example.proiekto_denda

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameAdapter(
    private val context: Context,
    private val gameList: List<DBHelper.Game>,
    private val onGameClick: (DBHelper.Game) -> Unit // Callback-a klikak kudeatzeko
) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gameList[position]
        holder.tvTitle.text = game.title
        holder.tvGenre.text = "Generoa: ${game.genre}"
        holder.tvPlatforms.text = "Plataformak: ${game.platforms}"
        holder.tvPrice.text = "Prezioa: ${game.price}€"
        holder.tvAvailability.text = if (game.available) "Eskuragarri" else "Ez dago eskuragarri"
        holder.tvDescription.text = "Deskribapena: ${game.description}"

        // Ekin klikaren listener-a gehitu
        holder.itemView.setOnClickListener {
            onGameClick(game) // Exekutatzen du callback-a aukeratutako jokoarekin
        }
    }

    override fun getItemCount(): Int = gameList.size

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvGenre: TextView = itemView.findViewById(R.id.tvGenre)
        val tvPlatforms: TextView = itemView.findViewById(R.id.tvPlatforms)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvAvailability: TextView = itemView.findViewById(R.id.tvAvailability)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    }
}
