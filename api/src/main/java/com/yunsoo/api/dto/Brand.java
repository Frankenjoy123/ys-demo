package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.BrandObject;
import org.joda.time.DateTime;

/**
 * Created by yan on 3/9/2016.
 */
public class Brand extends Organization {

    @JsonProperty("contact_name")
    private String contactName;

    @JsonProperty("contact_mobile")
    private String contactMobile;

    @JsonProperty("email")
    private String email;

    @JsonProperty("business_license_number")
    private String businessLicenseNumber;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("business_license_start")
    private DateTime businessLicenseStart;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("business_license_end")
    private DateTime businessLicenseEnd;

    @JsonProperty("business_sphere")
    private String businessSphere;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("carrier_id")
    private String carrierId;

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

    public Brand() {
    }

    public Brand(BrandObject object) {
        if (object != null) {
            setId(object.getId());
            setStatusCode(object.getStatusCode());
            setCreatedDateTime(object.getCreatedDateTime());
            setCreatedAccountId(object.getCreatedAccountId());
            setDescription(object.getDescription());
            setName(object.getName());
            setTypeCode(object.getTypeCode());
            setComments(object.getComments());
            setBusinessLicenseEnd(object.getBusinessLicenseEnd());
            setBusinessLicenseStart(object.getBusinessLicenseStart());
            setBusinessLicenseNumber(object.getBusinessLicenseNumber());
            setBusinessSphere(object.getBusinessSphere());
            setCarrierId(object.getCarrierId());
            setContactName(object.getContactName());
            setContactMobile(object.getContactMobile());
            setEmail(object.getEmail());

        }
    }

    public static BrandObject toBrand(Brand brand) {
        if (brand != null) {
            BrandObject object = new BrandObject();
            object.setId(brand.getId());
            object.setStatusCode(brand.getStatusCode());
            object.setCreatedDateTime(brand.getCreatedDateTime());
            object.setCreatedAccountId(brand.getCreatedAccountId());
            object.setDescription(brand.getDescription());
            object.setName(brand.getName());
            object.setTypeCode(brand.getTypeCode());
            object.setComments(brand.getComments());
            object.setBusinessLicenseEnd(brand.getBusinessLicenseEnd());
            object.setBusinessLicenseStart(brand.getBusinessLicenseStart());
            object.setBusinessLicenseNumber(brand.getBusinessLicenseNumber());
            object.setBusinessSphere(brand.getBusinessSphere());
            object.setCarrierId(brand.getCarrierId());
            object.setContactName(brand.getContactName());
            object.setContactMobile(brand.getContactMobile());
            object.setEmail(brand.getEmail());

            return object;
        }

        return null;
    }
}
