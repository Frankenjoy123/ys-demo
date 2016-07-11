package com.yunsoo.auth.service;

import com.yunsoo.auth.api.security.permission.PermissionEntry;
import com.yunsoo.auth.api.security.permission.expression.PermissionExpression;
import com.yunsoo.auth.api.security.permission.expression.PermissionExpression.SimplePermissionExpression;
import com.yunsoo.auth.api.security.permission.expression.PrincipalExpression;
import com.yunsoo.auth.api.security.permission.expression.PrincipalExpression.AccountPrincipalExpression;
import com.yunsoo.auth.api.security.permission.expression.PrincipalExpression.GroupPrincipalExpression;
import com.yunsoo.auth.api.security.permission.expression.RestrictionExpression;
import com.yunsoo.auth.api.security.permission.expression.RestrictionExpression.OrgRestrictionExpression;
import com.yunsoo.auth.api.security.permission.expression.RestrictionExpression.RegionRestrictionExpression;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.dao.entity.PermissionAllocationEntity;
import com.yunsoo.auth.dao.repository.PermissionAllocationRepository;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.PermissionAllocation;
import com.yunsoo.auth.dto.PermissionRegion;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-06
 * Descriptions:
 */
@Service
public class PermissionAllocationService {

    @Autowired
    private PermissionAllocationRepository permissionAllocationRepository;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AccountGroupService accountGroupService;

    @Autowired
    private AccountService accountService;


    /**
     * permission allocations of the account and it's groups
     *
     * @param accountId accountId
     * @return List<PermissionAllocation>
     */
    public List<PermissionAllocation> getAllPermissionAllocationsByAccountId(String accountId) {
        List<String> principals = new ArrayList<>();
        principals.add(new AccountPrincipalExpression(accountId).toString());
        principals.add(AccountPrincipalExpression.ANY.toString()); // account/*
        accountGroupService.getGroupIdsByAccountId(accountId).forEach(g -> {
            principals.add(new GroupPrincipalExpression(g).toString());
        });

        return getPermissionAllocationsByPrincipal(principals);
    }

    public List<PermissionAllocation> getPermissionAllocationsByAccountId(String accountId) {
        return StringUtils.isEmpty(accountId)
                ? new ArrayList<>()
                : getPermissionAllocationsByPrincipal(new AccountPrincipalExpression(accountId).toString());
    }

    public List<PermissionAllocation> getPermissionAllocationsByGroupId(String groupId) {
        return StringUtils.isEmpty(groupId)
                ? new ArrayList<>()
                : getPermissionAllocationsByPrincipal(new GroupPrincipalExpression(groupId).toString());
    }

    public void deletePermissionAllocationsByGroupId(String groupId) {
        getPermissionAllocationsByPrincipal(new GroupPrincipalExpression(groupId).toString())
                .forEach(pa -> {
                    deletePermissionAllocationById(pa.getId());
                });
    }

    public void allocateAdminPermissionOnCurrentOrgToAccount(String accountId) {
        Assert.hasText(accountId, "accountId not valid");
        allocatePermission(
                new AccountPrincipalExpression(accountId),
                OrgRestrictionExpression.CURRENT,
                SimplePermissionExpression.ADMIN,
                PermissionEntry.Effect.allow);
    }

    public void allocateAdminPermissionOnDefaultRegionToAccount(String accountId) {
        Account account = accountService.getById(accountId);
        Assert.notNull(account, "account not valid");

        PermissionRegion defaultPR = permissionService.getOrCreateDefaultPermissionRegion(account.getOrgId());
        allocatePermission(
                new AccountPrincipalExpression(accountId),
                new RegionRestrictionExpression(defaultPR.getId()),
                SimplePermissionExpression.ADMIN,
                PermissionEntry.Effect.allow);
    }

    public void allocatePermission(PrincipalExpression principal,
                                   RestrictionExpression restriction,
                                   PermissionExpression permission,
                                   PermissionEntry.Effect effect) {
        List<PermissionAllocation> pAOs = getPermissionAllocationsByPrincipal(principal.toString());
        for (PermissionAllocation p : pAOs) {
            if (isEqualPermissionAllocation(p, principal, restriction, permission, effect)) {
                return; //already has the same allocation item
            }
        }
        createPermissionAllocation(principal.toString(), restriction.toString(), permission.toString(), effect.name());
    }

    public void deallocatePermission(PrincipalExpression principal,
                                     RestrictionExpression restriction,
                                     PermissionExpression permission,
                                     PermissionEntry.Effect effect) {
        List<PermissionAllocation> pAOs = getPermissionAllocationsByPrincipal(principal.toString());
        for (PermissionAllocation p : pAOs) {
            if (isEqualPermissionAllocation(p, principal, restriction, permission, effect)) {
                //already has the same allocation item
                deletePermissionAllocationById(p.getId());
            }
        }
    }

    //region private methods

    private boolean isEqualPermissionAllocation(PermissionAllocation pao,
                                                PrincipalExpression principal,
                                                RestrictionExpression restriction,
                                                PermissionExpression permission,
                                                PermissionEntry.Effect effect) {
        OrgRestrictionExpression currentOrgRestriction = new OrgRestrictionExpression(AuthUtils.getCurrentAccount().getOrgId());
        RestrictionExpression restriction1 = OrgRestrictionExpression.CURRENT.equals(restriction) ? currentOrgRestriction : restriction;
        String paoRestriction1 = OrgRestrictionExpression.CURRENT.toString().equals(pao.getRestriction()) ? currentOrgRestriction.toString() : pao.getRestriction();

        return principal.toString().equals(pao.getPrincipal())
                && restriction1.toString().equals(paoRestriction1)
                && permission.toString().equals(pao.getPermission())
                && effect.name().equals(pao.getEffect());
    }

    private List<PermissionAllocation> getPermissionAllocationsByPrincipal(String principal) {
        if (StringUtils.isEmpty(principal)) {
            return new ArrayList<>();
        }
        return permissionAllocationRepository.findByPrincipal(principal)
                .stream()
                .map(this::toPermissionAllocation)
                .collect(Collectors.toList());
    }

    private List<PermissionAllocation> getPermissionAllocationsByPrincipal(List<String> principals) {
        if (principals == null || principals.size() == 0) {
            return new ArrayList<>();
        }
        return permissionAllocationRepository.findByPrincipalIn(principals)
                .stream()
                .map(this::toPermissionAllocation)
                .collect(Collectors.toList());
    }

    private PermissionAllocation createPermissionAllocation(String principal, String restriction, String permission, String effect) {
        PermissionAllocationEntity entity = new PermissionAllocationEntity();
        entity.setId(null);
        entity.setPrincipal(principal);
        entity.setRestriction(restriction);
        entity.setPermission(permission);
        entity.setEffect(effect);
        entity.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        entity.setCreatedDateTime(DateTime.now());
        return toPermissionAllocation(permissionAllocationRepository.save(entity));
    }

    private void deletePermissionAllocationById(String id) {
        permissionAllocationRepository.delete(id);
    }

    private PermissionAllocation toPermissionAllocation(PermissionAllocationEntity entity) {
        if (entity == null) {
            return null;
        }
        PermissionAllocation permissionAllocation = new PermissionAllocation();
        permissionAllocation.setId(entity.getId());
        permissionAllocation.setPrincipal(entity.getPrincipal());
        permissionAllocation.setRestriction(entity.getRestriction());
        permissionAllocation.setPermission(entity.getPermission());
        permissionAllocation.setEffect(entity.getEffect());
        permissionAllocation.setCreatedAccountId(entity.getCreatedAccountId());
        permissionAllocation.setCreatedDateTime(entity.getCreatedDateTime());
        return permissionAllocation;
    }

    private PermissionAllocationEntity toPermissionAllocationEntity(PermissionAllocation permissionAllocation) {
        if (permissionAllocation == null) {
            return null;
        }
        PermissionAllocationEntity entity = new PermissionAllocationEntity();
        entity.setId(permissionAllocation.getId());
        entity.setPrincipal(permissionAllocation.getPrincipal());
        entity.setRestriction(permissionAllocation.getRestriction());
        entity.setPermission(permissionAllocation.getPermission());
        entity.setEffect(permissionAllocation.getEffect());
        entity.setCreatedAccountId(permissionAllocation.getCreatedAccountId());
        entity.setCreatedDateTime(permissionAllocation.getCreatedDateTime());
        return entity;
    }

    //endregion

}
