package com.yunsoo.data.api.controller;

import com.yunsoo.common.web.exception.NotFoundException;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
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
    public UserProductBaseFollowingObject getFollowingProductsByFollowingId(@PathVariable(value = "id") Long id) {
        if (id == null || id <= 0) throw new BadRequestException("id不能为空");
        List<UserProductBaseFollowingEntity> userProductBaseFollowingEntities = userProductBaseFollowingRepository.findById(id);
        if (userProductBaseFollowingEntities == null || userProductBaseFollowingEntities.size() < 1) {
            throw new NotFoundException(40401, "找不到关注的记录! ID = " + id);
        }
        return toUserProductFollowingObject(userProductBaseFollowingEntities.get(0));
    }

    @RequestMapping(value = "/who/{id}", method = RequestMethod.GET)
    public List<UserProductBaseFollowingObject> getFollowingProductsByUserId(@PathVariable(value = "id") String userId,
                                                                          @RequestParam(value = "index") Integer index,
                                                                          @RequestParam(value = "size") Integer size) {
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值");

        Page<UserProductBaseFollowingEntity> list = userProductBaseFollowingRepository.findByUserIdAndIsFollowing(userId, true, new PageRequest(index, size));

        return list.getContent().stream().map(this::toUserProductFollowingObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public List<UserProductBaseFollowingObject> getFollowersByProductId(@PathVariable(value = "id") String prodId,
                                                                    @RequestParam(value = "index", required = false) Integer index,
                                                                    @RequestParam(value = "size", required = false) Integer size) {
        if (prodId == null || prodId.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index != null && index < 0) throw new BadRequestException("Index必须为不小于0的值");
        if (size != null && size < 0) throw new BadRequestException("Size必须为不小于0的值");

        if(index != null){
            Page<UserProductBaseFollowingEntity>   list = userProductBaseFollowingRepository.findByProductBaseId(prodId, new PageRequest(index, size));
            return list.getContent().stream().map(this::toUserProductFollowingObject).collect(Collectors.toList());
        }
        else{
            List<UserProductBaseFollowingEntity>  list = userProductBaseFollowingRepository.findByProductBaseId(prodId);
            return list.stream().map(this::toUserProductFollowingObject).collect(Collectors.toList());
        }



    }

    //Check whether the user - org link exists or not.
    @RequestMapping(value = "/who/{id}/product/{prodid}", method = RequestMethod.GET)
    public UserProductBaseFollowingObject getFollowingRecord(@PathVariable(value = "id") String userId,
                                                              @PathVariable(value = "prodid") String productId) {
        if (userId == null || userId.isEmpty()) throw new BadRequestException("id不能为空！");
        if (productId == null || productId.isEmpty()) throw new BadRequestException("prodid不能为空！");

        List<UserProductBaseFollowingEntity> userOrganizationFollowingList = userProductBaseFollowingRepository.findByUserIdAndProductBaseId(userId, productId);
        if (userOrganizationFollowingList == null || userOrganizationFollowingList.size() < 1) {
            return null;
        }
        return toUserProductFollowingObject(userOrganizationFollowingList.get(0));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long userFollow(@RequestBody UserProductBaseFollowingObject object) {
        UserProductBaseFollowingEntity entity = this.toUserOrganizationFollowingEntity(object);
        entity.setCreatedDateTime(DateTime.now());  //set created datetime
        entity.setModifiedDateTime(DateTime.now());
        userProductBaseFollowingRepository.save(entity);
        return entity.getId();
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateUserFollow(@RequestBody UserProductBaseFollowingObject object) {
        UserProductBaseFollowingObject orignalObj = getFollowingProductsByFollowingId(object.getId());
        if(orignalObj != null) {
            UserProductBaseFollowingEntity entity = this.toUserOrganizationFollowingEntity(object);
            entity.setCreatedDateTime(orignalObj.getCreatedDateTime());
            entity.setModifiedDateTime(DateTime.now());
            userProductBaseFollowingRepository.save(entity);
        }

    }

    private UserProductBaseFollowingEntity toUserOrganizationFollowingEntity(UserProductBaseFollowingObject object){
        UserProductBaseFollowingEntity entity = new UserProductBaseFollowingEntity();
        if(object.getId()!=null && object.getId()>0)
            entity.setId(object.getId());
        entity.setUserId(object.getUserId());
        entity.setIsFollowing(object.getIsFollowing());
        entity.setProductBaseId(object.getProductBaseId());
        return entity;
    }


    private UserProductBaseFollowingObject toUserProductFollowingObject(UserProductBaseFollowingEntity entity){
        UserProductBaseFollowingObject obj = new UserProductBaseFollowingObject();
        obj.setId(entity.getId());
        obj.setUserId(entity.getUserId());
        obj.setProductBaseId(entity.getProductBaseId());
        obj.setIsFollowing(entity.getIsFollowing());
        obj.setCreatedDateTime(entity.getCreatedDateTime());
        obj.setModifiedDateTime(entity.getModifiedDateTime());
        return obj;
    }

}
