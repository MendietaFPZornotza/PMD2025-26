package com.example.mvc_sharedprefs_izenanitzak.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Modela: izen anitz gordetzen ditu SharedPreferences bidez
class PrefsModel(context: Context) {

    // SharedPreferences fitxategiaren izena
    private val PREFS_NAME = "app_prefs"
    // Gorde nahi dugun datuaren gakoa
    private val SHARED_NAMES = "user_names"

    // SharedPreferences objektua lortzen dugu, aplikazio testuinguruarekin
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    // Izenak lortzeko metodoa
    fun getNames(): MutableList<String> {
        val json = prefs.getString(SHARED_NAMES, null)
        return if (json.isNullOrEmpty()) {
            mutableListOf() // Ez badago izenik, lista huts bat bueltatu
        } else {
            val type = object : TypeToken<MutableList<String>>() {}.type
            gson.fromJson(json, type) // JSON-etik lista bihurtu
        }
    }

    // Izen berria gehitzeko metodoa
    fun addName(name: String) {
        val list = getNames()
        if (!list.contains(name)) { // Errepikatu gabe
            list.add(name)
            saveList(list)
        }
    }

    // Izen bat ezabatzeko metodoa
    fun removeName(name: String) {
        val list = getNames()
        if (list.contains(name)) { // Izena listan badago
            list.remove(name)
            saveList(list)
        }
    }

    // Lista guztia SharedPreferences-en gordetzeko metodo pribatua
    private fun saveList(list: MutableList<String>) {
        val json = gson.toJson(list)
        prefs.edit().putString(SHARED_NAMES, json).apply()
    }
}