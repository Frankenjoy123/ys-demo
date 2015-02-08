package com.yunsoo.dao.impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.MessageDao;
import com.yunsoo.dbmodel.MessageModel;
import com.yunsoo.dbmodel.UserOrganizationModel;
import com.yunsoo.util.YunsooConfig;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
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
        return updateStatus(id, YunsooConfig.getMessageDeleteStatus());
//        MyObject myObject = (MyObject) sessionFactory.getCurrentSession().load(MyObject.class,id);
//        sessionFactory.getCurrentSession().delete(myObject);
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
    public List<MessageModel> getUnreadMessages(Long userId, Long companyId) {
        Session session = sessionFactory.getCurrentSession();
//        Criteria criteria = session.createCriteria(MessageModel.class);
//        criteria.add(Restrictions.eq("status", 3)); //approved message status = 3
//        criteria.add(Restrictions.eq("companyId", companyId));
//
//        Criteria criteriaUserOrg = criteria.createCriteria("userOrganizationModelSet");
//        criteriaUserOrg.add(Restrictions.eq("organizationId", companyId));
//        criteriaUserOrg.add(Restrictions.eq("userId", userId));
//
//        Criteria nestedCriteriaUser = criteriaUserOrg.createCriteria("userModel");
//        nestedCriteriaUser.add(Restrictions.eq("id", userId));
////        criteriaMessage.add(Restrictions.gt("id", Property.forName("lastReadMessageId").in(criteria)));
//        criteria.setProjection(
//                Projections.projectionList()
//                        .add(Property.forName("id"))
//                        .add(Property.forName("title"))
//                        .add(Property.forName("body"))
//                        .add(Property.forName("digest"))
//        );
//        List<Object> list = criteria.list();
//        Iterator it = list.iterator();
//        while (it.hasNext()) {
//            Object[] objects = (Object[]) it.next();
//        }

//        String hql = "from UserOrganizationModel where id=" + userId + " from MessageModel where companyId=" + companyId;
//        Query query = sessionFactory.getCurrentSession().createQuery(hql);

//        String hql = "select m from MessageModel m  " +
//                "inner join m.userOrganizationModelSet ua " +
//                "inner join ua.userModel u " +
//                "on ua.userId = u.id and m.id > ua.lastReadMessageId " +
//                "where m.status = "+ YunsooConfig.getMessageApprovedStatus() +
//                " and m.companyId = "+ companyId +
//                " and u.id = "+ userId +
//                " and ua.organizationId = " + companyId;
//        Query query = session.createQuery(hql);
//        List<MessageModel> messageModelList = query.list();

//        return messageModelList;

        return getMessagesByFilter(null, null, 18L, null);
    }
}
