package com.example.mvc_sd.controller

import android.content.Context
import com.example.mvc_sd.model.SDFileModel

// Controller-a: View eta Model arteko bitartekoa
class SDFileController(context: Context) {

    // Model objektua sortu, Context pasatuz
    private val model = SDFileModel(context)

    // Izen guztiak lortzeko
    fun getAllNames(): List<String> = model.getNames()

    // Izen bat gordetzeko
    fun saveName(name: String) = model.addName(name)

    // Izen bat ezabatzeko
    fun deleteName(name: String) = model.deleteName(name)
}