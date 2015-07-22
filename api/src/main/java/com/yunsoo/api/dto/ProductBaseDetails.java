package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by:  haitao
 * Created on:  2015/7/22
 * Descriptions:
 */
public class ProductBaseDetails {
    @JsonProperty("version")
    private String version;

    @JsonProperty("item")
    private List<Item> item;

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

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public static class Item {
        private String name;
        private String value;
    }

    public static class Contact {
        private String hotline;
        private String support;
    }

    public static class ECommerce {
        private String title;
        private String url;
    }

    public static class TCommerce {
        private String name;
        private String address;
    }

    public ProductBaseDetails() {
    }
}
