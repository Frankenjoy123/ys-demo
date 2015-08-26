package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.domain.UserFollowDomain;
import com.yunsoo.api.rabbit.dto.basic.UserOrganizationFollowing;
import com.yunsoo.common.data.object.OrganizationObject;
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
 * Created by Zhe on 2015/4/21.
 */
@Deprecated
@RestController
@RequestMapping("/user/following")
public class UserOrganizationFollowingOldController {
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
    public List<UserOrganizationFollowing> getFollowingOrgsByUserId(@PathVariable(value = "id") String id,
                                                        @RequestParam(value = "index", required = false) Integer index,
                                                        @RequestParam(value = "size", required = false) Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");


        List<UserOrganizationFollowing> userFollowingList = dataAPIClient.get("/userorganization/following/who/{0}?index={1}&size={2}", new ParameterizedTypeReference<List<UserOrganizationFollowing>>() {
        }, id, index, size);

        //fill organization Name
        HashMap<String, OrganizationObject> orgMap = new HashMap<>();
        for (UserOrganizationFollowing userFollowing : userFollowingList) {
            if (!orgMap.containsKey(userFollowing.getOrgId())) {
                OrganizationObject object = dataAPIClient.get("organization/{id}", OrganizationObject.class, userFollowing.getOrgId());
                if (object != null) {
                    orgMap.put(userFollowing.getOrgId(), object);
                    userFollowing.setOrgName(object.getName());
                    userFollowing.setOrgDescription(object.getDescription());
                } else {
                    userFollowing.setOrgName(orgMap.get(userFollowing.getOrgId()).getName());
                    userFollowing.setOrgDescription(orgMap.get(userFollowing.getOrgId()).getDescription());
                }
            }
        }

        return userFollowingList;

    }

    @RequestMapping(value = "/who/{id}/org/{orgid}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'UserFollowing', 'userfollowing:read')")
    public UserOrganizationFollowing getFollowingRecord(@PathVariable(value = "id") String id,
                                            @PathVariable(value = "orgid") String orgId) {
        UserOrganizationFollowing userFollowing = dataAPIClient.get("/userorganization/following/who/{id}/org/{orgid}", UserOrganizationFollowing.class, id, orgId);
        if (userFollowing == null) {
            throw new NotFoundException("找不到用户Follow的公司信息");
        }
        return userFollowing;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#userFollowing, 'authenticated')")
    public String userFollowingOrgs(@RequestBody UserOrganizationFollowing userFollowing) {
        if (userFollowing == null) throw new BadRequestException("userFollowing 不能为空！");
        return userFollowDomain.ensureFollow(userFollowing);
//        UserOrganizationFollowing existingUserFollowing = dataAPIClient.get("/useruserorganization/following/who/{id}/org/{orgid}", UserOrganizationFollowing.class, userFollowing.getUserId(), userFollowing.getOrgId());
//        if (existingUserFollowing != null) {
//            return existingUserFollowing.getId();
//        } else {
//            Long id = dataAPIClient.post("/userorganization/following/link", userFollowing, long.class);
//            return id;
//        }
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#userFollowing, 'authenticated')")
    public void userUnfollowOrg(@RequestBody UserOrganizationFollowing userFollowing) {
        if (userFollowing == null) throw new BadRequestException("userFollowing 不能为空！");

        UserOrganizationFollowing existingUserFollowing = dataAPIClient.get("/userorganization/following/who/{id}/org/{orgid}", UserOrganizationFollowing.class,
                userFollowing.getUserId(), userFollowing.getOrgId());
        if (existingUserFollowing == null) {
            throw new NotFoundException(40401, "找不到用户follow记录！");
        }
//        UserOrganizationFollowing userFollowing = dataAPIClient.get("/userorganization/following/{id}", UserOrganizationFollowing.class, Id);
        if (!userDomain.validateUser(userFollowing.getUserId())) {
            throw new UnauthorizedException("不能删除其他用户的收藏信息！");
        }
        dataAPIClient.patch("userorganization/following", existingUserFollowing); //just mark isFollowing as False.
    }
}
