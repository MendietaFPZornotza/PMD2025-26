package com.zerbitzaria.mail;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class EmailService {

    @Value("${mail.smtp.host}")
    private String host;
    @Value("${mail.smtp.port}")
    private int port;
    @Value("${mail.smtp.user}")
    private String user;
    @Value("${mail.smtp.pass}")
    private String pass;

    public void sendDownloadCode(String toEmail, String downloadCode) {
        if (toEmail == null || toEmail.isBlank()) return;
        if (downloadCode == null || downloadCode.isBlank()) return;

        Properties props = new Properties();

        // === SMTP SMTPS (465) ===
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");

        // 🔥 IMPORTANTE: SSL directo, NO starttls
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable", "false");

        // Protocolos
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Timeouts (para que NUNCA bloquee)
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(user, "EkiTop", StandardCharsets.UTF_8.name()));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail.trim()));
            msg.setSubject("EkiTop - Deskarga kodea", "UTF-8");

            String body =
                    "<p>Kaixo!</p>" +
                            "<p>Zure sarrerak deskargatzeko kodea hau da:</p>" +
                            "<p style='font-size:20px;font-weight:800;letter-spacing:1px'>" +
                            downloadCode +
                            "</p>" +
                            "<p>Profila → Nire sarrerak → Deskargatu</p>" +
                            "<p><b>EkiTop</b></p>";

            msg.setContent(body, "text/html; charset=UTF-8");
            Transport.send(msg);

        } catch (Exception e) {
            e.printStackTrace(); // NO rompe compra
        }
    }
}