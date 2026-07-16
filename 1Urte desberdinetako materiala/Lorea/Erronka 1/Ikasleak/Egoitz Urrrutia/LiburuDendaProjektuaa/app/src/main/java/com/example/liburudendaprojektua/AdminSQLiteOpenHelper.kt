package com.example.liburudendaprojektua

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
        // Crear la tabla para guardar datos de los usuarios
        val createUsuariosTable = """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                correo TEXT UNIQUE,
                password TEXT,
                genero TEXT,
                ciudad TEXT
            )
        """

        // Crear la tabla para guardar datos de los libros
        val createLiburuakTable = """
            CREATE TABLE liburuak (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                izenburua TEXT,
                egilea TEXT,
                generoa TEXT,
                argitaletxea TEXT,
                prezioa REAL,
                eskuragarritasuna INTEGER
            )
        """

        // Ejecutar la creación de ambas tablas
        db.execSQL(createUsuariosTable)
        db.execSQL(createLiburuakTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Borrar las tablas si ya existen y volver a crearlas
        /*db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS liburuak")
        onCreate(db)*/
    }
}
