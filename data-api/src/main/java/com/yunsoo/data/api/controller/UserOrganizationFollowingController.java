package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserOrganizationFollowingObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.UserOrganizationFollowingEntity;
import com.yunsoo.data.service.repository.UserOrganizationFollowingRepository;
import com.yunsoo.data.service.service.contract.UserOrganizationFollowing;
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
@RequestMapping("/userorganization/following")
public class UserOrganizationFollowingController {

    @Autowired
    private UserOrganizationFollowingRepository userOrganizationFollowingRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserOrganizationFollowingObject getFollowingOrgByFollowingId(@PathVariable(value = "id") Long id) {
        if (id == null || id <= 0) throw new BadRequestException("id不能为空！");
        List<UserOrganizationFollowing> userOrganizationFollowingList = UserOrganizationFollowing.FromEntityList(userOrganizationFollowingRepository.findById(id));
        if (userOrganizationFollowingList == null || userOrganizationFollowingList.size() < 1) {
            throw new NotFoundException(40401, "找不到关注的记录! ID = " + id);
        }
        return this.FromUserFollowing(userOrganizationFollowingList.get(0));
    }

    @RequestMapping(value = "/who/{id}", method = RequestMethod.GET)
    public List<UserOrganizationFollowingObject> getFollowingOrgsByUserId(@PathVariable(value = "id") String userId,
                                                        @RequestParam(value = "index") Integer index,
                                                        @RequestParam(value = "size") Integer size) {
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<UserOrganizationFollowing> userOrganizationFollowingList = UserOrganizationFollowing.FromEntityList(userOrganizationFollowingRepository.findByUserIdAndIsFollowing(userId, true, new PageRequest(index, size)));
//        if (userOrganizationFollowingList == null || userOrganizationFollowingList.size() < 1) {
//            throw new NotFoundException(40401, "找不到用户follow组织的记录! 用户ID = " + id);
//        }
        return this.FromUserFollowingList(userOrganizationFollowingList);
    }

    @RequestMapping(value = "/org/{id}", method = RequestMethod.GET)
    public List<UserOrganizationFollowingObject> getFollowersByOrgId(@PathVariable(value = "id") String orgId,
                                                  @RequestParam(value = "index", required = false) Integer index,
                                                  @RequestParam(value = "size", required = false) Integer size) {
        if (orgId == null || orgId.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index != null && index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size != null && size < 0) throw new BadRequestException("Size必须为不小于0的值！");
        List<UserOrganizationFollowing> userOrganizationFollowingList = null;
        if(index != null){
            userOrganizationFollowingList = UserOrganizationFollowing.FromEntityList(userOrganizationFollowingRepository.findByOrgId(orgId, new PageRequest(index, size)));
        }
        else{
            userOrganizationFollowingList = UserOrganizationFollowing.FromEntityList(userOrganizationFollowingRepository.findByOrgId(orgId));

        }

        return this.FromUserFollowingList(userOrganizationFollowingList);

    }

    //Check whether the user - org link exists or not.
    @RequestMapping(value = "/who/{id}/org/{orgid}", method = RequestMethod.GET)
    public UserOrganizationFollowingObject getFollowingRecord(@PathVariable(value = "id") String userId,
                                            @PathVariable(value = "orgid") String orgId) {
        if (userId == null || userId.isEmpty()) throw new BadRequestException("id不能为空！");
        if (orgId == null || orgId.isEmpty()) throw new BadRequestException("orgId不能为空！");

        List<UserOrganizationFollowing> userOrganizationFollowingList = UserOrganizationFollowing.FromEntityList(userOrganizationFollowingRepository.findByUserIdAndOrgId(userId, orgId));
        if (userOrganizationFollowingList == null || userOrganizationFollowingList.size() < 1) {
            return null;
        }
        return this.FromUserFollowing(userOrganizationFollowingList.get(0));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long userFollow(@RequestBody UserOrganizationFollowingObject userOrganizationFollowingObject) {
        UserOrganizationFollowing userOrganizationFollowing = this.ToUserFollowing(userOrganizationFollowingObject);
        userOrganizationFollowing.setCreatedDateTime(DateTime.now());  //set created datetime
        userOrganizationFollowing.setModifiedDateTime(DateTime.now());
        UserOrganizationFollowingEntity newEntity = userOrganizationFollowingRepository.save(UserOrganizationFollowing.ToEntity(userOrganizationFollowing));
        return newEntity.getId();
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateUserFollow(@RequestBody UserOrganizationFollowingObject userOrganizationFollowingObject) {
        UserOrganizationFollowingObject orignalObj = getFollowingOrgByFollowingId(userOrganizationFollowingObject.getId());
        if(orignalObj != null) {
            UserOrganizationFollowing userOrganizationFollowing = this.ToUserFollowing(userOrganizationFollowingObject);
            userOrganizationFollowing.setModifiedDateTime(DateTime.now());
            userOrganizationFollowing.setCreatedDateTime(orignalObj.getCreatedDateTime());
            userOrganizationFollowingRepository.save(UserOrganizationFollowing.ToEntity(userOrganizationFollowing));
        }
    }

    private UserOrganizationFollowingObject FromUserFollowing(UserOrganizationFollowing userOrganizationFollowing) {
        UserOrganizationFollowingObject userOrganizationFollowingObject = new UserOrganizationFollowingObject();
        BeanUtils.copyProperties(userOrganizationFollowing, userOrganizationFollowingObject);
        return userOrganizationFollowingObject;
    }

    private UserOrganizationFollowing ToUserFollowing(UserOrganizationFollowingObject userOrganizationFollowingObject) {
        UserOrganizationFollowing userOrganizationFollowing = new UserOrganizationFollowing();
        BeanUtils.copyProperties(userOrganizationFollowingObject, userOrganizationFollowing);
        return userOrganizationFollowing;
    }

    private List<UserOrganizationFollowingObject> FromUserFollowingList(List<UserOrganizationFollowing> userOrganizationFollowingList) {
        if (userOrganizationFollowingList == null) return null;

        List<UserOrganizationFollowingObject> userOrganizationFollowingObjectList = new ArrayList<>();
        for (UserOrganizationFollowing userOrganizationFollowing : userOrganizationFollowingList) {
            userOrganizationFollowingObjectList.add(this.FromUserFollowing(userOrganizationFollowing));
        }
        return userOrganizationFollowingObjectList;
    }

    private List<UserOrganizationFollowing> ToUserFollowingList(List<UserOrganizationFollowingObject> userOrganizationFollowingObjectList) {
        if (userOrganizationFollowingObjectList == null) return null;

        List<UserOrganizationFollowing> userOrganizationFollowingList = new ArrayList<>();
        for (UserOrganizationFollowingObject UserOrganizationFollowingObject : userOrganizationFollowingObjectList) {
            userOrganizationFollowingList.add(this.ToUserFollowing(UserOrganizationFollowingObject));
        }
        return userOrganizationFollowingList;
    }


}