package com.example.erronka_proba.data.tcp

import android.content.Context
import android.util.Log
import com.example.erronka_proba.model.domain.User


/**
 * Fitxategia: ProfileTcpService.kt
 *
 * Zertarako da?
 * - Profilarekin lotutako TCP eskaerak bidaltzeko: profila lortu, eguneratu eta ezabatu.
 *
 * Zer egiten du?
 * - getProfile(email): "PROFILE_GET;<email>" bidali eta User bihurtu.
 * - updateProfile(...): "PROFILE_UPDATE;<oldEmail>;<name>;<email>;<pass>" bidali eta User bihurtu.
 * - deleteProfile(userId): "DELETE_PROFILE;<userId>" bidali eta Unit itzuli.
 *
 * Nola erabiltzen da?
 * - ProfileController-ek erabiltzen du (getProfile/updateProfile).
 *
 * Log-ak:
 * - Bidalitako komandoen pausuak eta jasotako response-ak jarraitzeko.
 * - Kontuz: pasahitza ez dugu log-ean agertzen.
 */
class ProfileTcpService(
    private val base: TCPClient = TCPClient(),
) {




    companion object {
        private const val TAG = "ProfileTcpService"
    }

    /**
     * Profila eskuratzen du email-aren arabera.
     *
     * Response posibleak (portaera originala):
     * - PROFILE_OK;<id>;<name>;<email>   (teorian)
     * - PROFILE_NOT_FOUND
     * - MEZU_EZ_EZAGUNA
     * - bestela: Bad response
     */
    suspend fun getProfile(email: String): Result<User> {
        val trimmed = email.trim()
        Log.d(TAG, "getProfile(): email='$trimmed'")

        return base.sendLine("PROFILE_GET;$trimmed").mapCatching { line ->
            Log.d(TAG, "getProfile() response='$line'")

            val p = line.split(";")
            when (p[0]) {
                "PROFILE_OK" -> {
                    val id = p.getOrNull(1)?.toLongOrNull() ?: 0L

                    // Portaera originala mantentzen da (nahiz eta susmagarria izan):
                    // name = p.getOrNull(1), em = p.getOrNull(2)
                    val name = p.getOrNull(1).orEmpty()
                    val em = p.getOrNull(2).orEmpty()

                    Log.i(TAG, "PROFILE_OK: id=$id nameLen=${name.length} emailLen=${em.length}")
                    User(id = id, name = name, email = em)
                }

                "PROFILE_NOT_FOUND" -> {
                    Log.w(TAG, "PROFILE_NOT_FOUND")
                    throw IllegalStateException("PROFILE_NOT_FOUND")
                }

                "MEZU_EZ_EZAGUNA" -> {
                    Log.w(TAG, "SERVER_NO_HANDLER_PROFILE_GET")
                    throw IllegalStateException("SERVER_NO_HANDLER_PROFILE_GET")
                }

                else -> {
                    Log.w(TAG, "Bad response: '$line'")
                    throw IllegalStateException("Bad response: $line")
                }
            }
        }
    }

    /**
     * Profila eguneratzen du.
     *
     * Request:
     * - PROFILE_UPDATE;<oldEmail>;<newName>;<newEmail>;<newPassword>
     *
     * Response posibleak:
     * - UPDATE_OK;<id>;<name>;<email>  (teorian)
     * - UPDATE_NOT_FOUND
     * - UPDATE_EXISTS
     * - MEZU_EZ_EZAGUNA
     * - bestela: Bad response
     */
    suspend fun updateProfile(
        oldEmail: String,
        newName: String? = null,
        newEmail: String? = null,
        newPassword: String? = null
    ): Result<User> {
        val old = oldEmail.trim()

        // Field-ak beti string gisa bidaltzen dira (portaera originala)
        val nameField = newName?.trim().orEmpty()
        val emailField = newEmail?.trim().orEmpty()
        val passField = newPassword.orEmpty()

        Log.d(TAG, "updateProfile(): oldEmail='$old' nameLen=${nameField.length} emailLen=${emailField.length} passProvided=${passField.isNotEmpty()}")

        return base.sendLine("PROFILE_UPDATE;$old;$nameField;$emailField;$passField")
            .mapCatching { line ->
                Log.d(TAG, "updateProfile() response='$line'")

                val p = line.split(";")
                when (p[0]) {
                    "UPDATE_OK" -> {
                        // name = p.getOrNull(1), email = p.getOrNull(2)

                        val user = User(
                            id = p.getOrNull(1)?.toLongOrNull() ?: 0L,
                            name = p.getOrNull(2).orEmpty(),
                            email = p.getOrNull(3).orEmpty()
                        )

                        Log.i(TAG, "UPDATE_OK: id=${user.id} nameLen=${user.name.length} emailLen=${user.email.length}")
                        user
                    }

                    "UPDATE_NOT_FOUND" -> {
                        Log.w(TAG, "UPDATE_NOT_FOUND")
                        throw IllegalStateException("UPDATE_NOT_FOUND")
                    }

                    "UPDATE_EXISTS" -> {
                        Log.w(TAG, "UPDATE_EXISTS")
                        throw IllegalStateException("UPDATE_EXISTS")
                    }

                    "MEZU_EZ_EZAGUNA" -> {
                        Log.w(TAG, "SERVER_NO_HANDLER_PROFILE_UPDATE")
                        throw IllegalStateException("SERVER_NO_HANDLER_PROFILE_UPDATE")
                    }

                    else -> {
                        Log.w(TAG, "Bad response: '$line'")
                        throw IllegalStateException("Bad response: $line")
                    }
                }
            }
    }

    /**
     * Profila ezabatzen du userId-aren arabera.
     *
     * Response posibleak:
     * - DELETE_PROFILE_OK
     * - DELETE_PROFILE_FAIL;<reason>
     * - bestela: Bad response
     */
    suspend fun deleteProfile(userId: Long): Result<Unit> {
        Log.d(TAG, "deleteProfile(): userId=$userId")

        return base.sendLine("DELETE_PROFILE;$userId").mapCatching { line ->
            Log.d(TAG, "deleteProfile() response='$line'")

            val p = line.split(";")
            when (p[0]) {
                "DELETE_PROFILE_OK" -> {
                    Log.i(TAG, "DELETE_PROFILE_OK")
                    Unit
                }

                "DELETE_PROFILE_FAIL" -> {
                    val reason = p.getOrNull(1) ?: "FAIL"
                    Log.w(TAG, "DELETE_PROFILE_FAIL: $reason")
                    error(reason)
                }

                else -> {
                    Log.w(TAG, "Bad response: '$line'")
                    error("Bad response: $line")
                }
            }
        }
    }
}