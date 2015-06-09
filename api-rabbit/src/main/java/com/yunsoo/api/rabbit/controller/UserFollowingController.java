package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.domain.UserFollowDomain;
import com.yunsoo.api.rabbit.dto.basic.UserFollowing;
import com.yunsoo.api.rabbit.object.Constants;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/4/21.
 */
@RestController
@RequestMapping("/user/following")
public class UserFollowingController {
    @Autowired
    private RestClient dataAPIClient;
    @Autowired
    private UserDomain userDomain;
    @Autowired
    private UserFollowDomain userFollowDomain;
    //    private final String AUTH_HEADER_NAME = "YS_RABBIT_AUTH_TOKEN";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLikedProductController.class);

    @RequestMapping(value = "/who/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'UserFollowing', 'userfollowing:read')")
    public List<UserFollowing> getFollowingOrgsByUserId(@PathVariable(value = "id") String id,
                                                        @RequestParam(value = "index") Integer index,
                                                        @RequestParam(value = "size") Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        try {
            List<UserFollowing> userFollowingList = dataAPIClient.get("/user/following/who/{0}?index={1}&size={2}", new ParameterizedTypeReference<List<UserFollowing>>() {
            }, id, index, size);
            if (userFollowingList == null || userFollowingList.size() == 0) {
                throw new NotFoundException(40401, "User following list not found for userid = " + id);
            }
            return userFollowingList;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "User following list not found for useid = " + id);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#userFollowing, 'authenticated')")
    public long userFollowingOrgs(@RequestBody UserFollowing userFollowing) {
        if (userFollowing == null) throw new BadRequestException("userFollowing 不能为空！");
        return userFollowDomain.ensureFollow(userFollowing, true);
//        UserFollowing existingUserFollowing = dataAPIClient.get("/user/following/who/{id}/org/{orgid}", UserFollowing.class, userFollowing.getUserId(), userFollowing.getOrganizationId());
//        if (existingUserFollowing != null) {
//            return existingUserFollowing.getId();
//        } else {
//            Long id = dataAPIClient.post("/user/following/link", userFollowing, long.class);
//            return id;
//        }
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasPermission(#userFollowing, 'authenticated')")
    public void userUnfollowOrg(@RequestHeader(Constants.HttpHeaderName.ACCESS_TOKEN) String token, @RequestBody UserFollowing userFollowing) {
        if (userFollowing == null) throw new BadRequestException("userFollowing 不能为空！");

        UserFollowing existingUserFollowing = dataAPIClient.get("/user/following/who/{id}/org/{orgid}", UserFollowing.class,
                userFollowing.getUserId(), userFollowing.getOrganizationId());
        if (existingUserFollowing == null) {
            throw new NotFoundException(40401, "找不到用户follow记录！");
        }
//        UserFollowing userFollowing = dataAPIClient.get("/user/following/{id}", UserFollowing.class, Id);
        if (!userDomain.validateToken(token, userFollowing.getUserId())) {
            throw new UnauthorizedException("不能删除其他用户的收藏信息！");
        }
        existingUserFollowing.setIsFollowing(false);
        dataAPIClient.patch("user/following", existingUserFollowing); //just mark isFollowing as False.
    }
}
