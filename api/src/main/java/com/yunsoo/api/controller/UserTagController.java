package com.yunsoo.api.controller;

import com.yunsoo.api.domain.UserTagDomain;
import com.yunsoo.api.dto.UserTag;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.UserTagObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Admin on 6/14/2016.
 */
@RestController
@RequestMapping("/user/tag")
public class UserTagController {

    @Autowired
    private UserTagDomain userTagDomain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserTag> queryUser(@RequestParam(value = "user_id", required = false) String userId,
                                   @RequestParam(value = "ys_id", required = false) String ysId,
                                   @RequestParam(value = "org_id", required = false) String orgId) {

        orgId = AuthUtils.fixOrgId(orgId);
        List<UserTagObject> userList = userTagDomain.getUserTags(userId, ysId, orgId);

        return userList.stream()
                .map(UserTag::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void update(@RequestParam(value = "user_id", required = false) String userId,
                       @RequestParam(value = "ys_id", required = false) String ysId,
                       @RequestParam(value = "org_id", required = false) String orgId,
                       @RequestBody List<UserTag> userTags) {

        orgId = AuthUtils.fixOrgId(orgId);

        List<UserTagObject> userTagObjects = new ArrayList<>();

        if (userTags != null && userTags.size() > 0) {
            for (UserTag tag : userTags) {
                userTagObjects.add(toUserTagObject(tag));
            }
        }

        userTagDomain.updateTags(userId, ysId, orgId, userTagObjects);
    }

    private UserTagObject toUserTagObject(UserTag userTag) {
        if (userTag == null)
            return null;

        UserTagObject userTagObject = new UserTagObject();
        userTagObject.setId(userTag.getId());
        userTagObject.setUserId(userTag.getUserId());
        userTagObject.setYsId(userTag.getYsId());
        userTagObject.setOrgId(userTag.getOrgId());
        userTagObject.setTagId(userTag.getTagId());
        userTagObject.setTagName(userTag.getTagName());
        userTagObject.setCreatedAccountId(userTag.getCreatedAccountId());
        userTagObject.setCreatedDateTime(userTag.getCreatedDateTime());

        return userTagObject;
    }
}
