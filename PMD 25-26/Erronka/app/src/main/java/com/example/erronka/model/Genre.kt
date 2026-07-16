package com.example.erronka.model

/**class Genre {
    var liburua_id: Int = 0
    var generoa: String? = null

    constructor(liburua_id: Int, generoa: String?) {
        this.liburua_id = liburua_id
        this.generoa = generoa
    }

    constructor()

    override fun toString(): String {
        return "Genre{" + "liburua_id=" + liburua_id + ", generoa=" + generoa + '}'
    }
}**/
class Genre {

    private var liburua_id: Int = 0
    private var generoa: String = ""

    // Constructor con parámetros
    constructor(liburua_id: Int, generoa: String) {
        this.liburua_id = liburua_id
        this.generoa = generoa
    }

    // Constructor vacío
    constructor()

    // Getters y Setters
    fun getLiburua_id(): Int {
        return liburua_id
    }

    fun setLiburua_id(liburua_id: Int) {
        this.liburua_id = liburua_id
    }

    fun getGeneroa(): String {
        return generoa
    }

    fun setGeneroa(generoa: String) {
        this.generoa = generoa
    }

    // toString
    override fun toString(): String {
        return "Genre(liburua_id=$liburua_id, generoa=$generoa)"
    }
}