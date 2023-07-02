import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalTime;
import java.util.Properties;

public class SendEmail {
    static final String senderEmail = "mobiledevbyali@gmail.com";
    static final String senderPassword = "jiqwiaxikfsqhwov";

    public static void SendEmailToUser(String recipientEmail, String generatedCode) {

        String text = String.format("Hello! This is your code you have 2 minutes to Login %s ", generatedCode);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Hello from DB Project");
            message.setText(text);

            Transport.send(message);

            System.out.println("Email sent successfully");

            //Main.timeTrack = LocalTime.now();

        } catch (MessagingException e) {
            System.out.println("Failed to send email. Error: " + e.getMessage());
        }

    }
}
