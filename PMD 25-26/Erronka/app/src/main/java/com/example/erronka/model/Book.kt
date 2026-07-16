package com.example.erronka.model

/**class Book {
    var id: Int = 0
    var izenburua: String? = null
    var idazlea_id: Int = 0

    constructor(id: Int, izenburua: String?, idazle: Int) {
        this.id = id
        this.izenburua = izenburua
        this.idazlea_id = idazle
    }

    constructor()

    override fun toString(): String {
        return "Book{" + "id=" + id + ", izenburua=" + izenburua + ", idazlea_id=" + idazlea_id + '}'
    }
}**/
class Book {

    private var id: Int = 0
    private var izenburua: String = ""
    private var idazlea_id: Int = 0

    // Constructor con parámetros
    constructor(id: Int, izenburua: String) {
        this.id = id
        this.izenburua = izenburua
    }

    // Constructor vacío
    constructor()

    // Getters y Setters
    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getIzenburua(): String {
        return izenburua
    }

    fun setIzenburua(izenburua: String) {
        this.izenburua = izenburua
    }

    fun getIdazlea_id(): Int {
        return idazlea_id
    }

    fun setIdazlea_id(idazlea_id: Int) {
        this.idazlea_id = idazlea_id
    }

    // toString
    override fun toString(): String {
        return "Book(id=$id, izenburua=$izenburua, idazlea_id=$idazlea_id)"
    }
}
