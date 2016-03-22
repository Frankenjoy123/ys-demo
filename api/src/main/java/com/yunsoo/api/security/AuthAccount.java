package com.yunsoo.api.security;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-03-21
 * Descriptions:
 */
public class AuthAccount implements Serializable {

    private String id;

    private String orgId;

    private DateTime expires;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public DateTime getExpires() {
        return expires;
    }

    public void setExpires(DateTime expires) {
        this.expires = expires;
    }

}