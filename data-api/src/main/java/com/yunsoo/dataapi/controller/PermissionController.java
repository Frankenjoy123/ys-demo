package com.yunsoo.dataapi.controller;

import com.yunsoo.dataapi.dto.AccountDto;
import com.yunsoo.dataapi.dto.PermissionDto;
import com.yunsoo.service.AccountService;
import com.yunsoo.service.PermissionService;
import com.yunsoo.service.contract.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by KB on 3/13/2015.
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private final PermissionService service;

    @Autowired
    PermissionController(PermissionService service) {
        this.service = service;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<PermissionDto>> getList() {
        List<Permission> lists = service.getPermissions();
        return new ResponseEntity<List<PermissionDto>>(PermissionDto.FromPermissionList(lists), HttpStatus.OK);
    }
}
