package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.Account;
import com.yunsoo.data.service.service.contract.Organization;
import com.yunsoo.data.service.service.contract.Permission;

import java.util.List;

/**
 * @author KB
 */
public interface AccountOrgService {
    public List<Permission> getPermissions(String token, long orgId);
    public Boolean checkPermission(String token, long orgId, String resource, String action);
    public void add(Account account, Organization org);
    public boolean remove(Account account, Organization org);
}
