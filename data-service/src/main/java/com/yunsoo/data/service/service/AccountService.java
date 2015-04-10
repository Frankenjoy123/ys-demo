package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.Account;
import com.yunsoo.data.service.service.contract.Device;
import com.yunsoo.data.service.service.contract.AccountToken;

/**
 * @author KB
 */
public interface AccountService {
    public Account get(long id);
    public Account getByIdentifier(String identifier);
    public Account getByToken(String token);
    public Account verify(String identity, String password);
    public int add(Account account);
    public boolean inactivate(Account account);
    public boolean update(Account account);
    public void delete(Account account);
    public AccountToken getToken(long id, Device device);
    public AccountToken refreshToken(AccountToken token);
}
