package org.moultdb.api.service;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-25
 */
public interface MailService {
    
    public void sendEmail(String recipient, String subject, String content);
    
}
