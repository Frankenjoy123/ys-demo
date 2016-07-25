package com.yunsoo.common.web.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-07-21
 * Descriptions:
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class Health {

    @JsonProperty("status")
    private Status status;

    @JsonProperty("details")
    private Map<String, Object> details;


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
