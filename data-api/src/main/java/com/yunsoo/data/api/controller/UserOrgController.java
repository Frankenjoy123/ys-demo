package com.yunsoo.data.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.data.service.repository.UserOrgRepository;
import com.yunsoo.data.service.service.contract.UserOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/4/15.
 */
@RestController
//@EnableSpringDataWebSupport
@RequestMapping("/user/following/")
public class UserOrgController {

    @Autowired
    private UserOrgRepository userOrgRepository;


    @RequestMapping(value = "/who/{id}", method = RequestMethod.GET)
    public List<UserOrganization> getFollowingOrgsByUserId(@PathVariable(value = "id") String id,
                                                           @RequestParam(value = "index") Integer index,
                                                           @RequestParam(value = "size") Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<UserOrganization> userOrganizationList = UserOrganization.FromEntityList(userOrgRepository.findByUserId(id, new PageRequest(index, size)));
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
        return userOrganizationList;
    }

    //to-do

}
