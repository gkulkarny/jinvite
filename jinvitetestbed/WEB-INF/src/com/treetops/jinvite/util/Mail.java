package com.treetops.jinvite.util;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * This class represents Email. It allows the user to send a simple mail message as well as send
 * a message with an attachment.
 * @author chad
 *
 */
public class Mail {
    private String host;
    private String from;
    private String username;
    private String password;
    private Properties p = new Properties();
    Authenticator auth = new PopupAuthenticator();
   
   
    /**
     * Setup the mail object
     * @param from	the address you want the email to be from
     * @param SMTPHost	host responsible for sending out the mail
     * @param user	 SMTP user
     * @param pass	SMTP password
     */
    public Mail(String from, String SMTPHost, String user, String pass ) {
    	this.from = from;
    	this.host = SMTPHost;
    	this.username = user;
    	this.password = pass;
    }
    /**
     * Sends a simple email message to one recipient. 
     * @param ToAddress
     * @param Subject
     * @param Body
     * @throws Exception
     */
    public void sendMessage( String ToAddress, String Subject, String Body ) throws Exception {
    	p.put("mail.host",host);
    	p.put("mail.smtp.auth", "true");

    	MimeMessage message = new MimeMessage(Session.getInstance(p, auth));
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ToAddress));
        message.setSubject(Subject);
        message.setText(Body);
        
        Transport.send(message);
    }
    /**
     * Sends an email message to a recipient with one attachment.
     * @param ToAddress
     * @param Subject
     * @param Body
     * @param attachment
     * @throws Exception
     */
    public void sendMessage( String toAddress, String subject, String body,String attachment ) throws Exception {
    	p.put("mail.host",host);
    	p.put("mail.smtp.auth", "true");
    	
        // setup the message
        MimeMessage message = new MimeMessage(Session.getInstance(p, auth));
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
        message.setSubject(subject);
       
        // setup the attachment+msgbody
        File file = new File(attachment);
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body);
        Multipart multipart = new MimeMultipart();
        DataSource source = new FileDataSource(attachment);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(file.getName());
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        
        Transport.send(message);
    }
    class PopupAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username,password);
        }
    }
}