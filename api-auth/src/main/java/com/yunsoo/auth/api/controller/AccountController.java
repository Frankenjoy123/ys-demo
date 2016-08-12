package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.api.security.authorization.AuthorizationService;
import com.yunsoo.auth.api.security.permission.PermissionEntryService;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.api.util.PageUtils;
import com.yunsoo.auth.dto.*;
import com.yunsoo.auth.service.AccountLoginLogService;
import com.yunsoo.auth.service.AccountService;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-07
 * Descriptions:
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private PermissionEntryService permissionEntryService;

    @Autowired
    private AccountLoginLogService accountLoginLogService;

    //region account

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Account getById(@PathVariable("id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        Account account = findAccountById(accountId);
        if (!AuthUtils.isMe(accountId) && !Constants.SYSTEM_ACCOUNT_ID.equals(accountId)) {
            AuthUtils.checkPermission(account.getOrgId(), "account", "read");
        }
        return account;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'account:read')")
    public List<Account> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                     @RequestParam(value = "status_code", required = false) String statusCode,
                                     @RequestParam(value = "search_text", required = false) String searchText,
                                     @RequestParam(value = "created_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeGE,
                                     @RequestParam(value = "created_datetime_le", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeLE,
                                     @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                     Pageable pageable,
                                     HttpServletResponse response) {
        orgId = AuthUtils.fixOrgId(orgId); //auto fix current

        Page<Account> accountPage = accountService.search(orgId, statusCode, searchText, createdDateTimeGE, createdDateTimeLE, pageable);

        return PageUtils.response(response, accountPage, pageable != null);
    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'account:read')")
    public long count(@RequestParam(value = "org_id", required = false) String orgId,
                      @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn) {
        orgId = AuthUtils.fixOrgId(orgId); //auto fix current

        return accountService.count(orgId, statusCodeIn);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#request, 'account:create')")
    public Account create(@RequestBody @Valid AccountCreationRequest request) {
        return accountService.create(request);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdateAccount(@PathVariable("id") String accountId,
                                   @RequestBody Account account) {
        accountId = AuthUtils.fixAccountId(accountId);
        rejectUpdateOnSystemAccount(accountId);
        Account accountOld = findAccountById(accountId);
        if (!AuthUtils.isMe(accountId)) {
            AuthUtils.checkPermission(accountOld.getOrgId(), "account", "write");
        }
        account.setId(accountId);
        accountService.patchUpdate(account);
    }

    @RequestMapping(value = "{id}/disable", method = RequestMethod.POST)
    public void disableAccount(@PathVariable("id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        rejectUpdateOnSystemAccount(accountId);
        Account account = findAccountById(accountId);

        AuthUtils.checkPermission(account.getOrgId(), "account", "write");

        accountService.updateStatus(accountId, Constants.AccountStatus.DISABLED);
    }

    @RequestMapping(value = "{id}/enable", method = RequestMethod.POST)
    public void enableAccount(@PathVariable("id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        rejectUpdateOnSystemAccount(accountId);
        Account account = findAccountById(accountId);

        AuthUtils.checkPermission(account.getOrgId(), "account", "write");

        accountService.updateStatus(accountId, Constants.AccountStatus.AVAILABLE);
    }

    @RequestMapping(value = "{id}/password", method = RequestMethod.PUT)
    public void changePassword(@PathVariable("id") String accountId, @RequestBody String newPassword) {
        if (StringUtils.isEmpty(newPassword)) {
            throw new BadRequestException("password must not be null or empty");
        }
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        rejectUpdateOnSystemAccount(accountId);
        Account account = findAccountById(accountId);

        AuthUtils.checkPermission(account.getOrgId(), "account", "write");

        accountService.updatePassword(accountId, newPassword);
    }

    @RequestMapping(value = "current/password", method = RequestMethod.POST)
    public void changePassword(@RequestBody @Valid AccountUpdatePasswordRequest request) {
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        rejectUpdateOnSystemAccount(currentAccountId);
        Account account = findAccountById(currentAccountId);

        if (!accountService.validatePassword(request.getOldPassword(), account.getHashSalt(), account.getPassword())) {
            throw new UnprocessableEntityException("current password invalid");
        }

        accountService.updatePassword(currentAccountId, request.getNewPassword());
    }

    //endregion


    //region permission

    /**
     * get all permissions of the account.
     */
    @RequestMapping(value = "{account_id}/permission", method = RequestMethod.GET)
    public List<PermissionEntry> getAllPermissionByAccountId(@PathVariable("account_id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current

        if (AuthUtils.isMe(accountId)) {
            return AuthUtils.getAuthentication().getPermissionEntries().stream().map(PermissionEntry::new).collect(Collectors.toList());
        } else {
            Account account = findAccountById(accountId);
            String orgId = account.getOrgId();
            AuthUtils.checkPermission(orgId, "permission_allocation", "read");

            return permissionEntryService.getExpendedPermissionEntriesByAccountId(accountId).stream()
                    .map(p -> {
                        //fix orgRestriction
                        p.setRestriction(authorizationService.fixOrgRestriction(p.getRestriction(), orgId));
                        return new PermissionEntry(p);
                    })
                    .collect(Collectors.toList());
        }
    }

    //endregion

    @RequestMapping(value = "{account_id}/loginLog", method = RequestMethod.GET)
    public List<AccountLoginLog> getAccountLoginLogByAccountId(@PathVariable("account_id") String accountId,
                                                               @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                                               Pageable pageable,
                                                               HttpServletResponse response) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current

        if (!AuthUtils.isMe(accountId)) {
            Account Account = findAccountById(accountId);
            AuthUtils.checkPermission(Account.getOrgId(), "login_log", "read");
        }
        Page<AccountLoginLog> page = accountLoginLogService.getByAccountId(accountId, pageable);

        return PageUtils.response(response, page, pageable != null);
    }

    private Account findAccountById(String accountId) {
        Account account = accountService.getById(accountId);
        if (account == null) {
            throw new NotFoundException("account not found");
        }
        return account;
    }

    private void rejectUpdateOnSystemAccount(String accountId) {
        if (Constants.SYSTEM_ACCOUNT_ID.equals(accountId)) {
            throw new UnauthorizedException("system account can not be modified");
        }
    }

}
