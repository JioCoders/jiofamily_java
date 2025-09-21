package com.jiocoders.java.jiofamily.service.other;

import com.jiocoders.java.jiofamily.utils.Common;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SendOTPService {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public void sendOTPSMS(String mobileNumber, String otpString) {
        if (Common.checkNotNull(mobileNumber)) {
        }
        if (mobileNumber.length() != 10) {
        }
        HttpHeaders headers = new HttpHeaders();

        try {
            HttpEntity<String> req = new HttpEntity<>(headers);
            String url = "https://aaaa.zone?ver=1.0&key=xxxx==&encrpt=0&dest="
                    + mobileNumber +
                    "&send=zzzz&text=" + otpString + "%20xxx.";
            URL url1 = new URL(url);
            URLConnection conn = url1.openConnection();
            conn.connect();
            // get response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String sResult = "";
            while ((line = rd.readLine()) != null) {
                // Process line...
                sResult = sResult + line + " ";
            }
            rd.close();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void sendMail(String to, String subject, String content) {
        final String username = "noreply@xxxx.com";
        final String password = "zzzz";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "mail.smtp2go.com");
        props.put("mail.smtp.port", "2525"); // 8025, 587 and 25 can also be used.
        Session session = Session.getInstance(props,
                new jakarta.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            Multipart mp = new MimeMultipart("alternative");
            BodyPart textMessage = new MimeBodyPart();

            content = content
                    + "\n\n\nOTPs are secret. DO NOT disclose it to anyone.\nWe'll NEVER asks for OTP.";

            textMessage.setText(content);
            mp.addBodyPart(textMessage);
            message.setFrom(new InternetAddress("noreply@tempdomain.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(mp);
            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendOTPEmail(String email, String otp) {
        if (isValidEmail(email)) {
            sendMail(email, "OTP for Login",
                    "Dear Customer,\n\n" + otp + " is your OTP for login.");
        }
    }
}