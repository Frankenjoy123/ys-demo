package com.yunsoo.data.service.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;

/**
 * Created by yan on 4/26/2016.
 */
public class BrandApplicationHistoryEntity {

    @javax.persistence.Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.yunsoo.data.service.util.IdGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "brand_id")
    private String brandId;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "brand_desc")
    private String brandDescription;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_mobile")
    private String contactMobile;

    @Column(name = "email")
    private String email;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "business_license_number")
    private String businessLicenseNumber;

    @Column(name = "business_license_start")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime businessLicenseStart;

    @Column(name = "business_license_end")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime businessLicenseEnd;

    @Column(name = "business_sphere")
    private String businessSphere;

    @Column(name = "comments")
    private String comments;

    @Column(name = "created_account_id")
    private String createdAccountId;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "investigator_attachment")
    private String investigatorAttachment;

    @Column(name = "investigator_comments")
    private String investigatorComments;

    @Column(name = "reject_reason")
    private String rejectReason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandDescription() {
        return brandDescription;
    }

    public void setBrandDescription(String brandDescription) {
        this.brandDescription = brandDescription;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
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
}
