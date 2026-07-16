import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.erropadenda.Erropa
import com.example.erropadenda.R

class ItemAdapter(
    private val erropaList: ArrayList<Erropa>,
    private val onItemClicked: (Erropa) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION  // gordetzen da posiszioa click ematen duzun lekuan

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewIzena: TextView = itemView.findViewById(R.id.itemIzena)
        val textViewMota: TextView = itemView.findViewById(R.id.itemMota)
        val textViewPrezioa: TextView = itemView.findViewById(R.id.itemPrezioa)


        fun bind(erropa: Erropa, isSelected: Boolean) {
            textViewIzena.text = erropa.izena
            textViewMota.text = erropa.mota
            textViewPrezioa.text = erropa.prezioa+" €"
            //Aldatzeko fondoa aukeratzen badu edo ez
            itemView.setBackgroundColor(
                if (isSelected) itemView.context.getColor(R.color.colorSelected)
                else itemView.context.getColor(R.color.colorUnselected)
            )

            // TextView-ean click ematean
            itemView.setOnClickListener {
                // Posizioa eguneratu
                val previousPosition = selectedPosition
                selectedPosition = bindingAdapterPosition

                // bakarriki notifikatu aldatu diren posizioak
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                onItemClicked(erropa)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val erropa = erropaList[position]
        holder.bind(erropa, position == selectedPosition)
    }

    override fun getItemCount() = erropaList.size
}
