package com.sprint.ima.processor;

import com.sprint.ima.email.EmailSender;
import com.sprint.ima.handler.DataHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Denys Kovalenko on 9/6/2018.
 */
@Component
public class ExecutionProcessor {
    private static final Logger logger = LogManager.getLogger(ExecutionProcessor.class);

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private DataHandler dataHandler;

    @Scheduled(cron = "${email.trigger.cron}", zone = "${cron.time.zone}")
    public void sendIMARequest() {
        int connectionsNumber = dataHandler.getCurrentConnectionsNumber();
        if(connectionsNumber != -1) {
            emailSender.sendNotification(connectionsNumber);
        }
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void measureConnections() {
        int connectionsNumber = dataHandler.getCurrentConnectionsNumber();
        logger.info("Current connections number: " + connectionsNumber);
    }

}
