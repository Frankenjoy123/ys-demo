package com.yunsoo.dbmodel;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Zhe on 2015/2/5.
 */
@Entity
@Table(name = "user_organization")
@XmlRootElement
public class UserOrganizationModel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "organization_id")
    private long organizationId;

    @Column(name = "last_read_message_id")
    private long lastReadMessageId;

    @Column(name = "is_following")
    private long isFollowing;

//    @ManyToOne
//    @JoinColumn(name="user_id")
//    private UserModel userModel;
//
//    @ManyToOne
//    @JoinColumn(name="last_read_message_id")
//    private MessageModel messageModel;

//    @ManyToOne
//    @JoinColumn(name = "last_read_message_id")
//    public MessageModel getMessageModel() {
//        return messageModel;
//    }
//
//    public void setMessageModel(MessageModel messageModel) {
//        this.messageModel = messageModel;
//    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }

    public long getLastReadMessageId() {
        return lastReadMessageId;
    }

    public void setLastReadMessageId(long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }

    public long getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(long isFollowing) {
        this.isFollowing = isFollowing;
    }

//    @ManyToOne
//    @JoinColumn(name = "user_Id")
//    public UserModel getUserModel() {
//        return userModel;
//    }
//
//    public void setUserModel(UserModel userModel) {
//        this.userModel = userModel;
//    }

}
