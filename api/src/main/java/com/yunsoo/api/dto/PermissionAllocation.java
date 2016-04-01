package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    private String effect;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;


}
