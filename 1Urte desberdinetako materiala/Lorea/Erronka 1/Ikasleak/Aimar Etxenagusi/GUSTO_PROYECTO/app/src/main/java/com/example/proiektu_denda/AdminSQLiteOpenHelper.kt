package com.example.proiektu_denda

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.deleteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.proiektu_denda.modelo.Producto

class AdminSQLiteOpenHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {


    override fun onCreate(db: SQLiteDatabase) {
        //Clientes tabla sortu
        db.execSQL("""
        CREATE TABLE IF NOT EXISTS clientes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            erabiltzailea TEXT,
            postaElektronikoa TEXT,
            pasahitza TEXT,
            generoa TEXT,
            egoitzaHiria TEXT,
            diabetikoa INTEGER,
            laktosa INTEGER
        )
    """)

        //Produktuak tabla sortu
        db.execSQL("""
        CREATE TABLE IF NOT EXISTS productos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            izena TEXT,
            mota TEXT NOT NULL,
            jatorria TEXT,
            osagarria TEXT,
            prezioa DOUBLE NOT NULL,
            eskuragarritasuna TEXT
        )
    """)
    }


    //honek DB-aren bertsioa eguneratzen du, bertsio aldatzen abda
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        if (oldVersion < newVersion) {

            db.execSQL("""
            CREATE TABLE IF NOT EXISTS productos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                izena TEXT,
                mota TEXT NOT NULL,
                jatorria TEXT,
                osagarria TEXT,
                prezioa DOUBLE NOT NULL,  
                eskuragarritasuna TEXT
            )
        """)
        }
    }

    //Hau getter bat da, hartzeko DB-eko produktu guztien datu guztiak, lista egiteko adibidez
    fun getAllProducts(): List<Producto> {
        val productList = mutableListOf<Producto>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM productos", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val izena = cursor.getString(cursor.getColumnIndexOrThrow("izena"))
                val mota = cursor.getString(cursor.getColumnIndexOrThrow("mota"))
                val jatorria = cursor.getString(cursor.getColumnIndexOrThrow("jatorria"))
                val osagaiNagusia = cursor.getString(cursor.getColumnIndexOrThrow("osagarria"))
                val prezioa = cursor.getDouble(cursor.getColumnIndexOrThrow("prezioa"))
                val eskuragarritasuna = cursor.getString(cursor.getColumnIndexOrThrow("eskuragarritasuna"))

                val product = Producto(id, izena, mota, jatorria, osagaiNagusia, prezioa, eskuragarritasuna)
                productList.add(product)
            } while (cursor.moveToNext())
        }

        cursor.close()

        db.close()
        return productList
    }




}
