package com.example.proiekto_denda

data class Game(
    val id: Int,
    val title: String,
    val genre: String,
    val platforms: String,
    val price: Double,
    val description: String?,
    val available: Boolean
)
