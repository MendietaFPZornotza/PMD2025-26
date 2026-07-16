package com.example.myapplication

import Produktuak
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQL_User_Database(
    context: Context?,
    name: String? = "Database",
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = 2
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE erabiltzaileak( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "erabiltzailea TEXT, " +
                    "gmail TEXT, " +
                    "pasahitza TEXT, " +
                    "generoa TEXT)"
        )

        db.execSQL(
            "CREATE TABLE produktuak( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "izenburua TEXT, " +
                    "kategoria TEXT, " +
                    "marka TEXT, " +
                    "prezioa DOUBLE, " +
                    "eskuragarritasuna BOOLEAN)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS erabiltzaileak")
            db.execSQL("DROP TABLE IF EXISTS produktuak")
            onCreate(db)
        }
    }

    fun insertUser(erabiltzailea: String, gmail: String, pasahitza: String, generoa: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("erabiltzailea", erabiltzailea)
            put("gmail", gmail)
            put("pasahitza", pasahitza)
            put("generoa", generoa)
        }

        return db.insert("erabiltzaileak", null, values)
    }

    fun insertProduct(izenburua: String, kategoria: String, marka: String, prezioa: Double, eskuragarritasuna: Boolean): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("izenburua", izenburua)
            put("kategoria", kategoria)
            put("marka", marka)
            put("prezioa", prezioa)
            put("eskuragarritasuna", if (eskuragarritasuna) 1 else 0)
        }
        return db.insert("produktuak", null, values)
    }

    fun getProduktuak(): List<Produktuak> {
        val produktuak = ArrayList<Produktuak>()
        val db = this.readableDatabase


        val query = "SELECT id, izenburua, kategoria, marka, prezioa, eskuragarritasuna FROM produktuak"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val izenburua = cursor.getString(cursor.getColumnIndexOrThrow("izenburua"))
                val kategoria = cursor.getString(cursor.getColumnIndexOrThrow("kategoria"))
                val marka = cursor.getString(cursor.getColumnIndexOrThrow("marka"))  // Obtener el valor de 'marka'
                val prezioa = cursor.getDouble(cursor.getColumnIndexOrThrow("prezioa"))
                val eskuragarritasuna = cursor.getInt(cursor.getColumnIndexOrThrow("eskuragarritasuna")) == 1  // Obtener 'eskuragarritasuna' y convertir a booleano

                // Crear un objeto de tipo Produktu y añadirlo a la lista
                produktuak.add(Produktuak(id, izenburua, kategoria,  prezioa, marka, eskuragarritasuna))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return produktuak
    }


    fun getProductById(productId: Int): Produktuak? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Produktuak WHERE id = ?", arrayOf(productId.toString()))

        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val izenburua = cursor.getString(cursor.getColumnIndexOrThrow("izenburua"))
            val kategoria = cursor.getString(cursor.getColumnIndexOrThrow("kategoria"))
            val marka = cursor.getString(cursor.getColumnIndexOrThrow("marka"))
            val prezioa = cursor.getDouble(cursor.getColumnIndexOrThrow("prezioa"))
            val eskuragarritasuna = cursor.getInt(cursor.getColumnIndexOrThrow("eskuragarritasuna")) == 1

            cursor.close()
            Produktuak(id, izenburua, kategoria,prezioa, marka,  eskuragarritasuna)
        } else {
            cursor.close()
            null
        }
    }

    fun updateProduct(
        productId: Int,
        izenburua: String,
        kategoria: String,
        marka: String,
        prezioa: Double,
        eskuragarritasuna: Boolean
    ): Boolean {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put("izenburua", izenburua)
            put("kategoria", kategoria)
            put("marka", marka)
            put("prezioa", prezioa)
            put("eskuragarritasuna", if (eskuragarritasuna) 1 else 0)
        }

        val rowsAffected = db.update(
            "produktuak",
            values,
            "id = ?",
            arrayOf(productId.toString())
        )

        db.close()
        return rowsAffected > 0
    }

    fun deleteProduct(productId: Int): Int {
        val db = this.writableDatabase
        val rowsDeleted = db.delete("produktuak", "id = ?", arrayOf(productId.toString()))
        db.close()
        return rowsDeleted
    }
}