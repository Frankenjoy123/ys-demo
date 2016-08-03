package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.MktPrizeContactObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by:  haitao
 * Created on:  2016/7/27
 * Descriptions:
 */
public class MktPrizeContact {
    @JsonProperty("id")
    private String id;

    @JsonProperty("mkt_prize_id")
    private String mktPrizeId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("province")
    private String province;

    @JsonProperty("city")
    private String city;

    @JsonProperty("district")
    private String district;

    @JsonProperty("address")
    private String address;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("modified_datetime")
    private DateTime modifiedDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMktPrizeId() {
        return mktPrizeId;
    }

    public void setMktPrizeId(String mktPrizeId) {
        this.mktPrizeId = mktPrizeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public MktPrizeContact() {
    }

    public MktPrizeContact(MktPrizeContactObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setMktPrizeId(object.getMktPrizeId());
            this.setName(object.getName());
            this.setPhone(object.getPhone());
            this.setProvince(object.getProvince());
            this.setCity(object.getCity());
            this.setDistrict(object.getDistrict());
            this.setAddress(object.getAddress());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setModifiedDateTime(object.getModifiedDateTime());
        }
    }

    public MktPrizeContactObject toMktPrizeContactObject() {
        MktPrizeContactObject object = new MktPrizeContactObject();
        object.setId(this.getId());
        object.setMktPrizeId(this.getMktPrizeId());
        object.setName(this.getName());
        object.setPhone(this.getPhone());
        object.setProvince(this.getProvince());
        object.setCity(this.getCity());
        object.setDistrict(this.getDistrict());
        object.setAddress(this.getAddress());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setModifiedDateTime(this.getModifiedDateTime());
        return object;
    }

}
