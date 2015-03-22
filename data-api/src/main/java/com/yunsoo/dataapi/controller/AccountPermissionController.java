package com.yunsoo.dataapi.controller;

import com.yunsoo.dataapi.dto.AccountDto;
import com.yunsoo.service.AccountPermissionService;
import com.yunsoo.service.PermissionService;
import com.yunsoo.service.contract.AccountPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by KB on 3/13/2015.
 */
@RestController
@RequestMapping("/permission")
public class AccountPermissionController {
    @Autowired
    private final AccountPermissionService service;

    @Autowired
    AccountPermissionController(AccountPermissionService service) {
        this.service = service;
    }

    @RequestMapping(value = "/{orgId}/{userId}", method = RequestMethod.GET)
    public Object getPermissions(@PathVariable(value = "orgId")Long orgId, @PathVariable(value = "userId")Long userId) {
        AccountPermission ap = service.getPermissions(orgId, userId);
        Long permissions[] = ap.getPermissionGroups();
        return permissions;
    }
}
