package com.treetops.jinvite;

import java.sql.Connection;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import com.treetops.jinvite.pojo.InviteMessage;

public class Inviter {

	private String smtpHost;
	private String smtpUser;
	private String smtpPass;
	private int smtpPort = 26;
    private Properties p = new Properties();
    Authenticator auth = new PopupAuthenticator();
	
	public Inviter(String smtpHost, String smtpUser, String smtpPass) {
		this.smtpHost = smtpHost;
		this.smtpUser = smtpUser;
		this.smtpPass = smtpPass;
	}

	public Inviter(String smtpHost, int port, String smtpUser, String smtpPass) {
		this.smtpHost = smtpHost;
		this.smtpUser = smtpUser;
		this.smtpPass = smtpPass;
		this.smtpPort = port;
	}
	
	public void invite(InviteMessage msg) throws MessagingException,InviteMessageException {
    	p.put("mail.host",smtpHost);
    	p.put("mail.smtp.auth", "true");
    	p.put("mail.smtp.port", Integer.toString(smtpPort));
    	
    	MimeMessage message = new MimeMessage(Session.getInstance(p, auth));
        message.setFrom(new InternetAddress(msg.getFrom()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(msg.getTo()));
        message.setSubject(msg.getSubject());
        message.setText(msg.getMsgBody());
        
        Transport.send(message);
	}
    class PopupAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(smtpUser,smtpPass);
        }
    }
}
