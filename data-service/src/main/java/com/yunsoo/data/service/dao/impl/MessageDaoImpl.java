package com.yunsoo.data.service.dao.impl;

import com.yunsoo.data.service.dao.DaoStatus;
import com.yunsoo.data.service.dao.MessageDao;
import com.yunsoo.data.service.dbmodel.MessageModel;
import com.yunsoo.data.service.util.SpringBeanUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Zhe on 2015/1/27.
 */
@Repository("messageDao")
@Transactional
public class MessageDaoImpl implements MessageDao {

    @Autowired
    private SessionFactory sessionFactory;

    private final String message_deleted = "deleted";
    private final String message_approved = "approved";

    @Override
    public MessageModel get(long id) {
        return (MessageModel) sessionFactory.getCurrentSession().get(
                MessageModel.class, id);
    }

    @Override
    public long save(MessageModel messageModel) {
//       Session session =  sessionFactory.openSession();
//       long result = (long)sessionFactory.getCurrentSession().save(messageModelForPatch);
//        messageModel.setStatus(dataServiceSetting.getMessage_created_status_id());//always as newly created for newly created newMessage.
        messageModel.setCreatedDateTime(DateTime.now());
        messageModel.setExpireDateTime(DateTime.now().plusDays(100)); //default 100 days to expire.
        sessionFactory.getCurrentSession().save(messageModel);
        return messageModel.getId();
    }

    @Override
    public DaoStatus patchUpdate(MessageModel messageModelForPatch) {
        try {
            Session currentSession = sessionFactory.getCurrentSession();
            MessageModel modelInDB = (MessageModel) currentSession.get(MessageModel.class, messageModelForPatch.getId());
            //Set properties that needs to update in DB, ignore others are null.
            BeanUtils.copyProperties(messageModelForPatch, modelInDB, SpringBeanUtil.getNullPropertyNames(messageModelForPatch));
            currentSession.update(modelInDB);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log
            System.out.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
            return DaoStatus.fail;
        }
    }

    @Override
    public DaoStatus update(MessageModel messageModel) {
        try {
            sessionFactory.getCurrentSession().update(messageModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

    @Override
    public void delete(MessageModel messageModel) {
        sessionFactory.getCurrentSession().delete(messageModel);
    }

    @Override
    public DaoStatus delete(Long id) {
        //find in config file for deleted status
        return updateStatus(id, message_deleted);
//        MyObject myObject = (MyObject) sessionFactory.getCurrentSession().load(MyObject.class,id);
//        sessionFactory.getCurrentSession().deletePermanantly(myObject);
    }

    //Update status by message Id.
    public DaoStatus updateStatus(Long messageId, String status) {
        MessageModel messageModel = this.get(messageId);
        if (messageModel == null) return DaoStatus.NotFound;

        messageModel.setStatus(status); //find in config file for deleted status
        this.update(messageModel);
        return DaoStatus.success;
    }

    @Override
    public List<MessageModel> getMessagesByStatus(String status) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(MessageModel.class)
                .add(Restrictions.eq("status", status))
                .addOrder(Order.asc("id"));
        return c.list();
    }

    @Override
    public List<MessageModel> getMessagesByType(int typeId) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(MessageModel.class)
                .add(Restrictions.eq("type", typeId))
                .addOrder(Order.asc("id"));
        return c.list();
    }

    @Override
    public List<MessageModel> getMessagesByFilter(Integer type, String status, String orgId, Boolean ignoreExpireDate, int pageIndex, int pageSize) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(MessageModel.class);
        if (type != null) {
            c.add(Restrictions.eq("type", type.intValue()));
        }
        if (status != null) {
            c.add(Restrictions.eq("status", status));
        }
        if (orgId != null) {
            c.add(Restrictions.eq("orgId", orgId));
        }
        if (!ignoreExpireDate) {
            c.add(Restrictions.gt("expiredDateTime", DateTime.now()));
        }
        c.addOrder(Order.desc("createdDateTime"));
        c.setFirstResult(pageIndex * pageSize);
        c.setMaxResults(pageSize);
        return c.list();
    }

    @Override
    public List<MessageModel> getUnreadMessages(String orgId, Long lastReadMessageId) {
        Session session = sessionFactory.getCurrentSession();

        Criteria c = sessionFactory.getCurrentSession().createCriteria(MessageModel.class);
        if (lastReadMessageId != null) {
            c.add(Restrictions.gt("id", lastReadMessageId.longValue()));
        }
        if (orgId != null) {
            c.add(Restrictions.eq("orgId", orgId));
        }
        c.add(Restrictions.eq("status", message_approved));
        c.add(Restrictions.gt("expiredDateTime", DateTime.now()));

        return c.list();
    }
}
