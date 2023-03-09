package com.schedule.app.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * @author : Thành Lộc
 * @since : 11/17/2022, Thu
 **/
public class SendMail {

    public static void sendMailActiveAccount(String toEmail, String code) throws UnsupportedEncodingException, MessagingException {
        final String fromEmail = "nguyentraipro115@gmail.com";
        // Mat khai email cua ban
        final String password = "wexpkjbizfkqskgp";
        // dia chi email nguoi nhan
        final String subject = "Kích hoạt tài khoản";
        final String body = "Vui lòng nhấn vào link này để kích hoạt tài khoản, nếu bạn không thực hiện vui lòng bỏ qua " +
                "thông báo này.\n" +
                "https://tutor-service.azurewebsites.net/tutor-service/v1/auths/active?token="+code;
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);
        MimeMessage msg = new MimeMessage(session);
        //set message headers
        msg.addHeader("Content-type", "text/html; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");
        msg.setFrom(new InternetAddress(fromEmail, "Nông Lâm Tp.HCM"));
        msg.setReplyTo(InternetAddress.parse(fromEmail, false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        msg.setSentDate(new Date());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        msg.saveChanges();
        Transport.send(msg);
        System.out.println("Gui mail thanh cong");
    }

    public static void sendMailActiveAccountGG(String toEmail, String code, String passwordUser) throws UnsupportedEncodingException, MessagingException {
        final String fromEmail = "nguyentraipro115@gmail.com";
        // Mat khai email cua ban
        final String password = "lqiuxamlvtzazyxk";
        // dia chi email nguoi nhan
        final String subject = "Kích hoạt tài khoản";
        final String body = "Vui lòng nhấn vào link này để kích hoạt tài khoản, nếu bạn không thực hiện vui lòng bỏ qua " +
                "thông báo này.\n" +
                "https://tutor-service.azurewebsites.net/tutor-service/v1/auths/active?token="+code +"\n"+
                "Mật khẩu của bạn là: "+passwordUser;
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);
        MimeMessage msg = new MimeMessage(session);
        //set message headers
        msg.addHeader("Content-type", "text/html; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");
        msg.setFrom(new InternetAddress(fromEmail, "Nông Lâm Tp.HCM"));
        msg.setReplyTo(InternetAddress.parse(fromEmail, false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        msg.setSentDate(new Date());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        msg.saveChanges();
        Transport.send(msg);
        System.out.println("Gui mail thanh cong");
    }

    public static void sendMailOTP(String toEmail, int code) throws UnsupportedEncodingException, MessagingException {
        final String fromEmail = "nguyentraipro115@gmail.com";
        // Mat khai email cua ban
        final String password = "hponpoaskjkhxwnf";
        // dia chi email nguoi nhan
        final String subject = "Quên mật khẩu";
        final String body = "Vui lòng nhập mã OTP này đễ lấy lại mật khẩu: " +code+"\n"+
                "Nếu bạn không thực hiện thao tác vui lòng bỏ thông báo này.\n";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);
        MimeMessage msg = new MimeMessage(session);
        //set message headers
        msg.addHeader("Content-type", "text/html; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");
        msg.setFrom(new InternetAddress(fromEmail, "Nông Lâm Tp.HCM"));
        msg.setReplyTo(InternetAddress.parse(fromEmail, false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        msg.setSentDate(new Date());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        msg.saveChanges();
        Transport.send(msg);
        System.out.println("Gui mail thanh cong");
    }

    public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
        SendMail.sendMailActiveAccount("nguyenthanhlocpc@gmail.com","123");
    }

}
