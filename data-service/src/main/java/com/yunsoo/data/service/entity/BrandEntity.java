package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by yan on 3/9/2016.
 */
@Entity
@Table(name = "org_brand")
public class BrandEntity {

    @Id
    @Column(name = "org_id")
    private String orgId;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_mobile")
    private String contactMobile;

    @Column(name = "email")
    private String email;

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

    @Column(name = "carrier_id")
    private String carrierId;

    @OneToOne(targetEntity = OrganizationEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", insertable = false, updatable = false)
    private OrganizationEntity organization;

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "category_id")
    private String categoryId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }
}
