package com.example.erronka.model

/**class IdazleData // this.idazle_izena = idazle_izena;
    (//    public IdazleData() {
    //    }
    var id: Int, var izena: String?, //    public String getIdazle_izena() {
    //        return idazle_izena;
    //    }
    //
    //    public void setIdazle_izena(String idazle_izena) {
    //        this.idazle_izena = idazle_izena;
    //    }
    //private String idazle_izena;
    var liburuak: MutableList<Liburu?>?
) {
    // Opcional: para mostrar el nombre en el ComboBox
    override fun toString(): String {
        return izena!!
    }
}**/

class IdazleData(
    private var id: Int,
    private var izena: String,
    // List eta MutableList-en arteko desberdintasuna da
    // List soilik balio duela datuak irakurteko eta MutableList
    // add erabiltzea usten du eraz alda daiteke
    private var liburuak: MutableList<Liburu> = mutableListOf()
) {

    // Getters y Setters
    fun getId(): Int = id
    fun setId(id: Int) { this.id = id }

    fun getIzena(): String = izena
    fun setIzena(izena: String) { this.izena = izena }

    // Getter público: devuelve MutableList para poder usar add()
    fun getLiburuak(): MutableList<Liburu> = liburuak

    // Setter público: recibe List o MutableList y lo convierte internamente a MutableList
    fun setLiburuak(list: List<Liburu>) {
        liburuak = list.toMutableList()
    }

    // Liburua gehitzeko metodoa
    fun addLiburu(liburu: Liburu) {
        liburuak.add(liburu)
    }

    // toString para mostrar solo el nombre
    override fun toString(): String = izena
}