package com.example.erronka_proba.data.tcp

import android.util.Log
import com.example.erronka_proba.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Fitxategia: TCPClient.kt
 *
 * Zertarako da?
 * - TCP/SSL bidez zerbitzariarekin komunikatzeko oinarrizko bezeroa.
 *
 * Zer egiten du?
 * - sendLine(line): lerro bat bidali eta erantzun lerro bakarra irakurri.
 * - login()/signup(): helper komandoak (sendLine-ren gainean).
 * - createSocket(): SSL socket bat sortzen du (DEV ONLY: “trust all”).
 * - downloadFile(remotePath): PDF deskarga (base64) eta ByteArray bihurtu.
 *
 * Nola erabiltzen da?
 * - Repo/Service klaseek base.sendLine(...) deitzen dute.
 *
 * Log-ak:
 * - Sareko pausuak jarraitzeko: connect, send, response, timeouts, errors.
 * - Kontuz: pasahitzak edo datu oso sentikorrak ez log-ean agertzea.
 */
open class TCPClient(
    private val serverIp: String = BuildConfig.TCP_HOST,
    private val serverPort: Int = BuildConfig.TCP_PORT
) {

    companion object {
        private const val TAG = "TCPClient"
        private const val TIMEOUT_MS = 5000
    }

    /**
     * Lerro bat bidali eta erantzun lerro bakarra jaso.
     *
     * Portaera originala:
     * - Socket sortu
     * - out.write(line) + newline + flush
     * - input.readLine() (lerro bakarra)
     * - socket.close()
     * - Exception -> Result.failure
     */
    suspend fun sendLine(line: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val cmd = line.substringBefore(";").ifBlank { "NO_CMD" }
                Log.d(TAG, "sendLine(): cmd=$cmd host=$serverIp port=$serverPort")

                val socket = createSocket(serverIp, serverPort)

                val out = BufferedWriter(OutputStreamWriter(socket.outputStream))
                val input = BufferedReader(InputStreamReader(socket.inputStream))

                // Oharra: line osoa ez dugu beti log-ean jartzen (pasahitzagatik).
                out.write(line)
                out.newLine()
                out.flush()

                val response = input.readLine() ?: "NO_RESPONSE"
                Log.d(TAG, "sendLine(): cmd=$cmd response='$response'")

                socket.close()
                Result.success(response)

            } catch (e: Exception) {
                Log.e(TAG, "sendLine() failure: ${e.message}", e)
                Result.failure(e)
            }
        }

    /**
     * Helper: login komandoa (sendLine-ren gainean).
     * Kontuz: password pasatzen da komandoan; sendLine-k cmd bakarrik log-eatuko du.
     */
    suspend fun login(email: String, password: String): Result<String> =
        sendLine("LOGIN;$email;$password")

    /**
     * Helper: signup komandoa (sendLine-ren gainean).
     */
    suspend fun signup(name: String, email: String, password: String): Result<String> =
        sendLine("SIGNUP;$name;$email;$password")

    /**
     * SSLSocket que confía en TODO (DEV ONLY)
     *
     * Oharra:
     * - Hau “trust all certificates” da; ekoizpenean arriskutsua da.
     * - Hemen ez dugu logika aldatzen, soilik log-ak gehitzen.
     */
    protected open fun createSocket(ip: String, port: Int): SSLSocket {
        Log.v(TAG, "createSocket(): ip=$ip port=$port timeout=$TIMEOUT_MS")

        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
            }
        )

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())

        val socket = sslContext.socketFactory.createSocket() as SSLSocket

        socket.soTimeout = TIMEOUT_MS
        socket.connect(InetSocketAddress(ip, port), TIMEOUT_MS)

        // TLS 1.2 / 1.3
        socket.enabledProtocols = socket.supportedProtocols
            .filter { it.startsWith("TLSv1.3") || it.startsWith("TLSv1.2") }
            .toTypedArray()

        socket.startHandshake()
        Log.v(TAG, "createSocket(): handshake OK, protocols=${socket.enabledProtocols.joinToString(",")}")

        return socket
    }

    /**
     * PDF fitxategia deskargatzen du (base64) eta ByteArray itzultzen du.
     *
     * Protokoloa (portaera originala):
     * - "DOWNLOAD_PDF;<remotePath>" bidali
     * - 1. lerroa: PDF_OK;...
     * - Ondoren base64 lerroak PDF_END arte
     * - Base64 decode -> bytes
     */
    suspend fun downloadFile(remotePath: String): Result<ByteArray> =
        withContext(Dispatchers.IO) {
            try {
                val path = remotePath.trim()
                Log.d(TAG, "downloadFile(): remotePathLen=${path.length}")

                val socket = createSocket(serverIp, serverPort)

                val out = BufferedWriter(OutputStreamWriter(socket.outputStream))
                val input = BufferedReader(InputStreamReader(socket.inputStream))

                out.write("DOWNLOAD_PDF;$path")
                out.newLine()
                out.flush()

                // 1) lehen lerroa (header)
                val first = input.readLine() ?: throw RuntimeException("NO_RESPONSE")
                Log.d(TAG, "downloadFile(): firstLine='$first'")

                val head = first.split(";")
                if (head[0] != "PDF_OK") {
                    socket.close()
                    Log.w(TAG, "downloadFile(): PDF_OK ez -> failure")
                    return@withContext Result.failure(RuntimeException(first))
                }

                // 2) base64 lerroak irakurri PDF_END arte
                val sb = StringBuilder()
                while (true) {
                    val line = input.readLine() ?: break
                    if (line == "PDF_END") break
                    sb.append(line.trim())
                }

                socket.close()

                val b64 = sb.toString()
                Log.i(TAG, "downloadFile(): base64Len=${b64.length}")

                val bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT)
                Log.i(TAG, "downloadFile(): bytes=${bytes.size}")

                Result.success(bytes)

            } catch (e: Exception) {
                Log.e(TAG, "downloadFile() failure: ${e.message}", e)
                Result.failure(e)
            }
        }
}