package com.example.erronka.model

/**class Writer {
    var id: Int = 0
    var izena: String? = null

    constructor(id: Int, izena: String?) {
        this.id = id
        this.izena = izena
    }

    constructor()

    override fun toString(): String {
        return "Writer{" + "id=" + id + ", izena=" + izena + '}'
    }
}**/


class Writer {

    private var id: Int = 0
    private var izena: String = ""

    // Constructor con parámetros
    constructor(id: Int, izena: String) {
        this.id = id
        this.izena = izena
    }

    // Constructor vacío
    constructor()

    // Getters y Setters
    fun getId(): Int = id
    fun setId(id: Int) { this.id = id }

    fun getIzena(): String = izena
    fun setIzena(izena: String) { this.izena = izena }

    // toString
    override fun toString(): String {
        return "Writer(id=$id, izena=$izena)"
    }
}