package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.MessageModel;
import org.joda.time.DateTime;

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

    public List<MessageModel> getMessagesByType(String type);

    public List<MessageModel> getMessagesByFilter(String type, String status, String orgId, Boolean ignoreExpireDate, DateTime postShowtime, int pageIndex, int pageSize);

    public List<MessageModel> getUnreadMessages(String orgId, Long lastReadMessageId);

    public int countMessage(List<String> typeList, List<String> statusList, String orgId, DateTime postShowtime);

}
