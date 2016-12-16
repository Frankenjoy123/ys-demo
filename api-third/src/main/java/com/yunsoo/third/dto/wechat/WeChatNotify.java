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
public class WeChatNotify extends WeChatXmlBaseType {

    @XmlElement(name = "appid")
    private String appId;


    /*
    *
    *
    *
    * */

}
