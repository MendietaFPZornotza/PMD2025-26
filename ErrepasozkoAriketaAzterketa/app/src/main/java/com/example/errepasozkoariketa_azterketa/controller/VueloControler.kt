package com.example.errepasozkoariketa_azterketa.controller

import android.content.Context
import com.example.errepasozkoariketa_azterketa.model.TipoVuelo
import com.example.errepasozkoariketa_azterketa.model.Vuelo
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class VueloControler {

    private val fileName = "Vuelos.txt"

    private fun file(context: Context): File {
        return File(context.getExternalFilesDir(null), fileName)
    }

    // -----------------------------
    // SPINNER
    // -----------------------------
    fun getTipos(): List<String> {
        return TipoVuelo.entries.map { it.name }
    }

    fun getIndex(tipo: String): Int {
        return TipoVuelo.entries.indexOf(TipoVuelo.valueOf(tipo))
    }

    // -----------------------------
    // FILE READ
    // -----------------------------
    fun getAll(context: Context): ArrayList<String> {
        val list = ArrayList<String>()
        val f = file(context)

        if (!f.exists()) return list

        f.readLines().forEach {
            list.add(it)
        }

        return list
    }

    // -----------------------------
    // PARSE
    // -----------------------------
    fun parse(line: String): Vuelo {
        val p = line.split(";")

        return Vuelo(
            p[0],
            TipoVuelo.valueOf(p[1]),
            p[2],
            p[3],
            p[4],
            p[5],
            p[6].toBoolean()
        )
    }

    // -----------------------------
    // ADD
    // -----------------------------
    fun add(
        context: Context,
        codigo: String,
        tipo: String,
        origen: String,
        destino: String,
        fecha: String,
        aerolinea: String,
        disponible: Boolean
    ) {
        val list = getAll(context)

        list.add(
            "$codigo;$tipo;$origen;$destino;$fecha;$aerolinea;$disponible"
        )

        save(context, list)
    }

    // -----------------------------
    // EDIT
    // -----------------------------
    fun edit(
        context: Context,
        index: Int,
        codigo: String,
        tipo: String,
        origen: String,
        destino: String,
        fecha: String,
        aerolinea: String,
        disponible: Boolean
    ) {
        val list = getAll(context)

        list[index] =
            "$codigo;$tipo;$origen;$destino;$fecha;$aerolinea;$disponible"

        save(context, list)
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    fun delete(context: Context, index: Int) {
        val list = getAll(context)
        if (index >= 0 && index < list.size) {
            list.removeAt(index)
            save(context, list)
        }
    }

    // -----------------------------
    // VALIDATION (MEJORADA)
    // -----------------------------
    fun validate(codigo: String, origen: String, fecha: String): Boolean {
        return codigo.isNotEmpty()
                && origen.isNotEmpty()
                && validateFecha(fecha)
    }

    fun validateFecha(fecha: String): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        format.isLenient = false

        return try {
            val parsed = format.parse(fecha)

            // Extra: evita fechas vacías o mal formadas
            parsed != null && fecha.length == 10
        } catch (e: Exception) {
            false
        }
    }

    // -----------------------------
    // SAVE
    // -----------------------------
    private fun save(context: Context, list: ArrayList<String>) {
        val f = file(context)
        f.printWriter().use { out ->
            list.forEach { out.println(it) }
        }
    }
}