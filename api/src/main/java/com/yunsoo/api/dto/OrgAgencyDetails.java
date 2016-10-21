package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 10/21/2016.
 */
public class OrgAgencyDetails {

    @JsonProperty("oauth_name")
    private String oauthName;

    @JsonProperty("oauth_gravatar_url")
    private String oauthGravatarUrl;

    @JsonProperty("children_count")
    private int childrenCount;

    @JsonProperty("authorized_children_count")
    private int authorizedChildrenCount;

    public String getOauthName() {
        return oauthName;
    }

    public void setOauthName(String oauthName) {
        this.oauthName = oauthName;
    }

    public String getOauthGravatarUrl() {
        return oauthGravatarUrl;
    }

    public void setOauthGravatarUrl(String oauthGravatarUrl) {
        this.oauthGravatarUrl = oauthGravatarUrl;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    public int getAuthorizedChildrenCount() {
        return authorizedChildrenCount;
    }

    public void setAuthorizedChildrenCount(int authorizedChildrenCount) {
        this.authorizedChildrenCount = authorizedChildrenCount;
    }
}
