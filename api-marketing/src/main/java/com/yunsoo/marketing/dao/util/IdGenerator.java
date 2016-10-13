package com.yunsoo.marketing.dao.util;

import com.yunsoo.common.util.ObjectIdGenerator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-10-13
 * Descriptions:
 */
public class IdGenerator implements IdentifierGenerator {

    public static final String CLASS = "com.yunsoo.marketing.dao.util.IdGenerator";

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return ObjectIdGenerator.getNew();
    }
}
