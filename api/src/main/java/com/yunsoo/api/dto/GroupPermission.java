package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.GroupPermissionObject;
import org.joda.time.DateTime;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/25
 * Descriptions:
 */
public class GroupPermission {

    @JsonProperty("id")
    private String id;

    @JsonProperty("group_id")
    private String groupId;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("resource_code")
    private String resourceCode;

    @JsonProperty("action_code")
    private String actionCode;

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
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



    public GroupPermission() {
    }

    public GroupPermission(GroupPermissionObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setGroupId(object.getGroupId());
            this.setOrgId(object.getOrgId());
            this.setResourceCode(object.getResourceCode());
            this.setActionCode(object.getActionCode());
            this.setCreatedAccountId(object.getCreatedAccountId());
            this.setCreatedDateTime(object.getCreatedDatetime());
        }
    }

    public GroupPermissionObject toGroupPermissionObject() {
        GroupPermissionObject object = new GroupPermissionObject();
        object.setId(this.getId());
        object.setGroupId(this.getGroupId());
        object.setOrgId(this.getOrgId());
        object.setResourceCode(this.getResourceCode());
        object.setActionCode(this.getActionCode());
        object.setCreatedAccountId(this.getCreatedAccountId());
        object.setCreatedDatetime(this.getCreatedDateTime());
        return object;
    }
}
