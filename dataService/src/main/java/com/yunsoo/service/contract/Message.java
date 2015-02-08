package com.yunsoo.service.contract;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.yunsoo.dbmodel.MessageModel;
import org.joda.time.DateTime;

import java.sql.Date;
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
    private long companyId;
    private String createdDateTime;
    private int createdBy; //associate to company's accountId
    private String expiredDateTime;
    private String link;
    private int type;
    private int status;
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

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getExpireDateTime() {
        return expiredDateTime;
    }

    public void setExpireDateTime(String expiredDateTime) {
        this.expiredDateTime = expiredDateTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
        message.setCreatedDateTime(model.getCreatedDateTime().toString());
        message.setExpireDateTime(model.getExpireDateTime().toString());
        message.setCompanyId(model.getCompanyId());
        message.setType(model.getType());
        message.setStatus(model.getStatus());
        if (model.getLastUpdatedDateTime() != null) {
            message.setLastUpatedDateTime(model.getLastUpdatedDateTime().toString());
        }
        if (model.getLastUpdatedBy() != null) {
            message.setLastUpdatedBy(model.getLastUpdatedBy());
        }
        if (model.getPostShowTime() != null) {
            message.setPostShowTime(model.getPostShowTime().toString());
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
        if (!message.getCreatedDateTime().isEmpty()) {
            model.setCreatedDateTime(DateTime.parse(message.getCreatedDateTime())); //convert string to datetime
        }
        model.setExpireDateTime(DateTime.parse(message.getExpireDateTime()));
        model.setCompanyId(message.getCompanyId());
        model.setType(message.getType());
        model.setStatus(message.getStatus());
        if (message.getLastUpatedDateTime() != null) {
            model.setLastUpdatedDateTime(DateTime.parse(message.getLastUpatedDateTime()));
        }
        if (message.getPostShowTime() != null) {
            model.setPostShowTime(DateTime.parse(message.getPostShowTime()));
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
