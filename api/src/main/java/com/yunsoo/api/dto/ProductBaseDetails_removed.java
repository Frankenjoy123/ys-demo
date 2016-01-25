package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by:  haitao
 * Created on:  2015/7/22
 * Descriptions:
 */
public class ProductBaseDetails_removed {

    @JsonProperty("version")
    private String version;

    @JsonProperty("details")
    private List<Item> details;

    @JsonProperty("contact")
    private Contact contact;

    @JsonProperty("e_commerce")
    private List<ECommerce> eCommerce;

    @JsonProperty("t_commerce")
    private List<TCommerce> tCommerce;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Item> getDetails() {
        return details;
    }

    public void setDetails(List<Item> details) {
        this.details = details;
    }

    public static class Item {

        @JsonProperty("name")
        private String name;

        @JsonProperty("value")
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Contact {

        @JsonProperty("hotline")
        private String hotline;

        @JsonProperty("support")
        private String support;

        public String getHotline() {
            return hotline;
        }

        public void setHotline(String hotline) {
            this.hotline = hotline;
        }

        public String getSupport() {
            return support;
        }

        public void setSupport(String support) {
            this.support = support;
        }
    }

    public static class ECommerce {

        @JsonProperty("title")
        private String title;

        @JsonProperty("url")
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class TCommerce {

        @JsonProperty("address")
        private String address;

        @JsonProperty("tel")
        private String tel;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }

    public ProductBaseDetails_removed() {
    }
}
