package com.example.thebeautycorner

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteOpenHelper(
    context: Context?,
    name: String = "thebeautycorner",
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    // DBB-a sortu
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE erabiltzaileak (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                erabiltzaileizena TEXT,
                postaelektronikoa TEXT,
                pasahitza TEXT,
                generoa TEXT,
                egoitzahiria TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE produktuak (
                kodea INTEGER PRIMARY KEY AUTOINCREMENT,
                izena TEXT,
                mota TEXT,
                marka TEXT,
                jatorria TEXT,
                prezioa TEXT,
                eskuragarritasuna TEXT
            )
        """)

        val values = ContentValues().apply {
            put("erabiltzaileizena", "aitorleon")
            put("postaelektronikoa", "aitorleonsalazar@gmail.com")
            put("pasahitza", "aitorleon") // Usa un hash en producción
            put("generoa", "Gizona")
            put("egoitzahiria", "Barcelona")
        }
        db.insert("erabiltzaileak", null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}