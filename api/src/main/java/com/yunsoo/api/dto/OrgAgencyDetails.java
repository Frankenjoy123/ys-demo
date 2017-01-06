package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 10/21/2016.
 */
public class OrgAgencyDetails {

    @JsonProperty("agency_id")
    private String agencyId;

    @JsonProperty("oauth_name")
    private String oauthName;

    @JsonProperty("oauth_gravatar_url")
    private String oauthGravatarUrl;

    @JsonProperty("children_count")
    private int childrenCount;

    @JsonProperty("authorized_children_count")
    private int authorizedChildrenCount;

    @JsonProperty("parent_name")
    private String parentName;

    @JsonProperty("org_name")
    private String orgName;

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

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
