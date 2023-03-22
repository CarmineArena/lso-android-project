package com.example.history4fun;

import javax.mail.*; // TODO: MODIFICARE IL GRADLE?
import android.util.Log;
import java.util.Properties;

public class EmailSender {
    /* TODO
        È importante notare che per utilizzare JavaMail API è necessario configurare le impostazioni SMTP del proprio provider di posta elettronica.
        Inoltre, se si utilizza Gmail, sarà necessario abilitare l'accesso alle app meno sicure per consentire l'invio di email da parte del proprio programma Java.
    * */

    private String to            = null; // Indirizzo email del destinatario
    private String from          = null; // TODO: Inserire Indirizzo email del mittente, ne creiamo uno nuovo? Per non usare la mia
    private String password_from = null; // TODO: INSERIRE
    private String host          = null; // TODO: INSERIRE IL NOME HOST DEL SERVER SMTP DI GMAIL (LE MAIL VERRANNO INVIATE DA QUESTO COMPUTER) --> "smtp.gmail.com"
    private final int port       = 587;
    private String starttls      = "true";
    private String auth          = "true";

    public EmailSender(String email_dest) {
        this.to = email_dest;
    }

    public void sendEmail() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.starttls.enable", starttls);
        properties.put("mail.smtp.auth", auth);

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password_from);
            }
        };

        Session session = Session.getDefaultInstance(properties, authenticator);

        // TODO: COME NOTIFICO UN ERRORE ALLA SCHERMATA DI FORGOT PASSWORD?
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Oggetto dell'email");
            message.setText("Testo del messaggio");

            Transport.send(message);
            Log.i("EMAIL_SNDR: ", "Email sent correctly!");
        } catch (MessagingException ex) {
            Log.i("EMAIL_SNDR: ", "Failed to send email.");
        }

    }
}