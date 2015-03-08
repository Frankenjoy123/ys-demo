package com.yunsoo.service.Impl;

import com.yunsoo.dao.AccountDao;
import com.yunsoo.service.AccountService;
import com.yunsoo.service.contract.Account;
import com.yunsoo.service.contract.AccountToken;
import com.yunsoo.service.contract.Device;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author KB
 */
@Service("userService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    public Account get(long id) {
        return Account.fromModel(accountDao.get(id));
    }
    public Account verify(String identity, String password) {
        return null;
    }
    public int add(Account account) {
        return 0;
    }
    public boolean inactivate(Account account) {
        return false;
    }
    public boolean update(Account account) {
        return false;
    }
    public void delete(Account account) {

    }
    public AccountToken getToken(long id, Device device) {
        return null;
    }
    public AccountToken refreshToken(AccountToken token) {
        return null;
    }
}