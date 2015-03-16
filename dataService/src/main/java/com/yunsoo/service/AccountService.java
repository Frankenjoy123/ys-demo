package com.yunsoo.service;

import com.yunsoo.service.contract.Account;
import com.yunsoo.service.contract.AccountToken;
import com.yunsoo.service.contract.Device;
import com.yunsoo.service.contract.Permission;

import java.util.List;

/**
 * @author KB
 */
public interface AccountService {
    public Account get(long id);
    public Account get(String username);
    public Account getByToken(String token);
    public Account verify(String identity, String password);
    public int add(Account account);
    public boolean inactivate(Account account);
    public boolean update(Account account);
    public void delete(Account account);
    public AccountToken getToken(long id, Device device);
    public AccountToken refreshToken(AccountToken token);
}
