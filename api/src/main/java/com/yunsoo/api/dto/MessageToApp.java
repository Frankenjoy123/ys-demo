package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.api.dto.detectable.OrgIdDetectable;

/**
 * Created by admin on 2015/8/14.
 */
public class MessageToApp implements OrgIdDetectable {

    @JsonProperty("message_id")
    private String messageId;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("org_name")
    private String orgName;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MessageToApp() {

    }

}
