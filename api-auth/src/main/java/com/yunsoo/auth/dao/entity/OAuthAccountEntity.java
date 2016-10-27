package com.yunsoo.auth.dao.entity;

import com.yunsoo.auth.dao.util.IdGenerator;
import com.yunsoo.auth.dto.OAuthAccount;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

/**
 * Created by yan on 10/19/2016.
 */
@Entity
@Table(name = "oauth_account")
public class OAuthAccountEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = IdGenerator.CLASS)
    @Column(name = "id")
    private String id;

    @Column(name = "oauth_type_code")
    private String oAuthTypeCode;

    @Column(name = "oauth_openid")
    private String oAuthOpenId;

    @Column(name = "name")
    private String name;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "token")
    private String token;

    @Column(name = "disabled")
    private Boolean disabled;

    @Column(name = "source_type_code")
    private String sourceTypeCode;

    @Column(name = "source")
    private String source;

    @Column(name = "gravatar_url")
    private String gravatarUrl;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getoAuthTypeCode() {
        return oAuthTypeCode;
    }

    public void setoAuthTypeCode(String oAuthTypeCode) {
        this.oAuthTypeCode = oAuthTypeCode;
    }

    public String getoAuthOpenId() {
        return oAuthOpenId;
    }

    public void setoAuthOpenId(String oAuthOpenId) {
        this.oAuthOpenId = oAuthOpenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getSourceTypeCode() {
        return sourceTypeCode;
    }

    public void setSourceTypeCode(String sourceTypeCode) {
        this.sourceTypeCode = sourceTypeCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public OAuthAccountEntity(){}

    public OAuthAccountEntity(OAuthAccount account){
        BeanUtils.copyProperties(account, this);
    }
}
