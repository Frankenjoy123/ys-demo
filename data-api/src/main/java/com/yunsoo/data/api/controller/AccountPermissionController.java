package com.yunsoo.data.api.controller;

import com.yunsoo.data.service.service.AccountPermissionService;
import com.yunsoo.data.service.service.PermissionService;
import com.yunsoo.data.service.service.contract.AccountPermission;
import com.yunsoo.data.service.service.contract.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KB on 3/13/2015.
 */
@RestController
@RequestMapping("/accountPermission")
public class AccountPermissionController {
    @Autowired
    private final AccountPermissionService apService;
    @Autowired
    private final PermissionService permissionService;

    @Autowired
    AccountPermissionController(AccountPermissionService apService,
                                PermissionService permissionService) {
        this.apService = apService;
        this.permissionService = permissionService;
    }

    @RequestMapping(value = "/{orgId}/{userId}", method = RequestMethod.GET)
    public Object getPermissions(@PathVariable(value = "orgId")Long orgId, @PathVariable(value = "userId")Long userId) {
        AccountPermission ap = apService.getPermissions(orgId, userId);
        Long permissions[] = ap.getPermissionGroups();
        return permissions;
    }

    @RequestMapping(value = "/{orgId}/{userId}/{resource}/{action}", method = RequestMethod.GET)
    public Boolean checkPermissions(@PathVariable(value = "orgId")Long orgId,
                                    @PathVariable(value = "userId")Long userId,
                                    @PathVariable(value = "resource")String resource,
                                    @PathVariable(value = "action")String action) {
        AccountPermission ap = apService.getPermissions(orgId, userId);
        Long permissions[] = ap.getPermissionGroups();
        Permission permission = permissionService.getPermission(resource, action);
        return checkPermission(permissions, permission.getGroupId(), permission.getOrder());
    }

    private Boolean checkPermission(Long[] permissions, Long groupId, Long order) {
        Long permission = permissions[groupId.intValue() - 1];
        if (permission == null) { return  false; }
        List<Integer> items = DecimalToBinaryArray(permission);
        Integer item = items.get(order.intValue() - 1);
        if (item == null) { return false; }
        return item == 1;
    }

    private List<Integer> DecimalToBinaryArray(Long decimal) {
        String binaryString = Long.toBinaryString(decimal);
        List<Integer> resp = new ArrayList<Integer>();
        for (int i=0; i<binaryString.length(); i++) {
            int num = Character.digit(binaryString.charAt(i), 10);
            resp.add(num);
        }
        return resp;
    }
}
