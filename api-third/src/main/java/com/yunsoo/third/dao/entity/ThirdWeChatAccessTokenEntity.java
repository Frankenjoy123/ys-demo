package com.yunsoo.third.dao.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by Admin on 6/28/2016.
 */
@Entity
@Table(name = "third_wechat_access_token")
public class ThirdWeChatAccessTokenEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.yunsoo.third.dao.util.IdGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "jsapi_ticket")
    private String jsapiTicket;

    @Column(name = "updated_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime updatedDateTime;

    @Column(name = "expired_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime expiredDatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getJsapiTicket() {
        return jsapiTicket;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public DateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(DateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public DateTime getExpiredDatetime() {
        return expiredDatetime;
    }

    public void setExpiredDatetime(DateTime expiredDatetime) {
        this.expiredDatetime = expiredDatetime;
    }
}
