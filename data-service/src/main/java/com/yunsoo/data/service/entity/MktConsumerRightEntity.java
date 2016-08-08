package com.yunsoo.data.service.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by  : Haitao
 * Created on  : 2016/6/21
 * Descriptions:
 */
@Entity
@Table(name = "mkt_consumer_right")
public class MktConsumerRightEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.yunsoo.data.service.util.IdGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "comments")
    private String comments;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "cmcc_flow_id")
    private Integer cmccFlowId;

    @Column(name = "cucc_flow_id")
    private Integer cuccFlowId;

    @Column(name = "ctcc_flow_id")
    private Integer ctccFlowId;

    @Column(name = "store_url")
    private String storeUrl;

    @Column(name = "created_account_id")
    private String createdAccountId;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "modified_account_id")
    private String modifiedAccountId;

    @Column(name = "modified_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modifiedDateTime;

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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getCmccFlowId() {
        return cmccFlowId;
    }

    public void setCmccFlowId(Integer cmccFlowId) {
        this.cmccFlowId = cmccFlowId;
    }

    public Integer getCuccFlowId() {
        return cuccFlowId;
    }

    public void setCuccFlowId(Integer cuccFlowId) {
        this.cuccFlowId = cuccFlowId;
    }

    public Integer getCtccFlowId() {
        return ctccFlowId;
    }

    public void setCtccFlowId(Integer ctccFlowId) {
        this.ctccFlowId = ctccFlowId;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
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
}
