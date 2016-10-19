package com.yunsoo.auth.api.security;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-03-21
 * Descriptions:
 */
public class AuthAccount implements Serializable {

    private String id;

    private String orgId;

    private String oAuthAccountId;

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

    public String getOAuthAccountId() {
        return oAuthAccountId;
    }

    public void setOAuthAccountId(String oAuthAccountId) {
        this.oAuthAccountId = oAuthAccountId;
    }

}