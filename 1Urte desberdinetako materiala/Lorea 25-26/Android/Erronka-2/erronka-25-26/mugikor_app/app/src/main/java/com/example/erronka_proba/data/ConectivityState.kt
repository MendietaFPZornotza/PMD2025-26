package com.example.erronka_proba.data

/**
 * Fitxategia: ConnectivityState.kt
 *
 * Zertarako da?
 * - Aplikazioan “online/offline” egoera partekatzeko bandera sinplea.
 *
 * Zer egiten du?
 * - online: Boolean bat, volatile markarekin, thread desberdinetatik irakurtzeko/eguneratzeko.
 *
 * Nola erabiltzen da?
 * - TCP deiak ondo badoaz: ConnectivityState.online = true
 * - Huts egiten badute: ConnectivityState.online = false
 * - Controller batzuek egoera honen arabera botoiak desgaitzen dituzte (erosketa, profila...).
 *
 * Oharra:
 * - Hemen ez dut Log gehitzen (objektu oso sinplea da), baina behar baduzu,
 *   controller/repoetan dagoeneko log-ak uzten ari gara.
 */
object ConnectivityState {
    @Volatile var online: Boolean = true
}