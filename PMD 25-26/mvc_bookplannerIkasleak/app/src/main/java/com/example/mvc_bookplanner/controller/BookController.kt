package com.example.mvc_bookplanner.controller

import android.content.Context
import android.widget.Toast
import com.example.mvc_bookplanner.model.Book
import com.example.mvc_bookplanner.model.ModeBook



// Controller: Model eta View arteko komunikazioa kudeatzen du
class BookController(private val view: BookView, private val context: Context) {

    private val model = BookModel()

    // View interfazeak inplementatu beharreko metodoak
    interface BookView {
        fun updateRecyclerView(books: List<Book>)
    }

    // Liburu bat gehitu eta View eguneratu
    fun addBook(genre: String, format: String, extras: List<String>) {
        if (genre.isEmpty() || format.isEmpty()) {
            Toast.makeText(context, "Mesedez, hautatu genero eta formatua", Toast.LENGTH_SHORT).show()
            return
        }

        model.addBook(genre, format, extras)
        updateView()

        // Aukeraketa baieztatzeko Toast
        Toast.makeText(context, "Liburua gehitu da!", Toast.LENGTH_SHORT).show()
    }

    // View eguneratu RecyclerView-rekin
    private fun updateView() {
        val books = model.getSelectedBooks()
        view.updateRecyclerView(books)
    }
}