package com.example.demo;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotification {

	private JavaMailSender javaMailSender; // Extended org.springframework.mail.MailSender interface for
											// JavaMail,supporting MIME messages both as direct arguments and through
											// preparationcallbacks. Typically used in conjunction with the
											// MimeMessageHelperclass for convenient creation of JavaMail
											// MimeMessages,including attachments etc

	public EmailNotification(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendMail(String email, String message) { // Models a simple mail message, including data such as the
															// from, to, cc, subject,and text fields
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email, email);
		msg.setSubject("Event Reminder");
		msg.setText(message);
		javaMailSender.send(msg);
	}
}
