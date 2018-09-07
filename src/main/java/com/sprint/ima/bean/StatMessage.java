package com.sprint.ima.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Denys Kovalenko on 9/6/2018.
 */
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatMessage {
    @JsonProperty("Version")
    private String version;
    @JsonProperty("Server")
    private Server server;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public com.sprint.ima.bean.Server getServer() {
        return server;
    }

    public void setServer(com.sprint.ima.bean.Server server) {
        this.server = server;
    }
}
