package com.yunsoo.data.service.service.contract;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.data.service.dbmodel.MessageModel;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/1/26.
 * This is the data contract for Message to end users.
 */
@JsonAutoDetect
public class Message {
    private long Id;
    private String title;
    private String body;
    private String digest;
    private String orgId;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;
    private String createdBy; //associate to company's accountId
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime expiredDateTime;
    private String link;
    private String type;
    private String status;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime lastUpdatedDateTime;
    private String lastUpdatedBy; //associate to company's accountId
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime postShowTime;

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

    public DateTime getLastUpdatedDateTime() {
        return lastUpdatedDateTime;
    }

    public void setLastUpdatedDateTime(DateTime lastUpdatedDateTime) {
        this.lastUpdatedDateTime = lastUpdatedDateTime;
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

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
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

    public static Message FromModel(MessageModel model) {
        if (model == null) {
            return null;
        }
        Message message = new Message();
        message.setId(model.getId());
        message.setTitle(model.getTitle());
        message.setBody(model.getBody());
        message.setDigest(model.getDigest());
        message.setCreatedBy(model.getCreatedBy());
        message.setLink(model.getLink());
        message.setCreatedDateTime(model.getCreatedDateTime());
        message.setExpiredDateTime(model.getExpiredDateTime());
        message.setOrgId(model.getOrgId());
        message.setType(model.getType());
        message.setStatus(model.getStatus());
        if (model.getLastUpdatedDateTime() != null) {
            message.setLastUpdatedDateTime(model.getLastUpdatedDateTime());
        }
        if (model.getLastUpdatedBy() != null) {
            message.setLastUpdatedBy(model.getLastUpdatedBy());
        }
        if (model.getPostShowTime() != null) {
            message.setPostShowTime(model.getPostShowTime());
        }
        return message;
    }

    public static MessageModel ToModel(Message message) {
        if (message == null) return null;
        MessageModel model = new MessageModel();
        model.setId(message.getId());
        model.setTitle(message.getTitle());
        model.setBody(message.getBody());
        model.setDigest(message.getDigest());
        model.setCreatedBy(message.getCreatedBy());
        model.setLink(message.getLink());
        if (message.getCreatedDateTime() != null) {
            model.setCreatedDateTime(message.getCreatedDateTime()); //convert string to datetime
        }
        if (message.getExpiredDateTime() != null) {
            model.setExpiredDateTime(message.getExpiredDateTime());
        }
        model.setOrgId(message.getOrgId());
        model.setType(message.getType());
        model.setStatus(message.getStatus());
        if (message.getLastUpdatedDateTime() != null) {
            model.setLastUpdatedDateTime(message.getLastUpdatedDateTime());
        }
        if (message.getPostShowTime() != null) {
            model.setPostShowTime(message.getPostShowTime());
        }
        model.setLastUpdatedBy(message.getLastUpdatedBy());
        return model;
    }

    public static List<Message> FromModelList(List<MessageModel> modelList) {
        if (modelList == null) return null;
        List<Message> messageList = new ArrayList<Message>();
        for (MessageModel model : modelList) {
            messageList.add(Message.FromModel(model));
        }
        return messageList;
    }

    public static List<MessageModel> ToModelList(List<Message> messageList) {
        if (messageList == null) return null;
        List<MessageModel> modelList = new ArrayList<MessageModel>();
        for (Message message : messageList) {
            modelList.add(Message.ToModel(message));
        }
        return modelList;
    }

}
