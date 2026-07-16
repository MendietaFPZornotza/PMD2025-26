package com.example.erronka_proba.model

/**
 * Fitxategia: LoginResult.kt
 *
 * Zertarako da?
 * - Login/Signup prozesuen emaitza modelatzeko.
 *
 * Zer egiten du?
 * - Sealed class 3 kasurekin:
 *   - Ok: id, name, email
 *   - Fail: erabiltzaileari erakusteko mezua (balidazio/cred okerrak...)
 *   - Error: konexio edo salbuespen kasuak
 *
 * Nola erabiltzen da?
 * - AuthRepository-k sortzen du TCP erantzuna parseatuta.
 * - LoginController/SignupController-ek when(res) egiten dute.
 */
sealed class LoginResult {
    data class Ok(val id: Long, val name: String, val email: String) : LoginResult()
    data class Fail(val msg: String) : LoginResult()
    data class Error(val msg: String) : LoginResult()
}