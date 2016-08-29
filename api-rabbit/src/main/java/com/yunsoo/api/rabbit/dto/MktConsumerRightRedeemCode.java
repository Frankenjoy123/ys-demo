package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.MktConsumerRightRedeemCodeObject;

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

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("value")
    private String value;

    @JsonProperty("draw_prize_id")
    private String drawPrizeId;

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

    public MktConsumerRightRedeemCode() {
    }

    public MktConsumerRightRedeemCode(MktConsumerRightRedeemCodeObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setConsumerRightId(object.getConsumerRightId());
            this.setTypeCode(object.getTypeCode());
            this.setStatusCode(object.getStatusCode());
            this.setValue(object.getValue());
            this.setDrawPrizeId(object.getDrawPrizeId());
        }
    }

    public MktConsumerRightRedeemCodeObject toMktConsumerRightRedeemCodeObject() {
        MktConsumerRightRedeemCodeObject object = new MktConsumerRightRedeemCodeObject();
        object.setId(this.getId());
        object.setConsumerRightId(this.getConsumerRightId());
        object.setTypeCode(this.getTypeCode());
        object.setStatusCode(this.getStatusCode());
        object.setValue(this.getValue());
        object.setDrawPrizeId(this.getDrawPrizeId());
        return object;
    }

}
