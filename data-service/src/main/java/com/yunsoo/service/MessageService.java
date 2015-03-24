package com.yunsoo.service;

import com.yunsoo.service.contract.Message;

import java.util.List;

/**
 * Created by Zhe on 2015/1/27.
 */
public interface MessageService {

    public Message get(long id);

    public long save(Message message);

    public boolean patchUpdate(Message message);

    public boolean update(Message message);

    public ServiceOperationStatus updateStatus(Long messageId, int status);

    public void delete(Message message);

    public boolean delete(Long messageId);

    public List<Message> getMessagesByStatus(int statusId);

    public List<Message> getMessagesByType(int typeId);

    public List<Message> getMessagesByFilter(Integer type, Integer status, Long companyId, Boolean ignoreExpireDate);

    public List<Message> getUnreadMessages(Long userId, Long companyId, Long lastReadMessageId);
}
