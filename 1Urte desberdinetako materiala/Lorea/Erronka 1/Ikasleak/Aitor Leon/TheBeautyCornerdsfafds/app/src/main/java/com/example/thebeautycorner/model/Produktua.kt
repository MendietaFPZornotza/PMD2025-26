package com.example.thebeautycorner.model

// ProduktuaEditatu klasean erabiltzeko modeloa
data class Produktua(
    val id: String,
    val izena: String,
    val mota: String,
    val marka: String,
    val jatorria: String,
    val prezioa: Double,
    val eskuragarritasuna: Boolean
)

// ProduktuenLista klasean erabiltzeko modeloa
data class ProdListatu(
    val id: Int,
    val izena : String,
    val mota : String,
    val prezioa : Double
)

