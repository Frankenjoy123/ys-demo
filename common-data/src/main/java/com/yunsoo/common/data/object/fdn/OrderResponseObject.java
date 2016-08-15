package com.yunsoo.common.data.object.fdn;

/**
 * Created by yan on 8/15/2016.
 */
public class OrderResponseObject {

    private int responseCode;

    private String responseMsg;

    private OrderResponseData responseData;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public OrderResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(OrderResponseData responseData) {
        this.responseData = responseData;
    }

    public class OrderResponseData{
        private String orderId;
        private String transNo;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getTransNo() {
            return transNo;
        }

        public void setTransNo(String transNo) {
            this.transNo = transNo;
        }
    }

}
