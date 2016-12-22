package com.yunsoo.third.dto.wechat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by yan on 12/16/2016.
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.NONE)
public class WeChatPayResult extends WeChatXmlBaseType {

    @XmlElement(name = "transaction_id")
    private String weChatOrderId;

    @XmlElement(name = "attach")
    private String orderType;

    @XmlElement(name="out_trade_no")
    private String orderId;

    @XmlElement(name="openid")
    private String openId;

    @XmlElement(name="trade_state_desc")
    private String nextStepDesc;

    public String getWeChatOrderId() {
        return weChatOrderId;
    }

    public void setWeChatOrderId(String weChatOrderId) {
        this.weChatOrderId = weChatOrderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNextStepDesc() {
        return nextStepDesc;
    }

    public void setNextStepDesc(String nextStepDesc) {
        this.nextStepDesc = nextStepDesc;
    }
}
