package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.PermissionAllocationObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2016-03-31
 * Descriptions:
 */
public class PermissionAllocation {

    @JsonProperty("id")
    private String id;

    @JsonProperty("principal")
    private String principal;

    @JsonProperty("restriction")
    private String restriction;

    @JsonProperty("permission")
    private String permission;

    @JsonProperty("effect")
    private Effect effect;

    @JsonProperty("created_account_id")
    private String createdAccountId;

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

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
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

    public PermissionAllocation() {
    }

    public PermissionAllocation(PermissionAllocationObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setPrincipal(object.getPrincipal());
            this.setRestriction(object.getRestriction());
            this.setPermission(object.getPermission());
            this.setEffect(Effect.valueOf(object.getEffect().name()));
            this.setCreatedAccountId(object.getCreatedAccountId());
            this.setCreatedDateTime(object.getCreatedDateTime());
        }
    }

    public enum Effect {
        allow,
        deny;
    }
}
