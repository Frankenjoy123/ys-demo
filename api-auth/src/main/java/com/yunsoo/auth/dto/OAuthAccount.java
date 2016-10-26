package com.yunsoo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.auth.dao.entity.OAuthAccountEntity;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

/**
 * Created by:   Lijian
 * Created on:   2016-10-19
 * Descriptions:
 */
public class OAuthAccount {

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "oauth_type_code")
    private String oAuthTypeCode;

    @JsonProperty(value = "oauth_openid")
    private String oAuthOpenId;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "account_id")
    private String accountId;

    @JsonProperty(value = "token")
    private String token;

    @JsonProperty(value = "disabled")
    private Boolean disabled;

    @JsonProperty(value = "source_type_code")
    private String sourceTypeCode;

    @JsonProperty(value = "source")
    private String source;

    @JsonProperty(value = "gravatar_url")
    private String gravatarUrl;

    @JsonProperty(value = "created_datetime")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getoAuthTypeCode() {
        return oAuthTypeCode;
    }

    public void setoAuthTypeCode(String oAuthTypeCode) {
        this.oAuthTypeCode = oAuthTypeCode;
    }

    public String getoAuthOpenId() {
        return oAuthOpenId;
    }

    public void setoAuthOpenId(String oAuthOpenId) {
        this.oAuthOpenId = oAuthOpenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getSourceTypeCode() {
        return sourceTypeCode;
    }

    public void setSourceTypeCode(String sourceTypeCode) {
        this.sourceTypeCode = sourceTypeCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public OAuthAccount(){}

    public OAuthAccount(OAuthAccountEntity entity){
        BeanUtils.copyProperties(entity, this);
    }
}
