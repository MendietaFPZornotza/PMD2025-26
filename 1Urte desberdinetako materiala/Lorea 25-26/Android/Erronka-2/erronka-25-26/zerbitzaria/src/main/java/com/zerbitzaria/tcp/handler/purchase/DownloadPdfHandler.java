package com.zerbitzaria.tcp.handler.purchase;

import com.zerbitzaria.mail.FTPService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
class DownloadPdfHandler implements TcpHandler {

    private final FTPService ftp;

    public DownloadPdfHandler(FTPService ftp) {
        this.ftp = ftp;
    }

    @Override
    public String command() {
        return "DOWNLOAD_PDF";
    }

    @Override
    public String handle(TcpRequest req) {
        if (req.args().length != 1) return "PDF_FAIL;BAD_ARGS";

        String remotePath = req.args()[0].trim();
        if (remotePath.isBlank()) return "PDF_FAIL;EMPTY_PATH";

        try {
            byte[] data = ftp.download(remotePath);
            if (data == null || data.length == 0) return "PDF_FAIL;EMPTY_FILE";

            String b64 = Base64.getEncoder().encodeToString(data);

            // lo partimos para no reventar la línea (muy importante)
            int chunk = 3500;
            StringBuilder sb = new StringBuilder();
            sb.append("PDF_OK;").append(data.length).append("\n");

            for (int i = 0; i < b64.length(); i += chunk) {
                int end = Math.min(i + chunk, b64.length());
                sb.append(b64, i, end).append("\n");
            }
            sb.append("PDF_END");

            return sb.toString();

        } catch (Exception e) {
            return "PDF_FAIL;" + safe(e.getMessage());
        }
    }

    private String safe(String s) {
        if (s == null) return "SERVER";
        return s.replace(";", " ").replace("|", " ");
    }
}