package com.sprint.ima.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Denys Kovalenko on 9/6/2018.
 */
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Server {
    @JsonProperty("ActiveConnections")
    private int activeConnections;

    public int getActiveConnections() {
        return activeConnections;
    }

    public void setActiveConnections(int activeConnections) {
        this.activeConnections = activeConnections;
    }
}
