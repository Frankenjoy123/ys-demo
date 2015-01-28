package com.yunsoo.dao;

import com.yunsoo.dbmodel.MessageModel;

import java.util.List;

/**
 * Created by Zhe on 2015/1/27.
 */
public interface MessageDao {

    public MessageModel get(long id);

    public long save(MessageModel messageModel);

    public void update(MessageModel messageModel);

    public DaoStatus updateStatus(int messageId, int status);

    public void delete(MessageModel messageModel);

    public DaoStatus delete(int id);

    public List<MessageModel> getMessagesByStatus(int statusId);

    public List<MessageModel> getMessagesByType(int typeId);

    public List<MessageModel> getMessagesByFilter(Integer type, Integer status, Integer companyId, boolean ignoreExpireDate);
}
