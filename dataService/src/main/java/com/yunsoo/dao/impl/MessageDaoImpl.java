package com.yunsoo.dao.impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.MessageDao;
import com.yunsoo.dbmodel.MessageModel;
import com.yunsoo.util.YunsooConfig;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
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

    @Override
    public MessageModel get(long id) {
        return (MessageModel) sessionFactory.getCurrentSession().get(
                MessageModel.class, id);
    }

    @Override
    public long save(MessageModel messageModel) {
//       Session session =  sessionFactory.openSession();
//       long result = (long)sessionFactory.getCurrentSession().save(messageModel);
        messageModel.setStatus(YunsooConfig.getMessageCreatedStatus());//always as newly created for newly created newMessage.
        sessionFactory.getCurrentSession().save(messageModel);
        return messageModel.getId();
    }

    @Override
    public void update(MessageModel messageModel) {
        sessionFactory.getCurrentSession().update(messageModel);
    }

    @Override
    public void delete(MessageModel messageModel) {
        sessionFactory.getCurrentSession().delete(messageModel);
    }

    @Override
    public DaoStatus delete(int id) {
//        MessageModel messageModel = this.get(id);
//        if (messageModel == null) return DaoStatus.NotFound;
//
//        messageModel.setStatus(YunsooConfig.getMessageDeleteStatus());
//        this.update(messageModel);
//        return DaoStatus.success;
        //find in config file for deleted status
        return updateStatus(id, YunsooConfig.getMessageDeleteStatus());
//        MyObject myObject = (MyObject) sessionFactory.getCurrentSession().load(MyObject.class,id);
//        sessionFactory.getCurrentSession().delete(myObject);
    }

    //Update status by message Id.
    public DaoStatus updateStatus(int messageId, int status) {
        MessageModel messageModel = this.get(messageId);
        if (messageModel == null) return DaoStatus.NotFound;

        messageModel.setStatus(status); //find in config file for deleted status
        this.update(messageModel);
        return DaoStatus.success;
    }

    @Override
    public List<MessageModel> getMessagesByStatus(int statusId) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(MessageModel.class)
                .add(Restrictions.eq("status", statusId))
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
    public List<MessageModel> getMessagesByFilter(Integer type, Integer status, Integer companyId, boolean ignoreExpireDate) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(MessageModel.class);
        if (type != null) {
            c.add(Restrictions.eq("type", type.intValue()));
        }
        if (status != null) {
            c.add(Restrictions.eq("status", status.intValue()));
        }
        if (companyId != null) {
            c.add(Restrictions.eq("companyId", companyId.intValue()));
        }
        if (!ignoreExpireDate) {
            c.add(Restrictions.gt("expiredDateTime", DateTime.now()));
        }
        return c.list();
    }
}
