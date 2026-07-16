package com.example.liburudendaprojektua
//Libuiruaren d 3 datu bistaratzeko
data class Liburua(
    val id: Int,
    val izenburua: String,
    val egilea: String,
    val prezioa: Double
)
//Liburuen datu denak bistaratzen ditu
data class LiburuakListatu(
    val id: Int,
    val izenburua: String,
    val egilea: String,
    val generoa: String,
    val argitaletxea: String,
    val prezioa: Double,
    val eskuragarritasuna: Boolean
)
