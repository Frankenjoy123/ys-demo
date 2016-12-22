package com.yunsoo.third.dao.util;

import com.yunsoo.common.util.ObjectIdGenerator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-07-01
 * Descriptions:
 */
public class IdGenerator implements IdentifierGenerator {

    public static final String CLASS = "com.yunsoo.third.dao.util.IdGenerator";

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return ObjectIdGenerator.getNew();
    }
}
