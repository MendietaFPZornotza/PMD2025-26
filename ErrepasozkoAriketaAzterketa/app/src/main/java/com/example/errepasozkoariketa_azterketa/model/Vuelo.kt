package com.example.errepasozkoariketa_azterketa.model


class Vuelo(
    val codigo: String,
    val tipo: TipoVuelo,
    val origen: String,
    val destino: String,
    val fecha: String,
    val aerolinea: String,
    val disponible: Boolean
) {
    fun toLine(): String {
        return "$codigo;${tipo.name};$origen;$destino;$fecha;$aerolinea;$disponible"
    }
}