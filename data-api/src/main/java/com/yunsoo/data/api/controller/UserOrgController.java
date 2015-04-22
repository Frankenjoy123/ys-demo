package com.yunsoo.data.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.UserOrgEntity;
import com.yunsoo.data.service.repository.UserOrgRepository;
import com.yunsoo.data.service.service.contract.UserOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/4/15.
 *  * * ErrorCode
 * 40401    :   UserOrganization not found!
 */
@RestController
//@EnableSpringDataWebSupport
@RequestMapping("/user/following/")
public class UserOrgController {

    @Autowired
    private UserOrgRepository userOrgRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public List<UserOrganization> getFollowingOrgsByUserId(@PathVariable(value = "id") String id) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        List<UserOrganization> userOrganizationList = UserOrganization.FromEntityList(userOrgRepository.findById(id));
        if (userOrganizationList == null || userOrganizationList.size() < 1) {
            throw new NotFoundException(40401, "找不到组织的记录! ID = " + id);
        }
        return userOrganizationList;
    }

    @RequestMapping(value = "/who/{id}", method = RequestMethod.GET)
    public List<UserOrganization> getFollowingOrgsByUserId(@PathVariable(value = "id") String id,
                                                           @RequestParam(value = "index") Integer index,
                                                           @RequestParam(value = "size") Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<UserOrganization> userOrganizationList = UserOrganization.FromEntityList(userOrgRepository.findByUserId(id, new PageRequest(index, size)));
        if (userOrganizationList == null || userOrganizationList.size() < 1) {
            throw new NotFoundException(40401, "找不到用户follow组织的记录! 用户ID = " + id);
        }
        return userOrganizationList;
    }

    @RequestMapping(value = "/org/{id}", method = RequestMethod.GET)
    public List<UserOrganization> getFollowerByOrgId(@PathVariable(value = "id") String id,
                                                     @RequestParam(value = "index") Integer index,
                                                     @RequestParam(value = "size") Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<UserOrganization> userOrganizationList = UserOrganization.FromEntityList(userOrgRepository.findByOrganizationId(id, new PageRequest(index, size)));
        if (userOrganizationList == null || userOrganizationList.size() < 1) {
            throw new NotFoundException(40401, "找不到组织的粉丝记录! 组织ID = " + id);
        }
        return userOrganizationList;
    }

    @RequestMapping(value = "/link", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long userLikeProduct(@RequestBody UserOrganization userLikedProduct) {
        UserOrgEntity newEntity = userOrgRepository.save(UserOrganization.ToEntity(userLikedProduct));
        return newEntity.getId();
    }

    @RequestMapping(value = "/unlink", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userUnlikeProduct(@RequestBody Long Id) {
        userOrgRepository.delete(Id);
    }
}
