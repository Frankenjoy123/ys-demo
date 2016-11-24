package com.yunsoo.third.dao.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by  : Haitao
 * Created on  : 2016/7/6
 * Descriptions:
 */
@Entity
@Table(name = "third_mobile_verification_code")
public class ThirdMobileVerificationCodeEntity {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.yunsoo.third.dao.util.IdGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "used_flag")
    private Boolean usedFlag;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "sent_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime sentDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Boolean getUsedFlag() {
        return usedFlag;
    }

    public void setUsedFlag(Boolean usedFlag) {
        this.usedFlag = usedFlag;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public DateTime getSentDateTime() {
        return sentDateTime;
    }

    public void setSentDateTime(DateTime sentDateTime) {
        this.sentDateTime = sentDateTime;
    }
}
