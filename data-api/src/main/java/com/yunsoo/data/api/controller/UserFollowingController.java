package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserFollowingObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.UserFollowingEntity;
import com.yunsoo.data.service.repository.UserFollowingRepository;
import com.yunsoo.data.service.service.contract.UserFollowing;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/4/15.
 * * * ErrorCode
 * 40401    :   UserOrganization not found!
 */
@RestController
//@EnableSpringDataWebSupport
@RequestMapping("/user/following")
public class UserFollowingController {

    @Autowired
    private UserFollowingRepository userFollowingRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserFollowingObject getFollowingOrgsByUserId(@PathVariable(value = "id") Long id) {
        if (id == null || id <= 0) throw new BadRequestException("id不能为空！");
        List<UserFollowing> userFollowingList = UserFollowing.FromEntityList(userFollowingRepository.findById(id));
        if (userFollowingList == null || userFollowingList.size() < 1) {
            throw new NotFoundException(40401, "找不到组织的记录! ID = " + id);
        }
        return this.FromUserFollowing(userFollowingList.get(0));
    }

    @RequestMapping(value = "/who/{id}", method = RequestMethod.GET)
    public List<UserFollowingObject> getFollowingOrgsByUserId(@PathVariable(value = "id") String id,
                                                        @RequestParam(value = "index") Integer index,
                                                        @RequestParam(value = "size") Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<UserFollowing> userFollowingList = UserFollowing.FromEntityList(userFollowingRepository.findByUserIdAndIsFollowing(id, true, new PageRequest(index, size)));
//        if (userFollowingList == null || userFollowingList.size() < 1) {
//            throw new NotFoundException(40401, "找不到用户follow组织的记录! 用户ID = " + id);
//        }
        return this.FromUserFollowingList(userFollowingList);
    }

    @RequestMapping(value = "/org/{id}", method = RequestMethod.GET)
    public List<UserFollowingObject> getFollowerByOrgId(@PathVariable(value = "id") String id,
                                                  @RequestParam(value = "index") Integer index,
                                                  @RequestParam(value = "size") Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<UserFollowing> userFollowingList = UserFollowing.FromEntityList(userFollowingRepository.findByOrganizationId(id, new PageRequest(index, size)));
//        if (userFollowingList == null || userFollowingList.size() < 1) {
//            throw new NotFoundException(40401, "找不到组织的粉丝记录! 组织ID = " + id);
//        }
        return this.FromUserFollowingList(userFollowingList);
    }

    //Check whether the user - org link exists or not.
    @RequestMapping(value = "/who/{id}/org/{orgid}", method = RequestMethod.GET)
    public UserFollowingObject getFollowingRecord(@PathVariable(value = "id") String id,
                                            @PathVariable(value = "orgid") String orgId) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (orgId == null || orgId.isEmpty()) throw new BadRequestException("orgId不能为空！");

        List<UserFollowing> userFollowingList = UserFollowing.FromEntityList(userFollowingRepository.findByUserIdAndOrganizationId(id, orgId));
        if (userFollowingList == null || userFollowingList.size() < 1) {
            return null;
        }
        return this.FromUserFollowing(userFollowingList.get(0));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long userFollow(@RequestBody UserFollowingObject userFollowingObject) {
        UserFollowing userFollowing = this.ToUserFollowing(userFollowingObject);
        userFollowing.setCreatedDateTime(DateTime.now());  //set created datetime
        userFollowing.setLastUpdatedDateTime(DateTime.now());
        UserFollowingEntity newEntity = userFollowingRepository.save(UserFollowing.ToEntity(userFollowing));
        return newEntity.getId();
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateUserFollow(@RequestBody UserFollowingObject userFollowingObject) {
        UserFollowing userFollowing = this.ToUserFollowing(userFollowingObject);
        userFollowing.setLastUpdatedDateTime(DateTime.now());
        userFollowingRepository.save(UserFollowing.ToEntity(userFollowing));

    }

    private UserFollowingObject FromUserFollowing(UserFollowing userFollowing) {
        UserFollowingObject userFollowingObject = new UserFollowingObject();
        BeanUtils.copyProperties(userFollowing, userFollowingObject);
        return userFollowingObject;
    }

    private UserFollowing ToUserFollowing(UserFollowingObject userFollowingObject) {
        UserFollowing userFollowing = new UserFollowing();
        BeanUtils.copyProperties(userFollowingObject, userFollowing);
        return userFollowing;
    }

    private List<UserFollowingObject> FromUserFollowingList(List<UserFollowing> userFollowingList) {
        if (userFollowingList == null) return null;

        List<UserFollowingObject> userFollowingObjectList = new ArrayList<>();
        for (UserFollowing userFollowing : userFollowingList) {
            userFollowingObjectList.add(this.FromUserFollowing(userFollowing));
        }
        return userFollowingObjectList;
    }

    private List<UserFollowing> ToUserFollowingList(List<UserFollowingObject> UserFollowingObjectList) {
        if (UserFollowingObjectList == null) return null;

        List<UserFollowing> userFollowingList = new ArrayList<>();
        for (UserFollowingObject UserFollowingObject : UserFollowingObjectList) {
            userFollowingList.add(this.ToUserFollowing(UserFollowingObject));
        }
        return userFollowingList;
    }

}