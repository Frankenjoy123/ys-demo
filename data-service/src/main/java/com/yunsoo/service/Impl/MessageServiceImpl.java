package com.yunsoo.service.Impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.MessageDao;
import com.yunsoo.dbmodel.MessageModel;
import com.yunsoo.service.contract.Message;
import com.yunsoo.service.MessageService;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Zhe on 2015/1/27.
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public Message get(long id) {
        return Message.FromModel(messageDao.get(id));
    }

    //create New Message.
    @Override
    public long save(Message newMessage) {
        // newMessage.setStatusId(YunsooConfig.getMessageCreatedStatus());
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
    public ServiceOperationStatus updateStatus(Long messageId, int status) {
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
    public List<Message> getMessagesByStatus(int statusId) {
        return Message.FromModelList(messageDao.getMessagesByStatus(statusId));
    }

    @Override
    public List<Message> getMessagesByType(int typeId) {
        return Message.FromModelList(messageDao.getMessagesByType(typeId));
    }

    @Override
    @Transactional
    public List<Message> getMessagesByFilter(Integer type, Integer status, Long companyId, Boolean ignoreExpireDate) {
        return Message.FromModelList(messageDao.getMessagesByFilter(type, status, companyId, ignoreExpireDate));
    }

    @Override
    public List<Message> getUnreadMessages(Long userId, Long companyId) {
        return Message.FromModelList(messageDao.getUnreadMessages(userId, companyId));
    }

    //Convert Dto to Model,just copy properties which is not null.
    private MessageModel getPatchModel(Message message) {
        //ProductStatusModel model = productStatusDao.getById(id);
        MessageModel model = new MessageModel();
        BeanUtils.copyProperties(message, model, SpringBeanUtil.getNullPropertyNames(message));
        return model;
    }
}