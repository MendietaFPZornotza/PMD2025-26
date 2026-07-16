package com.zerbitzaria.mail;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FTPService {

    @Value("${ftp.host}") private String host;
    @Value("${ftp.port}") private int port;
    @Value("${ftp.user}") private String user;
    @Value("${ftp.pass}") private String pass;
    @Value("${ftp.baseDir}") private String baseDir; // ej: /tickets

    public String uploadFile(File file, String remoteFileName) {
        if (file == null || !file.exists()) throw new RuntimeException("File not found: " + file);
        if (remoteFileName == null || remoteFileName.isBlank()) throw new RuntimeException("remoteFileName empty");

        FTPClient ftp = new FTPClient();
        try {
            ftp.setConnectTimeout(8000);
            ftp.connect(host, port);

            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                throw new IOException("FTP connect failed: " + ftp.getReplyString());
            }

            if (!ftp.login(user, pass)) {
                throw new IOException("FTP login fail: " + ftp.getReplyString());
            }

            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            String cleanBase = normalizeDir(baseDir); // "/tickets"
            ensureDirs(ftp, cleanBase);

            String fullPath = cleanBase + "/" + remoteFileName; // "/tickets/ABC.pdf"

            try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
                boolean ok = ftp.storeFile(fullPath, in);
                if (!ok) throw new IOException("FTP storeFile failed: " + ftp.getReplyString());
            }

            return fullPath;

        } catch (Exception e) {
            throw new RuntimeException("FTP upload error: " + e.getMessage(), e);
        } finally {
            try { if (ftp.isConnected()) ftp.logout(); } catch (Exception ignored) {}
            try { if (ftp.isConnected()) ftp.disconnect(); } catch (Exception ignored) {}
        }
    }

    public byte[] download(String remotePath) {
        if (remotePath == null || remotePath.isBlank()) throw new RuntimeException("remotePath empty");

        FTPClient ftp = new FTPClient();
        try {
            ftp.setConnectTimeout(8000);
            ftp.connect(host, port);

            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                throw new IOException("FTP connect failed: " + ftp.getReplyString());
            }

            if (!ftp.login(user, pass)) {
                throw new IOException("FTP login fail: " + ftp.getReplyString());
            }

            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            String fullPath = normalizeRemotePath(remotePath);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                boolean ok = ftp.retrieveFile(fullPath, out);
                if (!ok) throw new IOException("FTP retrieveFile failed: " + ftp.getReplyString());
                return out.toByteArray();
            }

        } catch (Exception e) {
            throw new RuntimeException("FTP download error: " + e.getMessage(), e);
        } finally {
            try { if (ftp.isConnected()) ftp.logout(); } catch (Exception ignored) {}
            try { if (ftp.isConnected()) ftp.disconnect(); } catch (Exception ignored) {}
        }
    }

    private void ensureDirs(FTPClient ftp, String path) throws IOException {
        String[] parts = path.split("/");
        String curr = "";
        for (String p : parts) {
            if (p.isBlank()) continue;
            curr += "/" + p;
            ftp.makeDirectory(curr); // si ya existe, OK
        }
    }

    private String normalizeDir(String dir) {
        if (dir == null || dir.isBlank()) return "";
        String d = dir.trim();
        if (!d.startsWith("/")) d = "/" + d;
        if (d.endsWith("/")) d = d.substring(0, d.length() - 1);
        return d;
    }

    private String normalizeRemotePath(String remotePath) {
        String rp = remotePath.trim();
        if (rp.startsWith("/")) return rp;
        String cleanBase = normalizeDir(baseDir);
        return cleanBase + "/" + rp;
    }
}
