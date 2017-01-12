package com.yunsoo.api.third.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by yan on 7/22/2016.
 */
@XmlRootElement(name = "xml")
public class WeChatRedPacket {

    @XmlElement(name="act_name")
    private String actName;

    @XmlElement(name="act_name")
    private String mchBillNo;

    @XmlElement(name="client_ip")
    private String clientIP;

    @XmlElement(name="mch_id")
    private String mchId;

    @XmlElement(name="nonce_str")
    private String nonceString;

    @XmlElement(name="re_openid")
    private String reOpenId;

    @XmlElement(name="remark")
    private String remark;

    @XmlElement(name="send_name")
    private String sendName;

    @XmlElement(name="total_num")
    private int totalNumber;

    @XmlElement(name="total_amount")
    private int  totalAmount;

    @XmlElement(name="wishing")
    private String wishing;

    @XmlElement(name="wxappid")
    private String wxappId;

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getMchBillNo() {
        return mchBillNo;
    }

    public void setMchBillNo(String mchBillNo) {
        this.mchBillNo = mchBillNo;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceString() {
        return nonceString;
    }

    public void setNonceString(String nonceString) {
        this.nonceString = nonceString;
    }

    public String getReOpenId() {
        return reOpenId;
    }

    public void setReOpenId(String reOpenId) {
        this.reOpenId = reOpenId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getWishing() {
        return wishing;
    }

    public void setWishing(String wishing) {
        this.wishing = wishing;
    }

    public String getWxappId() {
        return wxappId;
    }

    public void setWxappId(String wxappId) {
        this.wxappId = wxappId;
    }
}
