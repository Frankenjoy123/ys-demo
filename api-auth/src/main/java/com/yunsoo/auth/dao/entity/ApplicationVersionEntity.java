package com.yunsoo.auth.dao.entity;

import com.yunsoo.auth.dao.util.IdGenerator;
import com.yunsoo.auth.dto.ApplicationVersion;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by:   Xiaowu
 * Created on:   2016-11-29
 * Descriptions:
 */
@Entity
@Table(name = "application_version")
public class ApplicationVersionEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "version_code")
    private int versionCode;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "comments")
    private String comments;

    @Column(name = "status_code")
    private String statusCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public static ApplicationVersion toApplicationVersion(ApplicationVersionEntity entity){
        if (entity==null){
            return null;
        }
        ApplicationVersion applicationVersion=new ApplicationVersion();
        applicationVersion.setId(entity.getId());
        applicationVersion.setAppId(entity.getAppId());
        applicationVersion.setVersionCode(entity.getVersionCode());
        applicationVersion.setComments(entity.getComments());
        applicationVersion.setCreatedDateTime(entity.getCreatedDateTime());
        applicationVersion.setDownloadUrl(entity.getDownloadUrl());
        applicationVersion.setStatusCode(entity.getStatusCode());
        return applicationVersion;
    }
}
