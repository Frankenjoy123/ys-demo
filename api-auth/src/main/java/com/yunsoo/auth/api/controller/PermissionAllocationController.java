package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.Group;
import com.yunsoo.auth.dto.PermissionAllocation;
import com.yunsoo.auth.service.AccountService;
import com.yunsoo.auth.service.GroupService;
import com.yunsoo.auth.service.PermissionAllocationService;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.security.permission.PermissionEntry;
import com.yunsoo.common.web.security.permission.expression.PermissionExpression;
import com.yunsoo.common.web.security.permission.expression.PrincipalExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression.OrgRestrictionExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-07
 * Descriptions:
 */

@RestController
@RequestMapping("/permissionAllocation")
public class PermissionAllocationController {

    @Autowired
    private PermissionAllocationService permissionAllocationService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private GroupService groupService;


    //region account

    @RequestMapping(value = "account/{accountId}", method = RequestMethod.GET)
    public List<PermissionAllocation> getByAccount(@PathVariable("accountId") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId);
        Account account = findAccountById(accountId);
        AuthUtils.checkPermission(account.getOrgId(), "permission_allocation", "read");
        return permissionAllocationService.getPermissionAllocationsByAccountId(accountId);
    }

    /**
     * @param accountId            account principal
     * @param orgId                org restriction
     * @param permissionAllocation permission and effect
     */
    @RequestMapping(value = "account/{accountId}/allocate", method = RequestMethod.POST)
    public List<PermissionAllocation> allocateByAccount(@PathVariable("accountId") String accountId,
                                                        @RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
                                                        @RequestBody PermissionAllocation permissionAllocation) {
        accountId = AuthUtils.fixAccountId(accountId);
        AuthUtils.checkPermission(orgId, "permission_allocation", "write");
        findAccountById(accountId);

        PrincipalExpression principalExp = new PrincipalExpression.AccountPrincipalExpression(accountId);
        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = tryParseEffect(permissionAllocation.getEffect());
        if (permissionExp == null) {
            throw new BadRequestException("permission expression not valid");
        }
        permissionAllocationService.allocatePermission(principalExp, restrictionExp, permissionExp, effect);
        return permissionAllocationService.getPermissionAllocationsByAccountId(accountId);
    }

    /**
     * @param accountId            account principal
     * @param orgId                org restriction
     * @param permissionAllocation permission and effect
     */
    @RequestMapping(value = "account/{accountId}/deallocate", method = RequestMethod.POST)
    public List<PermissionAllocation> deallocateByAccount(@PathVariable("accountId") String accountId,
                                                          @RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
                                                          @RequestBody PermissionAllocation permissionAllocation) {
        accountId = AuthUtils.fixAccountId(accountId);
        AuthUtils.checkPermission(orgId, "permission_allocation", "write");
        findAccountById(accountId);

        PrincipalExpression principalExp = new PrincipalExpression.AccountPrincipalExpression(accountId);
        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = tryParseEffect(permissionAllocation.getEffect());
        if (permissionExp == null) {
            throw new BadRequestException("permission expression not valid");
        }
        permissionAllocationService.deallocatePermission(principalExp, restrictionExp, permissionExp, effect);
        return permissionAllocationService.getPermissionAllocationsByAccountId(accountId);
    }

    //endregion

    //region group

    @RequestMapping(value = "group/{groupId}", method = RequestMethod.GET)
    public List<PermissionAllocation> getByGroup(@PathVariable("groupId") String groupId) {
        Group group = findGroupById(groupId);
        AuthUtils.checkPermission(group.getOrgId(), "permission_allocation", "read");
        return permissionAllocationService.getPermissionAllocationsByGroupId(groupId);
    }

    /**
     * @param groupId              group principal
     * @param orgId                org restriction
     * @param permissionAllocation permission and effect
     */
    @RequestMapping(value = "group/{groupId}/allocate", method = RequestMethod.POST)
    public List<PermissionAllocation> allocateByGroup(@PathVariable("groupId") String groupId,
                                                      @RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
                                                      @RequestBody PermissionAllocation permissionAllocation) {
        AuthUtils.checkPermission(orgId, "permission_allocation", "write");
        findGroupById(groupId);

        PrincipalExpression principalExp = new PrincipalExpression.GroupPrincipalExpression(groupId);
        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = tryParseEffect(permissionAllocation.getEffect());
        if (permissionExp == null) {
            throw new BadRequestException("permission expression not valid");
        }
        permissionAllocationService.allocatePermission(principalExp, restrictionExp, permissionExp, effect);
        return permissionAllocationService.getPermissionAllocationsByGroupId(groupId);
    }

    /**
     * @param groupId              group principal
     * @param orgId                org restriction
     * @param permissionAllocation permission and effect
     */
    @RequestMapping(value = "group/{groupId}/deallocate", method = RequestMethod.POST)
    public List<PermissionAllocation> deallocateByGroup(@PathVariable("groupId") String groupId,
                                                        @RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
                                                        @RequestBody PermissionAllocation permissionAllocation) {
        AuthUtils.checkPermission(orgId, "permission_allocation", "write");
        findGroupById(groupId);

        PrincipalExpression principalExp = new PrincipalExpression.GroupPrincipalExpression(groupId);
        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = tryParseEffect(permissionAllocation.getEffect());
        if (permissionExp == null) {
            throw new BadRequestException("permission expression not valid");
        }
        permissionAllocationService.deallocatePermission(principalExp, restrictionExp, permissionExp, effect);
        return permissionAllocationService.getPermissionAllocationsByGroupId(groupId);
    }

    //endregion


    private Account findAccountById(String id) {
        Account account = accountService.getById(id);
        if (account == null) {
            throw new NotFoundException("account not found");
        }
        return account;
    }

    private Group findGroupById(String id) {
        Group group = groupService.getById(id);
        if (group == null) {
            throw new NotFoundException("group not found");
        }
        return group;
    }

    private PermissionEntry.Effect tryParseEffect(String effect) {
        PermissionEntry.Effect result = StringUtils.isEmpty(effect) ? PermissionEntry.Effect.allow : PermissionEntry.Effect.parse(effect);
        if (result == null) {
            throw new BadRequestException("effect not valid");
        }
        return result;
    }
}
