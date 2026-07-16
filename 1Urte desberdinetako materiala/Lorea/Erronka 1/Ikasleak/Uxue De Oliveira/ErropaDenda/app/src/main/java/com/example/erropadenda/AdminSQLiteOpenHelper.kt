package com.example.erropadenda

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteOpenHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table usuarios(kodea INTEGER  primary key autoincrement, izena text, abizena text, email text, pasahitza text, arropaGustukoena text,egoitzaHiria text,jakinarazpenIrizpideak text )")
        db.execSQL("create table productos(kodea INTEGER  primary key autoincrement, izena text, mota text, talla text, kolorea text,prezioa text,eskuragarritasuna text)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


}