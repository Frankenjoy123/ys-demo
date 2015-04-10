package com.yunsoo.data.service.service.Impl;

import com.yunsoo.data.service.dao.AccountDao;
import com.yunsoo.data.service.service.AccountService;
import com.yunsoo.data.service.service.contract.Account;
import com.yunsoo.data.service.service.contract.Device;
import com.yunsoo.data.service.service.contract.AccountToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author KB
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public Account get(long id) {
        return Account.FromModel(accountDao.get(id));
    }
    @Override
    public Account getByIdentifier(String identifier) {
        return Account.FromModel(accountDao.getByIdentifier(identifier));
    }
    @Override
    public Account getByToken(String token) {
        return Account.FromModel(accountDao.getByToken(token));
    }
    @Override
    public Account verify(String identity, String password) {
        return null;
    }
    @Override
    public int add(Account account) {
        return 0;
    }
    @Override
    public boolean inactivate(Account account) {
        return false;
    }
    @Override
    public boolean update(Account account) {
        return false;
    }
    @Override
    public void delete(Account account) {

    }
    @Override
    public AccountToken getToken(long id, Device device) {
        return null;
    }
    @Override
    public AccountToken refreshToken(AccountToken token) {
        return null;
    }
}