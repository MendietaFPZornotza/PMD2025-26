package com.example.mvc_sharedprefs.controller

import android.content.Context
import com.example.mvc_sharedprefs.model.PrefsModel

// Controller: View eta Model arteko bitartekoa
class PrefsController(context: Context) {

    // Model objektua sortzen dugu, datuak gordetzeko
    private val model = PrefsModel(context)

    // Izena gordetzeko metodoa
    fun saveName(name: String) {
        model.name = name // Controller-ek modelari agintzen dio izena gordetzeko
    }

    // Izena lortzeko metodoa
    fun getName(): String? {
        return model.name // Controller-ek modelatik izena ateratzen du
    }

    // Izena ezabatzeko metodoa
    fun deleteName() {
        model.clearName() // Controller-ek modelari agintzen dio izena ezabatzeko
    }
}