package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.MessageModel;

import java.util.List;

/**
 * Created by Zhe on 2015/1/27.
 */
public interface MessageDao {

    public MessageModel get(long id);

    public long save(MessageModel messageModel);

    public DaoStatus patchUpdate(MessageModel messageModelForPatch);

    public DaoStatus update(MessageModel messageModel);

    public DaoStatus updateStatus(Long messageId, String status);

    public void delete(MessageModel messageModel);

    public DaoStatus delete(Long id);

    public List<MessageModel> getMessagesByStatus(String status);

    public List<MessageModel> getMessagesByType(int typeId);

    public List<MessageModel> getMessagesByFilter(Integer type, String status, String orgId, Boolean ignoreExpireDate, int pageIndex, int pageSize);

    public List<MessageModel> getUnreadMessages(String orgId, Long lastReadMessageId);
}
