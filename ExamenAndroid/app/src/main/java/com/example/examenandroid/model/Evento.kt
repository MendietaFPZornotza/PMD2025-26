package com.example.examenandroid.model

data class Evento(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val fecha: String,
    val lugar: String,
    val tipo: String
) {
    // Modeloa fitxategira bihurtzeko lerro
    fun toLine(): String = "$id|$nombre|$descripcion|$fecha|$lugar|$tipo"

    companion object {
        // Fitxategiko lerro bat Eventora bihurtu
        fun fromLine(line: String): Evento? {
            val parts = line.split("|")
            return if (parts.size == 6) {
                Evento(
                    id = parts[0].toInt(),
                    nombre = parts[1],
                    descripcion = parts[2],
                    fecha = parts[3],
                    lugar = parts[4],
                    tipo = parts[5]
                )
            } else null
        }
    }
}