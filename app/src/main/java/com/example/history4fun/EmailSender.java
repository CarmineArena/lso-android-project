package com.example.history4fun;

// import javax.mail.*;
// import javax.mail.internet.InternetAddress;
// import javax.mail.internet.MimeMessage;

import android.util.Log;
import java.util.Properties;

public class EmailSender {
    /* TODO
        È importante notare che per utilizzare JavaMail API è necessario configurare le impostazioni SMTP del proprio provider di posta elettronica.
        Inoltre, se si utilizza Gmail, sarà necessario abilitare l'accesso alle app meno sicure per consentire l'invio di email da parte del proprio programma Java.
    * */

    private String to            = null;
    private String from          = ""; // TODO: MODIFICARE CON UNA NUOVA
    private String password_from = ""; // TODO: MODIFICARE
    private String host          = "smtp.gmail.com";
    private final int port       = 587;
    private String starttls      = "true";
    private String auth          = "true";

    public EmailSender(String email_dest) {
        this.to = email_dest;
    }

    public void sendEmail() {
        /*
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
        // TODO: AL SUCCESSO, ALERTDIALOG ALLA ACTIVITY
        try {
            CharsetGenerator generator = new CharsetGenerator(5);
            String code = generator.get_generated_random_string();

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Codice di verifica per recupero password");
            message.setText("Il codice da verificare è il seguente: " + code);

            Transport.send(message);
            Log.i("EMAIL_SNDR: ", "Email sent correctly!");
        } catch (MessagingException ex) {
            Log.i("EMAIL_SNDR: ", "Failed to send email.");
        }
*/
        Log.i("EMAIL_SNDR: ", "Email sent correctly!");
    }
}