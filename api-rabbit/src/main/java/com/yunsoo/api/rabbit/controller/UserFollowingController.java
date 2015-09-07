package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.UserFollowDomain;
import com.yunsoo.api.rabbit.dto.UserOrganizationFollowing;
import com.yunsoo.api.rabbit.dto.UserProductFollowing;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.web.client.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    private UserFollowDomain userFollowDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    //organization

    @RequestMapping(value = "organization", method = RequestMethod.GET)
    public List<UserOrganizationFollowing> getUserOrganizationFollowing(Pageable pageable,
                                                                        HttpServletResponse response) {
        String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();

        Page<UserOrganizationFollowing> followingPage = userFollowDomain.getUserOrganizationFollowingsByUserId(userId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", followingPage.toContentRange());
        }
        return followingPage.getContent();
    }

    @RequestMapping(value = "organization", method = RequestMethod.POST)
    public UserOrganizationFollowing followOrganization(@RequestParam(value = "org_id", required = true) String orgId) {
        String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        return new UserOrganizationFollowing(userFollowDomain.ensureUserOrganizationFollowing(userId, orgId));
    }

    @RequestMapping(value = "organization", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollowOrganization(@RequestParam(value = "org_id", required = true) String orgId) {
        String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        userFollowDomain.deleteUserOrganizationFollowing(userId, orgId);
    }


    //product

    @RequestMapping(value = "product", method = RequestMethod.GET)
    public List<UserProductFollowing> getUserProductFollowing(Pageable pageable,
                                                              HttpServletResponse response) {
        String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();

        Page<UserProductFollowing> followingPage = userFollowDomain.getUserProductFollowingsByUserId(userId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", followingPage.toContentRange());
        }
        return followingPage.getContent();
    }

    @RequestMapping(value = "product", method = RequestMethod.POST)
    public UserProductFollowing followProduct(@RequestParam(value = "product_base_id", required = true) String productBaseId) {
        String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        return new UserProductFollowing(userFollowDomain.ensureUserProductFollowing(userId, productBaseId));
    }

    @RequestMapping(value = "product", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollowProduct(@RequestParam(value = "product_base_id", required = true) String productBaseId) {
        String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        userFollowDomain.deleteUserProductFollowing(userId, productBaseId);
    }

}
