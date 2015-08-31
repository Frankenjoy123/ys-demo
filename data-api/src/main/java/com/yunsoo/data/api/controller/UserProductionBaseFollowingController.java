package com.yunsoo.data.api.controller;

import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.service.contract.UserOrganizationFollowing;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import com.yunsoo.common.data.object.UserProductBaseFollowingObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.data.service.entity.UserProductBaseFollowingEntity;
import com.yunsoo.data.service.repository.UserProductBaseFollowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yan on 8/17/2015.
 */

@RestController

@RequestMapping("/userproduct/following")
public class UserProductionBaseFollowingController {

    @Autowired
    private UserProductBaseFollowingRepository userProductBaseFollowingRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserProductBaseFollowingObject getFollowingProductsByFollowingId(@PathVariable(value = "id") String id) {
        if (id == null) throw new BadRequestException("id不能为空");
        List<UserProductBaseFollowingEntity> userProductBaseFollowingEntities = userProductBaseFollowingRepository.findById(id);
        if (userProductBaseFollowingEntities == null || userProductBaseFollowingEntities.size() < 1) {
            throw new NotFoundException(40401, "找不到关注的记录! ID = " + id);
        }
        return toUserProductFollowingObject(userProductBaseFollowingEntities.get(0));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserProductBaseFollowingObject> getByFilters(@RequestParam(value = "userid", required = false) String userId,
                                                             @RequestParam(value = "productid", required = false) String productId,
                                                             Pageable pageable, HttpServletResponse response) {

        Page<UserProductBaseFollowingEntity> entityPage = null;
        if(userId!=null)
            entityPage = userProductBaseFollowingRepository.findByUserId(userId, pageable);
        else if(productId!=null)
            entityPage = userProductBaseFollowingRepository.findByProductBaseId(productId, pageable);
        else
            throw new BadRequestException("userid或者productid不能为空！");

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return entityPage.getContent().stream().map(this::toUserProductFollowingObject).collect(Collectors.toList());
    }

    //Check whether the user - org link exists or not.
    @RequestMapping(value = "/who/{id}/product/{productid}", method = RequestMethod.GET)
    public UserProductBaseFollowingObject getFollowingRecord(@PathVariable(value = "id") String userId,
                                                              @PathVariable(value = "productid") String productId) {
        if (userId == null || userId.isEmpty()) throw new BadRequestException("id不能为空！");
        if (productId == null || productId.isEmpty()) throw new BadRequestException("productid不能为空！");

        List<UserProductBaseFollowingEntity> list = userProductBaseFollowingRepository.findByUserIdAndProductBaseId(userId, productId);
        if (list == null || list.size() < 1) {
            return null;
        }
        return toUserProductFollowingObject(list.get(0));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String userFollow(@RequestBody UserProductBaseFollowingObject object) {
        UserProductBaseFollowingEntity entity = this.toUserOrganizationFollowingEntity(object);
        entity.setCreatedDateTime(DateTime.now());  //set created datetime
        userProductBaseFollowingRepository.save(entity);
        return entity.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserFollow(@PathVariable(value = "id") String id) {

        List<UserProductBaseFollowingEntity> entityList = userProductBaseFollowingRepository.findById(id);
        if(entityList==null || entityList.size()==0)
            throw new BadRequestException(40401, "删除失败！找不到关注的记录! ID = " + id);

        userProductBaseFollowingRepository.delete(entityList.get(0));

    }


    @RequestMapping(value = "/count/{ids}", method = RequestMethod.GET)
    public Map<String, Long> getFollowingsNumberByProductBaseIds(@PathVariable(value = "ids") List<String> productBaseIds) {
        Map<String, Long> resultMap = new HashMap<String, Long>();
        productBaseIds.forEach(id->{
            resultMap.put(id,userProductBaseFollowingRepository.countByProductBaseId(id));
        });
        return resultMap;
    }

    private UserProductBaseFollowingEntity toUserOrganizationFollowingEntity(UserProductBaseFollowingObject object){
        UserProductBaseFollowingEntity entity = new UserProductBaseFollowingEntity();
        if(object.getId()!=null)
            entity.setId(object.getId());
        entity.setUserId(object.getUserId());
        entity.setProductBaseId(object.getProductBaseId());
        return entity;
    }


    private UserProductBaseFollowingObject toUserProductFollowingObject(UserProductBaseFollowingEntity entity){
        UserProductBaseFollowingObject obj = new UserProductBaseFollowingObject();
        obj.setId(entity.getId());
        obj.setUserId(entity.getUserId());
        obj.setProductBaseId(entity.getProductBaseId());
        obj.setCreatedDateTime(entity.getCreatedDateTime());
        return obj;
    }

}
