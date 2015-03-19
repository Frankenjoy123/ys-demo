package com.yunsoo.dataapi.controller;

import com.yunsoo.dataapi.dto.AccountDto;
import com.yunsoo.service.AccountPermissionService;
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
@RequestMapping("/device")
public class AccountPermissionController {
    @Autowired
    private final AccountPermissionService apService;

    @Autowired
    AccountPermissionController(AccountPermissionService apService) {
        this.apService = apService;
    }

    public Object getPermissoins(String token, long orgId) {
        return null;
    }

    public boolean hasPermission(String token, long orgId, String resource, String action) {
        return false;
    }

    public Object getPermissionMeta() {
        return null;
    }

    private Object getPermissionsBySet(long groupId, long set) {
        return null;
    }
}
