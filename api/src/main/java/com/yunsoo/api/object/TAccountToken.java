package com.yunsoo.api.object;

/**
 * Created by KB on 2015/2/27.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import javax.print.DocFlavor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TAccountToken {
    private Long id;
    private Integer status;
    private TAccount account;
    private Long deviceId;
    private String accessToken;


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public TAccount getAccount() { return account; }
    public void setAccount(TAccount account) { this.account = account; }

    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long device) { this.deviceId = deviceId; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

}
