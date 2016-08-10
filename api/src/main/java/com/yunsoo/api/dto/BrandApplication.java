package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.BrandApplicationObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-05
 * Descriptions:
 */
public class BrandApplication implements Serializable {

    @JsonProperty("id")
    private String id;

    @NotBlank(message = "brand_name must not be null or empty")
    @JsonProperty("brand_name")
    private String brandName;

    @NotEmpty(message = "brand_desc must not be null or empty")
    @JsonProperty("brand_desc")
    private String brandDesc;

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
    @JsonProperty("business_license_start")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime businessLicenseStart;

    @JsonProperty("business_license_end")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime businessLicenseEnd;

    @NotEmpty(message = "business_sphere must not be null or empty")
    @JsonProperty("business_sphere")
    private String businessSphere;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("carrier_id")
    private String carrierId;

    @JsonProperty("payment_id")
    private String paymentId;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonProperty("created_datetime")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("attachment")
    private String attachment;

    @NotEmpty(message = "identifier must not be null or empty")
    @JsonProperty("identifier")
    private String identifier;

    @NotEmpty(message = "password must not be null or empty")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("password")
    private String password;

    @JsonProperty("investigator_attachment")
    private String investigatorAttachment;

    @JsonProperty("investigator_comments")
    private String investigatorComments;

    @JsonProperty("reject_reason")
    private String rejectReason;

    @JsonProperty("category_id")
    private String categoryId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("attachment_list")
    private List<Attachment> attachmentList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("investigator_attachment_list")
    private List<Attachment> investigatorAttachmentList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("payment")
    private Payment payment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandDesc() {
        return brandDesc;
    }

    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
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

    public List<Attachment> getInvestigatorAttachmentList() {
        return investigatorAttachmentList;
    }

    public void setInvestigatorAttachmentList(List<Attachment> investigatorAttachmentList) {
        this.investigatorAttachmentList = investigatorAttachmentList;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public BrandApplication() {
    }

    public BrandApplication(BrandApplicationObject obj) {
        if (obj != null) {
            this.setId(obj.getId());
            this.setBrandName(obj.getBrandName());
            this.setBrandDesc(obj.getBrandDesc());
            this.setContactName(obj.getContactName());
            this.setContactMobile(obj.getContactMobile());
            this.setEmail(obj.getEmail());
            this.setBusinessLicenseNumber(obj.getBusinessLicenseNumber());
            this.setBusinessLicenseStart(obj.getBusinessLicenseStart());
            this.setBusinessLicenseEnd(obj.getBusinessLicenseEnd());
            this.setBusinessSphere(obj.getBusinessSphere());
            this.setComments(obj.getComments());
            this.setCarrierId(obj.getCarrierId());
            this.setPaymentId(obj.getPaymentId());
            this.setCreatedAccountId(obj.getCreatedAccountId());
            this.setCreatedDateTime(obj.getCreatedDateTime());
            this.setStatusCode(obj.getStatusCode());
            this.setAttachment(obj.getAttachment());
            this.setIdentifier(obj.getIdentifier());
            this.setInvestigatorAttachment(obj.getInvestigatorAttachment());
            this.setInvestigatorComments(obj.getInvestigatorComments());
            this.setRejectReason(obj.getRejectReason());
            this.setCategoryId(obj.getCategoryId());
        }
    }

    public BrandApplicationObject toBrandApplicationObject() {
        BrandApplicationObject obj = new BrandApplicationObject();
        obj.setId(this.getId());
        obj.setBrandName(this.getBrandName());
        obj.setBrandDesc(this.getBrandDesc());
        obj.setContactName(this.getContactName());
        obj.setContactMobile(this.getContactMobile());
        obj.setEmail(this.getEmail());
        obj.setBusinessLicenseNumber(this.getBusinessLicenseNumber());
        obj.setBusinessLicenseStart(this.getBusinessLicenseStart());
        obj.setBusinessLicenseEnd(this.getBusinessLicenseEnd());
        obj.setBusinessSphere(this.getBusinessSphere());
        obj.setComments(this.getComments());
        obj.setCarrierId(this.getCarrierId());
        obj.setPaymentId(this.getPaymentId());
        obj.setCreatedAccountId(this.getCreatedAccountId());
        obj.setCreatedDateTime(this.getCreatedDateTime());
        obj.setStatusCode(this.getStatusCode());
        obj.setAttachment(this.getAttachment());
        obj.setIdentifier(this.getIdentifier());
        obj.setPassword(this.getPassword());
        obj.setInvestigatorAttachment(this.getInvestigatorAttachment());
        obj.setInvestigatorComments(this.getInvestigatorComments());
        obj.setRejectReason(this.getRejectReason());
        obj.setCategoryId(this.getCategoryId());
        return obj;
    }
}
