package org.moultdb.api.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.MailNotSentException;
import org.moultdb.api.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-25
 */
@Service
public class MailServiceImpl implements MailService {
    
    private final static Logger logger = LogManager.getLogger(MailServiceImpl.class.getName());
    
    @Value("${mail.user}")
    private String senderEmail;
    
    @Value("${mail.smtp.host}")
    private String smtpHost;
    
    public void sendEmail(String recipientEmail, String subject, String content) {
        
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
    
        Session session = Session.getDefaultInstance(props);
    
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(content);
    
            logger.debug("Sending...");
            Transport.send(message);
            logger.debug("Sent message successfully....");
        } catch (Exception e) {
            throw new MailNotSentException("Unable to send the mail. Error: " + e.getMessage(), e);
        }
    }
}
