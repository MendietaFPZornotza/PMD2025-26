package com.example.lorategi_proiektua

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
        db.execSQL("create table users(user_id integer primary key autoincrement, mail text, pass text, user text, gender text, notifications integer, city text)")
        db.execSQL("create table products (product_id integer primary key autoincrement, name text, type text, origin text, color text, price double, avaliable boolean)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS products")
        onCreate(db)
    }

}