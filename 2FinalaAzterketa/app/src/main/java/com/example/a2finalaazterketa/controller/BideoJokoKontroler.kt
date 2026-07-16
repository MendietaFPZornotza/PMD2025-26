package com.example.a2finalaazterketa.controller

import android.content.Context
import com.example.a2finalaazterketa.model.BideoJokoa
import com.example.a2finalaazterketa.model.Generoa


class BideoJokoKontroler {

    private val PREF = "BIDEO_PREF"
    private val KEY = "LISTA"

    // ENUM SOLO AQUÍ
    fun getGeneroak(): List<String> {
        return Generoa.entries.map { it.name }
    }

    // --------------------------
    // VALIDACIONES
    // --------------------------
    fun validate(
        izena: String,
        generoa: String,
        urtea: String,
        plataforma: String,
        enpresa: String
    ): String? {

        if (izena.isBlank()) return "Izena hutsik"
        if (izena.length < 3) return "Izena laburregia"

        if (generoa.isBlank()) return "Generoa aukeratu"

        val year = urtea.toIntOrNull()
            ?: return "Urtea zenbakia izan behar da"

        if (year < 1970 || year > 2026) {
            return "Urte okerra (1970-2026)"
        }

        if (plataforma.isBlank()) return "Plataforma hutsik"
        if (enpresa.isBlank()) return "Enpresa hutsik"

        return null
    }

    // 🔥 REGLA EXTRA
    fun validateLogic(generoa: String, multiplayer: Boolean): String? {
        if (generoa == "SHOOTER" && !multiplayer) {
            return "SHOOTER jokoek multiplayer izan behar dute"
        }
        return null
    }

    // --------------------------
    // SHARED PREFERENCES
    // --------------------------
    fun getAll(context: Context): ArrayList<String> {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return ArrayList(prefs.getStringSet(KEY, emptySet()) ?: emptySet())
    }

    private fun save(context: Context, list: ArrayList<String>) {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        prefs.edit().putStringSet(KEY, list.toSet()).apply()
    }

    // --------------------------
    // CRUD
    // --------------------------
    fun add(
        context: Context,
        izena: String,
        generoa: String,
        urtea: String,
        plataforma: String,
        enpresa: String,
        multi: Boolean
    ) {
        val list = getAll(context)
        list.add("$izena;$generoa;$urtea;$plataforma;$enpresa;$multi")
        save(context, list)
    }

    fun edit(
        context: Context,
        index: Int,
        izena: String,
        generoa: String,
        urtea: String,
        plataforma: String,
        enpresa: String,
        multi: Boolean
    ) {
        val list = getAll(context)
        list[index] = "$izena;$generoa;$urtea;$plataforma;$enpresa;$multi"
        save(context, list)
    }

    fun delete(context: Context, index: Int) {
        val list = getAll(context)
        list.removeAt(index)
        save(context, list)
    }

    // --------------------------
    // LAGUNTZAK
    // --------------------------
   /** fun parse(line: String): BideoJokoa {
        val p = line.split(";")

        return BideoJokoa(
            p[0],
            Generoa.valueOf(p[1]),
            p[2].toInt(),
            p[3],
            p[4],
            p[5].toBoolean()
        )
    }**/
    fun getFields(line: String): List<String> {
        return line.split(";")
    }
    fun getIndex(genero: String): Int {
        return Generoa.entries.indexOf(Generoa.valueOf(genero))
    }
}