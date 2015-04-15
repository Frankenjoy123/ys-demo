package com.yunsoo.data.service.util;

import com.yunsoo.common.util.UuidUtil;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
public class IdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return UuidUtil.get().toString();
    }
}
