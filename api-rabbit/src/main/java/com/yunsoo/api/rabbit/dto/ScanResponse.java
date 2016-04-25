package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2016-04-25
 * Descriptions:
 */
public class ScanResponse {

    @JsonProperty("product")
    private Product product;

    @JsonProperty("organization")
    private Organization organization;

    @JsonProperty("scan_record")
    private ScanRecord scanRecord;

    @JsonProperty("security")
    private Security security;

    @JsonProperty("social")
    private Social social;


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

    public ScanRecord getScanRecord() {
        return scanRecord;
    }

    public void setScanRecord(ScanRecord scanRecord) {
        this.scanRecord = scanRecord;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Social getSocial() {
        return social;
    }

    public void setSocial(Social social) {
        this.social = social;
    }


    public static class Product {

        @JsonProperty("key")
        private String key;

        @JsonProperty("id")
        private String id; //productBaseId

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

        @JsonProperty("device_id")
        private String deviceId;

        @JsonProperty("ip")
        private String ip;

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

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
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

    public static class Security {

        @JsonProperty("scan_count")
        private int scanCount;

        @JsonProperty("first_scan")
        private ScanRecord firstScan;

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
    }

    public static class Social {

        @JsonProperty("org_following")
        private Boolean orgFollowing;

        @JsonProperty("product_following")
        private Boolean productFollowing;

        public Boolean getOrgFollowing() {
            return orgFollowing;
        }

        public void setOrgFollowing(Boolean orgFollowing) {
            this.orgFollowing = orgFollowing;
        }

        public Boolean getProductFollowing() {
            return productFollowing;
        }

        public void setProductFollowing(Boolean productFollowing) {
            this.productFollowing = productFollowing;
        }
    }
}
