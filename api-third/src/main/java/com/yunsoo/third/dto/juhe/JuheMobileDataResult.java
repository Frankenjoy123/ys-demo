package com.yunsoo.third.dto.juhe;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 6/13/2016.
 */
public class JuheMobileDataResult {
    private String reason;

    @JsonProperty("error_code")
    private int errorCode;

    private DataResultObject result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public DataResultObject getResult() {
        return result;
    }

    public void setResult(DataResultObject result) {
        this.result = result;
    }

    public class DataResultObject {

        @JsonProperty("cardname")
        private String cardName;

        @JsonProperty("ordercash")
        private String orderPrice;

        @JsonProperty("sporder_id")
        private String juheOrderId;

        @JsonProperty("orderid")
        private String orderId;

        @JsonProperty("phone")
        private String phone;

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public String getOrderPrice() {
            return orderPrice;
        }

        public void setOrderPrice(String orderPrice) {
            this.orderPrice = orderPrice;
        }

        public String getJuheOrderId() {
            return juheOrderId;
        }

        public void setJuheOrderId(String juheOrderId) {
            this.juheOrderId = juheOrderId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

}
