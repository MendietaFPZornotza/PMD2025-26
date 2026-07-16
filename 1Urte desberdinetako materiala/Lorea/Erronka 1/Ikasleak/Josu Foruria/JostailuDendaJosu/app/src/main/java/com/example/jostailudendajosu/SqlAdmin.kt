package com.example.jostailudendajosu
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Button
import android.widget.EditText

class SqlAdmin(

    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    //Datu basea Login (Pertsonak)
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table pertsonak(codigo int primary key,izena text, email text, password text, sexua text, spamJaso boolean, hiria text)")

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}