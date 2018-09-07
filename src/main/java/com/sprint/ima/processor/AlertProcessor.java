package com.sprint.ima.processor;

import com.sprint.ima.email.EmailSender;
import com.sprint.ima.handler.DataHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Denys Kovalenko on 9/6/2018.
 */
@Component
public class AlertProcessor {
    private static final Logger LOGGER = LogManager.getLogger(AlertProcessor.class);
    private int previousValue;
    private boolean alert;

    @Value("${alert.threshold}")
    private int alertThreshold;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private DataHandler dataHandler;

    @Scheduled(cron = "${alert.trigger.cron}", zone = "${cron.time.zone}")
    public void sendIMARequestForAlert() {
        LOGGER.info("Checking for alerts ...");

        int connectionsNumber = dataHandler.getCurrentConnectionsNumber();
        if (connectionsNumber == -1) {
            return;
        }

        alert = updateForAlert(connectionsNumber, previousValue);
        previousValue = connectionsNumber;

        if (alert) {
            emailSender.sendAlertNotification(connectionsNumber);
        }
    }

    private boolean updateForAlert(int currentNumber, int previousValue) {
        if (previousValue != 0 && currentNumber < previousValue * (100 - alertThreshold) / 100) {
            return true;
        }
        return false;
    }
}
