package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.AccountModel;

/**
 * @author KB
 */
public interface AccountDao {
    public AccountModel get(long id);
    public AccountModel getByIdentifier(String identifier);
    public AccountModel getByToken(String token);
}