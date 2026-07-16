package com.example.mvc_bookplanner.view
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvc_bookplanner.R
import com.example.mvc_bookplanner.model.Book

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val books = mutableListOf<Book>()

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textInfo: TextView = itemView.findViewById(R.id.textBookInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.textInfo.text = buildString {
            append("Genero: ${book.genre}\n")
            append("Formatua: ${book.format}\n")
            if (book.extras.isNotEmpty()) {
                append("Osagai gehigarriak: ${book.extras.joinToString(", ")}")
            }
        }
    }

    override fun getItemCount(): Int = books.size

    fun submitList(newBooks: List<Book>) {
        books.clear()
        books.addAll(newBooks)
        notifyDataSetChanged()
    }
}