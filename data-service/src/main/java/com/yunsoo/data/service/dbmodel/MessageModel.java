package com.yunsoo.data.service.dbmodel;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by Zhe on 2015/1/27.
 */
@Entity
@Table(name = "message")
public class MessageModel {
    //    private static final long serialVersionUID = 8926784294293357769L;
    @javax.persistence.Id
    @GeneratedValue
    @Column(name = "id")
    private long Id;
    @Column(name = "title")
    private String title;
    @Column(name = "body")
    private String body;
    @Column(name = "digest")
    private String digest;
    @Column(name = "orgId")
    private String orgId;
    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;
    @Column(name = "created_by")
    private String createdBy; //associate to company's accountId
    @Column(name = "expired_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime expiredDateTime;
    @Column(name = "link")
    private String link;
    @Column(name = "type")
    private String type; //e.g. WelcomeMessage, PromotionMessage, System...
    @Column(name = "status")
    private String status;
    @Column(name = "last_updated_datetime", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastUpdatedDateTime;
    @Column(name = "last_updated_by", nullable = true)
    private String lastUpdatedBy; //associate to company's accountId
    @Column(name = "post_show_time", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime postShowTime;
//    @OneToMany
//    @JoinTable(name="user_organization")
//    @JoinColumn(name = "user_Id")
//    private Set<UserOrganizationModel> userOrganizationModelSet;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getExpiredDateTime() {
        return expiredDateTime;
    }

    public void setExpiredDateTime(DateTime expiredDateTime) {
        this.expiredDateTime = expiredDateTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getLastUpdatedDateTime() {
//        if(this.lastUpdatedDateTime.isPresent()) {
        return lastUpdatedDateTime;
    }

    public void setLastUpdatedDateTime(DateTime lastUpdatedDateTime) {
        this.lastUpdatedDateTime = lastUpdatedDateTime;
    }

    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public DateTime getPostShowTime() {
        return postShowTime;
    }

    public void setPostShowTime(DateTime postShowTime) {
        this.postShowTime = postShowTime;
    }
}
