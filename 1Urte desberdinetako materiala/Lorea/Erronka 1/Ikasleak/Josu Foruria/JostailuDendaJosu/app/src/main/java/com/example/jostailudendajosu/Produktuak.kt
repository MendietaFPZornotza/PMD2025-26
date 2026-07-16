package com.example.jostailudendajosu

//Produktu klasea baloreak izateko (Constructor)
data class Produktuak(
    val codigo: Int,
    val izenburua: String,
    val mota: String,
    val adin: Int,
    val jatorria: String,
    val prezioa: Double,
    val eskuragarri: Boolean
)