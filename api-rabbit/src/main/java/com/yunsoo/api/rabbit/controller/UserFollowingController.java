package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.dto.basic.UserOrganizationFollowing;
import com.yunsoo.api.rabbit.dto.basic.UserProductFollowing;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/8/28
 * Descriptions:
 */
@RestController
@RequestMapping("/userfollowing")
public class UserFollowingController {


    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    //organization
    @RequestMapping(value = "organization", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'UserFollowing', 'userfollowing:read')")
    public List<UserOrganizationFollowing> getUserOrganizationFollowing(Pageable pageable,
                                                                        HttpServletResponse response) {
        String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        //todo

        return new ArrayList<>();
    }


    //product

    @RequestMapping(value = "product", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'UserFollowing', 'userfollowing:read')")
    public List<UserProductFollowing> getUserProductFollowing(Pageable pageable,
                                                              HttpServletResponse response) {
        String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        //todo

        return new ArrayList<>();
    }
}
