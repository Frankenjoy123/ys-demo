package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.domain.UserFollowDomain;
import com.yunsoo.api.rabbit.dto.basic.UserOrganizationFollowing;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by yan on 8/20/2015.
 */
@RestController
@RequestMapping("/userorganization/following")
public class UserOrganizationFollowingController {

    @Autowired
    private UserDomain userDomain;
    @Autowired
    private UserFollowDomain userFollowDomain;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLikedProductController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'UserFollowing', 'userfollowing:read')")
    public List<UserOrganizationFollowing> getByFilter(@RequestParam(value = "userid") String id,
                                                                    Pageable pageable,   HttpServletResponse response) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");

        Page<UserOrganizationFollowing> followingPage = userFollowDomain.getUserOrganizationFollowingsByUserId(id, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", followingPage.toContentRange());
        }
        return followingPage.getContent();
    }

    @RequestMapping(value = "/who/{id}/org/{orgid}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'UserFollowing', 'userfollowing:read')")
    public UserOrganizationFollowing getFollowingRecord(@PathVariable(value = "id") String id,
                                                        @PathVariable(value = "orgid") String orgId) {


        UserOrganizationFollowing userFollowing = userFollowDomain.getUserOrganizationFollowing(id, orgId);
        if (userFollowing == null) {
            throw new NotFoundException("找不到用户Follow的公司信息");
        }
        return userFollowing;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#userFollowing, 'authenticated')")
    public List<UserOrganizationFollowing> userFollowingOrgs(@RequestBody UserOrganizationFollowing userFollowing) {
        if (userFollowing == null)
            throw new BadRequestException("userFollowing 不能为空");

        userFollowDomain.ensureFollow(userFollowing);
        //return all the following organization list according to api requirement
        return userFollowDomain.getUserOrganizationFollowingsByUserId(userFollowing.getUserId(), null).getContent();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#userFollowing, 'authenticated')")
    public void userUnfollowOrg(@PathVariable(value = "id") String id) {
        if (id == null) throw new BadRequestException("userOrganizationFollowing id 不能为空");
        if (!userDomain.validateUser(id)) {
            throw new UnauthorizedException("不能删除其他用户的收藏信息！");
        }

        UserOrganizationFollowing following = userFollowDomain.getUserOrganizationFollowing(id);

        userFollowDomain.deleteUserOrganizationFollowing(id);

        //return all the following organization list according to api requirement
        return userFollowDomain.getUserOrganizationFollowingsByUserId(following.getUserId(), null).getContent();

    }
}
