package com.example.mvc_sd.model

import android.content.Context
import java.io.File

// Modela: izenak SD txartelean gordetzen ditu
class SDFileModel(context: Context) {

    // SD txartelean fitxategia: app-specific directory
    private val file: File = File(context.getExternalFilesDir(null), "names_sd.txt")

    // Izen guztiak lortzeko
    fun getNames(): MutableList<String> {
        if (!file.exists()) file.createNewFile()  // Fitxategia sortu
        return file.readLines().toMutableList()    // Lerroak irakurri eta zerrenda bueltatu
    }

    // Izen berri bat gehitzeko
    fun addName(name: String) {
        val names = getNames()   // Lehenengo izen guztiak lortu
        names.add(name)          // Izen berria gehitu
        file.writeText(names.joinToString("\n")) // Fitxategia eguneratu
    }

    // Izen bat ezabatzeko
    fun deleteName(name: String) {
        val names = getNames()   // Izen guztiak lortu
        if (names.remove(name)) {
            file.writeText(names.joinToString("\n")) // Fitxategia berriro idatzi
        }
    }
}