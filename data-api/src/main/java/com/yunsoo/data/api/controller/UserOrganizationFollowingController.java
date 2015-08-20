package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserOrganizationFollowingObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.UserOrganizationFollowingEntity;
import com.yunsoo.data.service.repository.UserOrganizationFollowingRepository;
import com.yunsoo.data.service.service.contract.UserOrganizationFollowing;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

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
    public UserOrganizationFollowingObject getFollowingOrgByFollowingId(@PathVariable(value = "id") String id) {
        if (id == null) throw new BadRequestException("id不能为空！");
        List<UserOrganizationFollowingEntity> userOrganizationFollowingList = userOrganizationFollowingRepository.findById(id);
        if (userOrganizationFollowingList == null || userOrganizationFollowingList.size() < 1) {
            throw new NotFoundException(40401, "找不到关注的记录! ID = " + id);
        }
        return toUserOrganizationFollowingObject(userOrganizationFollowingList.get(0));
    }

    @RequestMapping(value = "/who/{id}", method = RequestMethod.GET)
    public List<UserOrganizationFollowingObject> getFollowingOrgsByUserId(@PathVariable(value = "id") String userId,
                                                                          Pageable pageable,
                                                                          HttpServletResponse response) {

        Page<UserOrganizationFollowingEntity> entityPage = userOrganizationFollowingRepository.findByUserId(userId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return entityPage.getContent().stream().map(this::toUserOrganizationFollowingObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/org/{id}", method = RequestMethod.GET)
    public List<UserOrganizationFollowingObject> getFollowersByOrgId(@PathVariable(value = "id") String orgId,
                                                                     Pageable pageable,
                                                                     HttpServletResponse response) {
        if (orgId == null || orgId.isEmpty()) throw new BadRequestException("id不能为空！");

        Page<UserOrganizationFollowingEntity> entityPage  = userOrganizationFollowingRepository.findByOrgId(orgId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return entityPage.getContent().stream().map(this::toUserOrganizationFollowingObject).collect(Collectors.toList());
    }

    //Check whether the user - org link exists or not.
    @RequestMapping(value = "/who/{id}/org/{orgid}", method = RequestMethod.GET)
    public UserOrganizationFollowingObject getFollowingRecord(@PathVariable(value = "id") String userId,
                                            @PathVariable(value = "orgid") String orgId) {
        if (userId == null || userId.isEmpty()) throw new BadRequestException("id不能为空！");
        if (orgId == null || orgId.isEmpty()) throw new BadRequestException("orgId不能为空！");

        List<UserOrganizationFollowingEntity> userOrganizationFollowingList =userOrganizationFollowingRepository.findByUserIdAndOrgId(userId, orgId);
        if (userOrganizationFollowingList == null || userOrganizationFollowingList.size() < 1) {
            return null;
        }
        return toUserOrganizationFollowingObject(userOrganizationFollowingList.get(0));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String userFollow(@RequestBody UserOrganizationFollowingObject userOrganizationFollowingObject) {
        UserOrganizationFollowingEntity newEntity = toUserOrganizationFollowingEntity(userOrganizationFollowingObject);
        newEntity.setCreatedDateTime(DateTime.now());  //set created datetime
        userOrganizationFollowingRepository.save(newEntity);
        return newEntity.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserFollow(@PathVariable(value = "id") String id) {
        List<UserOrganizationFollowingEntity> entityList = userOrganizationFollowingRepository.findById(id);
        if(entityList==null || entityList.size()==0)
            throw new BadRequestException(40401, "删除失败！找不到关注的记录! ID = " + id);

        userOrganizationFollowingRepository.delete(entityList.get(0));
    }


    private UserOrganizationFollowingEntity toUserOrganizationFollowingEntity(UserOrganizationFollowingObject object){
        UserOrganizationFollowingEntity entity = new UserOrganizationFollowingEntity();
        if(object.getId()!=null)
            entity.setId(object.getId());
        entity.setUserId(object.getUserId());
        entity.setOrgId(object.getOrgId());
        return entity;
    }



    private UserOrganizationFollowingObject toUserOrganizationFollowingObject(UserOrganizationFollowingEntity entity){
        UserOrganizationFollowingObject obj = new UserOrganizationFollowingObject();
        obj.setId(entity.getId());
        obj.setUserId(entity.getUserId());
        obj.setOrgId(entity.getOrgId());
        obj.setCreatedDateTime(entity.getCreatedDateTime());
        return obj;
    }

}