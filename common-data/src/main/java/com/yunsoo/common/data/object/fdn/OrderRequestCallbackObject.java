package com.yunsoo.common.data.object.fdn;

/**
 * Created by yan on 8/15/2016.
 */
public class OrderRequestCallbackObject {

    private String orderId;
    private String resultCode;
    private String resultMsg;
    private String cpOrderNo;
    private String transNo;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getCpOrderNo() {
        return cpOrderNo;
    }

    public void setCpOrderNo(String cpOrderNo) {
        this.cpOrderNo = cpOrderNo;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }
}
