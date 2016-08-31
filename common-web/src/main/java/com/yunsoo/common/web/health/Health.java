package com.yunsoo.common.web.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.web.client.RestClient;
import org.joda.time.DateTime;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-07-21
 * Descriptions:
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class Health {

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private Status status;

    @JsonProperty("time")
    private Long time;

    @JsonProperty("details")
    private Map<String, Object> details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }


    public Health() {
        this(Status.UNKNOWN);
    }

    public Health(Status status) {
        this.status = status;
        this.details = new HashMap<>();
    }

    public Health mergeStatus(Status status) {
        this.status = this.status.merge(status);
        return this;
    }

    public Health withDetail(String key, Object data) {
        Assert.notNull(key, "key must not be null");
        Assert.notNull(data, "data must not be null");
        if (this.details == null) {
            this.details = new HashMap<>();
        }
        this.details.put(key, data);
        return this;
    }

    public Health checkClient(RestClient client, List<String> path, boolean debug) {
        if (client == null) {
            return this;
        }
        long startTime = DateTime.now().getMillis();
        Health clientHealth = client.checkHealth(path, debug);
        clientHealth.setTime(DateTime.now().getMillis() - startTime);
        if (debug) {
            clientHealth.withDetail("base_url", client.getBaseUrl());
        }
        String name = StringUtils.isEmpty(clientHealth.getName()) ? client.getClass().getSimpleName() : clientHealth.getName();
        return this
                .mergeStatus(clientHealth.getStatus())
                .withDetail(name, clientHealth);
    }

    public enum Status {
        UP,
        DOWN,
        UNKNOWN;

        public Status merge(Status status) {
            switch (this) {
                case UP:
                    return status;
                case DOWN:
                    return DOWN;
                case UNKNOWN:
                    return UP.equals(status) ? UNKNOWN : status;
                default:
                    return this;
            }
        }
    }

}
