package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.AccountTokenModel;

/**
 * Created by Chen Jerry on 3/15/2015.
 */
public interface AccountTokenDao {
    public AccountTokenModel get(long id);
    public AccountTokenModel getByIdentifier(String identifier);

    public long save(AccountTokenModel accountTokenModel);

    public DaoStatus update(AccountTokenModel accountTokenModel);

    public DaoStatus delete(long id, int deleteStatus);
}
