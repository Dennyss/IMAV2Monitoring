package com.sprint.ima.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.ima.bean.StatMessage;
import com.sprint.ima.rest.RestGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by Denys Kovalenko on 9/6/2018.
 */
@Component
public class DataHandler {
    private static final Logger LOGGER = LogManager.getLogger(DataHandler.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Value("${ima.http.request.url}")
    private String imaHTTPRequestUrl;

    @Autowired
    private RestGateway restGateway;

    public int getCurrentConnectionsNumber() {
        ResponseEntity<String> httpResponse = restGateway.get(imaHTTPRequestUrl, null, String.class);
        if (!HttpStatus.OK.equals(httpResponse.getStatusCode())) {
            LOGGER.debug("Received HTTP response from IMA with not expected status code: " + httpResponse.getStatusCodeValue() + ". Skipping response and stopping flow ...");
            return -1;
        }

        LOGGER.debug("Received HTTP response from IMA with status code: " + httpResponse.getStatusCodeValue() + ". Response body: " + httpResponse.getBody());

        StatMessage statMessage = null;

        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            statMessage = mapper.readValue(httpResponse.getBody(), StatMessage.class);
        } catch (IOException e) {
            LOGGER.error("Exception while parsing input payload. Stopping flow", e);
            return -1;
        }

        return statMessage.getServer().getActiveConnections();
    }

}
