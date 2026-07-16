package com.example.erronka_proba.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.erronka_proba.data.entity.LastLoginEntity
import com.example.erronka_proba.data.tcp.LastLoginDao

/**
 * Fitxategia: AppDatabase.kt
 *
 * Zertarako da?
 * - Room datu-basearen konfigurazioa eta singleton instantzia emateko.
 *
 * Zer egiten du?
 * - "erronka.db" izeneko DB-a sortu/irekitzen du.
 * - lastLoginDao() eskaintzen du (LastLoginEntity taularako).
 *
 * Nola erabiltzen da?
 * - AppDatabase.get(context) deituz singleton-a lortzen da.
 * - Repository-ek DAO-ak hartzen dituzte: AppDatabase.get(ctx).lastLoginDao()
 *
 * Log-ak:
 * - DB instantzia noiz sortzen den / noiz berrerabiltzen den ikusteko.
 */
@Database(
    entities = [LastLoginEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun lastLoginDao(): LastLoginDao

    companion object {
        private const val TAG = "AppDatabase"
        private const val DB_NAME = "erronka.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Singleton DB instantzia lortzen du (thread-safe).
         * Portaera originala: databaseBuilder(...).build()
         */
        fun get(context: Context): AppDatabase {
            val existing = INSTANCE
            if (existing != null) {
                Log.v(TAG, "get(): INSTANCE existitzen da -> reuse")
                return existing
            }

            return synchronized(this) {
                val again = INSTANCE
                if (again != null) {
                    Log.v(TAG, "get(): INSTANCE existitzen da (sync barruan) -> reuse")
                    again
                } else {
                    Log.i(TAG, "get(): INSTANCE sortzen -> dbName=$DB_NAME")
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    ).build().also { INSTANCE = it }
                }
            }
        }
    }
}