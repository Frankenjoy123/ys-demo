package com.yunsoo.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

;

/**
 * Created by:   Lijian
 * Created on:   2016-03-31
 * Descriptions:
 */
@RestController
@RequestMapping("/permissionAllocation")
public class PermissionAllocationController {
//
//    @Autowired
//    private AccountDomain accountDomain;
//
//    @Autowired
//    private GroupDomain groupDomain;

    //region account

//    @RequestMapping(value = "account/{accountId}", method = RequestMethod.GET)
//    public List<PermissionAllocation> getByAccount(@PathVariable("accountId") String accountId) {
//        accountId = AuthUtils.fixAccountId(accountId);
//        AccountObject accountObject = findAccountById(accountId);
//        AuthUtils.checkPermission(accountObject.getOrgId(), "permission_allocation", "read");
//        return getPermissionAllocationsByAccountId(accountId);
//    }
//
//    /**
//     * @param accountId            account principal
//     * @param orgId                org restriction
//     * @param permissionAllocation permission and effect
//     */
//    @RequestMapping(value = "account/{accountId}/allocate", method = RequestMethod.POST)
//    public List<PermissionAllocation> allocateByAccount(@PathVariable("accountId") String accountId,
//                                                        @RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
//                                                        @RequestBody PermissionAllocation permissionAllocation) {
//        accountId = AuthUtils.fixAccountId(accountId);
//        AuthUtils.checkPermission(orgId, "permission_allocation", "write");
//        findAccountById(accountId);
//
//        PrincipalExpression principalExp = new PrincipalExpression.AccountPrincipalExpression(accountId);
//        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
//        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
//        PermissionEntry.Effect effect = getPermissionEntryEffect(permissionAllocation.getEffect());
//        if (permissionExp == null) {
//            throw new BadRequestException("permission expression not valid");
//        }
//        permissionAllocationDomain.allocatePermission(principalExp, restrictionExp, permissionExp, effect);
//        return getPermissionAllocationsByAccountId(accountId);
//    }
//
//    /**
//     * @param accountId            account principal
//     * @param orgId                org restriction
//     * @param permissionAllocation permission and effect
//     */
//    @RequestMapping(value = "account/{accountId}/deallocate", method = RequestMethod.POST)
//    public List<PermissionAllocation> deallocateByAccount(@PathVariable("accountId") String accountId,
//                                                          @RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
//                                                          @RequestBody PermissionAllocation permissionAllocation) {
//        accountId = AuthUtils.fixAccountId(accountId);
//        AuthUtils.checkPermission(orgId, "permission_allocation", "write");
//        findAccountById(accountId);
//
//        PrincipalExpression principalExp = new PrincipalExpression.AccountPrincipalExpression(accountId);
//        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
//        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
//        PermissionEntry.Effect effect = getPermissionEntryEffect(permissionAllocation.getEffect());
//        if (permissionExp == null) {
//            throw new BadRequestException("permission expression not valid");
//        }
//        permissionAllocationDomain.deallocatePermission(principalExp, restrictionExp, permissionExp, effect);
//        return getPermissionAllocationsByAccountId(accountId);
//    }
//
//    //endregion
//
//    //region group
//
//    @RequestMapping(value = "group/{groupId}", method = RequestMethod.GET)
//    public List<PermissionAllocation> getByGroup(@PathVariable("groupId") String groupId) {
//        GroupObject groupObject = findGroupById(groupId);
//        AuthUtils.checkPermission(groupObject.getOrgId(), "permission_allocation", "read");
//        return getPermissionAllocationsByGroupId(groupId);
//    }
//
//    /**
//     * @param groupId              group principal
//     * @param orgId                org restriction
//     * @param permissionAllocation permission and effect
//     */
//    @RequestMapping(value = "group/{groupId}/allocate", method = RequestMethod.POST)
//    public List<PermissionAllocation> allocateByGroup(@PathVariable("groupId") String groupId,
//                                                      @RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
//                                                      @RequestBody PermissionAllocation permissionAllocation) {
//        AuthUtils.checkPermission(orgId, "permission_allocation", "write");
//        findGroupById(groupId);
//
//        PrincipalExpression principalExp = new PrincipalExpression.GroupPrincipalExpression(groupId);
//        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
//        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
//        PermissionEntry.Effect effect = getPermissionEntryEffect(permissionAllocation.getEffect());
//        if (permissionExp == null) {
//            throw new BadRequestException("permission expression not valid");
//        }
//        permissionAllocationDomain.allocatePermission(principalExp, restrictionExp, permissionExp, effect);
//        return getPermissionAllocationsByGroupId(groupId);
//    }
//
//    /**
//     * @param groupId              group principal
//     * @param orgId                org restriction
//     * @param permissionAllocation permission and effect
//     */
//    @RequestMapping(value = "group/{groupId}/deallocate", method = RequestMethod.POST)
//    public List<PermissionAllocation> deallocateByGroup(@PathVariable("groupId") String groupId,
//                                                        @RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
//                                                        @RequestBody PermissionAllocation permissionAllocation) {
//        AuthUtils.checkPermission(orgId, "permission_allocation", "write");
//        findGroupById(groupId);
//
//        PrincipalExpression principalExp = new PrincipalExpression.GroupPrincipalExpression(groupId);
//        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
//        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
//        PermissionEntry.Effect effect = getPermissionEntryEffect(permissionAllocation.getEffect());
//        if (permissionExp == null) {
//            throw new BadRequestException("permission expression not valid");
//        }
//        permissionAllocationDomain.deallocatePermission(principalExp, restrictionExp, permissionExp, effect);
//        return getPermissionAllocationsByGroupId(groupId);
//    }
//
//    //endregion
//
//
//    private AccountObject findAccountById(String id) {
//        AccountObject accountObject = accountDomain.getById(id);
//        if (accountObject == null) {
//            throw new NotFoundException("account not found");
//        }
//        return accountObject;
//    }
//
//    private GroupObject findGroupById(String id) {
//        GroupObject groupObject = groupDomain.getById(id);
//        if (groupObject == null) {
//            throw new NotFoundException("group not found");
//        }
//        return groupObject;
//    }
//
//    private List<PermissionAllocation> getPermissionAllocationsByAccountId(String accountId) {
//        return permissionAllocationDomain.getPermissionAllocationsByAccountId(accountId).stream()
//                .map(PermissionAllocation::new)
//                .collect(Collectors.toList());
//    }
//
//    private List<PermissionAllocation> getPermissionAllocationsByGroupId(String groupId) {
//        return permissionAllocationDomain.getPermissionAllocationsByGroupId(groupId).stream()
//                .map(PermissionAllocation::new)
//                .collect(Collectors.toList());
//    }
//
//    private PermissionEntry.Effect getPermissionEntryEffect(PermissionAllocation.Effect effect) {
//        return effect == null || effect.equals(PermissionAllocation.Effect.allow)
//                ? PermissionEntry.Effect.allow
//                : PermissionEntry.Effect.deny;
//    }

}
