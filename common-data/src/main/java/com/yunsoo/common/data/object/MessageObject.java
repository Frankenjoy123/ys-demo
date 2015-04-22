package com.yunsoo.common.data.object;

/**
 * Created by Zhe on 2015/3/9.
 */
public class MessageObject {
    private long Id;
    private String title;
    private String body;
    private String digest;
    private String orgId;
    private String createdDateTime;
    private String createdBy; //associate to company's accountId
    private String expiredDateTime;
    private String link;
    private String type;
    private String status;
    private String lastUpatedDateTime;
    private Integer lastUpdatedBy; //associate to company's accountId
    private String postShowTime;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
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

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getExpiredDateTime() {
        return expiredDateTime;
    }

    public void setExpiredDateTime(String expiredDateTime) {
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

    public String getLastUpatedDateTime() {
        return lastUpatedDateTime;
    }

    public void setLastUpatedDateTime(String lastUpatedDateTime) {
        this.lastUpatedDateTime = lastUpatedDateTime;
    }

    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getPostShowTime() {
        return postShowTime;
    }

    public void setPostShowTime(String postShowTime) {
        this.postShowTime = postShowTime;
    }
}
