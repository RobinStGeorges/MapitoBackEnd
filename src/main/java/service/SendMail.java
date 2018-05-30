package service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;


public class SendMail {
    public static void sendMessage(String subject, String text, String destinataire, String copyDest) {

        String login  = "mapitolerance@gmail.com";
        String passwrd = "superjeuxdemot2018";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setText(text);
            message.setSubject(subject);
            message.addRecipients(Message.RecipientType.TO, destinataire);
            message.addRecipients(Message.RecipientType.CC, copyDest);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Transport transport;
        try {
            transport = session.getTransport("smtp");
            transport.connect(login, passwrd);
            transport.sendMessage(message, new Address[] { new InternetAddress(destinataire),
                    new InternetAddress(copyDest) });
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
