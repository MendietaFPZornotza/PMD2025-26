package com.example.a2finalaazterketa.model

class BideoJokoa(
    val izena: String,
    val generoa: Generoa,
    val urtea: Int,
    val plataformak: String,
    val enpresa: String,
    val multiplayer: Boolean
) {
    fun toLine(): String {
        return "$izena;${generoa.name};$urtea;$plataformak;$enpresa;$multiplayer"
    }
}
