package com.example.mvc_passwordapp.model

// MODEL: datuak eta logika gordetzen dituen geruza.
// Hemen pasahitz zuzena definitzen dugu eta egiaztatzeko metodoa eskaintzen dugu.
class PasswordModel {

    // Pasahitz zuzena hemen gordetzen da.
    private val correctPassword = "abc123"

    // Pasahitza egiaztatzeko metodoa.
    fun checkPassword(input: String): Boolean {
        // True itzultzen du zuzena bada, false bestela.
        return input == correctPassword
    }
}