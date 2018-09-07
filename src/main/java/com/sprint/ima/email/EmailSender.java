package com.sprint.ima.email;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Denys Kovalenko on 9/6/2018.
 */
@Component
public class EmailSender implements InitializingBean {
    private static final Logger logger = LogManager.getLogger(EmailSender.class);
    private Session session;

    @Value("${email.from.address}")
    private String fromAddress;

    @Value("${email.to.recipientsList}")
    private String toRecipientsList;

    @Value("${email.cc.recipientsList}")
    private String ccRecipientsList;

    @Value("${alert.threshold}")
    private int alertThreshold;

    @Value("${alert.to.recipientsList}")
    private String alertToRecipientsList;

    @Value("${alert.cc.recipientsList}")
    private String alertCcRecipientsList;

    @Value("${ima.http.request.url}")
    private String imaHTTPRequestUrl;

    @Value("${email.server.host}")
    private String serverHost;

    @Value("${email.server.port}")
    private String serverPort;

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties props = new Properties();
        props.put("mail.host", serverHost);
        props.put("mail.port", serverPort);
        session = Session.getDefaultInstance(props);
    }

    public void sendNotification(int activeConnections) {
        String body = "Hello All," +
                "\n\nLatest monitoring statistics for: " + imaHTTPRequestUrl +
                "\n\nActive connections: " + activeConnections +
                "\n\n\nThis email was sent by IMA Monitoring Application. Please do not reply on this message.";

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toRecipientsList));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccRecipientsList));

            message.setSubject("IMA active connections: " + activeConnections);
            message.setText(body);

            Transport.send(message);
            logger.info("The email has been sent, connections number: " + activeConnections);
        } catch (MessagingException e) {
            logger.error("Error during sending email notification: ", e);
        }
    }

    public void sendAlertNotification(int activeConnections) {
        String body = "Hello All," +
                "\n\nActive connections number significant drop detected: > " + alertThreshold + "% !" +
                "\nCurrent active connections number: " + activeConnections +
                "\nMonitoring statistics for: " + imaHTTPRequestUrl +
                "\n\n\nThis alert was sent by IMA Monitoring Application. Please do not reply on this message.";

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(alertToRecipientsList));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(alertCcRecipientsList));

            message.setSubject("IMA Alert! Connections drop " + alertThreshold + "%! Currently: " + activeConnections);
            message.setText(body);

            Transport.send(message);
            logger.info("The alert has been sent, connections number: " + activeConnections + ". Threshold: " + alertThreshold);
        } catch (MessagingException e) {
            logger.error("Error during sending email alert: ", e);
        }
    }
}
