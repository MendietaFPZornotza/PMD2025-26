package com.example.proiekto_denda

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ProiektoDenda.db"
        private const val DATABASE_VERSION = 1

        // Erabiltzaileen taula
        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_GENDER = "gender"
        private const val COLUMN_CITY = "city"

        // Bideo-jokoen taula
        private const val TABLE_GAMES = "games"
        private const val COLUMN_GAME_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_GENRE = "genre"
        private const val COLUMN_PLATFORMS = "platforms"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_AVAILABILITY = "availability"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Erabiltzaileen taula sortu
        val createUsersTableQuery = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_GENDER TEXT,
                $COLUMN_CITY TEXT
            )
        """.trimIndent()
        db.execSQL(createUsersTableQuery)

        // Bideo-jokoen taula sortu
        val createGamesTableQuery = """
        CREATE TABLE $TABLE_GAMES (
            $COLUMN_GAME_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_TITLE TEXT NOT NULL,
            $COLUMN_GENRE TEXT NOT NULL,
            $COLUMN_PLATFORMS TEXT NOT NULL,
            $COLUMN_PRICE REAL NOT NULL,
            $COLUMN_DESCRIPTION TEXT,
            $COLUMN_AVAILABILITY INTEGER NOT NULL
        )
    """.trimIndent()
        db.execSQL(createGamesTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Taulak ezabatu eta berriro sortu
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        onCreate(db)
    }

    // Erabiltzaileen taularako funtzioak
    fun insertUser(username: String, email: String, password: String, gender: String, city: String): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_GENDER, gender)
            put(COLUMN_CITY, city)
        }

        val result = db.insert(TABLE_USERS, null, contentValues)
        return result != -1L
    }

    fun checkUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val userExists = cursor.moveToFirst()
        cursor.close()
        return userExists
    }

    // Bideo-jokoen taularako funtzioak
    fun insertGame(title: String, genre: String, platforms: String, price: Double, description: String, available: Boolean): Boolean {
        return try {
            val db = writableDatabase
            val contentValues = ContentValues().apply {
                put(COLUMN_TITLE, title)
                put(COLUMN_GENRE, genre)
                put(COLUMN_PLATFORMS, platforms)
                put(COLUMN_PRICE, price)
                put(COLUMN_DESCRIPTION, description)
                put(COLUMN_AVAILABILITY, if (available) 1 else 0)
            }

            val result = db.insert(TABLE_GAMES, null, contentValues)
            result != -1L // Egon daiteke ondo sartu bada
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // DBHelper-en metodoa
    fun getAllGames(): List<Game> {
        val games = mutableListOf<Game>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_GAMES", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val genre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE))
                val platforms = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLATFORMS))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val availability = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVAILABILITY)) == 1

                games.add(Game(id, title, genre, platforms, price, description, availability))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return games
    }

    fun deleteGameById(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_GAMES, "$COLUMN_GAME_ID = ?", arrayOf(id.toString()))
        return result > 0
    }

    fun updateGame(
        id: Int,
        title: String,
        genre: String,
        platforms: String,
        price: Double,
        description: String,
        available: Boolean
    ): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_GENRE, genre)
            put(COLUMN_PLATFORMS, platforms)
            put(COLUMN_PRICE, price)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_AVAILABILITY, if (available) 1 else 0)
        }

        val result = db.update(TABLE_GAMES, contentValues, "$COLUMN_GAME_ID = ?", arrayOf(id.toString()))
        return result > 0
    }

    // Metodoa posta elektronikoa dagoen egiaztatzeko
    fun checkEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM users WHERE email = ?"
        val cursor = db.rawQuery(query, arrayOf(email))

        // Cursorrek emaitzak baditu, posta elektronikoa existitzen da
        val exists = cursor.count > 0
        cursor.close()
        db.close()

        return exists
    }

    // Bideo-jokoen datuak modelatzeko klasea
    data class Game(
        val id: Int,
        val title: String,
        val genre: String,
        val platforms: String,
        val price: Double,
        val description: String?,
        val available: Boolean
    )
}
