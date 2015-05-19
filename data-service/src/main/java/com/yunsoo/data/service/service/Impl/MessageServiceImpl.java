package com.yunsoo.data.service.service.Impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.data.service.dao.DaoStatus;
import com.yunsoo.data.service.dao.MessageDao;
import com.yunsoo.data.service.dao.S3ItemDao;
import com.yunsoo.data.service.dbmodel.MessageModel;
import com.yunsoo.data.service.service.contract.Message;
import com.yunsoo.data.service.service.MessageService;
import com.yunsoo.data.service.service.ServiceOperationStatus;
import com.yunsoo.data.service.util.SpringBeanUtil;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Created by Zhe on 2015/1/27.
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private S3ItemDao s3ItemDao;

    @Override
    public Message get(long id) {
        return Message.FromModel(messageDao.get(id));
    }

    //create New Message.
    @Override
    public long save(Message newMessage) {
        // newMessage.setStatus(YunsooConfig.getMessageCreatedStatus());
        return messageDao.save(Message.ToModel(newMessage));
    }

    @Override
    public boolean patchUpdate(Message message) {
        DaoStatus daoStatus = messageDao.patchUpdate(getPatchModel(message));
        if (daoStatus == DaoStatus.success) return true;
        else return false;
    }

    @Override
    public boolean update(Message message) {
        DaoStatus daoStatus = messageDao.update(getPatchModel(message));
        if (daoStatus == DaoStatus.success) return true;
        else return false;
    }

    @Override
    public ServiceOperationStatus updateStatus(Long messageId, String status) {
        DaoStatus daoStatus = messageDao.updateStatus(messageId, status);
        if (daoStatus == DaoStatus.success) {
            return ServiceOperationStatus.Success;
        } else if (daoStatus == DaoStatus.NotFound) {
            return ServiceOperationStatus.ObjectNotFound;
        } else {
            return ServiceOperationStatus.Fail;
        }
    }

    @Override
    public void delete(Message message) {
        messageDao.delete(Message.ToModel(message));
    }

    @Override
    public boolean delete(Long messageId) {
        DaoStatus daoStatus = messageDao.delete(messageId);
        if (daoStatus == DaoStatus.success) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    @Transactional
    public List<Message> getMessagesByStatus(String status) {
        return Message.FromModelList(messageDao.getMessagesByStatus(status));
    }

    @Override
    public List<Message> getMessagesByType(String type) {
        return Message.FromModelList(messageDao.getMessagesByType(type));
    }

    @Override
    @Transactional
    public List<Message> getMessagesByFilter(String type, String status, String orgId, Boolean ignoreExpireDate, DateTime postShowtime, int pageIndex, int pageSize) {
        return Message.FromModelList(messageDao.getMessagesByFilter(type, status, orgId, ignoreExpireDate, postShowtime, pageIndex, pageSize));
    }

    @Override
    public List<Message> getUnreadMessages(String userId, String orgId, Long lastReadMessageId) {
        //to do: log user read someone's message
        return Message.FromModelList(messageDao.getUnreadMessages(orgId, lastReadMessageId));
    }

    //Convert Dto to Model,just copy properties which is not null.
    private MessageModel getPatchModel(Message message) {
        //ProductStatusModel model = productStatusDao.getById(id);
        MessageModel model = new MessageModel();
        BeanUtils.copyProperties(message, model, SpringBeanUtil.getNullPropertyNames(message));
        return model;
    }

    @Override
    public S3Object getMessageImage(String bucket, String imgKey) throws IOException {
        try {
            S3Object item = s3ItemDao.getItem(bucket, imgKey);
            return item;
        } catch (AmazonS3Exception s3ex) {
            if (s3ex.getErrorCode() == "NoSuchKey") {
                //log
            }
            return null;
        } catch (Exception ex) {
            //to-do: log
            return null;
        }
    }
}
