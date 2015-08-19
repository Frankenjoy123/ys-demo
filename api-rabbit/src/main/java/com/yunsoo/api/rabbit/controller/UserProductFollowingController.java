package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.domain.UserFollowDomain;
import com.yunsoo.api.rabbit.dto.basic.UserProductFollowing;
import com.yunsoo.api.rabbit.object.Constants;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.UserProductBaseFollowingObject;
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

import java.util.HashMap;
import java.util.List;

/**
 * Created by yan on 8/18/2015.
 */
@RestController
@RequestMapping("/userproduct/following")
public class UserProductFollowingController {
    @Autowired
    private RestClient dataAPIClient;
    @Autowired
    private UserFollowDomain userFollowDomain;
    @Autowired
    private UserDomain userDomain;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProductFollowingController.class);

    @RequestMapping(value = "/who/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'UserFollowing', 'userfollowing:read')")
    public List<UserProductFollowing> getFollowingProductsByUserId(@PathVariable(value = "id") String id,
                                                        @RequestParam(value = "index") Integer index,
                                                        @RequestParam(value = "size") Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<UserProductFollowing> userFollowingList = dataAPIClient.get("/userproduct/following/who/{0}?index={1}&size={2}", new ParameterizedTypeReference<List<UserProductFollowing>>() {
        }, id, index, size);

        for (UserProductFollowing userFollowing : userFollowingList) {
            ProductBaseObject object = null;
            try {
                object = dataAPIClient.get("productbase/{id}", ProductBaseObject.class, userFollowing.getProductId());
            } catch (NotFoundException ex) {
                LOGGER.error("get product base id error: " + userFollowing.getProductId(), ex);
            }
            if (object != null) {
                userFollowing.setProductName(object.getName());
                userFollowing.setProductDescription(object.getDescription());
            }
            else{
                //if the product is not exist, should remove the item from following list
                userFollowing.setUserId(null);
            }
        }

        userFollowingList.removeIf( userProductFollowing -> userProductFollowing.getUserId() == null);
        return userFollowingList;

    }

    @RequestMapping(value = "/who/{id}/product/{productid}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'UserFollowing', 'userfollowing:read')")
    public UserProductFollowing getFollowingRecord(@PathVariable(value = "id") String id,
                                                        @PathVariable(value = "productid") String productId) {
        UserProductFollowing userFollowing = dataAPIClient.get("/userproduct/following/who/{id}/product/{productid}", UserProductFollowing.class, id, productId);
        if (userFollowing == null) {
            throw new NotFoundException("找不到用户Follow的公司信息");
        }
        return userFollowing;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#userFollowing, 'authenticated')")
    public long userFollowingOrgs(@RequestBody UserProductFollowing userFollowing) {
        if (userFollowing == null) throw new BadRequestException("userFollowing 不能为空！");
        return userFollowDomain.ensureFollow(userFollowing, true);

    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#userFollowing, 'authenticated')")
    public void userUnfollowOrg(@RequestHeader(Constants.HttpHeaderName.ACCESS_TOKEN) String token, @RequestBody UserProductFollowing userFollowing) {
        if (userFollowing == null) throw new BadRequestException("userFollowing 不能为空！");

        UserProductFollowing existingUserFollowing = userFollowDomain.getUserProductFollowing(userFollowing.getUserId(), userFollowing.getProductId());
        if (existingUserFollowing == null) {
            throw new NotFoundException(40401, "找不到用户follow记录！");
        }

        if (!userDomain.validateToken(token, userFollowing.getUserId())) {
            throw new UnauthorizedException("不能删除其他用户的收藏信息！");
        }
        existingUserFollowing.setIsFollowing(false);
        dataAPIClient.patch("userproduct/following", existingUserFollowing); //just mark isFollowing as False.
    }


}
