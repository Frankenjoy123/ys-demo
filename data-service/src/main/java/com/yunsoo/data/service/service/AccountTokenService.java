package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.AccountToken;

/**
 * Created by Jerry on 3/15/2015.
 */
public interface AccountTokenService {
    public AccountToken get(long id);
    public AccountToken getByIdentifier(String identifier);

    public long save(AccountToken accountToken);

    public ServiceOperationStatus update(AccountToken accountToken);

    public boolean delete(int id, int deleteStatus);
}