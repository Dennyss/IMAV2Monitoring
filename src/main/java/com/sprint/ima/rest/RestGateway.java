package com.sprint.ima.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by denys.kovalenko on 9/6/2018.
 */
@Component
public class RestGateway {
    private static final Logger LOGGER = LogManager.getLogger(RestGateway.class);

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> get(String url, HttpHeaders headers, Class<String> respType) {
        long timeElapsed = 0;
        HttpEntity<String> requestEntity = null;
        ResponseEntity<String> responseEntity = null;
        try {
            requestEntity = new HttpEntity<>(headers);
            long lStartTime = System.currentTimeMillis();
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, respType);
            long lEndTime = System.currentTimeMillis();
            timeElapsed = lEndTime - lStartTime;
        } catch (RestClientException e) {
            LOGGER.error("Exception occurred while executing Rest GET request", e);
        }
        logDetails(url, requestEntity, responseEntity, timeElapsed);
        return responseEntity;
    }

    private void logDetails(String url, HttpEntity<String> requestEntity, ResponseEntity<String> responseEntity, long timeElapsed) {
        StringBuilder builder = new StringBuilder();
        builder.append("Rest Service Details:\n");
        builder.append("Request URL: ").append(url).append("\n");
        if (requestEntity != null) {
            if (requestEntity.getBody() != null) {
                builder.append("Request Body: ").append(requestEntity.getBody()).append("\n");
            }
            HttpHeaders headers = requestEntity.getHeaders();
            if (headers != null) {
                builder.append("Request Headers:").append("\n");
                for (String key : headers.keySet()) {
                    builder.append(key).append(" : ").append(headers.get(key)).append("\n");
                }
            }
        }
        if (responseEntity != null) {
            builder.append("Response Code: ").append(responseEntity.getStatusCodeValue()).append("\n");
            if (responseEntity.getBody() != null) {
                builder.append("Response Body: ").append(responseEntity.getBody()).append("\n");
            }
        }
        builder.append("Response time: ").append(timeElapsed).append(" millis");
        LOGGER.debug(builder.toString());
    }
}
