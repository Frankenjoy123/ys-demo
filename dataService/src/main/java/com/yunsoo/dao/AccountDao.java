package com.yunsoo.dao;

import com.yunsoo.dbmodel.AccountModel;

/**
 * @author KB
 */
public interface AccountDao {
    public AccountModel get(long id);
    public AccountModel getByIdentifier(String identifier);
}