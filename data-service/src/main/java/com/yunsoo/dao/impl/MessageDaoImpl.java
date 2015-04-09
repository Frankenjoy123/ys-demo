package com.yunsoo.dao.impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.MessageDao;
import com.yunsoo.dbmodel.MessageModel;
import com.yunsoo.config.DataServiceSetting;
import com.yunsoo.util.SpringBeanUtil;
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

    @Autowired
    private DataServiceSetting dataServiceSetting;

    @Override
    public MessageModel get(long id) {
        return (MessageModel) sessionFactory.getCurrentSession().get(
                MessageModel.class, id);
    }

    @Override
    public long save(MessageModel messageModel) {
//       Session session =  sessionFactory.openSession();
//       long result = (long)sessionFactory.getCurrentSession().save(messageModelForPatch);
        messageModel.setStatus(dataServiceSetting.getMessage_created_status_id());//always as newly created for newly created newMessage.
        messageModel.setCreatedDateTime(DateTime.now());
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
        return updateStatus(id, dataServiceSetting.getMessage_delete_status_id());
//        MyObject myObject = (MyObject) sessionFactory.getCurrentSession().load(MyObject.class,id);
//        sessionFactory.getCurrentSession().deletePermanantly(myObject);
    }

    //Update status by message Id.
    public DaoStatus updateStatus(Long messageId, int status) {
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
    public List<MessageModel> getMessagesByFilter(Integer type, Integer status, Long companyId, Boolean ignoreExpireDate) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(MessageModel.class);
        if (type != null) {
            c.add(Restrictions.eq("type", type.intValue()));
        }
        if (status != null) {
            c.add(Restrictions.eq("status", status.intValue()));
        }
        if (companyId != null) {
            c.add(Restrictions.eq("companyId", companyId.longValue()));
        }
        if (!ignoreExpireDate) {
            c.add(Restrictions.gt("expiredDateTime", DateTime.now()));
        }
        return c.list();
    }

    @Override
    public List<MessageModel> getUnreadMessages(Long companyId, Long lastReadMessageId) {
        Session session = sessionFactory.getCurrentSession();

        Criteria c = sessionFactory.getCurrentSession().createCriteria(MessageModel.class);
        if (lastReadMessageId != null) {
            c.add(Restrictions.gt("id", lastReadMessageId.longValue()));
        }
        if (companyId != null) {
            c.add(Restrictions.eq("companyId", companyId.longValue()));
        }
        c.add(Restrictions.eq("status", dataServiceSetting.getMessage_approved_status_id()));
        c.add(Restrictions.gt("expiredDateTime", DateTime.now()));

        return c.list();
    }
}
