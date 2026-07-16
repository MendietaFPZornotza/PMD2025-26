package org.beginsecure.mahaigaineko_app.infrastructure.tcp;

import org.beginsecure.mahaigaineko_app.model.config.AppConfig;

import javax.net.ssl.*;
import java.io.*;

public class TcpClient {

    protected static final String SERVER_IP = AppConfig.get("TCP_HOST");
    protected static final int SERVER_PORT = AppConfig.getInt("TCP_PORT");

    /**
     * SSL socket-a sortzen du eta ziurtagiri guztiak "fidagarri" bezala hartzen ditu.
     * Oharra: garapenean erosoa da, baina ekoizpenean arriskutsua izan daiteke.
     */
    protected static SSLSocket createSocket() throws Exception {
        TrustManager[] trustAll = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAll, new java.security.SecureRandom());

        SSLSocketFactory factory = sc.getSocketFactory();
        return (SSLSocket) factory.createSocket(SERVER_IP, SERVER_PORT);
    }

    /**
     * Mezua bidali eta erantzun lerro bakarra jasotzen du.
     */
    protected static String sendMessage(String message) throws Exception {
        try (SSLSocket socket = createSocket();
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.write(message);
            out.newLine();
            out.flush();

            return in.readLine();
        }
    }
}