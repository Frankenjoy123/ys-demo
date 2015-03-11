package com.yunsoo.service;

import com.yunsoo.service.contract.*;

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
