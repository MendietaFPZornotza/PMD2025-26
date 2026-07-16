package com.example.erronka_proba.data.ftps

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPSClient
import java.io.ByteArrayOutputStream

/**
 * Fitxategia: FtpsImageClient.kt
 *
 * Zertarako da?
 * - Irudiak FTPS bidez deskargatzeko (TCP base64-a saihesteko).
 *
 * Oharra:
 * - Passive mode + Binary beti (irudiak ez apurtzeko).
 * - PBSZ/PROT beharrezkoak izaten dira FTPES (Explicit FTPS) kasuan.
 */
class FtpsImageClient(
    private val host: String,
    private val port: Int,
    private val user: String,
    private val pass: String
) {
    companion object { private const val TAG = "FtpsImageClient" }

    suspend fun download(path: String): Result<ByteArray> = withContext(Dispatchers.IO) {
        val ftps = FTPSClient(false) // false = Explicit FTPS (FTPES, port 21)
        try {
            Log.d(TAG, "connect(): $host:$port user=$user")
            ftps.connect(host, port)

            val okLogin = ftps.login(user, pass)
            if (!okLogin) return@withContext Result.failure(RuntimeException("FTPS login failed"))

            // 🔥 GARRANTZITSUA: datu-kanala pribatua (bestela retrieveFile() huts egin dezake)
            ftps.execPBSZ(0)
            ftps.execPROT("P")

            // 🔥 Irudietarako ezinbestekoa: pasiboa + binarioa
            ftps.enterLocalPassiveMode()
            ftps.setFileType(FTP.BINARY_FILE_TYPE)

            // 🔥 Path normalizazioa:
            // BD: "Dokumentuak/Argazkiak/dslf.jpg"
            // Benetako bidea: /home/zornotza/Dokumentuak/Argazkiak/dslf.jpg
            // FTPSn normalean root = /home/zornotza -> "Dokumentuak/Argazkiak/..." erabiliko dugu.
            val candidates = buildCandidates(path)

            var lastErr: String? = null
            for (remote in candidates) {
                Log.d(TAG, "RETR: $remote")

                val bos = ByteArrayOutputStream()
                val ok = ftps.retrieveFile(remote, bos)
                if (ok) {
                    val bytes = bos.toByteArray()
                    if (bytes.isEmpty()) {
                        lastErr = "Empty file? $remote"
                        continue
                    }
                    return@withContext Result.success(bytes)
                } else {
                    lastErr = ftps.replyString?.trim()
                    Log.w(TAG, "RETR failed: $remote reply=$lastErr")
                }
            }

            Result.failure(RuntimeException("FTPS RETR failed: ${lastErr ?: "unknown"}"))

        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            try { if (ftps.isConnected) ftps.logout() } catch (_: Exception) {}
            try { if (ftps.isConnected) ftps.disconnect() } catch (_: Exception) {}
        }
    }

    /**
     * FTPS-rako bide posibleak sortzen ditu.
     *
     * Oharra:
     * - Batzuetan FTP root-a erabiltzailearen HOME da (/home/zornotza).
     * - Beste batzuetan root-a "/" da eta "home/zornotza/..." behar du.
     */
    private fun buildCandidates(pathRaw: String): List<String> {
        val p = pathRaw.trim().removePrefix("/")

        // 1) Kasu arrunta: home root-ean gaude -> Dokumentuak/Argazkiak/xxx.jpg
        val a = p

        // 2) Kasu alternatiboa: root "/" -> home/zornotza/...
        val b = if (p.startsWith("home/")) p else "home/$user/$p"

        // 3) Badator absolutua BDn edo norbaitek "/home/zornotza/..." pasatzen badu
        val c = p.removePrefix("home/$user/").removePrefix("home/")

        // Ordena: lehenengo arruntena, gero alternatiboa
        return listOf(a, b, c).distinct()
    }
}
