package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.Group;
import com.yunsoo.auth.dto.PermissionAllocation;
import com.yunsoo.auth.dto.PermissionRegion;
import com.yunsoo.auth.service.AccountService;
import com.yunsoo.auth.service.GroupService;
import com.yunsoo.auth.service.PermissionAllocationService;
import com.yunsoo.auth.service.PermissionRegionService;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import com.yunsoo.common.web.security.permission.PermissionEntry;
import com.yunsoo.common.web.security.permission.expression.PermissionExpression;
import com.yunsoo.common.web.security.permission.expression.PrincipalExpression;
import com.yunsoo.common.web.security.permission.expression.PrincipalExpression.AccountPrincipalExpression;
import com.yunsoo.common.web.security.permission.expression.PrincipalExpression.GroupPrincipalExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression.CollectionRestrictionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression.OrgRestrictionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression.RegionRestrictionExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private PermissionRegionService permissionRegionService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<PermissionAllocation> getByPrincipal(@RequestParam(value = "principal") String principal) {
        PrincipalExpression principalExp = PrincipalExpression.parse(principal);
        if (principalExp == null) {
            return new ArrayList<>();
        }
        checkPermission(principalExp, "read");
        return permissionAllocationService.getPermissionAllocationsByPrincipal(principalExp);
    }

    @RequestMapping(value = "allocate", method = RequestMethod.POST)
    public List<PermissionAllocation> allocate(@RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
                                               @RequestBody PermissionAllocation permissionAllocation) {
        PrincipalExpression principalExp = PrincipalExpression.parse(permissionAllocation.getPrincipal());
        RestrictionExpression restrictionExp = RestrictionExpression.parse(permissionAllocation.getRestriction());
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = parseEffect(permissionAllocation.getEffect());
        if (restrictionExp == null) {
            restrictionExp = new OrgRestrictionExpression(orgId);
        }
        restrictionExp = expendRestrictionExpression(restrictionExp, orgId); //expend default region
        tryAllocatePermission(principalExp, restrictionExp, permissionExp, effect);
        return permissionAllocationService.getPermissionAllocationsByPrincipal(principalExp);
    }


    @RequestMapping(value = "deallocate", method = RequestMethod.POST)
    public List<PermissionAllocation> deallocate(@RequestParam(value = "org_id", required = false, defaultValue = "current") String orgId,
                                                 @RequestBody PermissionAllocation permissionAllocation) {
        PrincipalExpression principalExp = PrincipalExpression.parse(permissionAllocation.getPrincipal());
        RestrictionExpression restrictionExp = RestrictionExpression.parse(permissionAllocation.getRestriction());
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = parseEffect(permissionAllocation.getEffect());
        if (restrictionExp == null) {
            restrictionExp = new OrgRestrictionExpression(orgId);
        }
        restrictionExp = expendRestrictionExpression(restrictionExp, orgId); //expend default region
        tryDeallocatePermission(principalExp, restrictionExp, permissionExp, effect);
        return permissionAllocationService.getPermissionAllocationsByPrincipal(principalExp);
    }


    //region account

    @RequestMapping(value = "account/{accountId}", method = RequestMethod.GET)
    public List<PermissionAllocation> getByAccount(@PathVariable("accountId") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId);
        AuthUtils.checkPermission(findAccountById(accountId).getOrgId(), "permission_allocation", "read");
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
        findAccountById(accountId);

        PrincipalExpression principalExp = new AccountPrincipalExpression(accountId);
        RestrictionExpression restrictionExp = RestrictionExpression.parse(permissionAllocation.getRestriction());
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = parseEffect(permissionAllocation.getEffect());
        if (restrictionExp == null) {
            restrictionExp = new OrgRestrictionExpression(orgId);
        }
        restrictionExp = expendRestrictionExpression(restrictionExp, orgId); //expend default region
        tryAllocatePermission(principalExp, restrictionExp, permissionExp, effect);
        return permissionAllocationService.getPermissionAllocationsByPrincipal(principalExp);
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
        findAccountById(accountId);

        PrincipalExpression principalExp = new AccountPrincipalExpression(accountId);
        RestrictionExpression restrictionExp = RestrictionExpression.parse(permissionAllocation.getRestriction());
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = parseEffect(permissionAllocation.getEffect());
        if (restrictionExp == null) {
            restrictionExp = new OrgRestrictionExpression(orgId);
        }
        restrictionExp = expendRestrictionExpression(restrictionExp, orgId); //expend default region
        tryDeallocatePermission(principalExp, restrictionExp, permissionExp, effect);
        return permissionAllocationService.getPermissionAllocationsByPrincipal(principalExp);
    }

    //endregion

    //region group

    @RequestMapping(value = "group/{groupId}", method = RequestMethod.GET)
    public List<PermissionAllocation> getByGroup(@PathVariable("groupId") String groupId) {
        AuthUtils.checkPermission(findGroupById(groupId).getOrgId(), "permission_allocation", "read");
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
        findGroupById(groupId);

        PrincipalExpression principalExp = new GroupPrincipalExpression(groupId);
        RestrictionExpression restrictionExp = RestrictionExpression.parse(permissionAllocation.getRestriction());
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = parseEffect(permissionAllocation.getEffect());
        if (restrictionExp == null) {
            restrictionExp = new OrgRestrictionExpression(orgId);
        }
        restrictionExp = expendRestrictionExpression(restrictionExp, orgId); //expend default region
        tryAllocatePermission(principalExp, restrictionExp, permissionExp, effect);
        return permissionAllocationService.getPermissionAllocationsByPrincipal(principalExp);
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
        findGroupById(groupId);

        PrincipalExpression principalExp = new GroupPrincipalExpression(groupId);
        RestrictionExpression restrictionExp = RestrictionExpression.parse(permissionAllocation.getRestriction());
        PermissionExpression permissionExp = PermissionExpression.parse(permissionAllocation.getPermission());
        PermissionEntry.Effect effect = parseEffect(permissionAllocation.getEffect());
        if (restrictionExp == null) {
            restrictionExp = new OrgRestrictionExpression(orgId);
        }
        restrictionExp = expendRestrictionExpression(restrictionExp, orgId); //expend default region
        tryDeallocatePermission(principalExp, restrictionExp, permissionExp, effect);
        return permissionAllocationService.getPermissionAllocationsByPrincipal(principalExp);
    }

    //endregion

    //region private methods

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

    private <T extends PrincipalExpression> void checkPermission(T principal, String action) {
        String orgId = null;
        if (principal instanceof AccountPrincipalExpression) {
            if (Constants.SYSTEM_ACCOUNT_ID.equals(principal.getValue())) {
                throw new UnauthorizedException("system account can not be modified");
            }
            orgId = findAccountById(principal.getValue()).getOrgId();
        } else if (principal instanceof GroupPrincipalExpression) {
            orgId = findGroupById(principal.getValue()).getOrgId();
        }
        if (orgId == null) {
            throw new ForbiddenException("access denied");
        }
        AuthUtils.checkPermission(orgId, "permission_allocation", action);
    }

    private <T extends RestrictionExpression> void checkPermission(T restriction, String action) {
        String orgId = null;
        if (restriction instanceof OrgRestrictionExpression) {
            orgId = restriction.getValue();
        } else if (restriction instanceof RegionRestrictionExpression) {
            PermissionRegion region = permissionRegionService.getById(restriction.getValue());
            orgId = region != null ? region.getOrgId() : null;
        } else if (restriction instanceof CollectionRestrictionExpression) {
            for (RestrictionExpression r : ((CollectionRestrictionExpression) restriction).distinct().getExpressions()) {
                checkPermission(r, action);
            }
            return;
        }
        if (orgId == null) {
            throw new ForbiddenException("access denied");
        }
        AuthUtils.checkPermission(orgId, "permission_allocation", action);
    }


    private void tryAllocatePermission(PrincipalExpression principalExp,
                                       RestrictionExpression restrictionExp,
                                       PermissionExpression permissionExp,
                                       PermissionEntry.Effect effect) {
        if (principalExp == null) {
            throw new BadRequestException("principal not valid");
        }
        if (restrictionExp == null) {
            throw new BadRequestException("restriction not valid");
        }
        if (permissionExp == null) {
            throw new BadRequestException("permission not valid");
        }
        if (effect == null) {
            throw new BadRequestException("effect not valid");
        }
        checkPermission(principalExp, "write");
        checkPermission(restrictionExp, "write");

        permissionAllocationService.allocatePermission(principalExp, restrictionExp, permissionExp, effect);
    }

    private void tryDeallocatePermission(PrincipalExpression principalExp,
                                         RestrictionExpression restrictionExp,
                                         PermissionExpression permissionExp,
                                         PermissionEntry.Effect effect) {
        if (principalExp == null) {
            throw new BadRequestException("principal not valid");
        }
        if (restrictionExp == null) {
            throw new BadRequestException("restriction not valid");
        }
        if (permissionExp == null) {
            throw new BadRequestException("permission not valid");
        }
        if (effect == null) {
            throw new BadRequestException("effect not valid");
        }
        checkPermission(principalExp, "write");
        checkPermission(restrictionExp, "write");

        permissionAllocationService.deallocatePermission(principalExp, restrictionExp, permissionExp, effect);
    }

    private RestrictionExpression expendRestrictionExpression(RestrictionExpression restrictionExp, String orgId) {
        if (restrictionExp != null) {
            if (restrictionExp instanceof CollectionRestrictionExpression) {
                return RestrictionExpression.collect(((CollectionRestrictionExpression) restrictionExp)
                        .getExpressions()
                        .stream()
                        .map(r -> expendRestrictionExpression(r, orgId))
                        .collect(Collectors.toList()));
            } else if (restrictionExp instanceof RegionRestrictionExpression && Constants.PermissionRegionType.DEFAULT.equals(restrictionExp.getValue())) {
                PermissionRegion region = permissionRegionService.getOrCreateDefaultPermissionRegion(AuthUtils.fixOrgId(orgId));
                return new RegionRestrictionExpression(region.getId());
            }
        }
        return restrictionExp;
    }

    private PermissionEntry.Effect parseEffect(String effect) {
        return StringUtils.isEmpty(effect) ? PermissionEntry.Effect.allow : PermissionEntry.Effect.parse(effect);
    }

    //endregion

}
