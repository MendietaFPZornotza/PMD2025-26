package com.example.mvc_sharedprefs_izenanitzak.controller

import android.content.Context
import com.example.mvc_sharedprefs_izenanitzak.model.PrefsModel

// Controller: View eta Model arteko bitartekoa
class PrefsController(context: Context) {

    private val model = PrefsModel(context)

    fun getNames(): List<String> = model.getNames()

    fun addName(name: String) = model.addName(name)

    fun removeName(name: String) = model.removeName(name)

    fun nameExists(name: String): Boolean = model.getNames().contains(name)
}