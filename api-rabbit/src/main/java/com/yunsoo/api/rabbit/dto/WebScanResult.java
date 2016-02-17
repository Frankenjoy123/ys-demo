package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:   Lijian
 * Created on:   2016-02-17
 * Descriptions:
 */
public class WebScanResult {

    @JsonProperty("key")
    private String key;

    @JsonProperty("product")
    private Product product;

    @JsonProperty("organization")
    private Organization organization;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public static class Product {

        @JsonProperty("id")
        private String id;

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

        @JsonProperty("details")
        private String details;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
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

}
