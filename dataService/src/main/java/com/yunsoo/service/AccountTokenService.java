package com.yunsoo.service;

import com.yunsoo.service.contract.AccountToken;

/**
 * Created by Jerry on 3/15/2015.
 */
public interface AccountTokenService {
    public AccountToken get(long id);

    public long save(AccountToken accountToken);

    public ServiceOperationStatus update(AccountToken accountToken);

    public boolean delete(int id, int deleteStatus);
}
