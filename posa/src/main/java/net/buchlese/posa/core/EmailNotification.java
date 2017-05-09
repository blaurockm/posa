package net.buchlese.posa.core;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailNotification {

	public static String herkunft = "Kasse unbekannt";

	public static String emailUser = "buchlese.dornhan@gmail.com";
	public static String emailPass = "123456";
	public static String emailFrom = "dornhan@buchlese.net";
	public static String emailTo  = "blaurock@buchlese.net";

	public static void sendAdminMail(String text, Throwable t) {    
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailUser,emailPass);
				}
			});

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(emailFrom));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));

			// Set Subject: header field
			message.setSubject("Buchlesemail aus " + herkunft);

			String msg = "Es gibt folgendes zu melden: \n " + text;
			if (t != null) {
				msg += "\n\n" + t.toString();
			}
			// Now set the actual message
			message.setText(msg);

			// Send message
			Transport.send(message);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
