package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserLikedProductObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.UserLikedProductEntity;
import com.yunsoo.data.service.repository.UserLikedProductRepository;
import com.yunsoo.data.service.service.contract.UserLikedProduct;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/4/3.
 * *  * * ErrorCode
 * 40401    :   UserLikedProduct not found!
 */

@RestController
@RequestMapping("/user/collection")
public class UserLikedProductController {
    @Autowired
    private UserLikedProductRepository userLikedProductRepository;

    @RequestMapping(value = "/userid/{id}", method = RequestMethod.GET)
    public List<UserLikedProductObject> getUserCollectionByUserId(@PathVariable(value = "id") String id,
                                                                  @RequestParam(value = "index") Integer index,
                                                                  @RequestParam(value = "size") Integer size) {
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        return this.FromUserLikedProductList(UserLikedProduct.FromEntityList(userLikedProductRepository.findByUserIdAndActive(id, true, new PageRequest(index, size)))); //add active filter
    }

    @RequestMapping(value = "/who/{id}/product/{pid}", method = RequestMethod.GET)
    public UserLikedProductObject getUserLikedProduct(@PathVariable(value = "id") String id, @PathVariable(value = "pid") String pid) {
        List<UserLikedProduct> result = UserLikedProduct.FromEntityList(userLikedProductRepository.findByUserIdAndBaseProductId(id, pid));
        if (result.isEmpty()) {
            return null;
        }
        return this.FromUserLikedProduct(result.get(0));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserLikedProductObject getUserCollectionByUserId(@PathVariable(value = "id") Long id) {
        List<UserLikedProduct> userLikedProductList = UserLikedProduct.FromEntityList(userLikedProductRepository.findById(id));
        if (userLikedProductList.isEmpty()) {
            throw new NotFoundException(40401, "找不到UserLikedProduct记录. ID = " + id);
        }
        return this.FromUserLikedProduct(userLikedProductList.get(0));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long userLikeProduct(@RequestBody UserLikedProductObject userLikedProductObject) {
        UserLikedProduct userLikedProduct = this.ToUserLikedProduct(userLikedProductObject);
        userLikedProduct.setCreatedDateTime(DateTime.now());
        userLikedProduct.setLastUpdatedDateTime(DateTime.now());
        UserLikedProductEntity newEntity = userLikedProductRepository.saveAndFlush(UserLikedProduct.ToEntity(userLikedProduct));
        return newEntity.getId();
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateUserLikeProduct(@RequestBody UserLikedProductObject userLikedProductObject) {
        UserLikedProduct userLikedProduct = this.ToUserLikedProduct(userLikedProductObject);
        userLikedProduct.setLastUpdatedDateTime(DateTime.now());
        userLikedProductRepository.save(UserLikedProduct.ToEntity(userLikedProduct));
    }

    private UserLikedProductObject FromUserLikedProduct(UserLikedProduct userLikedProduct) {
        UserLikedProductObject userLikedProductObject = new UserLikedProductObject();
        BeanUtils.copyProperties(userLikedProduct, userLikedProductObject);
        return userLikedProductObject;
    }

    private UserLikedProduct ToUserLikedProduct(UserLikedProductObject userLikedProductObject) {
        UserLikedProduct userLikedProduct = new UserLikedProduct();
        BeanUtils.copyProperties(userLikedProductObject, userLikedProduct);
        return userLikedProduct;
    }

    private List<UserLikedProductObject> FromUserLikedProductList(List<UserLikedProduct> userLikedProductList) {
        if (userLikedProductList == null) return null;

        List<UserLikedProductObject> userLikedProductObjectList = new ArrayList<>();
        for (UserLikedProduct userLikedProduct : userLikedProductList) {
            userLikedProductObjectList.add(this.FromUserLikedProduct(userLikedProduct));
        }
        return userLikedProductObjectList;
    }

    private List<UserLikedProduct> ToUserLikedProductList(List<UserLikedProductObject> userLikedProductObjectList) {
        if (userLikedProductObjectList == null) return null;

        List<UserLikedProduct> userLikedProductList = new ArrayList<>();
        for (UserLikedProductObject userLikedProductObject : userLikedProductObjectList) {
            userLikedProductList.add(this.ToUserLikedProduct(userLikedProductObject));
        }
        return userLikedProductList;
    }
}
