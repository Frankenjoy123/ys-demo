package com.yunsoo.data.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.UserFollowingEntity;
import com.yunsoo.data.service.repository.UserFollowingRepository;
import com.yunsoo.data.service.service.Impl.UserServiceImpl;
import com.yunsoo.data.service.service.UserService;
import com.yunsoo.data.service.service.contract.UserFollowing;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
//    @Autowired
//    private UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserFollowing getFollowingOrgsByUserId(@PathVariable(value = "id") Long id) {
        if (id == null || id <= 0) throw new BadRequestException("id不能为空！");
        List<UserFollowing> userFollowingList = UserFollowing.FromEntityList(userFollowingRepository.findById(id));
        if (userFollowingList == null || userFollowingList.size() < 1) {
            throw new NotFoundException(40401, "找不到组织的记录! ID = " + id);
        }
        return userFollowingList.get(0);
    }

    @RequestMapping(value = "/who/{id}", method = RequestMethod.GET)
    public List<UserFollowing> getFollowingOrgsByUserId(@PathVariable(value = "id") String id,
                                                        @RequestParam(value = "index") Integer index,
                                                        @RequestParam(value = "size") Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<UserFollowing> userFollowingList = UserFollowing.FromEntityList(userFollowingRepository.findByUserId(id, new PageRequest(index, size)));
        if (userFollowingList == null || userFollowingList.size() < 1) {
            throw new NotFoundException(40401, "找不到用户follow组织的记录! 用户ID = " + id);
        }
        return userFollowingList;
    }

    @RequestMapping(value = "/org/{id}", method = RequestMethod.GET)
    public List<UserFollowing> getFollowerByOrgId(@PathVariable(value = "id") String id,
                                                  @RequestParam(value = "index") Integer index,
                                                  @RequestParam(value = "size") Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<UserFollowing> userFollowingList = UserFollowing.FromEntityList(userFollowingRepository.findByOrganizationId(id, new PageRequest(index, size)));
        if (userFollowingList == null || userFollowingList.size() < 1) {
            throw new NotFoundException(40401, "找不到组织的粉丝记录! 组织ID = " + id);
        }
        return userFollowingList;
    }

    //Check whether the user - org link exists or not.
    @RequestMapping(value = "/who/{id}/org/{orgid}", method = RequestMethod.GET)
    public UserFollowing getFollowingRecord(@PathVariable(value = "id") String id,
                                            @PathVariable(value = "orgid") String orgId) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (orgId == null || orgId.isEmpty()) throw new BadRequestException("orgId不能为空！");

        List<UserFollowing> userFollowingList = UserFollowing.FromEntityList(userFollowingRepository.findByUserIdAndOrganizationId(id, orgId));
        if (userFollowingList == null || userFollowingList.size() < 1) {
            return null;
        }
        return userFollowingList.get(0);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long userFollow(@RequestBody UserFollowing userFollowing) {
        userFollowing.setCreatedDateTime(DateTime.now());  //set created datetime
        userFollowing.setLastUpdatedDateTime(DateTime.now());
//        return userService.createUserFollowing(userFollowing);
        UserFollowingEntity newEntity = userFollowingRepository.save(UserFollowing.ToEntity(userFollowing));
        return newEntity.getId();
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateUserFollow(@RequestBody UserFollowing userFollowing) {
        userFollowing.setLastUpdatedDateTime(DateTime.now());
        userFollowingRepository.save(UserFollowing.ToEntity(userFollowing));

    }

//    @RequestMapping(value = "/delete", method = RequestMethod.PATCH)
//    public void userUnfollow(@RequestBody UserFollowing userFollowing) {
//        userFollowing.setIsFollowing(false);
//        userFollowingRepository.save(UserFollowing.ToEntity(userFollowing)); //just mark isFollowing as False.
//    }
}
