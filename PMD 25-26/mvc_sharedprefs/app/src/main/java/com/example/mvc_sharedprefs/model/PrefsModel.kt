package com.example.mvc_sharedprefs.model

import android.content.Context
import android.content.SharedPreferences

// Modela: datuak gordetzen ditu SharedPreferences bidez
class PrefsModel(context: Context) {

    // SharedPreferences fitxategiaren izena
    private val PREFS_NAME = "app_prefs"

    // Gorde nahi dugun datuaren gakoa
    private val SHARED_NAME = "user_name"

    // SharedPreferences objektua lortzen dugu, aplikazio testuinguruarekin
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Property bat izena lortzeko eta gordetzeko
    var name: String?
        get() = prefs.getString(SHARED_NAME, null) // izena lortzen du, ez badago null bueltatzen du
        set(value) = prefs.edit().putString(SHARED_NAME, value).apply() // izena gordetzen du SharedPreferences-en

    /** Izena lortzeko funtzioa
    fun getName(): String? {
        // SharedPreferences-etik izena lortzen du
        // Ez badago, null bueltatzen du
        return prefs.getString(SHARED_NAME, null)
    }

    // Izena gordetzeko funtzioa
    fun setName(name: String) {
        // SHARED_NAME gakoarekin izena gordetzen du
        prefs.edit().putString(SHARED_NAME, name).apply()
    }**/



    // Izena ezabatzeko metodoa
    fun clearName() {
        prefs.edit().remove(SHARED_NAME).apply() // SHARED_NAME gakoa ezabatzen du
    }
}