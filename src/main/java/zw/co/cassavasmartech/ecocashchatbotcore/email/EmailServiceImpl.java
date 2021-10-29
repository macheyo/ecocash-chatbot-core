package zw.co.cassavasmartech.ecocashchatbotcore.email;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import zw.co.cassavasmartech.ecocashchatbotcore.email.data.EmailNotification;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private EmailConfigurationProperties emailConfigConfigProperties;
    private JavaMailSender mailSender;


    public EmailServiceImpl(EmailConfigurationProperties emailConfigConfigProperties, JavaMailSender mailSender) {
        this.emailConfigConfigProperties = emailConfigConfigProperties;
        this.mailSender = mailSender;
    }


    @Override
    public void send(EmailNotification message) {
        if(StringUtils.hasLength(message.getFilePath())){
            sendEmailWithAttachment(message);
        }else {
            sendBasicEmail(message);
        }
    }

    private void sendBasicEmail(EmailNotification message) {
        log.info("Sending Email: {}",message);
        try {
            Session session = Session.getInstance(properties(), authenticator());
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(message.getSender()));
            InternetAddress[] toAddresses = {new InternetAddress(message.getTo())};
            email.setRecipients(Message.RecipientType.TO, toAddresses);
            email.setSubject(message.getSubject());
            email.setSentDate(new Date());
            email.setContent(message.getTemplateName(), "text/html; charset=utf-8");
            log.info("=> Sending email for user: {}", message.getTo());
            mailSender.send(email);
            log.info("<= Done sending email for user: {}", message.getTo());
        } catch (Exception e) {
            log.error("Sending emailed failed with error: {}", e);
        }

    }

    public void sendEmailWithAttachment(EmailNotification emailMessage) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emailMessage.getSender());
            helper.setTo(emailMessage.getTo());
            helper.setSubject(emailMessage.getSubject());
            helper.setText(emailMessage.getTemplateName(),true);
            FileSystemResource file = new FileSystemResource(emailMessage.getFilePath());
            helper.addAttachment(file.getFilename(), file);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
        log.info("Sending email with attachment...");
        mailSender.send(message);
        log.info("Done sending email with attachment for user: {}", emailMessage.getTo());
    }


    private Properties properties(){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", emailConfigConfigProperties.getHost());
        properties.put("mail.smtp.port", emailConfigConfigProperties.getPort());
        //properties.put("mail.smtp.auth", emailConfigConfigProperties.getAuth());
        //properties.put("mail.smtp.starttls.enable", emailConfigConfigProperties.getStartTsl());
        //properties.put("mail.smtp.ssl.trust", "smtp.office365.com");
        //properties.put("mail.smtp.starttls.enable", "true");
        return properties;
    }

    private Authenticator authenticator(){
        return new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfigConfigProperties.getUsername(), emailConfigConfigProperties.getPassword());
            }
        };
    }

}
