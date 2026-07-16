package com.zerbitzaria.mail;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.zerbitzaria.sarrera.entity.ErositakoSarrera;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TicketPdfService {

    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public File generatePdf(String deskargaKodea, List<ErositakoSarrera> tickets) throws Exception {
        if (deskargaKodea == null || deskargaKodea.isBlank()) throw new IllegalArgumentException("deskargaKodea empty");
        if (tickets == null || tickets.isEmpty()) throw new IllegalArgumentException("tickets empty");

        try (PDDocument doc = new PDDocument()) {

            for (ErositakoSarrera s : tickets) {
                PDPage page = new PDPage();
                doc.addPage(page);

                try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                    // Título
                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    cs.newLineAtOffset(50, 750);
                    cs.showText("EKITOP - SARRERA");
                    cs.endText();

                    // Datos
                    String title = s.getEkitaldia() != null ? safe(s.getEkitaldia().getIzenburua()) : "-";
                    String dateTime = (s.getEkitaldia() != null && s.getEkitaldia().getHasiera() != null)
                            ? s.getEkitaldia().getHasiera().format(DT)
                            : "-";
                    String seat = rowLabel(s.getFila()) + s.getEserlekua();

                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 12);
                    cs.newLineAtOffset(50, 710);
                    cs.showText("Ekitaldia: " + title);

                    cs.newLineAtOffset(0, -18);
                    cs.showText("Data/Ordua: " + dateTime);

                    cs.newLineAtOffset(0, -18);
                    cs.showText("Eserlekua: " + seat);

                    cs.newLineAtOffset(0, -18);
                    cs.showText("Deskarga kodea: " + deskargaKodea);
                    cs.endText();

                    // QR (kodea por ticket)
                    String qrText = buildPlainQrText(s);
                    BufferedImage qr = buildQr(qrText);

                    PDImageXObject qrImg = LosslessFactory.createFromImage(doc, qr);
                    cs.drawImage(qrImg, 50, 470, 150, 150);

                    cs.beginText();
                    cs.setFont(PDType1Font.HELVETICA, 10);
                    cs.newLineAtOffset(50, 455);
                    cs.showText("QR kodea: " + safe(s.getKodea()));
                    cs.endText();
                }
            }

            File file = new File("/tmp/" + deskargaKodea + ".pdf");
            doc.save(file);
            return file;
        }
    }

    private BufferedImage buildQr(String text) throws Exception {
        BitMatrix matrix = new QRCodeWriter().encode(
                text, BarcodeFormat.QR_CODE, 300, 300
        );
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    private String buildPlainQrText(ErositakoSarrera s) {

        String title = (s.getEkitaldia() != null) ? safe(s.getEkitaldia().getIzenburua()) : "-";

        String start = "-";
        if (s.getEkitaldia() != null && s.getEkitaldia().getHasiera() != null) {
            // Ejemplo: 2026-01-23 13:35
            start = s.getEkitaldia().getHasiera().format(DT);
        }

        String seat = rowLabel(s.getFila()) + s.getEserlekua();

        // TEXTO PLANO (lo que verá cualquier móvil al escanear)
        return "Ekitaldia: " + title + "\n" +
                "Hasiera: " + start + "\n" +
                "Eserlekua: " + seat + "\n" +
                "Deskarga kodea: " + safe(s.getDeskargaKodea());
    }

    private String rowLabel(int r) {
        int n = r;
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            n--;
            sb.append((char) ('A' + (n % 26)));
            n /= 26;
        }
        return sb.reverse().toString();
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replace(";", " ").replace("|", " ");
    }
}