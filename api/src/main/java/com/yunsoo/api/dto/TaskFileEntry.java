package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.TaskFileEntryObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import com.yunsoo.common.web.security.util.OrgIdDetectable;
import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2016-06-02
 * Descriptions:
 */
public class TaskFileEntry implements OrgIdDetectable {

    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("package_count")
    private Integer packageCount;

    @JsonProperty("package_size")
    private Integer packageSize;

    @JsonProperty("product_count")
    private Integer productCount;

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public Integer getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(Integer packageCount) {
        this.packageCount = packageCount;
    }

    public Integer getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(Integer packageSize) {
        this.packageSize = packageSize;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public TaskFileEntry() {
    }

    public TaskFileEntry(TaskFileEntryObject obj) {
        if (obj == null) {
            return;
        }
        this.setFileId(obj.getFileId());
        this.setOrgId(obj.getOrgId());
        this.setAppId(obj.getAppId());
        this.setDeviceId(obj.getDeviceId());
        this.setName(obj.getName());
        this.setTypeCode(obj.getTypeCode());
        this.setStatusCode(obj.getStatusCode());
        this.setCreatedAccountId(obj.getCreatedAccountId());
        this.setCreatedDateTime(obj.getCreatedDateTime());
        this.setProductBaseId(obj.getProductBaseId());
        this.setPackageSize(obj.getPackageSize());
        this.setPackageCount(obj.getPackageCount());
        this.setProductCount(obj.getProductCount());

    }
}
