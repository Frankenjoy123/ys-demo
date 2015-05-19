package com.yunsoo.data.service.service;

import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.data.service.service.contract.Message;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

/**
 * Created by Zhe on 2015/1/27.
 */
public interface MessageService {

    public Message get(long id);

    public long save(Message message);

    public boolean patchUpdate(Message message);

    public boolean update(Message message);

    public ServiceOperationStatus updateStatus(Long messageId, String status);

    public void delete(Message message);

    public boolean delete(Long messageId);

    public List<Message> getMessagesByStatus(String status);

    public List<Message> getMessagesByType(String typeId);

    public List<Message> getMessagesByFilter(String type, String status, String orgId, Boolean ignoreExpireDate, DateTime postShowtime, int pageIndex, int pageSize);

    public List<Message> getUnreadMessages(String userId, String orgId, Long lastReadMessageId);

    public S3Object getMessageImage(String bucket, String imgKey) throws IOException;
}
