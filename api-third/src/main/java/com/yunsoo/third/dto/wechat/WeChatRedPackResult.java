package com.yunsoo.third.dto.wechat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by yan on 12/29/2016.
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.NONE)
public class WeChatRedPackResult extends WeChatXmlBaseType {
    @XmlElement(name = "mch_id")
    private String mchId;

    @XmlElement(name = "mch_billno")
    private String mchBillNo;

    @XmlElement(name = "wxappid")
    private String appId;

    @XmlElement(name = "re_openid")
    private String openId;

    @XmlElement(name = "total_amount")
    private int amout;   //单位是分

    @XmlElement(name = "send_listid")
    private String weChatOrderId;

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchBillNo() {
        return mchBillNo;
    }

    public void setMchBillNo(String mchBillNo) {
        this.mchBillNo = mchBillNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getAmout() {
        return amout;
    }

    public void setAmout(int amout) {
        this.amout = amout;
    }

    public String getWeChatOrderId() {
        return weChatOrderId;
    }

    public void setWeChatOrderId(String weChatOrderId) {
        this.weChatOrderId = weChatOrderId;
    }
}
