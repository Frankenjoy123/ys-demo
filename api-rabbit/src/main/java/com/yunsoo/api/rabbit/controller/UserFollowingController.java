package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.dto.basic.UserFollowing;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private final String AUTH_HEADER_NAME = "YS_RABBIT_AUTH_TOKEN";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLikedProductController.class);

    @RequestMapping(value = "/who/{id}", method = RequestMethod.GET)
    public List<UserFollowing> getFollowingOrgsByUserId(@PathVariable(value = "id") String id,
                                                           @RequestParam(value = "index") Integer index,
                                                           @RequestParam(value = "size") Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        try {
            List<UserFollowing> userFollowingList = dataAPIClient.get("/user/following/who/{0}?index={1}&size={2}", List.class, id, index, size);
            if (userFollowingList == null || userFollowingList.size() == 0) {
                throw new NotFoundException(40401, "User following list not found for userid = " + id);
            }
            return userFollowingList;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "User following list not found for useid = " + id);
        }

    }

    @RequestMapping(value = "/link", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long userFollowingOrgs(@RequestBody UserFollowing userFollowing) {
        long id = dataAPIClient.post("/user/following/link", userFollowing, Long.class);
        return id;
    }

    @RequestMapping(value = "/unlink/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userUnfollowOrg(@RequestHeader(AUTH_HEADER_NAME) String token, @PathVariable(value = "id") Long Id) {

        if (Id == null || Id < 0) {
            throw new BadRequestException("Id不应小于0！");
        }
        UserFollowing userFollowing = dataAPIClient.get("/user/following/{id}", UserFollowing.class, Id);
        if (!userDomain.validateToken(token, userFollowing.getUserId())) {
            throw new UnauthorizedException("不能删除其他用户的收藏信息！");
        }
        dataAPIClient.delete("user/following/unlink/{0}", Id);
    }
}
