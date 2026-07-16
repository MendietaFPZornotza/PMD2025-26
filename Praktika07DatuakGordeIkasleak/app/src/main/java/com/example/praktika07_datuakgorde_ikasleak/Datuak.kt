package com.example.praktika07_datuakgorde_ikasleak

import java.io.Serializable

// Clase con los datos que voy a almacenar y leer
// Serializable es porque debo poderlo mandar a un flujo (stream) para guardarlo en el archivo
class Datuak // Constructor para asignar datos
internal constructor(// Métodos para leer datos (getters)
    // Campos: información a guardar
    val izena: String, val herrialdea: String, val adina: Int
) : Serializable