package com.yunsoo.data.api.controller;

import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.UserLikedProductEntity;
import com.yunsoo.data.service.repository.UserLikedProductRepository;
import com.yunsoo.data.service.service.contract.UserLikedProduct;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/4/3.
 *  *  * * ErrorCode
 * 40401    :   UserLikedProduct not found!
 */

@RestController
@RequestMapping("/user/collection")
public class UserLikedProductController {
    @Autowired
    private UserLikedProductRepository userLikedProductRepository;

    @RequestMapping(value = "/userid/{id}", method = RequestMethod.GET)
    public List<UserLikedProduct> getUserCollectionByUserId(@PathVariable(value = "id") String id) {
        return UserLikedProduct.FromEntityList(userLikedProductRepository.findByUserId(id));
    }

    @RequestMapping(value = "/userid/{id}/product/{pid}", method = RequestMethod.GET)
    public UserLikedProduct getUserLikedProduct(@PathVariable(value = "id") String id, @PathVariable(value = "pid") String pid) {
        List<UserLikedProduct> result = UserLikedProduct.FromEntityList(userLikedProductRepository.findByUserIdAndBaseProductId(id, pid));
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserLikedProduct getUserCollectionByUserId(@PathVariable(value = "id") Long id) {
        List<UserLikedProduct> userLikedProductList = UserLikedProduct.FromEntityList(userLikedProductRepository.findById(id));
        if (userLikedProductList.isEmpty()) {
            throw new NotFoundException(40401, "找不到UserLikedProduct记录. ID = " + id);
        }
        return userLikedProductList.get(0);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long userLikeProduct(@RequestBody UserLikedProduct userLikedProduct) {
        userLikedProduct.setCreatedDateTime(DateTime.now());
        userLikedProduct.setLastUpdatedDateTime(DateTime.now());
        UserLikedProductEntity newEntity = userLikedProductRepository.save(UserLikedProduct.ToEntity(userLikedProduct));
        return newEntity.getId();
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateUserLikeProduct(@RequestBody UserLikedProduct userLikedProduct) {
        userLikedProduct.setLastUpdatedDateTime(DateTime.now());
        userLikedProductRepository.save(UserLikedProduct.ToEntity(userLikedProduct));
    }

//    @RequestMapping(value = "/unlike/{id}", method = RequestMethod.DELETE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void userUnlikeProduct(@PathVariable(value = "id") Long Id) {
//        userLikedProductRepository.delete(Id);
//    }
}
