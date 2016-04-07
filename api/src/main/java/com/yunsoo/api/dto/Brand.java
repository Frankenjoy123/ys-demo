package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.BrandObject;
import org.joda.time.DateTime;

import java.util.List;

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

    @JsonProperty("payment_id")
    private String paymentId;

    @JsonProperty("attachment")
    private String attachment;

    @JsonProperty("password")
    private String password;

    @JsonProperty("hash_salt")
    private String hashSalt;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("investigator_attachment")
    private String investigatorAttachment;

    @JsonProperty("investigator_comments")
    private String investigatorComments;

    private List<Attachment> attachmentList;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashSalt() {
        return hashSalt;
    }

    public void setHashSalt(String hashSalt) {
        this.hashSalt = hashSalt;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getInvestigatorAttachment() {
        return investigatorAttachment;
    }

    public void setInvestigatorAttachment(String investigatorAttachment) {
        this.investigatorAttachment = investigatorAttachment;
    }

    public String getInvestigatorComments() {
        return investigatorComments;
    }

    public void setInvestigatorComments(String investigatorComments) {
        this.investigatorComments = investigatorComments;
    }

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

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

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
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
            setPaymentId(object.getPaymentId());
            setContactName(object.getContactName());
            setContactMobile(object.getContactMobile());
            setEmail(object.getEmail());
            setAttachment(object.getAttachment());
            setIdentifier(object.getIdentifier());
            setPassword("******");
            setInvestigatorAttachment(object.getInvestigatorAttachment());
            setInvestigatorComments(object.getInvestigatorComments());
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
            object.setPaymentId(brand.getPaymentId());
            object.setContactName(brand.getContactName());
            object.setContactMobile(brand.getContactMobile());
            object.setEmail(brand.getEmail());
            object.setAttachment(brand.getAttachment());
            object.setIdentifier(brand.getIdentifier());
            object.setPassword(brand.getPassword());
            object.setInvestigatorAttachment(brand.getInvestigatorAttachment());
            object.setInvestigatorComments(brand.getInvestigatorComments());
            return object;
        }

        return null;
    }
}
