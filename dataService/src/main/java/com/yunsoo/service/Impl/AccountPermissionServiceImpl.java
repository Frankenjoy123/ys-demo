package com.yunsoo.service.Impl;

import com.yunsoo.dao.AccountDao;
import com.yunsoo.dao.AccountPermissionDao;
import com.yunsoo.service.AccountPermissionService;
import com.yunsoo.service.AccountService;
import com.yunsoo.service.contract.Account;
import com.yunsoo.service.contract.AccountPermission;
import com.yunsoo.service.contract.AccountToken;
import com.yunsoo.service.contract.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author KB
 */
@Service("accountPermissionService")
public class AccountPermissionServiceImpl implements AccountPermissionService {

    @Autowired
    private AccountPermissionDao accountPermissionDao;

    @Override
    public AccountPermission getPermissions(long accountId, long orgId) {
        return null;
    }

}