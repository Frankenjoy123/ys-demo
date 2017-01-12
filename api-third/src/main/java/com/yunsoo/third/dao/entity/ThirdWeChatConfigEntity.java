package com.yunsoo.third.dao.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Blob;

/**
 * Created by yan on 12/27/2016.
 */
@Entity
@Table(name = "third_wechat_config")
public class ThirdWeChatConfigEntity {

    @Id
    @Column(name = "org_id")
    private String orgId;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_secret")
    private String appSecret;

    @Column(name = "private_key")
    private String privateKey;

    @Column(name = "mch_id")
    private String mchId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ssl_key")
    private byte[] sslKey;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public byte[] getSslKey() {
        return sslKey;
    }

    public void setSslKey(byte[] sslKey) {
        this.sslKey = sslKey;
    }
}
