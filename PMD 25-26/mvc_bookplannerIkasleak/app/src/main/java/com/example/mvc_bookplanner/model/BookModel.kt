package com.example.mvc_bookplanner.model

data class Book(
    val genre: String,
    val format: String,
    val extras: List<String>
)
class ModeBook {
    // Aukeratutako liburuak gordetzeko lista
    private val selectedBooks = mutableListOf<Book>()
    // Liburu bat gehitu
    fun addBook(genre: String, format: String, extras: List<String>) {
        selectedBooks.add(Book(genre, format, extras))
    }

    // Hautatutako liburu guztiak lortu
    fun getSelectedBooks(): List<Book> {
        return selectedBooks
    }
}