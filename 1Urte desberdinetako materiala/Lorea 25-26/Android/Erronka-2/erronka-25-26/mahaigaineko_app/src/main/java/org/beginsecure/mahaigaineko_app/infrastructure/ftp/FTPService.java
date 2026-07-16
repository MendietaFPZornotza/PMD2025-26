package org.beginsecure.mahaigaineko_app.infrastructure.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;
import org.beginsecure.mahaigaineko_app.model.config.AppConfig;

import java.io.*;

public class FTPService {

    /**
     * FTPS konexioa sortu eta konfiguratu
     */
    private FTPSClient connect() throws IOException {

        String host = AppConfig.get("FTP_HOST");
        int port = AppConfig.getInt("FTP_PORT");
        String user = AppConfig.get("FTP_USER");
        String pass = AppConfig.get("FTP_PASS");

        // FTPS explicit (TLS STARTTLS modua)
        FTPSClient ftp = new FTPSClient(false);

        ftp.setConnectTimeout(10_000);
        ftp.connect(host, port);

        // Konexioaren erantzuna egiaztatu
        int reply = ftp.getReplyCode();
        if (!ftp.isConnected() || reply >= 400) {
            throw new IOException("FTPS connection failed. Reply code: " + reply);
        }

        // Login egin
        if (!ftp.login(user, pass)) {
            throw new IOException("FTPS login failed for user: " + user);
        }

        // TLS datu-kanala babestu
        ftp.execPBSZ(0);
        ftp.execPROT("P");

        // PASV modua (firewall friendly)
        ftp.enterLocalPassiveMode();

        // Fitxategiak binarioan bidali
        ftp.setFileType(FTP.BINARY_FILE_TYPE);

        // Errendimendu eta timeout konfigurazioa
        ftp.setBufferSize(1024 * 1024);
        ftp.setDataTimeout(120_000);
        ftp.setSoTimeout(120_000);
        ftp.setControlKeepAliveTimeout(300);

        return ftp;
    }

    /**
     * PDF bat FTPS bidez igo
     */
    public String uploadPdf(File file, String originalRemoteName) {

        FTPSClient ftp = null;

        try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {

            ftp = connect();

            // 📂 FTP-ko oinarrizko karpeta (adib. /Dokumentuak/Txostenak)
            String basePath = AppConfig.get("FTP_DOCUMENT_BASE_PATH");
            if (basePath == null || basePath.isBlank()) {
                throw new IOException("FTP_DOCUMENT_BASE_PATH not configured");
            }

            // 1) Karpeta existitzen dela ziurtatu (eta sartu)
            if (!ensureDirectory(ftp, basePath)) {
                return "Errorea: ezin da karpetara sartu: " + basePath;
            }

            // 2) Fitxategiaren izena garbitu
            String remoteName = sanitizePdfName(originalRemoteName);

            // 3) PDF-a igo
            boolean ok = ftp.storeFile(remoteName, in);

            return ok
                    ? "PDF ondo igota: " + basePath + "/" + remoteName
                    : "Errorea PDF igotzean: " + ftp.getReplyString();

        } catch (IOException e) {
            return "FTPS errorea: " + e.getMessage();

        } finally {
            try {
                if (ftp != null && ftp.isConnected()) {
                    ftp.logout();
                    ftp.disconnect();
                }
            } catch (IOException ignored) {}
        }
    }

    /**
     * Karpeta egitura sortu eta bertara mugitu (multi-maila)
     */
    private boolean ensureDirectory(FTPSClient ftp, String path) throws IOException {

        if (path.isBlank()) return false;

        // /Dokumentuak/Txostenak → ["", "Dokumentuak", "Txostenak"]
        String[] parts = path.split("/");

        // Hasieran root-era joan
        if (path.startsWith("/")) {
            if (!ftp.changeWorkingDirectory("/")) return false;
        }

        for (String dir : parts) {
            if (dir == null || dir.isBlank()) continue;

            if (!ftp.changeWorkingDirectory(dir)) {
                // Ez badago, sortu
                if (!ftp.makeDirectory(dir)) return false;
                if (!ftp.changeWorkingDirectory(dir)) return false;
            }
        }
        return true;
    }

    /**
     * Fitxategiaren izena segurua bihurtu eta .pdf derrigortu
     */
    private String sanitizePdfName(String name) {

        if (name == null || name.isBlank()) {
            name = "txostena.pdf";
        }

        // Path-ak kendu
        name = name.replace("\\", "/");
        if (name.contains("/")) {
            name = name.substring(name.lastIndexOf("/") + 1);
        }

        // Zuriuneak _ bihurtu
        name = name.trim().replaceAll("\\s+", "_");

        // Karaktere seguruak bakarrik
        name = name.replaceAll("[^a-zA-Z0-9._-]", "_");

        // .pdf derrigorrezkoa
        if (!name.toLowerCase().endsWith(".pdf")) {
            name += ".pdf";
        }

        return name;
    }

    public byte[] downloadBytes(String remotePath) throws IOException {
        if (remotePath == null || remotePath.isBlank()) return null;

        FTPSClient ftp = null;
        try {
            ftp = connect();

            // OJO: remotePath puede ser absoluto (/Dokumentuak/...) o relativo
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                boolean ok = ftp.retrieveFile(remotePath, out);
                if (!ok) {
                    throw new IOException("FTPS download failed: " + ftp.getReplyString());
                }
                return out.toByteArray();
            }

        } finally {
            try {
                if (ftp != null && ftp.isConnected()) {
                    ftp.logout();
                    ftp.disconnect();
                }
            } catch (IOException ignored) {}
        }
    }

    public String uploadEventImage(File file) throws IOException {
        if (file == null || !file.exists()) throw new IOException("Image file not found");

        FTPSClient ftp = null;
        try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
            ftp = connect();

            String basePath = AppConfig.get("FTP_EVENT_IMAGE_BASE_PATH");
            if (basePath == null || basePath.isBlank()) {
                throw new IOException("FTP_EVENT_IMAGE_BASE_PATH not configured");
            }

            if (!ensureDirectory(ftp, basePath)) {
                throw new IOException("Cannot enter/create remote dir: " + basePath);
            }

            String remoteName = uniqueImageName(file.getName());   // evt_<uuid>.png/jpg...
            boolean ok = ftp.storeFile(remoteName, in);

            if (!ok) throw new IOException("FTPS upload failed: " + ftp.getReplyString());

            // devolvemos path completo (recomendado guardar así en BD)
            return basePath.endsWith("/")
                    ? basePath + remoteName
                    : basePath + "/" + remoteName;

        } finally {
            try {
                if (ftp != null && ftp.isConnected()) {
                    ftp.logout();
                    ftp.disconnect();
                }
            } catch (IOException ignored) {}
        }
    }

    private String uniqueImageName(String original) {
        String ext = extractSafeImageExt(original); // .jpg/.png/.jpeg/.webp (si no, .jpg)
        String uuid = java.util.UUID.randomUUID().toString().replace("-", "");
        return "evt_" + uuid + ext;
    }

    private String extractSafeImageExt(String name) {
        if (name == null) return ".jpg";
        String n = name.trim().toLowerCase();
        String ext = "";
        int dot = n.lastIndexOf('.');
        if (dot >= 0) ext = n.substring(dot);

        return switch (ext) {
            case ".jpg", ".jpeg", ".png", ".webp" -> ext;
            default -> ".jpg";
        };
    }


}
