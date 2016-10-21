package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.OrgAgencyObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import com.yunsoo.common.web.security.util.OrgIdDetectable;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:  haitao
 * Created on:  2015/9/2
 * Descriptions:
 */
public class OrgAgency implements OrgIdDetectable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("location_ids")
    private List<String> locationIds;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("agency_responsible")
    private String agencyResponsible;

    @JsonProperty("agency_code")
    private String agencyCode;

    @JsonProperty("agency_phone")
    private String agencyPhone;

    @JsonProperty("address")
    private String address;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("modified_account_id")
    private String modifiedAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("modified_datetime")
    private DateTime modifiedDateTime;

    @JsonProperty("authorized")
    private Boolean authorized;

    @JsonProperty("details")
    private OrgAgencyDetails details;

    public OrgAgencyDetails getDetails() {
        return details;
    }

    public void setDetails(OrgAgencyDetails details) {
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<String> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(List<String> locationIds) {
        this.locationIds = locationIds;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getModifiedAccountId() {
        return modifiedAccountId;
    }

    public void setModifiedAccountId(String modifiedAccountId) {
        this.modifiedAccountId = modifiedAccountId;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public String getAgencyResponsible() {
        return agencyResponsible;
    }

    public void setAgencyResponsible(String agencyResponsible) {
        this.agencyResponsible = agencyResponsible;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getAgencyPhone() {
        return agencyPhone;
    }

    public void setAgencyPhone(String agencyPhone) {
        this.agencyPhone = agencyPhone;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    public OrgAgency() {
    }

    public OrgAgency(OrgAgencyObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setName(object.getName());
            this.setOrgId(object.getOrgId());
            this.setLocationIds(object.getLocationIds());
            this.setParentId(object.getParentId());
            this.setAddress(object.getAddress());
            this.setDescription(object.getDescription());
            this.setStatusCode(object.getStatusCode());
            this.setCreatedAccountId(object.getCreatedAccountId());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setModifiedAccountId(object.getModifiedAccountId());
            this.setModifiedDateTime(object.getModifiedDateTime());
            this.setAgencyResponsible(object.getAgencyResponsible());
            this.setAgencyCode(object.getAgencyCode());
            this.setAgencyPhone(object.getAgencyPhone());
        }
    }

    public OrgAgencyObject toOrgAgencyObject() {
        OrgAgencyObject object = new OrgAgencyObject();
        object.setId(this.getId());
        object.setName(this.getName());
        object.setOrgId(this.getOrgId());
        object.setLocationIds(this.getLocationIds());
        object.setParentId(this.getParentId());
        object.setAddress(this.getAddress());
        object.setDescription(this.getDescription());
        object.setStatusCode(this.getStatusCode());
        object.setCreatedAccountId(this.getCreatedAccountId());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setModifiedAccountId(this.getModifiedAccountId());
        object.setModifiedDateTime(this.getModifiedDateTime());
        object.setAgencyResponsible(this.getAgencyResponsible());
        object.setAgencyCode(this.getAgencyCode());
        object.setAgencyPhone(this.getAgencyPhone());
        return object;
    }


}
