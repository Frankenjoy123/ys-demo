package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.UserEventObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by Admin on 6/27/2016.
 */
public class UserEvent {

    @JsonProperty("id")
    private String id;

    @JsonProperty("scan_record_id")
    private String scanRecordId;

    @JsonProperty("product_key")
    private String productKey;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("value")
    private String value;

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

    public String getScanRecordId() {
        return scanRecordId;
    }

    public void setScanRecordId(String scanRecordId) {
        this.scanRecordId = scanRecordId;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public UserEventObject toUserEventObject() {

        UserEventObject object = new UserEventObject();

        object.setId(this.getId());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setTypeCode(this.getTypeCode());
        object.setScanRecordId(this.getScanRecordId());
        object.setProductKey(this.getProductKey());
        object.setProductBaseId(this.getProductBaseId());
        object.setValue(this.getValue());

        return object;
    }

    public UserEvent() {
    }

    public UserEvent(UserEventObject userEventObject) {
        if (userEventObject != null) {
            this.setId(userEventObject.getId());
            this.setCreatedDateTime(userEventObject.getCreatedDateTime());
            this.setTypeCode(userEventObject.getTypeCode());
            this.setScanRecordId(userEventObject.getScanRecordId());
            this.setProductKey(userEventObject.getProductKey());
            this.setProductBaseId(userEventObject.getProductBaseId());
            this.setValue(userEventObject.getValue());
        }
    }
}
