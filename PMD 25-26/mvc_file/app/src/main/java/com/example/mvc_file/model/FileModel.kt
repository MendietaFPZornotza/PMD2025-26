package com.example.mvc_file.model

import java.io.File
import android.content.Context

/**class FileModel(private val context: Context) {
    // Fitxategi finkoaren izena
    private val fileName = "names.txt"

    // Fitxategiaren erreferentzia
    private val file: File
        get() = File(context.filesDir, fileName)

    // Izen guztiak irakurtzeko metodoa
    fun getNames(): MutableList<String> {
        // Fitxategia existitzen ez bada, zerrenda hutsa bueltatu
        if (!file.exists()) return mutableListOf()

        // Fitxategiko lerro guztiak irakurri eta zerrenda batean sartu
        return file.readLines().toMutableList()
    }

    // Izen berri bat gehitzeko metodoa
    fun addName(name: String) {
        val names = getNames()   // Lehenengo izen guztiak lortu
        names.add(name)          // Izen berria gehitu
        file.writeText(names.joinToString("\n")) // Zerrenda berria fitxategian idatzi
    }

    // Izen bat ezabatzeko metodoa
    fun deleteName(name: String) {
        val names = getNames()        // Izen guztiak lortu
        names.remove(name)            // Aukeratutako izena kendu
        file.writeText(names.joinToString("\n")) // Fitxategia eguneratu
    }
}**/

// Modela: fitxategi finko batean izenak gordetzen ditu
class FileModel {

    // Fitxategi finkoaren path (aldatu zure package izenarekin)
    private val filePath = "/data/data/com.example.mvc_file/files/names.txt"
    private val file = File(filePath)

    // Izen guztiak lortzeko metodoa
    fun getNames(): MutableList<String> {
        // Fitxategia existitzen ez bada, zerrenda hutsa bueltatu
        if (!file.exists()) return mutableListOf()
        // Fitxategiko lerro guztiak irakurri
        return file.readLines().toMutableList()
    }

    // Izen berri bat gehitzeko metodoa
    fun addName(name: String) {
        val names = getNames()   // Izen guztiak lortu
        names.add(name)          // Izen berria gehitu
        file.writeText(names.joinToString("\n")) // Fitxategia eguneratu
    }

    // Izen bat ezabatzeko metodoa
    fun deleteName(name: String) {
        val names = getNames()        // Izen guztiak lortu
        if (names.remove(name)) {     // Izen bat kendu, true bueltatzen badu
            file.writeText(names.joinToString("\n")) // Fitxategia berriro idatzi
        }
    }
}
