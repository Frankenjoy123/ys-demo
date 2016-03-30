package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-02-17
 * Descriptions:
 */
public class WebScanResponse {

    //产品基本信息
    @JsonProperty("product")
    private Product product;

    //生成企业信息
    @JsonProperty("organization")
    private Organization organization;

    //防伪
    @JsonProperty("security")
    private Security security;

    @JsonProperty("marketing")
    private Marketing marketing;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }

    public static class Product {

        @JsonProperty("key")
        private String key;

        @JsonProperty("id")
        private String id;

        @JsonProperty("batch_id")
        private String batchId;

        @JsonProperty("batch_no")
        private String batchNo;

        @JsonProperty("category")
        private ProductCategory category;

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("shelf_life")
        private Integer shelfLife;

        @JsonProperty("shelf_life_interval")
        private String shelfLifeInterval;

        @JsonProperty("status_code")
        private String statusCode;

        @JsonSerialize(using = DateTimeJsonSerializer.class)
        @JsonDeserialize(using = DateTimeJsonDeserializer.class)
        @JsonProperty("manufacturing_datetime")
        private DateTime manufacturingDatetime;

        @JsonProperty("details")
        private String details;

        @JsonProperty("batch_details")
        private String batchDetails;

        @JsonProperty("key_details")
        private String keyDetails;


        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        public ProductCategory getCategory() {
            return category;
        }

        public void setCategory(ProductCategory category) {
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getShelfLife() {
            return shelfLife;
        }

        public void setShelfLife(Integer shelfLife) {
            this.shelfLife = shelfLife;
        }

        public String getShelfLifeInterval() {
            return shelfLifeInterval;
        }

        public void setShelfLifeInterval(String shelfLifeInterval) {
            this.shelfLifeInterval = shelfLifeInterval;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public DateTime getManufacturingDatetime() {
            return manufacturingDatetime;
        }

        public void setManufacturingDatetime(DateTime manufacturingDatetime) {
            this.manufacturingDatetime = manufacturingDatetime;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getBatchDetails() {
            return batchDetails;
        }

        public void setBatchDetails(String batchDetails) {
            this.batchDetails = batchDetails;
        }

        public String getKeyDetails() {
            return keyDetails;
        }

        public void setKeyDetails(String keyDetails) {
            this.keyDetails = keyDetails;
        }
    }

    public static class Organization {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("status_code")
        private String statusCode;

        @JsonProperty("description")
        private String description;

        @JsonProperty("details")
        private String details;


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

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }
    }

    public static class Security {

        @JsonProperty("scan_count")
        private int scanCount;

        @JsonProperty("first_scan")
        private ScanRecord firstScan;

        @JsonProperty("scan_records")
        private List<ScanRecord> scanRecords;

        public int getScanCount() {
            return scanCount;
        }

        public void setScanCount(int scanCount) {
            this.scanCount = scanCount;
        }

        public ScanRecord getFirstScan() {
            return firstScan;
        }

        public void setFirstScan(ScanRecord firstScan) {
            this.firstScan = firstScan;
        }

        public List<ScanRecord> getScanRecords() {
            return scanRecords;
        }

        public void setScanRecords(List<ScanRecord> scanRecords) {
            this.scanRecords = scanRecords;
        }
    }

    public static class ScanRecord {

        @JsonProperty("id")
        private String id;

        @JsonProperty("user_id")
        private String userId;

        @JsonProperty("product_key")
        private String productKey;

        @JsonProperty("product_base_id")
        private String productBaseId;

        @JsonProperty("ysid")
        private String ysid;

        @JsonProperty("longitude")
        private Double longitude;

        @JsonProperty("latitude")
        private Double latitude;

        @JsonProperty("province")
        private String province;

        @JsonProperty("city")
        private String city;

        @JsonProperty("address")
        private String address;

        @JsonProperty("details")
        private String details;

        @JsonSerialize(using = DateTimeJsonSerializer.class)
        @JsonDeserialize(using = DateTimeJsonDeserializer.class)
        @JsonProperty("created_datetime")
        private DateTime createdDateTime;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProductKey() {
            return productKey;
        }

        public void setProductKey(String productKey) {
            this.productKey = productKey;
        }

        public String getProductBaseId() {
            return productBaseId;
        }

        public void setProductBaseId(String productBaseId) {
            this.productBaseId = productBaseId;
        }

        public String getYsid() {
            return ysid;
        }

        public void setYsid(String ysid) {
            this.ysid = ysid;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public DateTime getCreatedDateTime() {
            return createdDateTime;
        }

        public void setCreatedDateTime(DateTime createdDateTime) {
            this.createdDateTime = createdDateTime;
        }
    }

    public static class Marketing {

        @JsonProperty("id")
        private String id;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
