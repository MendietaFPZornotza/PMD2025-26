package com.zerbitzaria.tcp;

import com.zerbitzaria.erabiltzailea.service.ErabiltzaileaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TcpServer:
 * - Infraestructura: abre el socket SSL y acepta conexiones
 * - NO contiene lógica de LOGIN o SIGNUP
 */
@Component
public class TcpServer implements CommandLineRunner {

    @Value("${tcp.port:5000}")
    private int port;

    @Value("${server.keystore.path}")
    private String keyStorePath;

    @Value("${server.keystore.password}")
    private String keyStorePassword;

    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final TcpMessageRouter router;

    public TcpServer(TcpMessageRouter router) {
        this.router = router;
    }

    @Override
    public void run(String... args) {
        // Lanzamos el server en un hilo aparte para no bloquear Spring Boot
        new Thread(this::startServer).start();
    }

    private void startServer() {
        try {
            SSLServerSocketFactory ssf = buildServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(port);

            // Permitimos TLS 1.2 y 1.3
            serverSocket.setEnabledProtocols(new String[]{"TLSv1.3", "TLSv1.2"});

            System.out.println("TCP SSL server running on port " + port);

            while (true) {
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                pool.submit(() -> handleClient(socket));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Construye el SSLServerSocketFactory usando el keystore.
     */
    private SSLServerSocketFactory buildServerSocketFactory() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");

        try (FileInputStream fis = new FileInputStream(keyStorePath)) {
            ks.load(fis, keyStorePassword.toCharArray());
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, keyStorePassword.toCharArray());

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);

        return sc.getServerSocketFactory();
    }

    /**
     * Maneja un cliente:
     * - Lee una línea
     * - La pasa al router
     * - Devuelve respuesta
     */
    private void handleClient(SSLSocket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String message = in.readLine();
            System.out.println("TCP <- " + message);

            String response = router.route(message);

            System.out.println("TCP -> " + response);
            out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}
