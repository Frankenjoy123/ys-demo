package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.MessageModel;
import org.joda.time.DateTime;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/1/26.
 * This is the data contract for Message to end users.
 */
public class Message {
    private long Id;
    private String title;
    private String body;
    private int companyId;
    private String createdDateTime;
    private int createdBy; //associate to company's accountId
    private String expiredDateTime;
    private String link;
    private int type;
    private int status;

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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
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

    public static Message FromModel(MessageModel model) {
        if (model == null) {
            return null;
        }
        Message message = new Message();
        message.setId(model.getId());
        message.setTitle(model.getTitle());
        message.setBody(model.getBody());
        message.setCreatedBy(model.getCreatedBy());
        message.setLink(model.getLink());
        message.setCreatedDateTime(model.getCreatedDateTime().toString());
        message.setExpireDateTime(model.getExpireDateTime().toString());
        message.setCompanyId(model.getCompanyId());
        message.setType(model.getType());
        message.setStatus(model.getStatus());
        return message;
    }

    public static MessageModel ToModel(Message message) {
        if (message == null) return null;
        MessageModel model = new MessageModel();
        model.setId(message.getId());
        model.setTitle(message.getTitle());
        model.setBody(message.getBody());
        model.setCreatedBy(message.getCreatedBy());
        model.setLink(message.getLink());
//        DateTime.parse(message.getCreatedDateTime())

        model.setCreatedDateTime(DateTime.parse(message.getCreatedDateTime())); //convert string to datetime
        model.setExpireDateTime(DateTime.parse(message.getExpireDateTime()));
        model.setCompanyId(message.getCompanyId());
        model.setType(message.getType());
        model.setStatus(message.getStatus());
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
