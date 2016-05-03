package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.GroupPermissionPolicyObject;
import org.joda.time.DateTime;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/25
 * Descriptions:
 */
public class GroupPermissionPolicy {

    @JsonProperty("id")
    private String id;

    @JsonProperty("group_id")
    private String groupId;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("policy_code")
    private String policyCode;

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

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
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

    public GroupPermissionPolicy() {
    }

    public GroupPermissionPolicy(GroupPermissionPolicyObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setGroupId(object.getGroupId());
            this.setOrgId(object.getOrgId());
            this.setPolicyCode(object.getPolicyCode());
            this.setCreatedAccountId(object.getCreatedAccountId());
            this.setCreatedDateTime(object.getCreatedDateTime());
        }
    }
    public GroupPermissionPolicyObject toGroupPermissionPolicyObject() {
        GroupPermissionPolicyObject object = new GroupPermissionPolicyObject();
        object.setId(this.getId());
        object.setGroupId(this.getGroupId());
        object.setOrgId(this.getOrgId());
        object.setPolicyCode(this.getPolicyCode());
        object.setCreatedAccountId(this.getCreatedAccountId());
        object.setCreatedDateTime(this.getCreatedDateTime());
        return object;
    }

}
