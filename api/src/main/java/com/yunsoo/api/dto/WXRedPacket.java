package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by yan on 7/22/2016.
 */
public class WXRedPacket {

    @JsonProperty("act_name")
    @XmlElement
    private String actName;
    @JsonProperty("mch_id")
    private String clientIP;
    @JsonProperty("act_name")
    private String mchBillNo;
    @JsonProperty("act_name")
    private String mchId;
    @JsonProperty("nonce_str")
    private String nonceString;
    @JsonProperty("re_openid")
    private String reOpenId;
    @JsonProperty("remark")
    private String remark;
    @JsonProperty("send_name")
    private String sendName;
    @JsonProperty("total_num")
    private int totalNumber;
    @JsonProperty("total_amount")
    private int  totalAmount;
    @JsonProperty("wishing")
    private String wishing;
    @JsonProperty("wxappid")
    private String wxappId;

/*
    var content = {
            act_name: redpack.actionName,
    client_ip: getCurrentIp(),
    mch_billno: redpack.drawId,
    mch_id: mchId,
    nonce_str: randString.generateKey(30),
    re_openid: redpack.openId,
    remark: redpack.comments,
    send_name: redpack.orgName,
    total_amount: redpack.amount * 100,
    total_num: 1,
    wishing: redpack.wishing,
    wxappid: appId*/

}
