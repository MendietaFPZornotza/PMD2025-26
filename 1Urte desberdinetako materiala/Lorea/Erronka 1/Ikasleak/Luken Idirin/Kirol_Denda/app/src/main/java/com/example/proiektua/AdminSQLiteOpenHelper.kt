package com.example.proiektua

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.proiektua.modelo.Product

class AdminSQLiteOpenHelper(
    context: Context?,
    name: String = DATABASE_NAME,
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = DATABASE_VERSION
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        private const val DATABASE_NAME = "sports_store.db"
        private const val DATABASE_VERSION = 2

        // Users table
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_GENDER = "gender"
        private const val COLUMN_CITY = "city"
        private const val COLUMN_NOTIFICATION_PREF = "notification_pref"

        // Products table
        private const val TABLE_PRODUCTS = "productos"
        private const val COLUMN_PRODUCT_ID = "id"
        private const val COLUMN_PRODUCT = "produktua"
        private const val COLUMN_TYPE = "tipo"
        private const val COLUMN_SIZE = "talla"
        private const val COLUMN_ORIGIN = "jatorria"
        private const val COLUMN_PRICE = "precio"
        private const val COLUMN_AVAILABILITY = "eskuragarritasuna"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Users table
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_GENDER TEXT,
                $COLUMN_CITY TEXT,
                $COLUMN_NOTIFICATION_PREF INTEGER
            )
        """.trimIndent()

        // Create Products table
        val createProductsTable = """
            CREATE TABLE $TABLE_PRODUCTS (
                $COLUMN_PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PRODUCT TEXT NOT NULL,
                $COLUMN_TYPE TEXT NOT NULL,
                $COLUMN_SIZE TEXT,
                $COLUMN_ORIGIN TEXT,
                $COLUMN_PRICE REAL NOT NULL,
                $COLUMN_AVAILABILITY INTEGER NOT NULL
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createProductsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }


    // Function to get all products
    fun getAllProducts(): List<Product> {
        val db = readableDatabase
        val productList = mutableListOf<Product>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCTS", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID))
                val produktua = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT))
                val tipo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
                val talla = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE))
                val jatorria = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORIGIN))
                val precio = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
                val eskuragarritasuna = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVAILABILITY))

                productList.add(Product(id, produktua, tipo, talla, jatorria, precio, eskuragarritasuna))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return productList
    }


    fun getAllUsers(): List<String> {
        val db = readableDatabase
        val userList = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT * FROM users", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                val gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"))
                val city = cursor.getString(cursor.getColumnIndexOrThrow("city"))
                val notificationPref = cursor.getInt(cursor.getColumnIndexOrThrow("notification_pref"))

                userList.add("ID: $id, Username: $username, Email: $email, Password: $password, Gender: $gender, City: $city, Notifications: $notificationPref")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }

}
