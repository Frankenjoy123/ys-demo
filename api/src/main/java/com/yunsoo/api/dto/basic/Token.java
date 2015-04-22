package com.yunsoo.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/22
 * Descriptions:
 */
public class Token {

    @JsonProperty("token")
    String token;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("expires")
    DateTime expires;

    public Token() {
    }

    public Token(String token, DateTime expires) {
        this.token = token;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DateTime getExpires() {
        return expires;
    }

    public void setExpires(DateTime expires) {
        this.expires = expires;
    }
}
