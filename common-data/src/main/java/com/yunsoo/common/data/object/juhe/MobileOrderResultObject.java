package com.yunsoo.common.data.object.juhe;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 6/13/2016.
 */
public class MobileOrderResultObject {

    private String reason;

    @JsonProperty("error_code")
    private int errorCode;

    private OrderResultObject result;

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

    public OrderResultObject getResult() {
        return result;
    }

    public void setResult(OrderResultObject result) {
        this.result = result;
    }

    public class OrderResultObject {
        @JsonProperty("cardid")
        private String cardId;

        @JsonProperty("cardnum")
        private String cardNumber;

        @JsonProperty("ordercash")
        private String orderPrice;

        @JsonProperty("sporder_id")
        private String juheOrderId;

        @JsonProperty("uorderid")
        private String orderId;

        @JsonProperty("game_userid")
        private String phone;

        @JsonProperty("game_state")
        private String state;

        @JsonProperty("cardname")
        private String cardName;

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }
    }



}
