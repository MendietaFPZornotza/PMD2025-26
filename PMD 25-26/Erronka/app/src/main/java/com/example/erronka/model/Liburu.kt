package com.example.erronka.model


/**class Liburu {
    var id: Int
    var izenburua: String?
    var idazlea: String? = null
    var generoak: MutableList<String?>

    constructor(
        id: Int,
        izenburua: String?,
        idazle_izena: String?,
        generoak: MutableList<String?>
    ) {
        this.id = id
        this.izenburua = izenburua
        this.idazlea = idazle_izena
        this.generoak = generoak
    }

    constructor(id: Int, izenburua: String?, generoak: MutableList<String?>) {
        this.id = id
        this.izenburua = izenburua
        this.generoak = generoak
    }

    val generoakAsString: String
        // Devuelve la lista de géneros en formato cadena
        get() = java.lang.String.join(", ", generoak)

    // Opcional: para mostrar el título en el TableView o ComboBox
    override fun toString(): String {
        return "LiburuDatu{" +
                "id=" + id +
                ", izenburua='" + izenburua + '\'' +
                ", generoak=" + generoak +
                ", idazle_izena='" + this.idazlea + '\'' +
                '}'
    }
}**/
class Liburu {

    private var id: Int = 0
    private var izenburua: String = ""
    private var idazle_izena: String = ""
    //private var generoak: List<String> = emptyList()
    private var generoak: MutableList<String> = mutableListOf()

    // Constructor completo
    //constructor(id: Int, izenburua: String, idazle_izena: String, generoak: List<String>) {
    constructor(id: Int, izenburua: String, idazle_izena: String, generoak: MutableList<String>) {
        this.id = id
        this.izenburua = izenburua
        this.idazle_izena = idazle_izena
        this.generoak = generoak
    }

    // Constructor sin idazle_izena
    // constructor(id: Int, izenburua: String, generoak: List<String>) {
    constructor(id: Int, izenburua: String, generoak: MutableList<String>) {
        this.id = id
        this.izenburua = izenburua
        this.generoak = generoak
    }

    // Getters y Setters
    fun getId(): Int = id
    fun setId(id: Int) { this.id = id }

    fun getIzenburua(): String = izenburua
    fun setIzenburua(izenburua: String) { this.izenburua = izenburua }

    fun getIdazlea(): String = idazle_izena
    fun setIdazlea(idazlea: String) { this.idazle_izena = idazlea }

    //fun getGeneroak(): List<String> = generoak
    //fun setGeneroak(generoak: List<String>) { this.generoak = generoak }

    // Getter público: devuelve MutableList para poder usar add()
    fun getGeneroak(): MutableList<String> = generoak

    // Setter público: recibe List o MutableList y lo convierte internamente a MutableList
    fun setGeneroak(list: MutableList<String>) {
        generoak = list.toMutableList()
    }

    // Liburua gehitzeko metodoa
    fun addLiburu(generoa: String) {
        generoak.add(generoa)
    }

    // Devuelve la lista de géneros como cadena
    fun getGeneroakAsString(): String = generoak.joinToString(", ")

    // toString equivalente
    override fun toString(): String {
        return "LiburuDatu(id=$id, izenburua='$izenburua', generoak=$generoak, idazle_izena='$idazle_izena')"
    }
}