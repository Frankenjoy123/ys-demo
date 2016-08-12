package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.api.auth.dto.Account;
import com.yunsoo.common.data.object.BrandApplicationHistoryObject;

/**
 * Created by:   yan
 * Created on:   4/27/2016
 * Descriptions:
 */
public class BrandApplicationHistory extends BrandApplication {

    @JsonProperty("history_id")
    private String historyId;

    @JsonProperty("account")
    private Account account;

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BrandApplicationHistory(BrandApplicationHistoryObject obj) {
        if (obj != null) {
            this.setHistoryId(obj.getHistoryId());
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
            this.setPassword(obj.getPassword());
            this.setInvestigatorAttachment(obj.getInvestigatorAttachment());
            this.setInvestigatorComments(obj.getInvestigatorComments());
            this.setRejectReason(obj.getRejectReason());
            this.setCategoryId(obj.getCategoryId());
        }
    }
}
