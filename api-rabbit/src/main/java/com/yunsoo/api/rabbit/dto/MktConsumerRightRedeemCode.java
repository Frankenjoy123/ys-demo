package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.MktConsumerRightRedeemCodeObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by:  haitao
 * Created on:  2016/8/19
 * Descriptions:
 */
public class MktConsumerRightRedeemCode {
    @JsonProperty("id")
    private String id;

    @JsonProperty("consumer_right_id")
    private String consumerRightId;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("value")
    private String value;

    @JsonProperty("draw_prize_id")
    private String drawPrizeId;

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

    public String getConsumerRightId() {
        return consumerRightId;
    }

    public void setConsumerRightId(String consumerRightId) {
        this.consumerRightId = consumerRightId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDrawPrizeId() {
        return drawPrizeId;
    }

    public void setDrawPrizeId(String drawPrizeId) {
        this.drawPrizeId = drawPrizeId;
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

    public MktConsumerRightRedeemCode() {
    }

    public MktConsumerRightRedeemCode(MktConsumerRightRedeemCodeObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setConsumerRightId(object.getConsumerRightId());
            this.setOrgId(object.getOrgId());
            this.setTypeCode(object.getTypeCode());
            this.setStatusCode(object.getStatusCode());
            this.setValue(object.getValue());
            this.setDrawPrizeId(object.getDrawPrizeId());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setModifiedDateTime(object.getModifiedDateTime());
        }
    }

    public MktConsumerRightRedeemCodeObject toMktConsumerRightRedeemCodeObject() {
        MktConsumerRightRedeemCodeObject object = new MktConsumerRightRedeemCodeObject();
        object.setId(this.getId());
        object.setConsumerRightId(this.getConsumerRightId());
        object.setOrgId(this.getOrgId());
        object.setTypeCode(this.getTypeCode());
        object.setStatusCode(this.getStatusCode());
        object.setValue(this.getValue());
        object.setDrawPrizeId(this.getDrawPrizeId());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setModifiedDateTime(this.getModifiedDateTime());
        return object;
    }

}
