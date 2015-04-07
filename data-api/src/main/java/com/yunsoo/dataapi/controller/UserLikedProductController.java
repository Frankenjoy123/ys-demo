package com.yunsoo.dataapi.controller;

import com.yunsoo.entity.UserLikedProductEntity;
import com.yunsoo.repository.UserLikedProductRepository;
import com.yunsoo.service.contract.UserLikedProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/4/3.
 */
@RestController
@RequestMapping("/user/collection")
public class UserLikedProductController {
    @Autowired
    private UserLikedProductRepository userLikedProductRepository;

    @RequestMapping(value = "/userid/{id}", method = RequestMethod.GET)
    public List<UserLikedProduct> getNewMessagesByMessageId(@PathVariable(value = "id") Long id) {
        return UserLikedProduct.FromEntityList(userLikedProductRepository.findByUserId(id));
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long userLikeProduct(@RequestBody UserLikedProduct userLikedProduct) {
        UserLikedProductEntity newEntity = userLikedProductRepository.save(UserLikedProduct.ToEntity(userLikedProduct));
        return newEntity.getId();
    }

    @RequestMapping(value = "/unlike", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userUnlikeProduct(@RequestBody Long Id) {
        userLikedProductRepository.delete(Id);
    }
}
