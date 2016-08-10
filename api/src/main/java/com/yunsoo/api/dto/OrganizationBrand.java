package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.api.auth.dto.Organization;
import com.yunsoo.common.data.object.OrgBrandObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-08
 * Descriptions:
 */
public class OrganizationBrand extends Organization {

    @NotEmpty(message = "contact_name must not be null or empty")
    @JsonProperty("contact_name")
    private String contactName;

    @NotEmpty(message = "contact_mobile must not be null or empty")
    @JsonProperty("contact_mobile")
    private String contactMobile;

    @NotEmpty(message = "email must not be null or empty")
    @JsonProperty("email")
    private String email;

    @NotEmpty(message = "business_license_number must not be null or empty")
    @JsonProperty("business_license_number")
    private String businessLicenseNumber;

    @NotNull(message = "business_license_start must not be null")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("business_license_start")
    private DateTime businessLicenseStart;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("business_license_end")
    private DateTime businessLicenseEnd;

    @NotEmpty(message = "business_sphere must not be null or empty")
    @JsonProperty("business_sphere")
    private String businessSphere;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("carrier_id")
    private String carrierId;

    @JsonProperty("attachment")
    private String attachment;

    @JsonProperty("category_id")
    private String categoryId;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("attachment_list")
    private List<Attachment> attachmentList;


    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusinessLicenseNumber() {
        return businessLicenseNumber;
    }

    public void setBusinessLicenseNumber(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }

    public DateTime getBusinessLicenseStart() {
        return businessLicenseStart;
    }

    public void setBusinessLicenseStart(DateTime businessLicenseStart) {
        this.businessLicenseStart = businessLicenseStart;
    }

    public DateTime getBusinessLicenseEnd() {
        return businessLicenseEnd;
    }

    public void setBusinessLicenseEnd(DateTime businessLicenseEnd) {
        this.businessLicenseEnd = businessLicenseEnd;
    }

    public String getBusinessSphere() {
        return businessSphere;
    }

    public void setBusinessSphere(String businessSphere) {
        this.businessSphere = businessSphere;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public OrganizationBrand() {
    }

    public OrganizationBrand(Organization organization, OrgBrandObject orgBrandObject) {
        if (organization != null) {
            this.setId(organization.getId());
            this.setName(organization.getName());
            this.setTypeCode(organization.getTypeCode());
            this.setStatusCode(organization.getStatusCode());
            this.setDescription(organization.getDescription());
            this.setCreatedAccountId(organization.getCreatedAccountId());
            this.setCreatedDateTime(organization.getCreatedDateTime());
        }
        if (orgBrandObject != null) {
            this.setContactName(orgBrandObject.getContactName());
            this.setContactMobile(orgBrandObject.getContactMobile());
            this.setEmail(orgBrandObject.getEmail());
            this.setBusinessLicenseNumber(orgBrandObject.getBusinessLicenseNumber());
            this.setBusinessLicenseStart(orgBrandObject.getBusinessLicenseStart());
            this.setBusinessLicenseEnd(orgBrandObject.getBusinessLicenseEnd());
            this.setBusinessSphere(orgBrandObject.getBusinessSphere());
            this.setComments(orgBrandObject.getComments());
            this.setCarrierId(orgBrandObject.getCarrierId());
            this.setAttachment(orgBrandObject.getAttachment());
            this.setCategoryId(orgBrandObject.getCategoryId());
        }
    }

    public OrgBrandObject toOrgBrandObject() {
        OrgBrandObject orgBrandObject = new OrgBrandObject();
        orgBrandObject.setOrgId(this.getId());
        orgBrandObject.setOrgName(this.getName());
        orgBrandObject.setContactName(this.getContactName());
        orgBrandObject.setContactMobile(this.getContactMobile());
        orgBrandObject.setEmail(this.getEmail());
        orgBrandObject.setBusinessLicenseNumber(this.getBusinessLicenseNumber());
        orgBrandObject.setBusinessLicenseStart(this.getBusinessLicenseStart());
        orgBrandObject.setBusinessLicenseEnd(this.getBusinessLicenseEnd());
        orgBrandObject.setBusinessSphere(this.getBusinessSphere());
        orgBrandObject.setComments(this.getComments());
        orgBrandObject.setCarrierId(this.getCarrierId());
        orgBrandObject.setAttachment(this.getAttachment());
        orgBrandObject.setCategoryId(this.getCategoryId());
        orgBrandObject.setCreatedDateTime(this.getCreatedDateTime());
        return orgBrandObject;
    }
}
