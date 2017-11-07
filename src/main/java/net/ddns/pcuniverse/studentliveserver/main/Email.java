package net.ddns.pcuniverse.studentliveserver.main;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Email {

    public static Boolean emailPassword(String username, String email, String password) {
        return email(email, "StudentLive - Forgot password", "Dear " + username + "\n\nYou requested to send your StudentLive password to your email.\nIf this wasn't you please contact us.\n\nPassword:\t" + password);
    }

    public static Boolean resetPassword(String username, String email, String password) {
        return email(email, "StudentLive - Reset password", "Dear " + username + "\n\nYour StudentLive password has been reset.\n\nHere is your new password: \t" + password + "\n\nYou wil be asked to change password upon login\nIf you did not request this reset please contact the admin office");
    }

    public static Boolean initPassword(String username, String email, String password) {
        return email(email, "StudentLive - Registered", "Dear " + username + "\n\nYour StudentLive account has been created.\n\nHere is your password: \t" + password + "\n\nYou wil be asked to change password upon login\nIf this is not you, please ignore this message");
    }

    private static Boolean email(String email, String emailSubject, String emailMessage) {
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", true);
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.user", "studentliverecovery@gmail.com");
            props.put("mail.smtp.password", "recoverystudentlive");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", true);
            Session session = Session.getInstance(props, null);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", "studentliverecovery", "recoverystudentlive");
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(emailSubject);
            message.setText(emailMessage);
            transport.sendMessage(message, message.getAllRecipients());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
