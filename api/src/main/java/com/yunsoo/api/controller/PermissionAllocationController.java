package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.GroupDomain;
import com.yunsoo.api.domain.PermissionAllocationDomain;
import com.yunsoo.api.dto.PermissionAllocation;
import com.yunsoo.api.security.permission.PermissionEntry;
import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.PrincipalExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression.OrgRestrictionExpression;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.GroupObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

;

/**
 * Created by:   Lijian
 * Created on:   2016-03-31
 * Descriptions:
 */
@RestController
@RequestMapping("/permissionAllocation")
public class PermissionAllocationController {

    @Autowired
    private PermissionAllocationDomain permissionAllocationDomain;

    @Autowired
    private AccountDomain accountDomain;

    @Autowired
    private GroupDomain groupDomain;

    //region account

    @RequestMapping(value = "account/{accountId}", method = RequestMethod.GET)
    public List<PermissionAllocation> getByAccount(@PathVariable("accountId") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId);
        AccountObject accountObject = findAccountById(accountId);
        AuthUtils.checkPermission(accountObject.getOrgId(), "permission_allocation", "read");
        return permissionAllocationDomain.getPermissionAllocationsByAccountId(accountId).stream()
                .map(PermissionAllocation::new)
                .collect(Collectors.toList());
    }

    /**
     * @param accountId            account principal
     * @param orgId                org restriction
     * @param permissionAllocation permission and effect
     */
    @RequestMapping(value = "account/{accountId}/allocate", method = RequestMethod.POST)
    public void allocateByAccount(@PathVariable("accountId") String accountId,
                                  @RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
                                  @RequestBody PermissionAllocation permissionAllocation) {
        accountId = AuthUtils.fixAccountId(accountId);
        AuthUtils.checkPermission(orgId, "permission_allocation", "write");
        findAccountById(accountId);

        PrincipalExpression principalExp = new PrincipalExpression.AccountPrincipalExpression(accountId);
        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = getPermissionEntryEffect(permissionAllocation.getEffect());
        if (permissionExp == null) {
            throw new BadRequestException("permission expression not valid");
        }
        permissionAllocationDomain.allocatePermission(principalExp, restrictionExp, permissionExp, effect);
    }

    /**
     * @param accountId            account principal
     * @param orgId                org restriction
     * @param permissionAllocation permission and effect
     */
    @RequestMapping(value = "account/{accountId}/deallocate", method = RequestMethod.POST)
    public void deallocateByAccount(@PathVariable("accountId") String accountId,
                                    @RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
                                    @RequestBody PermissionAllocation permissionAllocation) {
        accountId = AuthUtils.fixAccountId(accountId);
        AuthUtils.checkPermission(orgId, "permission_allocation", "write");
        findAccountById(accountId);

        PrincipalExpression principalExp = new PrincipalExpression.AccountPrincipalExpression(accountId);
        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = getPermissionEntryEffect(permissionAllocation.getEffect());
        if (permissionExp == null) {
            throw new BadRequestException("permission expression not valid");
        }
        permissionAllocationDomain.deallocatePermission(principalExp, restrictionExp, permissionExp, effect);
    }

    //endregion

    //region group

    @RequestMapping(value = "group/{groupId}", method = RequestMethod.GET)
    public List<PermissionAllocation> getByGroup(@PathVariable("groupId") String groupId) {
        GroupObject groupObject = findGroupById(groupId);
        AuthUtils.checkPermission(groupObject.getOrgId(), "permission_allocation", "read");
        return permissionAllocationDomain.getPermissionAllocationsByGroupId(groupId).stream()
                .map(PermissionAllocation::new)
                .collect(Collectors.toList());
    }

    /**
     * @param groupId              group principal
     * @param orgId                org restriction
     * @param permissionAllocation permission and effect
     */
    @RequestMapping(value = "group/{groupId}/allocate", method = RequestMethod.POST)
    public void allocateByGroup(@PathVariable("groupId") String groupId,
                                @RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
                                @RequestBody PermissionAllocation permissionAllocation) {
        AuthUtils.checkPermission(orgId, "permission_allocation", "write");
        findGroupById(groupId);

        PrincipalExpression principalExp = new PrincipalExpression.GroupPrincipalExpression(groupId);
        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = getPermissionEntryEffect(permissionAllocation.getEffect());
        if (permissionExp == null) {
            throw new BadRequestException("permission expression not valid");
        }
        permissionAllocationDomain.allocatePermission(principalExp, restrictionExp, permissionExp, effect);
    }

    /**
     * @param groupId              group principal
     * @param orgId                org restriction
     * @param permissionAllocation permission and effect
     */
    @RequestMapping(value = "group/{groupId}/deallocate", method = RequestMethod.POST)
    public void deallocateByGroup(@PathVariable("groupId") String groupId,
                                  @RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
                                  @RequestBody PermissionAllocation permissionAllocation) {
        AuthUtils.checkPermission(orgId, "permission_allocation", "write");
        findGroupById(groupId);

        PrincipalExpression principalExp = new PrincipalExpression.GroupPrincipalExpression(groupId);
        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = getPermissionEntryEffect(permissionAllocation.getEffect());
        if (permissionExp == null) {
            throw new BadRequestException("permission expression not valid");
        }
        permissionAllocationDomain.deallocatePermission(principalExp, restrictionExp, permissionExp, effect);
    }

    //endregion


    private AccountObject findAccountById(String id) {
        AccountObject accountObject = accountDomain.getById(id);
        if (accountObject == null) {
            throw new NotFoundException("account not found");
        }
        return accountObject;
    }

    private GroupObject findGroupById(String id) {
        GroupObject groupObject = groupDomain.getById(id);
        if (groupObject == null) {
            throw new NotFoundException("group not found");
        }
        return groupObject;
    }

    private PermissionEntry.Effect getPermissionEntryEffect(PermissionAllocation.Effect effect) {
        return effect == null || effect.equals(PermissionAllocation.Effect.allow)
                ? PermissionEntry.Effect.allow
                : PermissionEntry.Effect.deny;
    }

}
