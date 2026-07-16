package com.example.praktika07_datuakgorde

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    val PREFS_NAME = "com.example.praktika07_datuakgorde.sharedpreferences"
    val SHARED_NAME = "shared_name"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
    var name: String
        get() = prefs.getString(SHARED_NAME,"").toString()
        set(value) = prefs.edit().putString(SHARED_NAME, value).apply()

}