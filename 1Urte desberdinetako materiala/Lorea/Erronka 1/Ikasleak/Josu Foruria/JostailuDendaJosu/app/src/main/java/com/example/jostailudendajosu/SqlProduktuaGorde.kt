package com.example.jostailudendajosu
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Button
import android.widget.EditText

class SqlProduktuaGorde(

    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    //Datu basea sortu (Produktuak)
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table produktuak(codigo int primary key ,izenburua text, mota text, adin int, jatorria text, prezioa double, eskuragarri boolean)")

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    //Produktu Lista artzeko funtzioa
    fun getProduktuak(): List<Produktuak> {
        val db = this.readableDatabase
        val produktuak = mutableListOf<Produktuak>()
        val cursor = db.rawQuery("SELECT * FROM produktuak", null)

        if (cursor.moveToFirst()) {
            do {
                val produktua = Produktuak(
                    cursor.getInt(cursor.getColumnIndexOrThrow("codigo")),
                    cursor.getString(cursor.getColumnIndexOrThrow("izenburua")),
                    cursor.getString(cursor.getColumnIndexOrThrow("mota")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("adin")),
                    cursor.getString(cursor.getColumnIndexOrThrow("jatorria")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("prezioa")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("eskuragarri")) > 0
                )
                produktuak.add(produktua)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return produktuak
    }
    //Produktua id -aren bidez aurkitzeko  funtzioa
    fun getProduktuaById(id: Int): Produktuak? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM produktuak WHERE codigo = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            val produktua = Produktuak(
                cursor.getInt(cursor.getColumnIndexOrThrow("codigo")),
                cursor.getString(cursor.getColumnIndexOrThrow("izenburua")),
                cursor.getString(cursor.getColumnIndexOrThrow("mota")),
                cursor.getInt(cursor.getColumnIndexOrThrow("adin")),
                cursor.getString(cursor.getColumnIndexOrThrow("jatorria")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("prezioa")),
                cursor.getInt(cursor.getColumnIndexOrThrow("eskuragarri")) > 0
            )
            cursor.close()
            produktua

        } else {
            cursor.close()
            null
        }
    }
    //Ez da erabiltzen, da datu basea garbitzeko (delete All) funtzioa
    fun deleteAllProducts() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM produktuak")
        db.close()
    }
    //Produktu bat id -aren bidez borratzeko (delete id) funtzioa
    fun deleteProduktuaById(id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete("produktuak", "codigo = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }
    //Produktua aldatzeko (Update) funtzioa
    fun updateProduktua(produktua: Produktuak): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("izenburua", produktua.izenburua)
            put("mota", produktua.mota)
            put("adin", produktua.adin)
            put("jatorria", produktua.jatorria)
            put("prezioa", produktua.prezioa)
            put("eskuragarri", if (produktua.eskuragarri) 1 else 0)
        }

        val result = db.update("produktuak", contentValues, "codigo = ?", arrayOf(produktua.codigo.toString()))
        db.close()
        return result > 0
    }

}