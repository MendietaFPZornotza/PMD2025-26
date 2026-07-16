package com.example.proiektua.modelo

data class Product(
    val id: Int,
    val name: String,
    val type: String,
    val size: String?,
    val origin: String?,
    val price: Double,
    val availability: Int
)
