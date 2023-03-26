package com.example.history4fun;

// import java.util.Properties;
// import javax.mail.MessagingException;
// import javax.mail.Session;
// import javax.mail.Message;
// import javax.mail.Transport;
// import javax.mail.internet.InternetAddress;
// import javax.mail.internet.MimeMessage;
// import android.util.Log;

// TODO: HA SENSO MANTENERE QUESTA CLASSE?
public class EmailSender {
    private boolean error = false;
    private String dest     = null;

    public EmailSender(String email_dest) {
        this.dest = email_dest;
    }

    private void setError(boolean error) { this.error = error; }

    public boolean getError() { return this.error; }

    /*
    public void sendEmail() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("help.history4fun@gmail.com", "noreply_");
            }
        });

        Message message = new MimeMessage(session);

        CharsetGenerator generator = new CharsetGenerator(5);
        String code = generator.get_generated_random_string();
        try {
            message.setFrom(new InternetAddress("help.history4fun@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Codice di verifica per recupero password");
            message.setText("Il codice da verificare è il seguente: " + code);
            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            setError(true);
            Log.i("EMAIL_SNDR: ", "Failed to send email.");
        }
    } */
}