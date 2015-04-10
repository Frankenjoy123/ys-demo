package com.yunsoo.data.service.service.Impl;

import com.yunsoo.data.service.dao.AccountPermissionDao;
import com.yunsoo.data.service.service.contract.AccountPermission;
import com.yunsoo.data.service.service.AccountPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author KB
 */
@Service("accountPermissionService")
public class AccountPermissionServiceImpl implements AccountPermissionService {

    @Autowired
    private AccountPermissionDao dao;

    @Override
    public AccountPermission getPermissions(long orgId, long accountId) {
        return AccountPermission.FromModel(dao.get(orgId, accountId));
    }

}