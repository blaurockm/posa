package net.buchlese.posa.core;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailNotification {

	public static String herkunft = "Kasse unbekannt";

	public static void sendAdminMail(String text, Throwable t) {    
		// Recipient's email ID needs to be mentioned.
		String to = "blaurock@buchlese.net";

		// Sender's email ID needs to be mentioned
		String from = "dornhan@buchlese.net";

		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

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
		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
