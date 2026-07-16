package com.example.mvc_file.controller

import com.example.mvc_file.model.FileModel
import android.content.Context


class FileController(context: Context) {

    // Model objektua sortu
   // private val model = FileModel(context)
    private val model = FileModel()
    // Izen guztiak lortzeko
    fun getAllNames(): List<String> {
        return model.getNames()
    }

    // Izen bat gehitzeko
    fun saveName(name: String) {
        model.addName(name)
    }

    // Izen bat ezabatzeko
    fun deleteName(name: String) {
        model.deleteName(name)
    }
}