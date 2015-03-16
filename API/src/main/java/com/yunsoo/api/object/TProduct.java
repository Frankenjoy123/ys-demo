package com.yunsoo.api.object;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import org.joda.time.DateTime;

/**
 * Created by Zhe on 2015/2/27.
 */
public class TProduct {

    private String productKey;
    private int productBaseId;
    private int productStatusId;
    private DateTime manufacturingDateTime;
    private DateTime createdDateTime;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public int getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(int productBaseId) {
        this.productBaseId = productBaseId;
    }

    public int getProductStatusId() {
        return productStatusId;
    }

    public void setProductStatusId(int productStatusId) {
        this.productStatusId = productStatusId;
    }

    public DateTime getManufacturingDateTime() {
        return manufacturingDateTime;
    }

    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    public void setManufacturingDateTime(DateTime manufacturingDateTime) {
        this.manufacturingDateTime = manufacturingDateTime;
    }

    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
