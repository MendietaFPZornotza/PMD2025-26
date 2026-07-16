package com.example.erronka_proba.data.tcp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.erronka_proba.data.entity.LastLoginEntity

/**
 * Fitxategia: LastLoginDao.kt
 *
 * Zertarako da?
 * - Room datu-baseko "last_login" taularekin interakzioa egiteko DAO (Data Access Object).
 *
 * Zer egiten du?
 * - getEmail(): id=1 erregistroko email-a itzultzen du (edo null).
 * - getUserId(): id=1 erregistroko userId itzultzen du (edo null).
 * - save(entity): erregistroa gordetzen du (REPLACE).
 * - clear(): taulako erregistro guztiak ezabatzen ditu.
 *
 * Nola erabiltzen da?
 * - LastLoginRepository-k DAO hau erabiltzen du.
 *
 * Oharra:
 * - id=1 erabiltzen da “erregistro bakarra” mantentzeko (portaera orokorra proiektuan).
 */
@Dao
interface LastLoginDao {

    /**
     * Azken email-a eskuratu (id=1).
     */
    @Query("SELECT email FROM last_login WHERE id = 1")
    suspend fun getEmail(): String?

    /**
     * Azken userId-a eskuratu (id=1).
     */
    @Query("SELECT userId FROM last_login WHERE id = 1")
    suspend fun getUserId(): Long?

    /**
     * Erregistroa gorde/eguneratu.
     * REPLACE: id=1 bada, aurrekoa gainidatzi.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: LastLoginEntity)

    /**
     * Taula guztiz hustu.
     */
    @Query("DELETE FROM last_login")
    suspend fun clear()
}