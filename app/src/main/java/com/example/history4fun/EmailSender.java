package com.example.history4fun;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.util.Log;

public class EmailSender {
    /* TODO
        È importante notare che per utilizzare JavaMail API è necessario configurare le impostazioni SMTP del proprio provider di posta elettronica.
        Inoltre, se si utilizza Gmail, sarà necessario abilitare l'accesso alle app meno sicure per consentire l'invio di email da parte del proprio programma Java.
    * */
    private boolean error = false;
    private String to     = null;

    public EmailSender(String email_dest) {
        this.to = email_dest;
    }

    private void setError(boolean error) { this.error = error; }

    public boolean getError() { return this.error; }

    public void sendEmail() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("killuag09@gmail.com", "Darosiga1@_");
            }
        });

        Message message = new MimeMessage(session);

        CharsetGenerator generator = new CharsetGenerator(5);
        String code = generator.get_generated_random_string();
        try {
            message.setFrom(new InternetAddress("killuag09@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Codice di verifica per recupero password");
            message.setText("Il codice da verificare è il seguente: " + code);
            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            setError(true);
            Log.i("EMAIL_SNDR: ", "Failed to send email.");
        }
    }
}