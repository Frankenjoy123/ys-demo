package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.LocationObject;

/**
 * Created by:  haitao
 * Created on:  2015/9/6
 * Descriptions:
 */
public class Location {

    @JsonProperty("id")
    private String id;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("description")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location() {
    }

    public Location(LocationObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setLatitude(object.getLatitude());
            this.setLongitude(object.getLongitude());
            this.setName(object.getName());
            this.setTypeCode(object.getTypeCode());
            this.setParentId(object.getParentId());
            this.setDescription(object.getDescription());
        }
    }

    public LocationObject toLocationObject() {
        LocationObject object = new LocationObject();
        object.setId(this.getId());
        object.setLatitude(this.getLatitude());
        object.setLongitude(this.getLongitude());
        object.setName(this.getName());
        object.setTypeCode(this.getTypeCode());
        object.setParentId(this.getParentId());
        object.setDescription(this.getDescription());
        return object;
    }
}
